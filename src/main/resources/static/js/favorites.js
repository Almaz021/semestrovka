document.addEventListener("DOMContentLoaded", function () {
    const button = document.getElementById("favorite-button");

    button.addEventListener("click", function () {
        const apartmentId = button.getAttribute("data-id");
        const isFavorite = button.getAttribute("data-is-favorite") === "true";

        const url = isFavorite
            ? `/favorites/${apartmentId}/remove`
            : `/favorites/${apartmentId}`;

        fetch(url, {
            method: 'POST',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.status === 401) {
                    window.location.href = "/login";
                    return;
                }
                if (!response.ok) {
                    alert("Ошибка при обновлении избранного");
                    return;
                }

                const newIsFavorite = !isFavorite;
                button.setAttribute("data-is-favorite", newIsFavorite.toString());
                button.textContent = newIsFavorite ? "Удалить из избранного" : "Добавить в избранное";
            })
            .catch(err => {
                console.error(err);
                alert("Ошибка соединения");
            });
    });
});