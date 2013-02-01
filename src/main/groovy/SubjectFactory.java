import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.ratpackframework.Response;
import org.ratpackframework.session.Session;

public class SubjectFactory {

    private final SecurityManager securityManager;

    public SubjectFactory(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public Subject create(String principal, Response response) {
        Subject.Builder builder = new Subject.Builder(securityManager);
        builder.principals(new SimplePrincipalCollection(principal, "*"));
        Session session = response.getRequest().getSession();
        builder.sessionId(session.getId());
        return builder.buildSubject();
    }

}
