package user;

import com.google.inject.AbstractModule;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;

public class UserSecurityModule extends AbstractModule {

    @Override
    protected void configure() {
        SimpleAccountRealm literalRealm = new SimpleAccountRealm();
        literalRealm.addAccount("user1", "password");
        literalRealm.addAccount("user2", "password");
        DefaultSecurityManager securityManager = new DefaultSecurityManager(literalRealm);
        DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(new SessionStorageEvaluator() {
            @Override
            public boolean isSessionStorageEnabled(Subject subject) {
                return false;
            }
        });

        bind(SecurityManager.class).toInstance(securityManager);
        bind(SubjectFactory.class);

        bind(DefaultUser.class);
        bind(User.class).to(DefaultUser.class);
        bind(AuthenticatableUser.class).to(DefaultUser.class);

        bind(UserPersistentIdentityStore.class); // depends on StoreModule
        bind(CookieRememberedIdentityManager.class);
    }

}
