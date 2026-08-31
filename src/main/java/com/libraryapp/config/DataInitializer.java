package com.libraryapp.config;

import com.libraryapp.entity.AppUser;
import com.libraryapp.entity.Book;
import com.libraryapp.entity.BookCategory;
import com.libraryapp.entity.Role;
import com.libraryapp.repository.AppUserRepository;
import com.libraryapp.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminIfNotExists();
        createBooksIfNotExists();
    }

    private void createAdminIfNotExists() {
        if (appUserRepository.existsByEmail("admin@libraryapp.com")) {
            return;
        }

        AppUser admin = AppUser.builder()
                .firstName("Admin")
                .lastName("LibraryApp")
                .email("admin@libraryapp.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();

        appUserRepository.save(admin);
    }

    private void createBooksIfNotExists() {
        if (bookRepository.count() > 0) {
            return;
        }

        bookRepository.saveAll(List.of(
                Book.builder()
                        .title("Clean Code")
                        .author("Robert C. Martin")
                        .isbn("9780132350884")
                        .category(BookCategory.COMPUTER_SCIENCE)
                        .description("Un livre de référence sur l'écriture d'un code propre, lisible et maintenable.")
                        .imageUrl("")
                        .totalCopies(5)
                        .availableCopies(5)
                        .build(),

                Book.builder()
                        .title("Effective Java")
                        .author("Joshua Bloch")
                        .isbn("9780134685991")
                        .category(BookCategory.COMPUTER_SCIENCE)
                        .description("Un ouvrage avancé pour écrire du Java robuste, clair et professionnel.")
                        .imageUrl("")
                        .totalCopies(4)
                        .availableCopies(4)
                        .build(),

                Book.builder()
                        .title("Perry Mason")
                        .author("Erle Stanley Gardner")
                        .isbn("9782702428443")
                        .category(BookCategory.NOVEL)
                        .description("Rien ne prédisposait le très sérieux avocat Erle Stanley Gardner, du barreau de Californie, à se lancer dans la littérature policière, si ce n'est, peut-être, sa propre pratique juridique...")
                        .imageUrl("")
                        .totalCopies(10)
                        .availableCopies(10)
                        .build()
        ));
    }
}