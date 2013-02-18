package http;

import org.ratpackframework.Handler;
import org.ratpackframework.Response;
import user.User;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class UserPage implements Handler<Response> {

     @Inject
     User user;

    @Override
    public void handle(Response response) {
        if (user.getUsername() == null) {
            response.redirect("/");
            return;
        }

        Map<String, Object> model = new HashMap<>(1);
        model.put("user", user);
        model.put("content", "user.html");

        response.render(model, "skin.html");
    }
}
