package user.registration;

import json.Json;
import org.ratpackframework.app.Endpoint;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;

import javax.inject.Inject;

public class RegisterEndpoint implements Endpoint {

    private final Request request;
    private final Response response;
    private final Json json;
    private final RegisteredUserRepository repo;

    @Inject
    public RegisterEndpoint(Request request, Response response, Json json, RegisteredUserRepository repo) {
        this.request = request;
        this.response = response;
        this.json = json;
        this.repo = repo;
    }

    @Override
    public void respond(Request req, Response res) {
        RegisteredUser registeredUser = toRegisteredUser(json.bind(request, RegistrationRequest.class));
        RegistrationResponse registrationResponse = new RegistrationResponse();

        if (repo.findByUsername(registeredUser.getUsername()) != null) {
            registrationResponse.addFieldError("username", "duplicate");
        }
        if (repo.findByEmail(registeredUser.getEmail()) != null) {
            registrationResponse.addFieldError("email", "duplicate");
        }

        if (registrationResponse.isSuccess()) {
            repo.save(registeredUser);
            response.end();
        }  else {
            response.setStatus(400);
            json.render(response, registrationResponse);
        }
    }

    private RegisteredUser toRegisteredUser(RegistrationRequest registrationRequest) {
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(registrationRequest.getUsername());
        registeredUser.setPassword(registrationRequest.getPassword());
        registeredUser.setDisplayName(registrationRequest.getFullName());
        registeredUser.setEmail(registrationRequest.getEmail());
        return registeredUser;
    }

}
