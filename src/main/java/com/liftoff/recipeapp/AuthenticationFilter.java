package com.liftoff.recipeapp;

import com.liftoff.recipeapp.controllers.AuthenticationController;
import com.liftoff.recipeapp.data.UserRepository;
import com.liftoff.recipeapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/login", "/register", "/logout", "/css");

    private static boolean isWhiteListed(String path){
        for(String pathRoot : whitelist){
            if(path.startsWith(pathRoot)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException{

        if(isWhiteListed(request.getRequestURI())){
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        if(user != null){
            return true;
        }

        response.sendRedirect("/login");
        return false;
    }
}


