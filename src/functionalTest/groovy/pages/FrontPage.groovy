package pages

import geb.Page

class FrontPage extends Page {

    static at = {
        waitFor { title == "Gradle Plugins" }
    }

    static content = {
        topNav {  module TopNav }
    }
}
