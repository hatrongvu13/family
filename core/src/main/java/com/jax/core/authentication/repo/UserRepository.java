package com.jax.core.authentication.repo;

import com.jax.core.authentication.dvo.UserDVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDVO, Integer> {
    Optional<UserDVO> findByEmail(String email);
}
