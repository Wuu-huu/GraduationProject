package com.zzk.webapp.config;

import com.zzk.common.constant.SecurityConstants;
import com.zzk.common.security.JwtAuthenticationFilter;
import com.zzk.common.security.RestAccessDeniedHandler;
import com.zzk.common.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web 应用安全配置，统一接入 JWT 无状态认证。
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   RestAuthenticationEntryPoint authenticationEntryPoint,
                                                   RestAccessDeniedHandler accessDeniedHandler) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(registry -> {
                    for (String path : SecurityConstants.DEFAULT_WHITE_LIST) {
                        registry.requestMatchers(path).permitAll();
                    }
                    registry.requestMatchers(HttpMethod.GET, "/api/videos", "/api/videos/*", "/api/videos/*/stats",
                                    "/api/videos/*/parts", "/api/videos/*/category-tags",
                                    "/api/videos/*/comments", "/api/videos/*/danmakus",
                                    "/api/comments/*/replies",
                                    "/api/recommend/home", "/api/recommend/hot",
                                    "/api/recommend/videos/*/related", "/api/recommend/zones/*",
                                    "/api/videos/zone/*", "/api/video-series/*", "/api/video-series/*/videos",
                                    "/api/users/*/videos")
                            .permitAll();
                    registry.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
