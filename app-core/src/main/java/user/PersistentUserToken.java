package user;

public class PersistentUserToken {

    private final String series;
    private final String secretToken;

    public PersistentUserToken(String series, String secretToken) {
        this.series = series;
        this.secretToken = secretToken;
    }

    public String getSeries() {
        return series;
    }

    public String getSecret() {
        return secretToken;
    }

}
