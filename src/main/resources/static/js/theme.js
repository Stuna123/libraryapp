document.addEventListener("DOMContentLoaded", function () {
    const themeToggleButton = document.getElementById("themeToggle");
    const body = document.body;

    const savedTheme = localStorage.getItem("libraryapp-theme");

    if (savedTheme === "dark") {
        body.classList.add("dark-mode");
        updateButtonText("dark");
    } else {
        updateButtonText("light");
    }

    if (themeToggleButton) {
        themeToggleButton.addEventListener("click", function () {
            body.classList.toggle("dark-mode");

            if (body.classList.contains("dark-mode")) {
                localStorage.setItem("library-theme", "dark");
                updateButtonText("dark")
            } else {
                localStorage.setItem("libraryapp-theme", "light");
                updateButtonText("light");
            }
        });
    }

    function updateButtonText(theme) {
        if (!themeToggleButton) {
            return;
        }

        if (theme === "dark") {
            themeToggleButton.textContent = "Mode clair";
        } else {
            themeToggleButton.textContent = "Mode sombre";
        }
    }
})