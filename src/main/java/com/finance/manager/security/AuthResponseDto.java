package com.finance.manager.security;

public record AuthResponseDto(String accesToken, Integer acessTokenExpiry, String userName, TokenType tokenType) {
}
