package com.webshoprest.api.v1.validators;

import com.webshoprest.api.v1.exceptions.UserNotFoundException;
import com.webshoprest.api.v1.util.ValidationErrorInspector;
import com.webshoprest.domain.User;
import com.webshoprest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        Map<String, String> errorsMap = new HashMap<>();

        if ((user.getPassword() != null && user.getConfirmPassword() != null) && !user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "", "passwords must match");
        }

        if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername())) {
            errors.rejectValue("username", "", "username already exists");
        }

        if(user.getEmail() != null && userRepository.existsByEmail(user.getEmail())){
            errors.rejectValue("email", "", "email already registered");
        }

        if(user.getAddress() == null){
            errors.rejectValue("address", "", "address cannot be null");
        }

        if(user.getAddress() != null && user.getAddress().getCity() == null){
            errors.rejectValue("city", "", "city cannot be null");
        }

        if(user.getAddress() != null && user.getAddress().getCity() != null && user.getAddress().getCity().getCountry() == null){
            errors.rejectValue("country", "", "country cannot be null");
        }

        ValidationErrorInspector.inspect(errors);

    }

    public void validateForUpdate(Object o, Errors errors) {
        User user = (User) o;

        Map<String, String> errorsMap = new HashMap<>();

        Optional<User> fromDbUser = userRepository.findById(user.getUserId());

        if(fromDbUser.isEmpty()){
            throw new UserNotFoundException();
        }

        //marry //marry

        if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername()) && !fromDbUser.get().getUsername().equals(user.getUsername())) {
            errors.rejectValue("username", "", "username already exists");
        }

        if(user.getEmail() != null && userRepository.existsByEmail(user.getEmail()) && !fromDbUser.get().getEmail().equals(user.getEmail())){
            errors.rejectValue("email", "", "email already registered");
        }

        if ((user.getPassword() != null && user.getConfirmPassword() != null) && !user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "", "passwords must match");
        }

        if(user.getAddress() == null){
            errors.rejectValue("address", "", "address cannot be null");
        }

        if(user.getAddress() != null && user.getAddress().getCity() == null){
            errors.rejectValue("city", "", "city cannot be null");
        }

        if(user.getAddress() != null && user.getAddress().getCity() != null && user.getAddress().getCity().getCountry() == null){
            errors.rejectValue("country", "", "country cannot be null");
        }

        ValidationErrorInspector.inspect(errors);

    }


}