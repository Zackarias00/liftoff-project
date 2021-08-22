package com.liftoff.recipeapp.controllers;

import com.liftoff.recipeapp.data.UserRepository;
import com.liftoff.recipeapp.models.dto.LoginFormDTO;
import com.liftoff.recipeapp.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.liftoff.recipeapp.models.User;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session){
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if(userId == null){
            return null;
        }

        Optional<User> user = userRepository.findById(userId);


        if(user.isEmpty()){
            return null;
        }

        return user.get();
    }


    private static void setUserInSession(HttpSession session, User user){
        session.setAttribute(userSessionKey,user.getId());
    }

    @GetMapping("//TODO:")
    public String displayRegistrationForm(Model model){
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title","Register");
        return "register";
    }

    @PostMapping("//TODO:")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO, Errors errors, HttpServletRequest request, Model model){


        if(errors.hasErrors()){
            model.addAttribute("title","Register");
            return "register";
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        if(existingUser != null){
            errors.rejectValue("username","username.alreadyExists", "A user with that username already exists");
            model.addAttribute("title","Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if(!password.equals(verifyPassword)){
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:";
    }

    @GetMapping("//TODO1:")
    public String displayLoginForm(Model model){
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "";
    }

    @PostMapping("//TODO1:")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO, Errors errors, HttpServletRequest request, Model model){
        if(errors.hasErrors()){
            model.addAttribute("title", "Log In");
            return "";
        }

        User theUser = userRepository.findByUsername((loginFormDTO.getUsername()));

        if(theUser == null){
            errors.rejectValue("username", "user.invalid", "The given username does not exist.");
            model.addAttribute("title","Log In");
            return "";
        }

        String password = loginFormDTO.getPassword();

        if(!theUser.isMatchingPassword(password)){
            errors.rejectValue("password","password.invalid","Invalid password");
            model.addAttribute("title", "Log In");
            return "";
        }

        setUserInSession(request.getSession(), theUser);

        return "";

    }


    @GetMapping("")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "";
    }


}
