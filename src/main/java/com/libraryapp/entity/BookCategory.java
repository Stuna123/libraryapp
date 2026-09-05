package com.libraryapp.entity;

/**
 *  Represents the category of a book
 *  These values are used to classify books and filter the catalog
 */

public enum BookCategory {
    NOVEL("Romans"),
    SCIENCE("Sciences"),
    HISTORY("Histoire"),
    COMPUTER_SCIENCE("Informatique"),
    PERSONAL_DEVELOPMENT("Développement personnel"),
    THEOLOGY("Théologie"),
    OTHER("Autres");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
