package app;

import org.ratpackframework.app.Routing;
import org.ratpackframework.handler.Handler;

public class Routes implements Handler<Routing> {

    @Override
    public void handle(Routing routing) {
        routing.all("/data/current-user", routing.inject(CurrentUserInfoEndpoint.class));
        routing.post("/login", routing.inject(LoginEndpoint.class));
        routing.get("/logout", routing.inject(LogoutEndpoint.class));
    }

}
