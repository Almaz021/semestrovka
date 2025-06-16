async function loadFavorites(page = 0, size = 5) {
    try {
        const response = await fetch(`/api/favorites?page=${page}&size=${size}`);
        if (!response.ok) throw new Error('Ошибка при загрузке избранных квартир');

        const favorites = await response.json();
        const container = document.getElementById('favorites-container');
        container.innerHTML = '';

        if (favorites.content.length === 0) {
            container.innerHTML = '<p>У вас пока нет избранных квартир.</p>';
            return;
        }

        favorites.content.forEach(apartment => {
            const card = document.createElement('div');
            card.className = 'apartment-card';
            card.onclick = () => window.location.href = `/apartments/${apartment.id}`;
            card.innerHTML = `
                    <h2>${apartment.title}</h2>
                    <p>${apartment.description}</p>
                    <p>${apartment.address}</p>
                    <p>Цена: ${apartment.price} ₽</p>
                    <p>Комнат: ${apartment.rooms}</p>
                    <p>Площадь: ${apartment.area} кв.м.</p>
                    <p>Этаж: ${apartment.floor}</p>
                `;
            container.appendChild(card);
        });
    } catch (error) {
        document.getElementById('favorites-container').innerHTML =
            `<p style="color:red;">${error.message}</p>`;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadFavorites();
});