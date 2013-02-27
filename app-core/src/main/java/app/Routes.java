package app;

import org.ratpackframework.app.Routing;
import org.ratpackframework.handler.Handler;
import persist.H2ConsoleLaunchEndpoint;
import user.CurrentUserInfoEndpoint;
import user.LoginEndpoint;
import user.LogoutEndpoint;
import user.registration.RegisterEndpoint;

public class Routes implements Handler<Routing> {

    @Override
    public void handle(Routing routing) {
        routing.all("/data/current-user", routing.inject(CurrentUserInfoEndpoint.class));
        routing.post("/login", routing.inject(LoginEndpoint.class));
        routing.get("/logout", routing.inject(LogoutEndpoint.class));
        routing.post("/data/register", routing.inject(RegisterEndpoint.class));
        routing.get("/dbconsole", routing.inject(H2ConsoleLaunchEndpoint.class));
    }

}
