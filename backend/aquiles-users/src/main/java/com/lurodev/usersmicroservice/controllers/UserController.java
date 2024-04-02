package com.lurodev.usersmicroservice.controllers;

import com.lurodev.usersmicroservice.models.dto.RequestResponse;
import com.lurodev.usersmicroservice.models.dto.UserDTO;
import com.lurodev.usersmicroservice.models.User;
import com.lurodev.usersmicroservice.services.IUserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    ResponseEntity<RequestResponse> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        RequestResponse response = new RequestResponse(users, true, HttpStatus.OK.value(), "Users list");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponse> getUserById(@PathVariable("id") Long id){
        UserDTO user = userService.getUserById(id);
        RequestResponse response = new RequestResponse(null, false, HttpStatus.NOT_FOUND.value(), "User not found");

        if(user != null){
            response = new RequestResponse(user, true, HttpStatus.OK.value(), "User by id " + id);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/params")
    public ResponseEntity<RequestResponse> getAllUsersByParams(@RequestParam("regionalId") @Nullable Short regionalId,
                                                               @RequestParam("isActive") @Nullable Boolean isActive){
        RequestResponse response = null;
        List<UserDTO> users = null;

        if(regionalId != null){
            users = userService.getUsersByRegionalId(regionalId);
            response = new RequestResponse(users, true, HttpStatus.OK.value(), "Regional users by tenant id");
        } else if (isActive != null) {
            users = userService.getActiveUsers(isActive);
            response = new RequestResponse(users, true, HttpStatus.OK.value(), "Active users by tenant id");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RequestResponse> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PutMapping
    public ResponseEntity<RequestResponse> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<RequestResponse> deleteUserById(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
