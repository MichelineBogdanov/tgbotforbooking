function toggleEdit(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, select');
    const editBtn = row.querySelector('.edit-btn');
    const saveBtn = row.querySelector('.save-btn');
    inputs.forEach(input => {
        input.disabled = !input.disabled;
        input.style.width = '100%';
    });
    editBtn.style.display = editBtn.style.display === 'none' ? 'inline-block' : 'none';
    saveBtn.style.display = saveBtn.style.display === 'none' ? 'inline-block' : 'none';
}

function saveChanges(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, select');
    const editBtn = row.querySelector('.edit-btn');
    const saveBtn = row.querySelector('.save-btn');
    const deleteBtn = row.querySelector('.delete-btn');
    inputs.forEach(input => input.disabled = true);
    editBtn.style.display = 'inline-block';
    saveBtn.style.display = 'none';
    deleteBtn.style.display = 'inline-block';
    const userData = {
        id: row.dataset.userId,
        tgAccount: row.querySelector('input[name="tgAccount"]').value,
        firstName: row.querySelector('input[name="firstName"]').value,
        lastName: row.querySelector('input[name="lastName"]').value,
        chatId: row.dataset.userChatId,
        tgUserId: row.dataset.userTgUserId,
        notificationOn: row.querySelector('select[name="notificationsOn"]').value === 'true'
    }
    console.log(userData);

    saveUserToDatabase(userData).then(savedUser => {
        alert('Изменения успешно сохранены!');
        updateRowWithSavedUser(row, savedUser);
    })
}

// Функция для обновления строки с данными из сохраненной сущности
function updateRowWithSavedUser(row, savedUser) {
    row.dataset.userId = savedUser.id;
    row.dataset.tgUserId = savedUser.tgUserId;
    row.dataset.chatId = savedUser.chatId;
    row.querySelector('input[name="tgAccount"]').value = savedUser.tgAccount;
    row.querySelector('input[name="firstName"]').value = savedUser.firstName;
    row.querySelector('input[name="lastName"]').value = savedUser.lastName;
    row.querySelector('select[name="notificationsOn"]').value = savedUser.notificationsOn.toString();
}

function saveUserToDatabase(userData) {
    return fetch('/users/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    }).then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json();
    });
}

function deleteUser(button) {
    const row = button.closest('tr');
    const userId = row.dataset.userId;
    if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
        deleteUserFromDatabase(userId)
            .then(response => {
                console.log('Пользователь удален:', response);
                row.remove(); // Удаляем строку из таблицы
                alert('Пользователь успешно удален!');
                location.reload();
            })
            .catch(error => {
                console.error('Ошибка при удалении:', error);
                alert('Произошла ошибка при удалении пользователя.');
            });
    }
}

// Функция для отправки запроса на удаление
function deleteUserFromDatabase(userId) {
    return fetch(`/users/delete/${userId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка сети');
            }
            return response.text();
        });
}

function viewVisits(button) {
    const row = button.closest('tr');
    const userId = row.dataset.userId;
    window.location.href = `/users/visits?userId=${userId}`;
}