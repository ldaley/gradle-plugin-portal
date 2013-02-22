package launcher;

class AlreadyRunningApp implements App {

    private final int port

    AlreadyRunningApp(int port) {
        this.port = port
    }

    @Override
    String getUrl() {
        "http://localhost:${port}"
    }

    @Override
    void stop() {
        // no-op
    }

    @Override
    boolean isRunning() {
        try {
            new URL(url).bytes
            true
        } catch (Exception ignored) {
            false
        }
    }
}
