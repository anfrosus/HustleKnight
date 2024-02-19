package com.hustleknight.app.infra.security

import com.hustleknight.app.infra.security.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//@EnableMethodSecurity(securedEnabled = true) -> secured 사용할거라면 ...
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler
) {


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
//            .sessionManagement{
//                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            }
            // BasicAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .httpBasic { it.disable() }
            // UsernamePasswordAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .formLogin { it.disable() }
            // CsrfFilter 제외
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/jobLauncher**",
                    "/favicon.ico",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/h2/**",
                    "/error",
                    "/sse/**"
//                    "/api/players/2/hunts"
//                    "api/users/reissue"
                ).permitAll()
                    .requestMatchers(ApiInfo.REGISTER_PLAYER.method, ApiInfo.REGISTER_PLAYER.uri).permitAll()
                    .requestMatchers(ApiInfo.LOGIN.method, ApiInfo.LOGIN.uri).permitAll()
                    .anyRequest().authenticated()
            }
            //jwtAuthFilter 추가
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

//            .authorizeHttpRequests { it.requestMatchers("").authenticated() }
//            와 같이 설정가능

            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }

            .headers { it.disable() } //for h2
            .build()
    }
}