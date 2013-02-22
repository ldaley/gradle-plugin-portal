package support

import content.HomePage
import geb.spock.GebReportingSpec
import org.junit.Rule

class FunctionalSpec extends GebReportingSpec {

    def page

    @Rule RunningApp app

    def setup() {
        browser.baseUrl = app.url
    }

    HomePage home() {
        toAt(HomePage)
    }

    void pause() {
        js.exec """
            (function() {
                window.__gebPaused = true;
                var div = document.createElement("div");
                div.setAttribute('style', "\\
                    position: absolute; top: 0px;\\
                    z-index: 3000;\\
                    padding: 10px;\\
                    background-color: red;\\
                    border: 2px solid black;\\
                    border-radius: 2px;\\
                    text-align: center;\\
                ");

                var button = document.createElement("button");
                button.innerHTML = "Unpause Geb";
                button.onclick = function() {
                    window.__gebPaused = false;
                }
                button.setAttribute("style", "\\
                    font-weight: bold;\\
                ");

                div.appendChild(button);
                document.getElementsByTagName("body")[0].appendChild(div);
            })();
        """

        waitFor(300) { !js.__gebPaused }
    }

    boolean noModal() {
        waitFor { !$("div.modal").displayed }
    }

    boolean modalUp() {
        waitFor { $("div.modal").displayed }
    }
}
