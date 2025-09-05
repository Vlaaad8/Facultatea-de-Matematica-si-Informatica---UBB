package org.example.domain.validation;

import org.example.domain.User;

public class UserValidation implements Validation<User>{
    @Override
    public void validate(User entity){
        if(entity.getFirstName().isEmpty() || entity.getLastName().isEmpty()){
            throw new ValidationException("First Name and Last Name cannot be empty");
        }

    }

}
