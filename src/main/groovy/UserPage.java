import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.ratpackframework.Response;
import org.ratpackframework.session.Session;
import org.vertx.java.core.Handler;

import java.util.HashMap;
import java.util.Map;

public class UserPage implements Handler<Response> {

    private final SecurityManager securityManager;

    public UserPage(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public void handle(Response response) {
        Session session = response.getRequest().getSession();
        if (!session.containsKey("subject")) {
            response.redirect("/");
            return;
        }

        Subject subject = (Subject) session.get("subject");
        Map<String, Object> model = new HashMap<>(1);
        model.put("subject", subject);
        model.put("content", "user.html");

        response.render(model, "skin.html");
    }
}
