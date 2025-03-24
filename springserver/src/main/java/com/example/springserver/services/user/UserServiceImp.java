package com.example.springserver.services.user;

import com.example.springserver.dto.UserDto;
import com.example.springserver.models.User;
import com.example.springserver.models.mapper.UserMapper;
import com.example.springserver.repositories.UserRepository;
import com.example.springserver.services.EmailService;
import com.example.springserver.services.VerificationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<User> getUserById(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        return user;
    }

    @Override
    @Transactional
    public Optional<User> getUserByUsername(String username) {
        return this.userRepository.findUsersByUsername(username);
    }

    @Override
    @Transactional
    public Optional<User> getUserByFullName(String fullName) {
        return this.userRepository.findUsersByFullName(fullName);
    }

    @Override
    @Transactional
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findUsersByEmailAddress(email);
    }

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        User newUser = UserMapper.INSTANCE.userDtoToUser(userDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setVerified(false);
        VerificationService verificationService = new VerificationService(userRepository);
        verificationService.sendVerificationEmail(newUser);
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserDto updatedUserDto) {
        this.userRepository.findById(userId);
        User updatedUser = UserMapper.INSTANCE.userDtoToUser(updatedUserDto);
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        updatedUser.setUserId(userId);
        this.userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void patchUser(Long userId, Map<String, Object> updates) {
        Optional<User> user = this.userRepository.findById(userId);

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            switch (field) {
                case "full_name":
                    user.get().setFullName((String) value);
                    break;
                case "username":
                    user.get().setUsername((String) value);
                    break;
                case "email_address":
                    user.get().setEmailAddress((String) value);
                    break;
                case "password":
                    user.get().setPassword((String) value);
                    break;
                default:
                    break;
            }
        }
        this.userRepository.save(user.get());
    }


    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            this.userRepository.deleteById(userId);
        } else {
            System.out.println("No user with given Id");
        }
    }

    public boolean isEmailAddressAlreadyUsed(String emailAddress){
        return userRepository.existsUserByEmailAddress(emailAddress);
    }

}
