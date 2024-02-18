package com.finance.manager.security.model;

import com.finance.manager.security.util.TokenType;

public record AuthResponseModel(String accessToken, Integer accessTokenExpiry, String userName, TokenType tokenType) {
}
