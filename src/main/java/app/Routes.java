package app;

import org.ratpackframework.app.Routing;
import org.ratpackframework.handler.Handler;

public class Routes implements Handler<Routing> {

    @Override
    public void handle(Routing routing) {
        routing.all("/data/current-user", CurrentUserInfoHandler.class);
        routing.post("/login", LoginHandler.class);
        routing.get("/logout", LogoutHandler.class);
    }

}
