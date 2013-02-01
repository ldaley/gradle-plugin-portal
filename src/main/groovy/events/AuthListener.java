package events;

import org.apache.log4j.Logger;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;


public class AuthListener implements Handler<Message> {

    private Logger logger = Logger.getLogger(AuthListener.class);

    public AuthListener(EventBus eventBus) {
        eventBus.registerHandler("auth.login.success", this);
        eventBus.registerHandler("auth.login.failure", this);
        eventBus.registerHandler("auth.logout", this);
    }

    @Override
    public void handle(Message event) {
        logger.info("received auth message in server: " + event.body);
    }

}
