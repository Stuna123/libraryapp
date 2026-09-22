document.addEventListener("DOMContentLoaded", function () {
    const themeToggleButton = document.getElementById("themeToggle");
    const body = document.body;

    const savedTheme = localStorage.getItem("libraryapp-theme");

    if (savedTheme === "dark") {
        body.classList.add("dark-mode");
        updateButtonText("dark");
    } else {
        body.classList.remove("dark-mode");
        updateButtonText("light");
    }

    if (themeToggleButton) {
        themeToggleButton.addEventListener("click", function () {
            const isDarkMode = body.classList.toggle("dark-mode");

            if (isDarkMode) {
                localStorage.setItem("libraryapp-theme", "dark");
                updateButtonText("dark");
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
});