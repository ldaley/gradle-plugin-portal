import org.apache.shiro.mgt.DefaultSecurityManager
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.realm.SimpleAccountRealm
import org.apache.shiro.subject.Subject
import org.ratpackframework.groovy.Request
import org.ratpackframework.groovy.ClosureRouting
import org.vertx.java.core.json.JsonObject

(this as ClosureRouting).with {

    SimpleAccountRealm realm = new SimpleAccountRealm()
    realm.addAccount("user1", "password")
    realm.addAccount("user2", "password")

    SecurityManager securityManager = new DefaultSecurityManager(realm)
    SubjectFactory subjectFactory = new SubjectFactory(securityManager)

    get("/", new FrontPage())
    get("/user", new UserPage(securityManager))
    all("/login", new LoginPage(securityManager, subjectFactory, vertx.eventBus()))
    get("/events") { render "skin.html", content: "events.html", title: "Events" }

    get("/logout") { Request request ->
        Subject subject = request.session.subject as Subject
        if (subject) {
            vertx.eventBus().publish("auth.logout", new JsonObject().putString("username", subject.principal.toString()))
            request.session.invalidate()
        }
        redirect "/"
    }

}
