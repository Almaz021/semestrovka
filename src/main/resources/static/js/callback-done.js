$(document).ready(function () {
    $('.mark-done-btn').click(function () {
        const button = $(this);
        const id = button.data('id');

        $.ajax({
            url: `/operator/callback/${id}/mark-done`,
            type: 'POST',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            },
            success: function () {
                const row = button.closest('tr');
                row.find('.status').text('DONE');
                row.find('.action').html('✔');
            },
            error: function () {
                alert('Ошибка при обновлении статуса');
            }
        });
    });
});