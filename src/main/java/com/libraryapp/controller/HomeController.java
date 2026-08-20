package com.libraryapp.controller;

import com.libraryapp.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *  Controller responsible for the public home page
 */
@Controller
@RequiredArgsConstructor

public class HomeController {
    private final BookService bookService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredBooks", bookService.getAllBooks().stream().limit(3).toList());

        return "index";
    }
}
