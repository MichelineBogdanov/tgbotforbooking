function toggleEdit(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, select');
    const editBtn = row.querySelector('.edit-btn');
    const saveBtn = row.querySelector('.save-btn');
    inputs.forEach(input => {
        if (input.name !== 'tgAccount') {
            input.disabled = !input.disabled;
            input.style.width = '100%';
        }
    });
    editBtn.style.display = editBtn.style.display === 'none' ? 'inline-block' : 'none';
    saveBtn.style.display = saveBtn.style.display === 'none' ? 'inline-block' : 'none';
}

function saveChanges(button) {
    const row = button.closest('tr');
    const inputs = row.querySelectorAll('input, select');
    const editBtn = row.querySelector('.edit-btn');
    const saveBtn = row.querySelector('.save-btn');
    inputs.forEach(input => input.disabled = true);
    editBtn.style.display = 'inline-block';
    saveBtn.style.display = 'none';
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
            [document.querySelector('meta[name="_csrf_header"]').content]: document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify(userData),
        credentials: 'include'
    }).then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сети');
        }
        return response.json();
    });
}

function viewVisits(button) {
    const row = button.closest('tr');
    const userId = row.dataset.userId;
    window.location.href = `/users/visits?userId=${userId}`;
}

function createVisit(button) {
    const row = button.closest('tr');
    const tgUserId = row.dataset.userTgUserId;

    // Открываем форму в новом окне или модальном окне
    const width = 500, height = 600;
    const left = (screen.width - width) / 2;
    const top = (screen.height - height) / 2;

    window.open(`/users/visit-form?tgUserId=${tgUserId}`, '_blank',
        `width=${width},height=${height},left=${left},top=${top}`);
}

// Обработчик сообщений от дочернего окна
window.addEventListener('message', function(event) {
    if (event.data.visitCreated) {
        // Можно обновить таблицу или показать уведомление
        console.log('Визит создан');
    }
});