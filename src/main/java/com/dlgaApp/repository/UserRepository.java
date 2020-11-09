package com.dlgaApp.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dlgaApp.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

   @Query("SELECT u FROM User u WHERE u.username = :username")
   public User getUserByUsername(@Param("username") String username);

public void save(org.springframework.security.core.userdetails.User u);

@Modifying
@Query(value="INSERT INTO users_roles (user_id,role_id) values (:username_id,1)", nativeQuery = true)
public void añadirRolUsuario(@Param("username_id") Long username_id);
	
}
