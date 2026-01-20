package petstore.services;

import petstore.dto.UserRegistrationDto;
import petstore.models.entities.User;

import java.util.Optional;

public interface AuthService {
    void register(UserRegistrationDto registrationDTO);
    User getUser(String username);
}