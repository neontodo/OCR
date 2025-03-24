package com.example.springserver.controllers;

import com.example.springserver.dto.UserDto;
import com.example.springserver.models.User;
import com.example.springserver.services.user.UserServiceImp;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImp userService;

    @ApiOperation("Get all users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @ApiOperation("Get user by ID")
    @GetMapping("find-user-by-id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return this.userService.getUserById(userId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation("Get user by their Full Name")
    @GetMapping("find-user-by-full-name/{fullName}")
    public ResponseEntity<User> getUserByFullName(@PathVariable String fullName){
        return this.userService.getUserByFullName(fullName).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation("Get user by their username")
    @GetMapping("find-user-by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return this.userService.getUserByUsername(username).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation("Get user by their email address")
    @GetMapping("find-user-by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        return this.userService.getUserByEmail(email).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //CREATE
    @ApiOperation("Register a new user")
    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto){
        if(userService.isEmailAddressAlreadyUsed(userDto.getEmailAddress())){
            return ResponseEntity.badRequest().body("Email address is already registered");
        } else {
            userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully");
        }
    }

    //UPDATE
    @ApiOperation("Update an existing user")
    @PutMapping("/update-user/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserDto updatedUserDto){
        this.userService.updateUser(userId, updatedUserDto);
        return ResponseEntity.ok("The user has been updated");
    }

    //DELETE
    @ApiOperation("Delete an existing user")
    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId){
        this.userService.deleteUserById(userId);
        return ResponseEntity.ok("The user has been deleted");
    }

    //PATCH
    @ApiOperation("Partially update an existing user")
    @RequestMapping(value = "/user-patch/{userId}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdateUserPassword(@RequestBody Map<String, Object> updates, @PathVariable("userId") Long userId){
        this.userService.patchUser(userId, updates);
    }
}
