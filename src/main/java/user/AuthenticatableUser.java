package user;

public interface AuthenticatableUser extends User {

    void authenticate(String principal, String secret, boolean remember);

    void logout();

}
