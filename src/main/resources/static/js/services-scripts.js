// Функция для переключения режима редактирования
function toggleEdit(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, textarea');
    const editBtn = row.querySelector('.edit-btn');
    const saveBtn = row.querySelector('.save-btn');
    inputs.forEach(input => {
        input.disabled = !input.disabled;
        input.style.width = '100%'; // Фиксируем ширину
    });
    editBtn.style.display = editBtn.style.display === 'none' ? 'inline-block' : 'none';
    saveBtn.style.display = saveBtn.style.display === 'none' ? 'inline-block' : 'none';
}

// Функция для сохранения изменений
function saveChanges(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, textarea');
    const editBtn = row.querySelector('.edit-btn');
    const saveBtn = row.querySelector('.save-btn');
    const deleteBtn = row.querySelector('.delete-btn');
    inputs.forEach(input => input.disabled = true);
    editBtn.style.display = 'inline-block';
    saveBtn.style.display = 'none';
    deleteBtn.style.display = 'inline-block';
    const serviceData = {
        id: row.dataset.serviceId,
        name: row.querySelector('input[name="name"]').value,
        description: row.querySelector('textarea[name="description"]').value,
        price: parseFloat(row.querySelector('input[name="price"]').value)
    };
    saveServiceToDatabase(serviceData).then(savedService => {
        alert('Изменения успешно сохранены!');
        updateRowWithSavedService(row, savedService);
    })
}

function saveServiceToDatabase(serviceData) {
    return fetch('/services/update', {
        method: 'POST', // Используем POST для отправки данных
        headers: {
            'Content-Type': 'application/json', // Указываем тип содержимого
        },
        body: JSON.stringify(serviceData) // Преобразуем объект в JSON
    }).then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json(); // Парсим ответ сервера (сохраненную сущность)
    });
}

// Функция для обновления строки с данными из сохраненной сущности
function updateRowWithSavedService(row, savedService) {
    row.dataset.serviceId = savedService.id;
    row.querySelector('input[name="name"]').value = savedService.name;
    row.querySelector('textarea[name="description"]').value = savedService.description;
    row.querySelector('input[name="price"]').value = savedService.price;
}

// Функция для добавления новой услуги
function addNewService() {
    const table = document.getElementById('services-table').getElementsByTagName('tbody')[0];
    const newRow = table.insertRow();
    const cells = [
        newRow.insertCell(0), // Название услуги
        newRow.insertCell(1), // Описание
        newRow.insertCell(2), // Цена
        newRow.insertCell(3)  // Действия
    ];
    cells[0].innerHTML = '<input type="text" name="name" placeholder="Название услуги">';
    cells[1].innerHTML = '<textarea name="description" placeholder="Описание"></textarea>';
    cells[2].innerHTML = '<input type="number" name="price" placeholder="Цена">';
    cells[3].innerHTML = `
        <button class="edit-btn" onclick="toggleEdit(this)" style="display: none;">Редактировать</button>
        <button class="save-btn" onclick="saveChanges(this)">Сохранить</button>
        <button class="delete-btn" onclick="deleteService(this)" style="display: none;">Удалить</button>
    `;
}

function deleteService(button) {
    const row = button.closest('tr');
    const serviceId = row.dataset.serviceId; // Получаем ID пользователя
    if (confirm('Вы уверены, что хотите удалить эту услугу?')) {
        deleteServiceFromDatabase(serviceId)
            .then(response => {
                console.log('Услуга удалена:', response);
                row.remove(); // Удаляем строку из таблицы
                alert(response);
                location.reload();
            })
            .catch(error => {
                console.error('Ошибка при удалении:', error);
                alert(error);
            });
    }
}

// Функция для отправки запроса на удаление
function deleteServiceFromDatabase(serviceId) {
    return fetch(`/services/delete/${serviceId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка сети');
            }
            return response.text();
        });
}