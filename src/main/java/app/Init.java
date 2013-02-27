package app;

import com.google.inject.AbstractModule;
import org.h2.tools.Server;
import org.ratpackframework.bootstrap.RatpackServer;
import org.ratpackframework.bootstrap.internal.RootModule;
import org.ratpackframework.handler.Handler;
import persist.DbSchemaInitializer;

import javax.inject.Inject;
import java.sql.SQLException;

public class Init implements Handler<RatpackServer> {

    public static class Module extends AbstractModule {
        @Override
        protected void configure() {
            bind(RootModule.RATPACK_INIT).to(Init.class);
        }
    }

    private final DbSchemaInitializer dbSchemaInitializer;

    @Inject
    public Init(DbSchemaInitializer dbSchemaInitializer) {
        this.dbSchemaInitializer = dbSchemaInitializer;
    }

    @Override
    public void handle(RatpackServer server) {
        dbSchemaInitializer.run();
    }

}
