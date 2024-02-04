package com.matei.users.rest;

import com.matei.users.rest.model.user.UserAndRoleDto;
import com.matei.users.rest.model.user.UserRoleTokenDto;
import com.matei.users.rest.model.user.UserWithPasswordDto;
import com.matei.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public UserRoleTokenDto login(@RequestBody @Valid UserWithPasswordDto usernamePassword) {
        return userService.getAuthToken(usernamePassword);
    }

    @GetMapping("/details")
    public UserAndRoleDto getDetails() {
        return userService.getUserDetails();
    }

    @PostMapping("/new")
    public UserRoleTokenDto createUser(@RequestBody @Valid UserWithPasswordDto createDto) {
        return userService.addUser(createDto);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

    @PostMapping("/adminRole/add/{username}")
    public void addAsAdmin(@PathVariable String username) {
        userService.addAsAdmin(username);
    }

    @DeleteMapping("/adminRole/remove/{username}")
    public void removeAsAdmin(@PathVariable String username) {
        userService.removeAsAdmin(username);
    }

}
