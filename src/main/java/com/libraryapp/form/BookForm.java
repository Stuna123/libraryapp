package com.libraryapp.form;

import com.libraryapp.entity.BookCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  Form object used to collect and validate book data
 *  from the admin book form.
 *
 *  This class does not represent a database table.
 *  It only carries data submitted from the form.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BookForm {
    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "L'auteur est obligatoire")
    private String author;

    @NotBlank(message = "Le ISBN est obligatoire")
    private String isbn;

    @NotNull(message = "La catégorie est obligatoire")
    private BookCategory category;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 2000, message = "La description ne doit pas dépasser 2000 caractères")
    private String description;

    @Size(max = 500, message = "L'URL de l'image ne doit pas dépasser 500 caractères")
    private String imageUrl;

    @NotNull(message = "Le nombre total d'exemplaires est obligatoire")
    @Min(value = 1, message = "Le nombre total d'exemplaires doit être au moins égal à 1")
    private Integer totalCopies;
}
