document.addEventListener("DOMContentLoaded", function () {

    const bookCovers = document.querySelectorAll(".open-library-cover");
    function showPlaceholder(cover) {

        // Cache l'image qui n'a pas pu être chargée
        cover.classList.add("d-none");

        // Récupère le conteneur de l'image
        const wrapper = cover.parentElement;

        // Cherche le placeholder correspondant
        const placeholder = wrapper.querySelector(
            ".book-cover-placeholder, .book-detail-placeholder"
        );

        // Affiche le placeholder
        if (placeholder) {
            placeholder.classList.remove("d-none");
        }
    }


    bookCovers.forEach(function (cover) {
        // Cas 1 :
        // l'image échoue après l'ajout de l'écouteur
        cover.addEventListener("error", function () {
            showPlaceholder(cover);
        });

        // Cas 2 :
        // l'image avait déjà échoué avant l'exécution du JS
        if (cover.complete && cover.naturalWidth === 0) {
            showPlaceholder(cover);
        }
    });
});