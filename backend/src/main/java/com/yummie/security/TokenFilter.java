package com.yummie.security;

import com.yummie.entity.RefreshTokenEntity;
import com.yummie.repository.RefreshTokenRepository;
import com.yummie.service.RefreshTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenUtil.getTokenFromCookie("access_token", request);
        if (accessToken != null) {
            try {
                tokenUtil.setContext(accessToken);
            } catch (JwtException e) {
                String refreshToken = tokenUtil.getTokenFromCookie("refresh_token", request);
                RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByValueAndExpiresAtAfter(refreshToken, LocalDateTime.now());
                if (refreshTokenEntity != null) {
                    refreshTokenService.renewRefreshToken(refreshTokenEntity);
                    accessToken = tokenUtil.generateJwt(refreshTokenEntity.getUserEntity());
                    tokenUtil.setContext(refreshTokenEntity.getUserEntity());
                    tokenUtil.setCookie("access_token", accessToken, response);
                    tokenUtil.setCookie("refresh_token", accessToken, response);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
