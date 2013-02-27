package launcher

import bootstrap.Main

class AppLauncher {

    private static ThreadLocal<App> appThreadLocal = new ThreadLocal<>()

    App launch() {
        def existing = appThreadLocal.get()

        if (!existing) {
            def alreadyRunning = new AlreadyRunningApp(5050)
            if (alreadyRunning.running) {
                appThreadLocal.set(alreadyRunning)
            } else {
                def server = new Main().launch(new File("src/ratpack"), 0)
                appThreadLocal.set(new LaunchedApp(server) {
                    @Override
                    void stop() {
                        // noop
                    }
                })
                addShutdownHook {
                    server.stopAndWait()
                }
            }
        }

        appThreadLocal.get()
    }

}
