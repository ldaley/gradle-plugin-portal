package user;

import com.google.gson.JsonObject;
import json.Json;
import json.JsonBuilder;
import org.ratpackframework.app.Endpoint;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;
import org.ratpackframework.handler.Handler;
import user.AuthenticatableUser;

import javax.inject.Inject;

public class LoginEndpoint implements Endpoint {

    private final AuthenticatableUser user;
    private final Json json;

    @Inject
    public LoginEndpoint(AuthenticatableUser user, Json json) {
        this.user = user;
        this.json = json;
    }

    @Override
    public void respond(Request request, Response response) {
        if (!request.getContentType().isJson()) {
            response.end(415);
            return;
        }

        if (user.isAuthenticated()) {
            user.logout();
        }

        final AuthRequest authRequest = extractRequest(request);

        json.object(response, new Handler<JsonBuilder>() {
            public void handle(JsonBuilder json) {
                try {
                    user.authenticate(authRequest.username, authRequest.password, authRequest.remember);
                    json.name("success").value(true);
                } catch (Exception e) {
                    json.name("success").value(false);
                    json.name("failure").value(e.getMessage());
                }
            }
        });
    }

    private static class AuthRequest {
        String username;
        String password;
        boolean remember;
    }

    private AuthRequest extractRequest(Request request) {
        JsonObject incoming = json.object(request);
        AuthRequest authRequest = new AuthRequest();
        authRequest.username = incoming.get("username").getAsString();
        authRequest.password = incoming.get("password").getAsString();
        authRequest.remember = incoming.get("remember").getAsBoolean();
        return authRequest;
    }
}
