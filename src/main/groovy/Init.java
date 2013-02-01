import org.ratpackframework.app.RatpackApp;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;

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

        AuthListener authListener = new AuthListener();
        vertx.eventBus().registerHandler("auth.login.success", authListener);
        vertx.eventBus().registerHandler("auth.login.failure", authListener);
        vertx.eventBus().registerHandler("auth.logout", authListener);
    }
}
