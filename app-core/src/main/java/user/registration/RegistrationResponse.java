package user.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationResponse {

    private final Map<String, List<String>> fieldErrors = new HashMap<>();

    void addFieldError(String field, String error) {
        if (!fieldErrors.containsKey(field)) {
            fieldErrors.put(field, new ArrayList<String>(1));
        }
        fieldErrors.get(field).add(error);
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }

    boolean isSuccess() {
        return fieldErrors.isEmpty();
    }
}
