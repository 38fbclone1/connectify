package com.connectify.connectify.entity;

import com.connectify.connectify.enums.EPermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

//@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "permissions")
public class Permission {
    @Id
    @Enumerated(EnumType.STRING)
    private EPermission name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}
