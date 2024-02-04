package com.matei.users.service;

import com.matei.users.jwt.JwtService;
import com.matei.users.model.Role;
import com.matei.users.model.User;
import com.matei.users.repo.UserRepository;
import com.matei.users.rest.model.user.UserAndRoleDto;
import com.matei.users.rest.model.user.UserRoleTokenDto;
import com.matei.users.rest.model.user.UserWithPasswordDto;
import com.matei.users.service.external.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.matei.users.service.TextBoardException.conflict;
import static com.matei.users.service.TextBoardException.notFound;


@Component
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncodingService passwordEncodingService;
    private final UserRepository userRepository;

    private final PostsService postsService;
    private final JwtService jwtService;

    private User findOne(String username) {
        return userRepository.findById(username).orElseThrow(() -> notFound("User " + username + " does not exist"));
    }

    public UserRoleTokenDto getAuthToken(UserWithPasswordDto usernamePassword) {
        var found = findOne(usernamePassword.getUsername());
        passwordEncodingService.throwIfNotMatching(found.getHashedPassword(), usernamePassword.getPassword());
        String token = jwtService.generateToken(found.getUsername());
        return UserRoleTokenDto.builder()
                .username(found.getUsername())
                .role(found.getRole())
                .authorizationToken(token)
                .build();
    }

    public UserAndRoleDto getUserDetails() {
        return UserAuthorizationService.getCurrentUserDetails();
    }

    /**
     * @return authorization token for the new user
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserRoleTokenDto addUser(UserWithPasswordDto usernamePassword) {
        var found = userRepository.findById(usernamePassword.getUsername());
        if (found.isPresent()) {
            throw conflict("There is already a user with this username");
        }
        Role role = Role.USER;
        if (userRepository.count() == 0L) {
            role = Role.ADMIN;
        }

        var toSave = User.builder()
                .username(usernamePassword.getUsername())
                .hashedPassword(passwordEncodingService.encodePassword(usernamePassword.getPassword()))
                .role(role)
                .build();
        var saved = userRepository.save(toSave);
        String token = jwtService.generateToken(saved.getUsername());
        return UserRoleTokenDto.builder()
                .username(saved.getUsername())
                .role(saved.getRole())
                .authorizationToken(token)
                .build();
    }

    /**
     * Used by a user to delete their own account or by an admin to delete another user (the role validation will take
     * place inside the function)
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(String userToDelete) {
        var currentUser = UserAuthorizationService.getCurrentUserDetails();
        if (!currentUser.getUsername().equals(userToDelete)) {
            UserAuthorizationService.throwIfNotAdmin(currentUser);
        }

        var userToDeleteFromRepo = findOne(userToDelete);
        if (userToDeleteFromRepo.getRole() == Role.ADMIN) {
            if (!userRepository.existsByUsernameNotAndRole(currentUser.getUsername(), Role.ADMIN)) {
                throw conflict("Can not delete the last admin user");
            }
        }
        postsService.handleDeletedUser(userToDelete);
        userRepository.deleteById(userToDelete);
    }

    // for the following methods, it has already been confirmed that the current user is admin
    public void addAsAdmin(String username) {
        var found = findOne(username);
        found.setRole(Role.ADMIN);
        userRepository.save(found);
    }

    public void removeAsAdmin(String username) {
        var found = findOne(username);

        if (!userRepository.existsByUsernameNotAndRole(username, Role.ADMIN)) {
            throw conflict("Can not remove the admin role from the last user who has it");
        }
        found.setRole(Role.USER);
        userRepository.save(found);
    }
}
