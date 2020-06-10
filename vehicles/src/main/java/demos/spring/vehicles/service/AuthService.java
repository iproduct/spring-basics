package demos.spring.vehicles.service;

import demos.spring.vehicles.model.User;

public interface AuthService {
    User register(User user);
    User login(String username, String password);
}
