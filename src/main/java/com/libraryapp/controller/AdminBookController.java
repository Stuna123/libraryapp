package com.libraryapp.controller;

import com.libraryapp.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.libraryapp.form.BookForm;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller responsible for admin book management pages.
 *
 * It displays the book list, shows the book creation/edit form
 * and handles book creation and update and deletion from the admin area.
 */

@Controller
@RequiredArgsConstructor

public class AdminBookController {
    private final BookService bookService;

    @GetMapping("/admin/books")
    public String showAdminBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin-books";
    }

    @GetMapping("/admin/books/new")
    public String showCreateBookForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        model.addAttribute("categories", bookService.getAllCategories());

        return "admin-book-form";
    }

    @GetMapping("/admin/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("bookForm", bookService.getBookFormById(id));
        model.addAttribute("categories", bookService.getAllCategories());
        model.addAttribute("bookId", id);

        return "admin-book-form";
    }

    @PostMapping("/admin/books")
    public String createBook(
            @Valid @ModelAttribute("bookForm") BookForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", bookService.getAllCategories());
            return "admin-book-form";
        }

        try {
            bookService.createBook(form);
            redirectAttributes.addFlashAttribute("successMessage", "Le livre a été ajouté avec succès !");
            return "redirect:/admin/books";
        } catch (RuntimeException ex) {
            bindingResult.rejectValue("isbn", "isbn.exists", ex.getMessage());
            model.addAttribute("categories", bookService.getAllCategories());

            return "admin-book-form";
        }
    }

    @PostMapping("/admin/books/edit/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute("bookForm") BookForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", bookService.getAllCategories());
            model.addAttribute("bookId", id);
            return "admin-book-form";
        }

        try {
            bookService.updateBook(id, form);
            redirectAttributes.addFlashAttribute("successMessage", "Le livre a été modifié avec succès !");
            return "redirect:/admin/books";

        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("categories", bookService.getAllCategories());
            model.addAttribute("bookId", id);
            return "admin-book-form";
        }
    }

    @PostMapping("/admin/books/delete/{id}")
    public String deleteBook(
       @PathVariable Long id,
       RedirectAttributes redirectAttributes
    ) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Le livre a été supprimé avec succès !");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/books";
    }
}