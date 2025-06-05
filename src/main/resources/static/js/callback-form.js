console.log('callback-form.js loaded');
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('callback-form');
    if (form) {
        form.addEventListener('submit', function(event) {
            event.preventDefault();

            const formData = new FormData(form);

            fetch(form.action, {
                method: 'POST',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        document.getElementById('callback-message').textContent = 'Запрос успешно отправлен!';
                        document.getElementById('callback-message').style.display = 'block';
                        document.getElementById('callback-error').style.display = 'none';
                        form.reset();
                    } else {
                        return response.text().then(text => { throw new Error(text || 'Ошибка'); });
                    }
                })
                .catch(error => {
                    document.getElementById('callback-error').textContent = error.message;
                    document.getElementById('callback-error').style.display = 'block';
                    document.getElementById('callback-message').style.display = 'none';
                });
        });
    }
});
console.log('submit intercepted');