package com.matei.users.rest.model.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithPasswordDto {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
