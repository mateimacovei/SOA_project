package com.matei.application.rest.model.user;

import com.matei.application.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAndRoleDto {

    @NotNull
    private String username;
    @NotNull
    private Role role;
}
