package com.matei.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String commentText;
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Post replyingTo;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private long createdDate;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy;

}
