package user;

public class PersistentUserSessionId {

    private final String username;
    private final String series;

    public PersistentUserSessionId(String username, String series) {
        this.username = username;
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public String getSeries() {
        return series;
    }
}
