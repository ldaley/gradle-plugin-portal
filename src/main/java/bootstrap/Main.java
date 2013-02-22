package bootstrap;

import app.Routes;
import json.JsonModule;
import org.ratpackframework.assets.StaticAssetsConfig;
import org.ratpackframework.bootstrap.RatpackServer;
import org.ratpackframework.bootstrap.RatpackServerFactory;
import org.ratpackframework.session.store.MapSessionsModule;
import store.StoreModule;
import user.UserSecurityModule;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        new Main().launch(new File(System.getProperty("user.dir")), 5050);
    }

    public RatpackServer launch(File dir, int port) {
        RatpackServerFactory serverFactory = new RatpackServerFactory(dir, port, null, null);

        RatpackServer server = serverFactory.create(
                Routes.class, new StaticAssetsConfig(new File(dir, "public")),
                new UserSecurityModule(),
                new StoreModule(),
                new MapSessionsModule(100, 30),
                new JsonModule()
        );

        server.startAndWait();
        return server;
    }

}
