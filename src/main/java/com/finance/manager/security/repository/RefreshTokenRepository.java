package com.finance.manager.security.repository;

import com.finance.manager.security.database.RefreshTokenEntity;
import jakarta.validation.constraints.NotNull;
import org.apache.el.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(@NotNull String refreshToken);
}
