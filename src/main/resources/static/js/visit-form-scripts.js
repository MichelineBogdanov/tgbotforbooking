document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('visit-form');
    const closeBtn = document.querySelector('.close-btn');
    closeBtn.addEventListener('click', function () {
        window.close(); // или другой способ закрытия модального окна
    });
    form.addEventListener('submit', function (e) {
        e.preventDefault();
        const formData = new FormData(form);
        const visitData = {
            date: formData.get('date'),
            time: formData.get('time'),
            tgUserId: formData.get('tgUserId'),
            serviceId: formData.get('serviceId')
        };
        fetch('/visits/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [document.querySelector('meta[name="_csrf_header"]').content]: document.querySelector('meta[name="_csrf"]').content
            },
            body: JSON.stringify(visitData),
            credentials: 'include'
        })
            .then(async response => {
                const data = await response.json();
                if (!response.ok) {
                    if (response.status === 400) {
                        const errorMessages = Object.values(data).join('\n');
                        throw new Error(errorMessages);
                    }
                    throw new Error(data.error || 'Неизвестная ошибка');
                }
                return data;
            })
            .then(() => {
                alert('Визит успешно создан!');
                window.opener.postMessage({visitCreated: true}, '*');
                window.close();
            })
            .catch(error => {
                alert(`Ошибка при создании визита:\n${error.message}`);
            });
    });
});