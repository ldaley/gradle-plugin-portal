package support

import launcher.App
import launcher.AppLauncher
import org.junit.rules.ExternalResource

class RunningApp extends ExternalResource {

    private App app

    @Override
    protected void before() throws Throwable {
        app = new AppLauncher().launch()
    }

    @Override
    protected void after() {
        app.stop()
    }

    String getUrl() {
        app.url
    }

}
