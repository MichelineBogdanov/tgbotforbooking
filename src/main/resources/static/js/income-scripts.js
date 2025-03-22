document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('prev-month').addEventListener('click', function () {
        window.location.href = this.getAttribute('data-prev-month'); // Перенаправляем пользователя
    });

    document.getElementById('next-month').addEventListener('click', function () {
        window.location.href = this.getAttribute('data-next-month'); // Перенаправляем пользователя
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('income-chart').getContext('2d');
    const dailyIncome = JSON.parse(document.getElementById('income-chart').getAttribute('data-daily-income'));
    const labels = Array.from({ length: dailyIncome.length }, (_, i) => i + 1);
    const today = new Date();
    const currentYear = today.getUTCFullYear();
    const currentMonth = today.getUTCMonth() + 1;
    const currentDay = today.getUTCDate();
    const selectedMonth = document.getElementById('current-month').textContent;
    const [selectedYear, selectedMonthNumber] = selectedMonth.split('-').map(Number);
    const isCurrentMonth = currentYear === selectedYear && currentMonth === selectedMonthNumber;
    const annotation = isCurrentMonth ? {
        annotations: {
            line1: {
                type: 'line',
                xMin: currentDay - 1,
                xMax: currentDay - 1,
                borderColor: 'red',
                borderWidth: 2,
                label: {
                    content: 'Сегодня',
                    enabled: true,
                    position: 'top'
                }
            }
        }
    } : {};
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Доход по дням',
                data: dailyIncome,
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            plugins: {
                annotation: annotation
            }
        }
    });
});