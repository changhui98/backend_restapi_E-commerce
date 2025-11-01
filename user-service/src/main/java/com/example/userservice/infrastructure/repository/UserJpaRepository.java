package com.example.userservice.infrastructure.repository;

import com.example.userservice.domain.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByUserId(String userId);

    Optional<UserEntity> findByEmail(String username);
}