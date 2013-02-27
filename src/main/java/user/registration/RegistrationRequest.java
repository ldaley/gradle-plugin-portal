package user.registration;

public class RegistrationRequest {

    private final String username;
    private final String password;
    private final String confirmPassword;
    private final String fullName;
    private final String email;

    public RegistrationRequest(String username, String password, String confirmPassword, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.fullName = fullName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
