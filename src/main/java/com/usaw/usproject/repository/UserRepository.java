package com.usaw.usproject.repository;

import com.usaw.usproject.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);
}
