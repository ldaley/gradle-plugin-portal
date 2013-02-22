import geb.Page
import geb.spock.GebReportingSpec
import pages.FrontPage
import pages.LoginDialog
import spock.lang.Stepwise

@Stepwise
class ExampleSpec extends GebReportingSpec {

    def page

    def "start"() {
        when:
        go "http://localhost:5050"

        then:
        waitFor { at(FrontPage).topNav.loginButton }.click()
    }

    def "login"() {
        given:
        page = at LoginDialog

        when:
        page.username = "user1"
        page.password = "password"
        page.signIn.click()
        page = at FrontPage

        then:
        page.topNav.username.text() == "user1"
    }

}
