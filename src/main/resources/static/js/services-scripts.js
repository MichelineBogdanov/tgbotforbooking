// Функция для переключения режима редактирования
function toggleEdit(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, textarea');
    inputs.forEach(input => {
        input.disabled = !input.disabled;
        input.style.width = '100%';
    });
    changeDealColumnMode(row);
}

// Функция для показа/скрытия формы добавления услуги
function toggleAddServiceForm() {
    const form = document.getElementById('add-service-form');
    const button = document.getElementById('add-service-btn');

    if (form.style.display === 'none') {
        form.style.display = 'block';
        button.style.display = 'none';
    } else {
        form.style.display = 'none';
        button.style.display = 'inline-block';
    }

    // Очищаем форму
    document.getElementById('new-service-name').value = '';
    document.getElementById('new-service-description').value = '';
    document.getElementById('new-service-price').value = '';
    document.getElementById('new-service-duration').value = '';
}

// Функция для сохранения изменений
function saveChanges(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, textarea');
    inputs.forEach(input => input.disabled = true);
    const serviceData = {
        id: row.dataset.serviceId,
        name: row.querySelector('input[name="name"]').value,
        description: row.querySelector('textarea[name="description"]').value,
        price: parseFloat(row.querySelector('input[name="price"]').value),
        duration: parseInt(row.querySelector('input[name="duration"]').value)
    };
    saveServiceToDatabase(serviceData).then(savedService => {
        alert('Изменения успешно сохранены!');
        updateRowWithSavedService(row, savedService);
    })
    changeDealColumnMode(row);
}

function changeDealColumnMode(row) {
    const editBtn = row.querySelector('.edit-btn');
    editBtn.style.display = editBtn.style.display === 'none' ? 'inline-block' : 'none';

    const saveBtn = row.querySelector('.save-btn');
    saveBtn.style.display = saveBtn.style.display === 'none' ? 'inline-block' : 'none';

    const cancelBtn = row.querySelector('.cancel-changes-btn');
    cancelBtn.style.display = cancelBtn.style.display === 'none' ? 'inline-block' : 'none';

    const deleteBtn = row.querySelector('.delete-btn');
    deleteBtn.style.display = deleteBtn.style.display === 'none' ? 'inline-block' : 'none';
}

function saveServiceToDatabase(serviceData) {
    return fetch('/services/update',
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [document.querySelector('meta[name="_csrf_header"]').content]: document.querySelector('meta[name="_csrf"]').content
            },
            body: JSON.stringify(serviceData),
            credentials: 'include'
        }).then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json();
    });
}

// Функция для сохранения новой услуги
function saveNewService() {
    const serviceData = {
        name: document.getElementById('new-service-name').value,
        description: document.getElementById('new-service-description').value,
        price: parseFloat(document.getElementById('new-service-price').value),
        duration: parseInt(document.getElementById('new-service-duration').value)
    };

    // Проверка заполнения полей
    if (!serviceData.name || !serviceData.description || isNaN(serviceData.price) || isNaN(serviceData.duration)) {
        alert('Пожалуйста, заполните все поля корректно!');
        return;
    }

    saveServiceToDatabase(serviceData).then(() => {
        alert('Услуга успешно добавлена!');
        // Скрываем форму
        toggleAddServiceForm();
        // Обновляем страницу, чтобы показать новую услугу
        location.reload();
    })
        .catch(error => {
            console.error('Ошибка при сохранении:', error);
            alert('Произошла ошибка при сохранении услуги');
        });
}

// Функция для обновления строки с данными из сохраненной сущности
function updateRowWithSavedService(row, savedService) {
    row.dataset.serviceId = savedService.id;
    row.querySelector('input[name="name"]').value = savedService.name;
    row.querySelector('textarea[name="description"]').value = savedService.description;
    row.querySelector('input[name="price"]').value = savedService.price;
    row.querySelector('input[name="duration"]').value = savedService.duration;
}

function cancelChanges(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, textarea');
    const serviceId = row.dataset.serviceId;
    fetch(`/services/${serviceId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            [document.querySelector('meta[name="_csrf_header"]').content]: document.querySelector('meta[name="_csrf"]').content
        },
        credentials: 'include'
    }).then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json(); // Парсим JSON один раз
    }).then(service => {
        updateRowWithSavedService(row, service);
    });
    inputs.forEach(input => input.disabled = true);
    changeDealColumnMode(row);
}

function deleteService(button) {
    const row = button.closest('tr');
    const serviceId = row.dataset.serviceId;
    if (confirm('Вы уверены, что хотите удалить эту услугу?')) {
        deleteServiceFromDatabase(serviceId)
            .then(response => {
                console.log('Услуга удалена:', response);
                row.remove();
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
    return fetch(`/services/delete/${serviceId}`,
        {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                [document.querySelector('meta[name="_csrf_header"]').content]: document.querySelector('meta[name="_csrf"]').content
            },
            credentials: 'include'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка сети');
            }
            return response.text();
        });
}