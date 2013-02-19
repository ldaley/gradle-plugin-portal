package app;

import org.ratpackframework.app.Response;
import org.ratpackframework.handler.Handler;
import user.AuthenticatableUser;

import javax.inject.Inject;

public class LogoutHandler implements Handler<Response> {

    private final AuthenticatableUser user;

    @Inject
    public LogoutHandler(AuthenticatableUser user) {
        this.user = user;
    }

    @Override
    public void handle(Response response) {
        if (user.isAuthenticated()) {
            user.logout();
        }

        response.redirect("/");
    }
}
