package com.matei.users.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "textBoardUser")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User {

    @Id
    @EqualsAndHashCode.Include
    private String username;

    @NotNull
    private String hashedPassword;

    @NotNull
    @Column(name = "textBoardRole")
    private Role role;
}
