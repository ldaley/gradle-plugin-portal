package app;

import json.Json;
import json.JsonBuilder;
import org.ratpackframework.Handler;
import org.ratpackframework.Request;
import org.ratpackframework.Response;
import user.AuthenticatableUser;

import javax.inject.Inject;

public class CurrentUserInfoHandler implements Handler<Response> {

    private final AuthenticatableUser user;
    private final Json json;

    @Inject
    public CurrentUserInfoHandler(AuthenticatableUser user, Json json) {
        this.user = user;
        this.json = json;
    }

    public void handle(Response response) {
        Request request = response.getRequest();
        json.object(response, new Handler<JsonBuilder>() {
            public void handle(JsonBuilder json) {
                json.name("username").value(user.getUsername());
            }
        });
    }

}
