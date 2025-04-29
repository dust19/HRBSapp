package com.hmsapp.config;

import com.hmsapp.service.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {
   private JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //h(cd)
        http.csrf().disable().cors().disable();
        //this tells that add this filter only for the Urls that comes with token
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
        //haap
        //http.authorizeHttpRequests().anyRequest().permitAll();
        http.authorizeHttpRequests()
                .requestMatchers("/api/auth/sign-up","/api/auth/login","/api/auth/property/sign-up","/api/v1/country/addCountry","/api/v1/city/addCity","/api/v1/property/{searchParam}","/api/v1/property/PropertyId/{propId}","/api/v1/property/get/property/images","/api/v1/bookings/search/rooms","/api/auth/log-in/generate-otp","/api/auth/verify-otp")
                .permitAll()

                .requestMatchers("/api/v1/property/addProperty","/api/v1/property/updateProperty/{id}")
                .hasRole("OWNER")

                .requestMatchers("/api/v1/property/deleteProperty/{propertyId}")
                .hasAnyRole("OWNER","ADMIN")

                .requestMatchers("/api/v1/review/addReview/{propertyId}","/api/v1/review/user/viewReviews","/api/v1/review/property/{propertyId}","/api/v1/review/delete/{reviewId}","/api/v1/images/upload/file/{bucketName}/property/{propertyId}","/api/v1/property/upload/file/{bucketName}/property/{propertyId}")
                .hasRole("USER")

                .requestMatchers("/api/auth/blog/sign-up","/api/v1/country/addCountry","/api/v1/city/deleteCountry","/api/v1/city/updateCountry",
                "/api/v1/city/addCity","/api/v1/city/deleteCity","/api/v1/city/updateCity")
                .hasRole("ADMIN")

                .anyRequest().authenticated();
        return http.build();
        //returns this object to spring security
    }
}
