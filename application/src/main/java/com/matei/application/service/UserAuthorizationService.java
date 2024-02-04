package com.matei.application.service;

import com.matei.application.model.Role;
import com.matei.application.rest.model.user.UserAndRoleDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.matei.application.service.TextBoardException.forbidden;


public class UserAuthorizationService {

    private static Role getRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .findAny()
                .map(x -> Role.valueOf(x.getAuthority().replace("ROLE_", "")))
                .orElseThrow(() -> forbidden("No role found"));
    }

    public static UserAndRoleDto getCurrentUserDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return UserAndRoleDto.builder().username(authentication.getName()).role(getRole(authentication)).build();
    }

    public static void throwIfNotAdmin(UserAndRoleDto user) {
        if (user.getRole() != Role.ADMIN) {
            throw forbidden("Admin role required");
        }
    }

    /**
     * In multiple parts of the application, a operation can be done either by the owner of the entity, referenced in
     * the db row, or by an admin. This function checks if the username reference stored in the db matches the current
     * user, and if not requires the admin role
     *
     * @param ownerOfEntity username of the user who is the owner for a given entity
     * @throws TextBoardException if the current user is not the owner of the entity and does not have the admin role
     */
    public static void checkIfUserNameMatchingOrRequireAdmin(String ownerOfEntity) {
        var user = UserAuthorizationService.getCurrentUserDetails();
        if (!ownerOfEntity.equals(user.getUsername()) && user.getRole() != Role.ADMIN) {
            throw forbidden("Admin role required");
        }
    }
}
