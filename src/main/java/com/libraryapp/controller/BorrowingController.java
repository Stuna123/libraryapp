package com.libraryapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *  Controller responsible for displaying the user's borrowings page.
 *
 *  For now, this page is temporary. The real borrowing history will be added
 *  later when the borrowing feature is implemented.
 */

@Controller
public class BorrowingController {
    @GetMapping("/borrowings")
    public String showUserBorrowings() {
        return "borrowings";
    }
}
