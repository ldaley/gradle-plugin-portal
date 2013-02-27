package user;

import org.ratpackframework.app.Endpoint;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;
import org.ratpackframework.handler.Handler;
import user.AuthenticatableUser;

import javax.inject.Inject;

public class LogoutEndpoint implements Endpoint {

    private final AuthenticatableUser user;

    @Inject
    public LogoutEndpoint(AuthenticatableUser user) {
        this.user = user;
    }

    @Override
    public void respond(Request request, Response response) {
        if (user.isAuthenticated()) {
            user.logout();
        }

        response.redirect("/");
    }
}
