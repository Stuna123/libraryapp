package com.libraryapp.controller;

import com.libraryapp.form.RegisterForm;
import com.libraryapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller responsible for user registration
 *
 * It displays the registration form and handles the submitted data
 */

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(form);
        } catch (RuntimeException ex) {
            bindingResult.rejectValue("email", "email.exists", ex.getMessage());
            return "register";
        }

        return "redirect:/login?registered";
    }
}
