package com.example.domain.validators;

import com.example.domain.FriendRequest;

import java.util.Objects;

public class FriendRequestValidator implements Validator<FriendRequest> {

    public FriendRequestValidator() {

    }

    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String message = "";
        if (entity == null) {
            message += "Entity doesn't exist!";
            throw new ValidationException(message);
        } else {
            if (!Objects.equals(entity.getStatus(), "approved") && !Objects.equals(entity.getStatus(), "pending")
                    && !Objects.equals(entity.getStatus(), "rejected")) {
                message += "The status should be either pending, rejected or approved!";
            }
            if (message.length() > 0) {
                throw new ValidationException(message);
            }
        }
    }
}
