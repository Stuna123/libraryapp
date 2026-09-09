package com.libraryapp.controller;

import com.libraryapp.entity.Borrowing;
import com.libraryapp.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 *  Controller responsible for admin borrowing management pages.
 *
 *  It allows administrators to view all borrowings made by users.
 */

@Controller
@RequiredArgsConstructor

public class AdminBorrowingController {

    private final BorrowingService borrowingService;

    @GetMapping("/admin/borrowings")
    public String showAdminBorrowings(Model model) {
        List<Borrowing> borrowings = borrowingService.getAllBorrowings();

        model.addAttribute("borrowings", borrowings);

        return "admin-borrowings";
    }

}
