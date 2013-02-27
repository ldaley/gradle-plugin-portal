package specs

import content.HomePage
import content.LoginDialog
import spock.lang.Stepwise
import support.FunctionalSpec

@Stepwise
class ExampleSpec extends FunctionalSpec {

    def "start"() {
        when:
        page = home()

        then:
        waitFor { page.topNav.loginButton.displayed }
        page.topNav.loginButton.click()
        modalUp()
    }

    def "failed login"() {
        given:
        page = at(LoginDialog)

        when:
        page.username = "user1"
        page.password = "passw"
        page.signIn.click()

        then:
        page.errorAlertText.contains "Login failed"

        when:
        page.password = "password"
        page.signIn.click()

        then:
        noModal()
        waitFor { at(HomePage).topNav.username.text() == "user1" }
    }

}
