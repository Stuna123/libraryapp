package com.libraryapp.controller;

import com.libraryapp.entity.BookCategory;
import com.libraryapp.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  Controller responsible for public book pages.
 *  It handles the book catalog, category filtering and book details.
 */

@Controller
@RequiredArgsConstructor

public class BookController {
    private final BookService bookService;

    /**
     *
     * @param category
     * @param model
     * @return
     * Si category n’est pas null :
     *     récupérer les livres par catégorie
     *     ajouter selectedCategory au model
     * Sinon :
     *     récupérer tous les livres
     *
     * Dans tous les cas :
     *     ajouter toutes les catégories au model
     *     retourner la vue "books"
     */
    @GetMapping("/books")
    public String showBooks(@RequestParam(required = false) BookCategory category, Model model) {
        if(category != null) {
            model.addAttribute("books", bookService.getBooksByCategory(category));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }

        model.addAttribute("categories", bookService.getAllCategories());
        return "books";
    }

    /**
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/books/{id}")
    public String showBookDetails(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));

        return "book-details";
    }
}
