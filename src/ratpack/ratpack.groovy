import app.CurrentUserInfoHandler
import app.LoginHandler
import app.LogoutHandler
import org.ratpackframework.Routing

(this as Routing).with {
    all("/data/current-user", CurrentUserInfoHandler)

    post("/login", LoginHandler)
    get("/logout", LogoutHandler)
}
