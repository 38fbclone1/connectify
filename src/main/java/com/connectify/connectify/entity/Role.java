package com.connectify.connectify.entity;

import com.connectify.connectify.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    private ERole name;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "roles_permisions",
//            joinColumns = @JoinColumn(name = "role_id"),
//            inverseJoinColumns = @JoinColumn(name = "permission_id")
//    )
//    @Enumerated(EnumType.STRING)
//    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts;
}
