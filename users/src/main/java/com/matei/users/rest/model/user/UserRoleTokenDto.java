package com.matei.users.rest.model.user;

import com.matei.users.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleTokenDto {

    @NotNull
    private String username;
    @NotNull
    private Role role;
    @NotNull
    private String authorizationToken;
}
