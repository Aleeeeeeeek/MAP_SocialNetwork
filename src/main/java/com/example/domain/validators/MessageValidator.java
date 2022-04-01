
package com.example.domain.validators;

import com.example.domain.Message;

import java.time.LocalDate;

public class MessageValidator implements Validator<Message> {
    public MessageValidator() {
    }

    public void validate(Message entity) throws ValidationException {
        String message = "";
        if (entity == null) {
            message += "Entity doesn't exist!";
            throw new ValidationException(message);
        } else {
            if (entity.getMessage().length() == 0) {
                message += "The message cannot pe empty!";
            }
            if (entity.getData().getYear() < 1950 || entity.getData().getYear() > LocalDate.now().getYear()) {
                message += "The year should be over 1950 and less then the current year!";
            }
            if (message.length() > 0) {
                throw new ValidationException(message);
            }
        }
    }
}
