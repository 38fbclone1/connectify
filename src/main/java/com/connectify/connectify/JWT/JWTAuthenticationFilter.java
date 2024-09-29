package com.connectify.connectify.JWT;

import com.connectify.connectify.entity.Account;
import com.connectify.connectify.repository.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getToken(request);
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request,response);
            return;
        }
        setAuthenticationContext(token, request);
        filterChain.doFilter(request,response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        System.out.println(header);
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }

        return true;
    }

    private String getToken (HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    private void setAuthenticationContext (String token, HttpServletRequest request) {
        Account account = getAccount(token);

        UsernamePasswordAuthenticationToken authentication  = new UsernamePasswordAuthenticationToken(account, null , null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Account getAccount(String token) {
        String phoneNumber = jwtUtil.getSubject(token);
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(phoneNumber);
        Account account = optionalAccount.get();
        return account;
    }
}
