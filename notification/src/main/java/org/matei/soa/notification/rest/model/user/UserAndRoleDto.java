package org.matei.soa.notification.rest.model.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matei.soa.notification.model.Role;

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
