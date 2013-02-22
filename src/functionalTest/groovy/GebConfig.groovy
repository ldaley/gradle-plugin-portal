import org.openqa.selenium.chrome.ChromeDriver

reportsDir = new File("geb-reports")
driver = {
    new ChromeDriver()
}

def env = System.getenv()

// CI servers can set this environment variable to tune the waiting.
def envWait = env["GEB_DEFAULT_WAIT_SECS"]
if (envWait) {
    waiting {
        timeout = envWait.toInteger()
    }
}