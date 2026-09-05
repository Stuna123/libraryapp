package com.libraryapp.controller;

import com.libraryapp.entity.Borrowing;
import com.libraryapp.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

/**
 * Controller responsible for borrowing actions and displaying
 * the user's borrowings page.
 */

@Controller
@RequiredArgsConstructor
public class BorrowingController {
    private final BorrowingService borrowingService;

    @GetMapping("/borrowings")
    public String showUserBorrowings(Principal principal, Model model) {
        String userEmail = principal.getName();

        List<Borrowing> borrowings = borrowingService.getBorrowingsByUserEmail(userEmail);

        model.addAttribute("borrowings", borrowings);

        return "borrowings";
    }

    @PostMapping("/borrow/books/{id}")
    public String borrowBook(
            @PathVariable Long id,
            @RequestParam int numberOfDays,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            String userEmail = principal.getName();
            borrowingService.borrowBook(id, userEmail, numberOfDays);
            redirectAttributes.addFlashAttribute("successMessage", "Livre emprunté avec succès !");

            return "redirect:/borrowings";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/books/" + id;
        }
    }

    @PostMapping("/borrow/return/{id}")
    public String returnBook(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            String userEmail = principal.getName();
            borrowingService.returnBook(id, userEmail);

            redirectAttributes.addFlashAttribute("successMessage", "Livre retourné avec succès !");
            return "redirect:/borrowings";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/borrowings";
        }
    }
}
