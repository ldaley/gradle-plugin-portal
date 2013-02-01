package events;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;


public class AuthListener implements Handler<Message> {

    @Override
    public void handle(Message event) {
        System.out.println("received: " + event);
    }

}
