package content

import geb.Page

class HomePage extends Page {

    static at = {
        waitFor { title == "Gradle Plugins" }
    }

    static content = {
        topNav {  module TopNav }
    }
}
