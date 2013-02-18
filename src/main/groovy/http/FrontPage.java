package http;

import org.ratpackframework.Handler;
import org.ratpackframework.Response;
import org.ratpackframework.session.Session;
import user.User;

import javax.inject.Inject;

public class FrontPage implements Handler<Response> {

    @Inject
    User user;

    @Override
    public void handle(Response response) {
        Session session = response.getRequest().getSession();
        if (user.getUsername() != null) {
            response.redirect("/user");
        } else {
            response.redirect("/login");
        }
    }

}
