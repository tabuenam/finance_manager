package com.finance.manager.user.model;

import com.finance.manager.user.roles.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserAccountDetailModel(String userName,
                                     String email,
                                     Role role,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt)
{ }
