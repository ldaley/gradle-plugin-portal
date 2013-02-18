package user;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;

public class SubjectFactory {

    private final SecurityManager securityManager;

    @Inject
    public SubjectFactory(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public Subject create(String principal) {
        Subject.Builder builder = new Subject.Builder(securityManager);
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, "");
        builder.principals(principals);
        builder.session(null);
        builder.sessionId(null);
        builder.sessionCreationEnabled(false);
        return builder.buildSubject();
    }

}
