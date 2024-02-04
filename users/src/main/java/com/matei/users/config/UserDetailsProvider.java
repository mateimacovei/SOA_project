package com.matei.users.config;

import com.matei.users.repo.UserRepository;
import com.matei.users.rest.model.user.UserAndRoleDto;
import com.matei.users.service.TextBoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matei.users.service.TextBoardException.forbidden;

@Component
@RequiredArgsConstructor
public class UserDetailsProvider {

    private final UserRepository userRepository;

    public UserAndRoleDto getUserDetails(String username) throws TextBoardException {
        var found = userRepository.findById(username)
                .orElseThrow(() -> forbidden("Can not authenticate non-existing user " + username));
        return UserAndRoleDto.builder().username(found.getUsername()).role(found.getRole()).build();
    }
}
