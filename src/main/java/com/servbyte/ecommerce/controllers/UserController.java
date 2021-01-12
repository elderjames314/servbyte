package com.servbyte.ecommerce.controllers;

import com.servbyte.ecommerce.dtos.ApplicationUserDto;
import com.servbyte.ecommerce.dtos.ApplicationUserLoginDto;
import com.servbyte.ecommerce.service.UserService;
import com.servbyte.ecommerce.utility.AuthenticatedUser;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody ApplicationUserDto userDTO){
        userService.registerUser(userDTO);
        return ResponseEntity.ok("User saved successfully");
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findByUserName(@PathVariable String username){
        return ResponseEntity.ok(userService.findUserByEmail(username));
    }

    @GetMapping
    public ResponseEntity<?> fetchAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/auth")
    public ResponseEntity<?> generateToken(@RequestBody ApplicationUserLoginDto loginDTO) {
        return ResponseEntity.ok(userService.generateToken(loginDTO));
    }


    @GetMapping("/loggedUser")
    public ResponseEntity<?>  getLoggedInUser() {
        return ResponseEntity.ok(AuthenticatedUser.getLoggedInUser());
    }

   
}
