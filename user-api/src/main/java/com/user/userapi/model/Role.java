package com.user.userapi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity(name = "Role")
@Table(name = "role")
public class Role implements Serializable {
   static final int NAME_MAX = 20;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @NotEmpty
   @Size(max = NAME_MAX)
   @Column(name = "name", length = NAME_MAX)
   private String name;

   @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
   private Set<User> users;
}