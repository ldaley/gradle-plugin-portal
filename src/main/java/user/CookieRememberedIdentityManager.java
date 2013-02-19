package user;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.CipherService;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;

import javax.inject.Inject;
import java.nio.charset.Charset;

public class CookieRememberedIdentityManager {

    private static final byte[] DEFAULT_CIPHER_KEY_BYTES = Base64.decode("kPH+bIxk5D2deZiIxcaaaA==");
    private static final String COOKIE_NAME = "auth";
    private static final Charset UTF8 = Charset.forName("utf-8");
    private static final String SEPARATOR = ":";

    private final UserPersistentIdentityStore persistentIdentityStore;
    private final CipherService cipherService;
    private final byte[] cipherKey;

    @Inject
    public CookieRememberedIdentityManager(UserPersistentIdentityStore persistentIdentityStore) {
        this(persistentIdentityStore, new AesCipherService(), DEFAULT_CIPHER_KEY_BYTES);
    }

    public CookieRememberedIdentityManager(UserPersistentIdentityStore persistentIdentityStore, CipherService cipherService, byte[] cipherKey) {
        this.persistentIdentityStore = persistentIdentityStore;
        this.cipherService = cipherService;
        this.cipherKey = cipherKey;
    }

    public String detect(Request request, Response response) {
        String cookieValue = request.oneCookie(COOKIE_NAME);
        if (cookieValue == null) {
            return null;
        }

        String decrypted;
        try {
            decrypted = decrypt(cookieValue);
        } catch (Exception e) {
            response.expireCookie(COOKIE_NAME);
            return null;
        }
        String[] parts = decrypted.split(SEPARATOR, 3);
        if (parts.length != 3) {
            return null;
        }

        String username = parts[0];
        String series = parts[1];
        String secret = parts[2];

        PersistentUserToken refresh = persistentIdentityStore.refresh(username, new PersistentUserToken(series, secret));
        if (refresh == null) {
            persistentIdentityStore.clear(username);
            return null;
        }

        setCookie(username, refresh, response);
        return username;
    }

    public void clear(String username, Response response) {
        if (username != null) {
            persistentIdentityStore.clear(username);
        }
        response.expireCookie(COOKIE_NAME);
    }

    public void initiateNew(String username, Response response) {
        setCookie(username, persistentIdentityStore.initiate(username), response);
    }

    private void setCookie(String username, PersistentUserToken token, Response response) {
        String value = username + SEPARATOR + token.getSeries() + SEPARATOR + token.getSecret();
        String encrypted = encrypt(value);
        response.cookie(COOKIE_NAME, encrypted);
    }

    private String encrypt(String value) {
        return cipherService.encrypt(value.getBytes(UTF8), cipherKey).toBase64();
    }

    private String decrypt(String value) {
        return new String(cipherService.decrypt(Base64.decode(value), cipherKey).getBytes(), UTF8);
    }
}
