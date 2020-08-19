package com.pedsf.library.userapi.model;

import com.pedsf.library.Parameters;
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

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   @NotNull
   @NotEmpty
   @Size(max = Parameters.ROLE_MAX)
   @Column(name = "name", length = Parameters.ROLE_MAX)
   private String name;

   @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
   private Set<User> users;

   public Role(Integer id, @NotNull @NotEmpty @Size(max = Parameters.ROLE_MAX) String name) {
      this.id = id;
      this.name = name;
   }

   public Role() {
      // empty constructor for creating empty role and add attribute after
   }
}