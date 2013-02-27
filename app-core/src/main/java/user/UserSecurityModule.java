package user;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import javax.inject.Singleton;

public class UserSecurityModule extends AbstractModule {

    private final String configFileUrl;

    public UserSecurityModule(String configFileUrl) {
        this.configFileUrl = configFileUrl;
    }

    @Override
    protected void configure() {
        bind(SubjectFactory.class);

        bind(DefaultUser.class);
        bind(User.class).to(DefaultUser.class);
        bind(AuthenticatableUser.class).to(DefaultUser.class);

        bind(UserPersistentIdentityStore.class); // depends on StoreModule
        bind(CookieRememberedIdentityManager.class);
    }

    @Provides
    @Singleton
    SecurityManager provideSecurityManager() {
        Ini ini = Ini.fromResourcePath(configFileUrl);
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(ini);
        DefaultSecurityManager securityManager = (DefaultSecurityManager) factory.getInstance();
        DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(new SessionStorageEvaluator() {
            @Override
            public boolean isSessionStorageEnabled(Subject subject) {
                return false;
            }
        });
        return securityManager;
    }

}
