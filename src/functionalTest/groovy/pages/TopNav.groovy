package pages

import geb.Module

class TopNav extends Module {

    static base = {
        $("div[ng-controller=topnav]")
    }

    static content = {
        loginButton { $("button.login") }
        username(wait: true) { $("#logged-in-username") }
    }
}
