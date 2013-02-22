package launcher

import org.ratpackframework.bootstrap.RatpackServer

class LaunchedApp implements App {

    private final RatpackServer server

    LaunchedApp(RatpackServer server) {
        this.server = server
    }

    @Override
    String getUrl() {
        "http://localhost:$server.bindPort"
    }

    @Override
    void stop() {
        server.stopAndWait()
    }

    @Override
    boolean isRunning() {
        server.running
    }
}
