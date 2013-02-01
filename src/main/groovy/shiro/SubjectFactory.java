package shiro;

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
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, "ldap");
        principals.add(principal, "literal");
        builder.principals(principals);
        Session session = response.getRequest().getSession();
        builder.sessionId(session.getId());
        return builder.buildSubject();
    }

}
