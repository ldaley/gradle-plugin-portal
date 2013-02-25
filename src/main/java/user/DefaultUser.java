package user;

import com.google.inject.Inject;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.RequestScoped;
import org.ratpackframework.app.Response;
import org.ratpackframework.session.store.MapSessionStore;

import java.util.concurrent.ConcurrentMap;

@RequestScoped
public class DefaultUser implements AuthenticatableUser {

    private static final String USERNAME_KEY = "local.username";
    private final SubjectFactory subjectFactory;
    private final Request request;
    private final Response response;
    private final CookieRememberedIdentityManager rememberedIdentityManager;
    private final MapSessionStore localSessionStore;

    private String username;
    private boolean authenticated;
    private boolean init;

    @Inject
    public DefaultUser(SubjectFactory subjectFactory, Request request, Response response, CookieRememberedIdentityManager rememberedIdentityManager, MapSessionStore localSessionStore) {
        this.subjectFactory = subjectFactory;
        this.request = request;
        this.response = response;
        this.rememberedIdentityManager = rememberedIdentityManager;
        this.localSessionStore = localSessionStore;
    }

    private void init() {
        if (!init) {
            init = true;
            ConcurrentMap<String, Object> map = getSessionStore();
            Object localSessionUsername = map.get(USERNAME_KEY);
            if (localSessionUsername == null) {
                username = rememberedIdentityManager.detect(request, response);
                authenticated = false;
            } else {
                username = localSessionUsername.toString();
                authenticated = true;
            }
        }
    }

    private ConcurrentMap<String, Object> getSessionStore() {
        return localSessionStore.get(request);
    }

    @Override
    public boolean isAuthenticated() {
        init();
        return authenticated;
    }

    @Override
    public boolean isRemembered() {
        return getUsername() != null && !isAuthenticated();
    }

    @Override
    public String getUsername() {
        init();
        return username;
    }

    @Override
    public void authenticate(String username, String password, boolean remember) {
        Subject subject = subjectFactory.create(username);
        subject.login(new UsernamePasswordToken(username, password));
        this.username = username;
        this.authenticated = true;
        this.init = true;
        getSessionStore().put(USERNAME_KEY, username);
        if (remember) {
            rememberedIdentityManager.initiateNew(username, response);
        }
    }

    @Override
    public void logout() {
        getSessionStore().remove(USERNAME_KEY);
        rememberedIdentityManager.clear(username, response);
        username = null;
        authenticated = false;
    }
}
