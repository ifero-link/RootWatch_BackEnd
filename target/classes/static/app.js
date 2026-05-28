let chart; // Variable global para el gráfico

// 1. Función para obtener los datos del servidor
async function actualizarDashboard() {
    try {
        // Llamamos al endpoint de la última medición
        const response = await fetch('/api/mediciones/ultima');
        
        if (!response.ok) throw new Error("Error en la respuesta del servidor");
        
        const data = await response.json();

        // 2. Actualizamos los textos de las tarjetas con los nuevos IDs
        document.getElementById('temp-val').innerText = data.temperatura.toFixed(1) + "°C";
        document.getElementById('hum-val').innerText = data.humedad.toFixed(1) + "%";
        
        // Formateamos la fecha para que se vea bien
        const fecha = new Date(data.fecha);
        document.getElementById('fecha-val').innerText = "Última actualización: " + fecha.toLocaleTimeString();

        // 3. Actualizamos el histórico para el gráfico
        actualizarGrafico();

    } catch (error) {
        console.error("Error al obtener datos:", error);
    }
}

// 4. Función para traer los últimos 10 datos y dibujar el gráfico
async function actualizarGrafico() {
    try {
        const response = await fetch('/api/mediciones/todas'); // O el endpoint que devuelva la lista
        const datos = await response.json();

        const etiquetas = datos.map(d => new Date(d.fecha).toLocaleTimeString());
        const temps = datos.map(d => d.temperatura);
        const hums = datos.map(d => d.humedad);

        if (chart) {
            // Si el gráfico ya existe, solo actualizamos los datos
            chart.data.labels = etiquetas;
            chart.data.datasets[0].data = temps;
            chart.data.datasets[1].data = hums;
            chart.update('none'); // Update suave sin animaciones bruscas
        } else {
            // Si no existe, lo creamos
            const ctx = document.getElementById('grafico').getContext('2d');
            chart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: etiquetas,
                    datasets: [
                        {
                            label: 'Temperatura (°C)',
                            data: temps,
                            borderColor: '#2563eb',
                            backgroundColor: 'rgba(37, 99, 235, 0.1)',
                            fill: true,
                            tension: 0.4
                        },
                        {
                            label: 'Humedad (%)',
                            data: hums,
                            borderColor: '#10b981',
                            backgroundColor: 'transparent',
                            tension: 0.4
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { position: 'bottom' }
                    }
                }
            });
        }
    } catch (error) {
        console.log("Error cargando el histórico:", error);
    }
}

// 5. Ejecución inicial y bucle de refresco cada 5 segundos
actualizarDashboard();
setInterval(actualizarDashboard, 5000);