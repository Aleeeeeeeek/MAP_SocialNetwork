
package com.example.domain.validators;

import com.example.domain.User;

public class UserValidator implements Validator<User> {
    public UserValidator() {
    }

    public void validate(User entity) throws ValidationException {
        String message = "";
        if (entity.getId() == null) {
            message = message + "User doesn't exist!";
            throw new ValidationException(message);
        } else {
            if (entity.getFirstName().length() == 0) {
                message = message + "First name can't be an empty string!";
            }

            if (entity.getLastName().length() == 0) {
                message = message + "Last name can't be an empty string!";
            }

            if (message.length() > 0) {
                throw new ValidationException(message);
            }
        }
    }
}
