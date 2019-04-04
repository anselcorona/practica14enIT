package com.ansxl.practica14.Repositorios;

import com.ansxl.practica14.Modelos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select user from User user where user.email = :email and user.password = :password")
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query("select count(user) from User user")
    Integer cantidad();

    @Query(value = "SELECT * FROM user m offset(?1) limit(?2)", nativeQuery = true)
    List<User> pagination(int offset, int limit);
}
