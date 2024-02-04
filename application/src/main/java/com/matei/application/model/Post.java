package com.matei.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String postText;

    // this reference is only used for orphanRemoval
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "replyingTo", orphanRemoval = true)
    private List<Comment> comments;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private long createdDate;

    @LastModifiedDate
    @Column(updatable = false, nullable = false)
    private long modifiedDate;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy;
}
