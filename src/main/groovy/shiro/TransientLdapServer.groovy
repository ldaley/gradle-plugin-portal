package shiro

import org.apache.directory.server.core.DefaultDirectoryService
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition
import org.apache.directory.server.ldap.LdapService
import org.apache.directory.server.protocol.shared.SocketAcceptor
import org.apache.directory.shared.ldap.exception.LdapNameNotFoundException
import org.apache.directory.shared.ldap.ldif.LdifReader
import org.apache.directory.shared.ldap.ldif.LdifUtils
import org.apache.directory.shared.ldap.name.LdapDN

/**
 * Just an embedded ldap directory, loads ldif data in src/ratpack/ldif
 */
class TransientLdapServer {
    final static ldifFileNameFilter = [accept: { File dir, String name -> name.endsWith(".ldif") }] as FilenameFilter

    Integer port = 10389
    String base = "dc=example,dc=org"
    String[] indexed = ["objectClass", "ou", "uid"]

    private log

    DefaultDirectoryService directoryService
    LdapService ldapService

    LdapDN baseDn

    File workDir
    File dataDir

    boolean running = false

    TransientLdapServer(File workDir, File dataDir) {
        log = org.slf4j.LoggerFactory.getLogger(this.class)

        this.workDir = workDir
        this.dataDir = dataDir
        baseDn = new LdapDN(base)

        addShutdownHook {
            this.stop()
        }
    }

    void start() {
        if (!running) {
            log.info("ldap starting")
            startDirectoryService()

            loadLdif(dataDir)

            directoryService.changeLog.tag()

            startLdapService()
            running = true
            log.info("ldap starup complete")
        }
    }

    void stop() {
        if (running) {
            log.info("ldaop stopping")
            stopDirectoryService()
            stopLdapService()
            running = false
            log.info("ldap stopped")
        }
    }

    void restart() {
        stop()
        start()
    }

    void clean() {
        if (running) {
            log.info("ldap cleaning")
            directoryService.revert()
            directoryService.changeLog.tag()
        }
    }

    void loadLdif(File file) {
        if (file.exists()) {
            if (file.directory) {
                log.debug("Loading ldif in dir: ${file}")
                file.listFiles(ldifFileNameFilter).sort().each {
                    loadLdif(it)
                }
            } else {
                log.debug("Loading ldif in file: ${file}")
                consumeLdifReader(new LdifReader(file))
            }
        }
    }

    boolean exists(String dn) {
        directoryService.adminSession.exists(new LdapDN(dn as String))
    }

    Map getAt(String dn) {
        try {
            def entry = directoryService.adminSession.lookup(new LdapDN(dn))
            def entryMap = [:]
            entry.attributeTypes.each { at ->
                def attribute = entry.get(at)
                if (at.singleValue) {
                    entryMap[attribute.id] = (attribute.isHR()) ? attribute.string : attribute.bytes
                } else {
                    def values = []
                    attribute.all.each {
                        values << it.get()
                    }
                    entryMap[attribute.id] = values
                }
            }
            entryMap
        } catch (LdapNameNotFoundException e) {
            null
        }
    }

    private startDirectoryService() {

        directoryService = new DefaultDirectoryService()
        directoryService.changeLog.enabled = true
        def workingDir = getWorkDir()
        if (workingDir.exists()) workingDir.deleteDir()
        directoryService.workingDirectory = workingDir

        def partition = addPartition(baseDn.rdn.normValue, base)
        addIndex(partition, * indexed)

        directoryService.startup()
        createBase()
    }

    private startLdapService() {
        ldapService = new LdapService()
        ldapService.socketAcceptor = new SocketAcceptor(null)
        ldapService.directoryService = directoryService
        ldapService.ipPort = port

        ldapService.start()
    }

    private stopDirectoryService() {
        directoryService.shutdown()
    }

    private stopLdapService() {
        ldapService.stop()
    }

    private createBase() {
        def entry = directoryService.newEntry(baseDn)
        entry.add("objectClass", "top", "domain", "extensibleObject")
        entry.add(baseDn.rdn.normType, baseDn.rdn.normValue)
        directoryService.adminSession.add(entry)
    }

    private addPartition(partitionId, partitionDn) {
        def partition = new JdbmPartition()
        partition.id = partitionId
        partition.suffix = partitionDn
        directoryService.addPartition(partition)

        partition
    }

    private static addIndex(partition, String[] attrs) {
        partition.indexedAttributes = attrs.collect { new JdbmIndex(it) } as Set
    }

    private consumeLdifReader(LdifReader ldifReader) {
        while (ldifReader.hasNext()) {
            def entry = ldifReader.next()
            def ldif = LdifUtils.convertToLdif(entry, Integer.MAX_VALUE)
            directoryService.adminSession.add(directoryService.newEntry(ldif, entry.dn.toString()))
        }
    }

}