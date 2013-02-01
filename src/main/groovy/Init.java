import events.AuthListener;
import org.ratpackframework.app.RatpackApp;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import shiro.DatabaseServer;
import shiro.TransientLdapServer;

import java.io.File;

public class Init implements Handler<RatpackApp> {

    @Override
    public void handle(RatpackApp app) {
        Vertx vertx = app.getVertx();
        HttpServer httpServer = app.getHttpServer();

        // Setup a sockjs server @ /events
        SockJSServer sockJSServer = vertx.createSockJSServer(httpServer);
        JsonObject bridgeConf = new JsonObject();
        bridgeConf.putString("prefix", "/eventbus");
        JsonArray allowedIn = new JsonArray();
        JsonArray allowedOut = new JsonArray();
        allowedOut.add(new JsonObject().putString("address_re", "auth\\..+")); // any auth messages
        sockJSServer.bridge(bridgeConf, allowedIn, allowedOut);

        // register a server side listener, just for fun, just logs
        new AuthListener(vertx.eventBus());

        // Start the ldap and jdbc servers
        new TransientLdapServer(new File("ldapWork"), new File("ldif")).start();
        new DatabaseServer().start();
    }

}
