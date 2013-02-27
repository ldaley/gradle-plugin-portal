package user;

import json.Json;
import json.JsonBuilder;
import org.ratpackframework.app.Endpoint;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;
import org.ratpackframework.handler.Handler;
import user.AuthenticatableUser;

import javax.inject.Inject;

public class CurrentUserInfoEndpoint implements Endpoint {

    private final AuthenticatableUser user;
    private final Json json;

    @Inject
    public CurrentUserInfoEndpoint(AuthenticatableUser user, Json json) {
        this.user = user;
        this.json = json;
    }

    @Override
    public void respond(Request request, Response response) {
        json.object(response, new Handler<JsonBuilder>() {
            public void handle(JsonBuilder json) {
                json.name("username").value(user.getUsername());
            }
        });
    }

}
