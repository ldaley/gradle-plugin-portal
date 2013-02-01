package http;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.ratpackframework.Request;
import org.ratpackframework.Response;
import org.ratpackframework.http.MediaType;
import org.ratpackframework.session.Session;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;
import shiro.SubjectFactory;

import java.util.HashMap;
import java.util.Map;

public class LoginPage implements Handler<Response> {

    private final SecurityManager securityManager;
    private final SubjectFactory subjectFactory;
    private final EventBus eventBus;

    public LoginPage(SecurityManager securityManager, SubjectFactory subjectFactory, EventBus eventBus) {
        this.securityManager = securityManager;
        this.subjectFactory = subjectFactory;
        this.eventBus = eventBus;
    }

    @Override
    public void handle(Response response) {
        Request request = response.getRequest();
        if (request.getMethod().equals("GET")) {
            Map<String, Object> model = new HashMap<>();
            model.put("content", "login.html");
            String failure = (String) response.getRequest().getQueryParams().get("failure");
            if (failure != null) {
                model.put("failure", failure);
            }
            boolean ajax = response.getRequest().getQueryParams().containsKey("ajax");
            if (ajax) {
                model.put("ajax", true);
            }
            model.put("title", "Login");
            response.render(model, "skin.html");
        } else if (request.getMethod().equals("POST")) {
            doLogin(response);
        } else {
            response.setStatus(405);
        }
    }

    private void doLogin(final Response response) {
        final Request request = response.getRequest();

        MediaType contentType = request.getContentType();
        if (contentType.isJson()) {
            request.json(new Handler<JsonObject>() {
                public void handle(JsonObject jsonRequest) {
                    Map<String, Object> jsonResult = new HashMap<>();
                    try {
                        Subject subject = doLogin(response, jsonRequest.getString("username"), jsonRequest.getString("password"));
                        jsonResult.put("success", true);
                        jsonResult.put("username", subject.getPrincipal());
                        response.renderJson(jsonResult);
                    } catch (AuthenticationException e1) {
                        jsonResult.put("success", false);
                        jsonResult.put("failure", e1.getMessage());
                        response.renderJson(jsonResult);
                    }
                }
            });
        } else if (contentType.isForm()) {
            request.form(new Handler<Map<String, ?>>() {
                public void handle(Map<String, ?> params) {
                    try {
                        doLogin(response, params.get("username").toString(), params.get("password").toString());
                        response.redirect("/");
                    } catch (AuthenticationException e) {
                        response.redirect("login?failure=" + e.getMessage());
                    }
                }
            });
        }

    }

    private Subject doLogin(Response response, String username, String password) throws AuthenticationException {
        AuthenticationToken token = new UsernamePasswordToken(username, password);
        Subject subject = subjectFactory.create(username, response);
        try {
            securityManager.login(subject, token);
            Session session = response.getRequest().getSession();
            System.out.println("Putting subject in session " + session.getId());
            session.put("subject", subject);
            eventBus.publish("auth.login.success", new JsonObject().putString("username", username));
        } catch (AuthenticationException e) {
            eventBus.publish("auth.login.failure", new JsonObject().putString("username", username).putString("failure", e.getMessage()));
            throw e;
        }
        return subject;
    }

}
