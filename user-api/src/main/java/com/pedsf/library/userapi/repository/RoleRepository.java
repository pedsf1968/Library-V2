package com.pedsf.library.userapi.repository;


import com.pedsf.library.userapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
   Role getOne(Integer roleId);
   Role findByName(String name);

}
