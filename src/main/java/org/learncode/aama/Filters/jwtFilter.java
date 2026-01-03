package org.learncode.aama.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.learncode.aama.service.UserDetailServiceImpl;
import org.learncode.aama.service.jwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class jwtFilter extends OncePerRequestFilter {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private jwtService jwtservice;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader("Authorization");
        String token=null;
        String username=null;
        if(authToken!=null && authToken.startsWith("Bearer ")){
            token=authToken.substring(7);
            username=jwtservice.extractUsername(token);

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails userdetail = context.getBean(UserDetailServiceImpl.class).loadUserByUsername(username);
                if(jwtservice.validateToken(token, userdetail)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                            userdetail,null,userdetail.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
            }

        }
        filterChain.doFilter(request,response);
    }
    }

