package http;

import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;
import org.apache.shiro.authc.AuthenticationException;
import org.ratpackframework.Handler;
import org.ratpackframework.Request;
import org.ratpackframework.Response;
import org.ratpackframework.http.MediaType;
import user.AuthenticatableUser;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPage implements Handler<Response> {

    @Inject
    AuthenticatableUser user;

    @Override
    public void handle(Response response) {
        Request request = response.getRequest();
        if (request.getMethod().equals("GET")) {
            doShowLogin(response);
        } else if (request.getMethod().equals("POST")) {
            doLogin(response);
        } else {
            response.end(405);
        }
    }

    private void doShowLogin(Response response) {
        Map<String, Object> model = new HashMap<>();
        model.put("content", "login.html");
        List<String> failure = response.getRequest().getQueryParams().get("failure");
        if (failure != null) {
            model.put("failure", failure.get(0));
        }
        model.put("title", "Login");
        model.put("username", user.getUsername());
        response.render(model, "skin.html");
    }

    private void doLogin(final Response response) {
        final Request request = response.getRequest();

        MediaType contentType = request.getContentType();
        if (contentType.isJson()) {
            doJsonLogin(response, request);
        } else if (contentType.isForm()) {
            doFormLogin(response, request);
        } else {
            response.end(415);
        }
    }

    private void doFormLogin(final Response response, Request request) {
        Map<String, List<String>> params = request.getForm();
        try {
            user.authenticate(params.get("username").get(0), params.get("password").get(0), true);
            response.redirect("/");
        } catch (Exception e) {
            response.redirect("/login?failure=" + e.getMessage());
        }
    }

    private void doJsonLogin(final Response response, Request request) {
        Map<String, Object> jsonResult = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> jsonRequest = (Map<String, String>) new JsonSlurper().parseText(request.getText());

        try {
            String username = jsonRequest.get("username");
            String password = jsonRequest.get("password");
            user.authenticate(username, password, true);

            jsonResult.put("success", true);
            jsonResult.put("username", user.getUsername());
        } catch (AuthenticationException e1) {
            jsonResult.put("success", false);
            jsonResult.put("failure", e1.getMessage());
        }

        response.text("application/json", JsonOutput.toJson(jsonResult));
    }

}
