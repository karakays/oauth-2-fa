package com.karakays.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karakays.app.domain.UserCredentials;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    UserCredentials findByUsername(String username);
}
