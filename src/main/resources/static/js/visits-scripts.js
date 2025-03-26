function deleteVisit(button) {
    const row = button.closest('tr');
    const visitId = row.dataset.visitId;
    if (confirm('Вы уверены, что хотите удалить этот визит?')) {
        deleteVisitFromDatabase(visitId)
            .then(response => {
                console.log('Визит удален:', response);
                row.remove(); // Удаляем строку из таблицы
                alert('Визит успешно удален!');
                location.reload();
            })
            .catch(error => {
                console.error('Ошибка при удалении:', error);
                alert('Произошла ошибка при удалении визита.');
            });
    }
}

// Функция для отправки запроса на удаление
function deleteVisitFromDatabase(visitId) {
    return fetch(`/visits/delete/${visitId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка сети');
            }
            return response.text();
        });
}