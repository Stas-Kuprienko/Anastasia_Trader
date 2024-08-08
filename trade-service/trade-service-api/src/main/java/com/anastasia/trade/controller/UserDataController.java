package com.anastasia.trade.controller;

import com.anastasia.trade.controller.form.LogInUserForm;
import com.anastasia.trade.controller.form.SignUpUserForm;
import com.anastasia.trade.entities.user.User;
import com.anastasia.trade.model.UserDto;
import com.anastasia.trade.model.convertors.UserDtoConvertor;
import com.anastasia.trade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserDataController {

    private final UserService userService;
    private final UserDtoConvertor userDtoConvertor;

    @Autowired
    public UserDataController(UserService userService, UserDtoConvertor userDtoConvertor) {
        this.userService = userService;
        this.userDtoConvertor = userDtoConvertor;
    }


    @PostMapping("/user")
    public ResponseEntity<User> signUp(SignUpUserForm form) {
        User user = userService.createUser(
                form.login(),
                form.password(),
                form.name());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> logIn(LogInUserForm form) {
        User user = userService.findUserByLoginAndPassword(form.login(), form.password());
        return ResponseEntity.ok(userDtoConvertor.convert(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId);
        return ResponseEntity.ok(userDtoConvertor.convert(user));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByLogin(@RequestParam("login") String login) {
        User user = userService.findUserByLogin(login);
        return ResponseEntity.ok(userDtoConvertor.convert(user));
    }

}