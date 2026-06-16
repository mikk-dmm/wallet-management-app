package com.example.wallet_management_app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wallet_management_app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}