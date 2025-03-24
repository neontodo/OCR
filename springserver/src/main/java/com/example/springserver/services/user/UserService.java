package com.example.springserver.services.user;

import com.example.springserver.dto.UserDto;
import com.example.springserver.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    public List<User> getAllUsers();
    public Optional<User> getUserById(Long userId);
    public Optional<User> getUserByUsername(String username);
    public Optional<User> getUserByFullName(String fullName);
    public Optional<User> getUserByEmail(String email);
    public void registerUser(UserDto userDto);
    public void updateUser(Long userId, UserDto updatedUserDto);
    public void patchUser(Long userId, Map<String, Object> updates);
    public void deleteUserById(Long userId);
}
