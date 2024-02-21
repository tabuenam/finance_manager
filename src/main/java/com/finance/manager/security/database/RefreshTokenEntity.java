package com.finance.manager.security.database;

import com.finance.manager.user.database.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "refresh_token", length = 20000, unique = true)
    @NotNull
    private String refreshToken;

    @Column(name= "revoked")
    private Boolean revoked;

    @ManyToOne
    @JoinColumn(name= "user_id", referencedColumnName = "id")
    private UserEntity user;
}
