package com.connectify.connectify.entity;

import com.connectify.connectify.enums.EAudience;
import com.connectify.connectify.enums.EPostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt;

    private String content;

    @Enumerated(EnumType.STRING)
    private EPostType type;

    @Enumerated(EnumType.STRING)
    private EAudience audience;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
