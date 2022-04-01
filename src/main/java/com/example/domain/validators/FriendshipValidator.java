
package com.example.domain.validators;

import com.example.domain.Friendship;

import java.time.LocalDate;

public class FriendshipValidator implements Validator<Friendship> {
    public FriendshipValidator() {
    }

    public void validate(Friendship entity) throws ValidationException {
        String message = "";
        if (entity == null) {
            message = message + "Friendship doesn't exist!";
            throw new ValidationException(message);
        } else {
            if (entity.getData().getYear() < 1950 || entity.getData().getYear() > LocalDate.now().getYear()) {
                message += "The year should be over 1950 and less then the current year!";
            }
            if (message.length() > 0) {
                throw new ValidationException(message);
            }
        }
    }
}
