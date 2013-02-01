import http.FrontPage
import http.LoginPage
import http.UserPage
import org.apache.shiro.mgt.DefaultSecurityManager
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.realm.SimpleAccountRealm
import org.apache.shiro.realm.jdbc.JdbcRealm
import org.apache.shiro.realm.ldap.JndiLdapContextFactory
import org.apache.shiro.realm.ldap.JndiLdapRealm
import org.apache.shiro.realm.text.IniRealm
import org.apache.shiro.subject.Subject
import org.h2.jdbcx.JdbcDataSource
import org.ratpackframework.groovy.ClosureRouting
import org.ratpackframework.groovy.Request
import org.vertx.java.core.json.JsonObject
import shiro.SubjectFactory

(this as ClosureRouting).with {

    // This stuff is just in this file because it's automatically reloaded at development time
    // making it convenient to play with this stuff.
    // It wouldn't live here normally.

    // In memory store simple store
    def literalRealm = new SimpleAccountRealm()
    literalRealm.addAccount("user1", "password")
    literalRealm.addAccount("user2", "password")

    // Ldap
    def ldapRealm = new JndiLdapRealm()
    ldapRealm.userDnTemplate = "cn={0},dc=example,dc=org"
    JndiLdapContextFactory contextFactory = ldapRealm.contextFactory
    contextFactory.url = "ldap://localhost:10389"

    // JDBC
    def jdbcRealm = new JdbcRealm()
    jdbcRealm.authenticationQuery = "select password from user where user = ?"
    def ds = new JdbcDataSource();
    ds.setURL("jdbc:h2:data")
    ds.setUser("sa")
    ds.setPassword("")
    jdbcRealm.dataSource = ds

    def iniRealm = new IniRealm("auth.ini")

    SecurityManager securityManager = new DefaultSecurityManager([literalRealm, ldapRealm, jdbcRealm, iniRealm])
    SubjectFactory subjectFactory = new SubjectFactory(securityManager)

    get("/", new FrontPage())
    get("/user", new UserPage(securityManager))
    all("/login", new LoginPage(securityManager, subjectFactory, vertx.eventBus()))

    get("/events") { render "events.html", title: "Events" }

    get("/logout") { Request request ->
        Subject subject = request.session.subject as Subject
        if (subject) {
            vertx.eventBus().publish("auth.logout", new JsonObject().putString("username", subject.principal.toString()))
            request.session.invalidate()
        }
        redirect "/"
    }

}
