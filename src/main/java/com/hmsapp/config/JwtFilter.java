package com.hmsapp.config;

import com.hmsapp.entity.User;
import com.hmsapp.repository.UserRepository;
import com.hmsapp.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    // TODO: Implement JWT filter to validate and extract token from request headers.
    // Then, use the token to verify its validity and retrieve user information.
    private JWTService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JWTService jwtService,
                     UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
       // System.out.println(token);
        if(token!=null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(8,token.length()-1);
            System.out.println(jwtToken);
            String username = jwtService.getUsername(jwtToken);
            System.out.println(username);
            Optional<User> opUsername = userRepository.findByUsername(username);
            System.out.println(opUsername.isPresent());

            if (opUsername.isPresent()) {

                User user = opUsername.get();
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton( new SimpleGrantedAuthority(user.getRole())));

                userToken.setDetails(new WebAuthenticationDetails(request));

                //SGS
                SecurityContextHolder.getContext().setAuthentication(userToken);//checks whether the user can access this URL or not, using SecurityConfig file
            }

        }
        //whatever the incoming request is coming will be intern sent to internal filter mechanism of springSecurity
        filterChain.doFilter(request, response);

    }



}
