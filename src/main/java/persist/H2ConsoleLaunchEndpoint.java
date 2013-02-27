package persist;


import org.h2.server.web.WebServer;
import org.h2.tools.Server;
import org.ratpackframework.app.Endpoint;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

public class H2ConsoleLaunchEndpoint implements Endpoint {

    private final DataSource dataSource;

    @Inject
    public H2ConsoleLaunchEndpoint(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void respond(Request request, Response response) {
        String url;
        try {
            final WebServer webServer = new WebServer();
            Server web = new Server(webServer);
            web.start();
            Server server = new Server();
            webServer.setShutdownHandler(server);
            url = webServer.addSession(dataSource.getConnection());
            Thread thread = new Thread("h2console") {
                @Override
                public void run() {
                    try {
                        while (!webServer.isStopped()) {
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            };

            thread.setDaemon(true);
            thread.start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.redirect(url);
    }
}
