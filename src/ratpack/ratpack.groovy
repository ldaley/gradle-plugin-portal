import http.FrontPage
import http.LoginPage
import http.UserPage
import org.apache.shiro.mgt.DefaultSecurityManager
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.realm.SimpleAccountRealm
import org.apache.shiro.realm.ldap.JndiLdapContextFactory
import org.apache.shiro.realm.ldap.JndiLdapRealm
import org.apache.shiro.subject.Subject
import org.ratpackframework.groovy.Request
import org.ratpackframework.groovy.ClosureRouting
import org.vertx.java.core.json.JsonObject
import shiro.SubjectFactory

(this as ClosureRouting).with {

    SimpleAccountRealm literalRealm = new SimpleAccountRealm()
    literalRealm.name = "literal"
    literalRealm.addAccount("user1", "password")
    literalRealm.addAccount("user2", "password")

    JndiLdapRealm ldapRealm = new JndiLdapRealm()
    ldapRealm.name = "ldap"
    ldapRealm.userDnTemplate = "cn={0},dc=example,dc=org"
    JndiLdapContextFactory contextFactory = ldapRealm.contextFactory
    contextFactory.url = "ldap://localhost:10389"
//    contextFactory.setAuthenticationMechanism()
    SecurityManager securityManager = new DefaultSecurityManager([literalRealm, ldapRealm])
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
