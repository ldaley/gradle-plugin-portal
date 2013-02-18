package app;

import json.Json;
import json.JsonBuilder;
import org.ratpackframework.Handler;
import org.ratpackframework.Request;
import org.ratpackframework.Response;
import user.AuthenticatableUser;

import javax.inject.Inject;
import java.util.Map;

public class LoginHandler implements Handler<Response> {

    private final AuthenticatableUser user;
    private final Json json;

    @Inject
    public LoginHandler(AuthenticatableUser user, Json json) {
        this.user = user;
        this.json = json;
    }

    @Override
    public void handle(Response response) {
        Request request = response.getRequest();
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
                    json.property("success", true);
                } catch (Exception e) {
                    json.property("success", false);
                    json.property("message", e.getMessage());
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
        Map<String, Object> incoming = json.object(request);
        AuthRequest authRequest = new AuthRequest();
        authRequest.username = incoming.get("username").toString();
        authRequest.password = incoming.get("password").toString();
        authRequest.remember = (boolean) incoming.get("remember");
        return authRequest;
    }
}
