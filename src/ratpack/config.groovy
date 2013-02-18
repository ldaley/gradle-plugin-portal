import org.ratpackframework.config.Config
import org.ratpackframework.session.store.MapSessionsModule
import store.StoreModule
import user.UserSecurityModule

(this as Config).with {
    modules << new UserSecurityModule() << new StoreModule() << new MapSessionsModule(100, 30)

    sessionCookie.expiresMins = 0
}
