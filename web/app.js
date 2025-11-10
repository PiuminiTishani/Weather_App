// Weather Application Web Client
// Connects to Weather Server via WebSocket proxy

let socket = null;
let isConnected = false;
let isSubscribed = false;

// DOM Elements
const connectBtn = document.getElementById('connectBtn');
const disconnectBtn = document.getElementById('disconnectBtn');
const serverHost = document.getElementById('serverHost');
const serverPort = document.getElementById('serverPort');
const connectionStatus = document.getElementById('connectionStatus');
const subscriptionStatus = document.getElementById('subscriptionStatus');

const cityInput = document.getElementById('cityInput');
const getWeatherBtn = document.getElementById('getWeatherBtn');
const subscribeBtn = document.getElementById('subscribeBtn');
const unsubscribeBtn = document.getElementById('unsubscribeBtn');
const listClientsBtn = document.getElementById('listClientsBtn');

const weatherDisplay = document.getElementById('weatherDisplay');
const cityName = document.getElementById('cityName');
const temperature = document.getElementById('temperature');
const condition = document.getElementById('condition');
const humidity = document.getElementById('humidity');
const weatherIcon = document.getElementById('weatherIcon');

const alertsContainer = document.getElementById('alertsContainer');
const logContainer = document.getElementById('logContainer');
const clearLogBtn = document.getElementById('clearLogBtn');

// Event Listeners
connectBtn.addEventListener('click', connectToServer);
disconnectBtn.addEventListener('click', disconnectFromServer);
getWeatherBtn.addEventListener('click', getWeather);
subscribeBtn.addEventListener('click', subscribe);
unsubscribeBtn.addEventListener('click', unsubscribe);
listClientsBtn.addEventListener('click', listClients);
clearLogBtn.addEventListener('click', clearLog);

// Allow Enter key in city input
cityInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter' && !getWeatherBtn.disabled) {
        getWeather();
    }
});

function connectToServer() {
    const host = serverHost.value || 'localhost';
    const port = serverPort.value || '8080';
    
    log('INFO', `Connecting to ${host}:${port}...`);
    
    // Since we can't directly connect via WebSocket from browser to Java Socket,
    // we'll simulate the connection and use HTTP requests
    // In a real implementation, you'd need a WebSocket bridge server
    
    // For demonstration, we'll simulate the connection
    setTimeout(() => {
        isConnected = true;
        updateConnectionStatus(true);
        log('SUCCESS', 'Connected to Weather Server');
        enableControls(true);
    }, 500);
}

function disconnectFromServer() {
    if (isConnected) {
        isConnected = false;
        isSubscribed = false;
        updateConnectionStatus(false);
        updateSubscriptionStatus(false);
        log('INFO', 'Disconnected from server');
        enableControls(false);
        weatherDisplay.style.display = 'none';
    }
}

function updateConnectionStatus(connected) {
    if (connected) {
        connectionStatus.textContent = 'Connected';
        connectionStatus.className = 'status-connected';
        connectBtn.disabled = true;
        disconnectBtn.disabled = false;
    } else {
        connectionStatus.textContent = 'Disconnected';
        connectionStatus.className = 'status-disconnected';
        connectBtn.disabled = false;
        disconnectBtn.disabled = true;
    }
}

function updateSubscriptionStatus(subscribed) {
    if (subscribed) {
        subscriptionStatus.textContent = 'Subscribed';
        subscriptionStatus.className = 'status-active';
    } else {
        subscriptionStatus.textContent = 'Not Subscribed';
        subscriptionStatus.className = 'status-inactive';
    }
}

function enableControls(enabled) {
    getWeatherBtn.disabled = !enabled;
    subscribeBtn.disabled = !enabled;
    unsubscribeBtn.disabled = !enabled;
    listClientsBtn.disabled = !enabled;
    cityInput.disabled = !enabled;
}

function getWeather() {
    const city = cityInput.value.trim();
    
    if (!city) {
        log('ERROR', 'Please enter a city name');
        return;
    }
    
    if (!isConnected) {
        log('ERROR', 'Not connected to server');
        return;
    }
    
    log('INFO', `Fetching weather for ${city}...`);
    
    // Simulate weather fetch
    // In real implementation, this would send a request to your Java server
    setTimeout(() => {
        const mockWeather = generateMockWeather(city);
        displayWeather(mockWeather);
        log('SUCCESS', `Weather data received for ${city}`);
        
        if (isSubscribed) {
            addAlert(`Weather Update: ${city} - ${mockWeather.temperature}°C, ${mockWeather.condition}`, 'broadcast');
        }
    }, 800);
}

function generateMockWeather(city) {
    const conditions = ['Clear', 'Cloudy', 'Rainy', 'Sunny', 'Partly Cloudy', 'Windy'];
    const icons = ['☀️', '⛅', '☁️', '🌧️', '⛈️', '🌤️'];
    
    return {
        city: city,
        temperature: (Math.random() * 20 + 10).toFixed(1),
        condition: conditions[Math.floor(Math.random() * conditions.length)],
        humidity: Math.floor(Math.random() * 40 + 40),
        icon: icons[Math.floor(Math.random() * icons.length)]
    };
}

function displayWeather(weather) {
    weatherDisplay.style.display = 'block';
    cityName.textContent = weather.city;
    temperature.textContent = `${weather.temperature}°C`;
    condition.textContent = weather.condition;
    humidity.textContent = `${weather.humidity}%`;
    weatherIcon.textContent = weather.icon;
    
    // Scroll to weather display
    weatherDisplay.scrollIntoView({ behavior: 'smooth' });
}

function subscribe() {
    if (!isConnected) {
        log('ERROR', 'Not connected to server');
        return;
    }
    
    isSubscribed = true;
    updateSubscriptionStatus(true);
    log('SUCCESS', 'Subscribed to weather alerts');
    addAlert('You are now subscribed to weather alerts!', 'info');
}

function unsubscribe() {
    if (!isConnected) {
        log('ERROR', 'Not connected to server');
        return;
    }
    
    isSubscribed = false;
    updateSubscriptionStatus(false);
    log('INFO', 'Unsubscribed from weather alerts');
}

function listClients() {
    if (!isConnected) {
        log('ERROR', 'Not connected to server');
        return;
    }
    
    log('INFO', 'Requesting client list...');
    
    // Simulate client list
    setTimeout(() => {
        const clientCount = Math.floor(Math.random() * 5) + 1;
        log('SUCCESS', `${clientCount} client(s) connected to server`);
        
        let message = 'Connected Clients:\n';
        for (let i = 1; i <= clientCount; i++) {
            message += `- Client ${i}: /127.0.0.1:${50000 + Math.floor(Math.random() * 10000)}\n`;
        }
        
        addAlert(message, 'info');
    }, 500);
}

function addAlert(message, type = 'info') {
    // Remove "no alerts" message if present
    const noAlerts = alertsContainer.querySelector('.no-alerts');
    if (noAlerts) {
        noAlerts.remove();
    }
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert-item ${type}`;
    
    const timeDiv = document.createElement('div');
    timeDiv.className = 'alert-time';
    timeDiv.textContent = new Date().toLocaleTimeString();
    
    const messageDiv = document.createElement('div');
    messageDiv.className = 'alert-message';
    messageDiv.textContent = message;
    messageDiv.style.whiteSpace = 'pre-line';
    
    alertDiv.appendChild(timeDiv);
    alertDiv.appendChild(messageDiv);
    
    alertsContainer.insertBefore(alertDiv, alertsContainer.firstChild);
    
    // Keep only last 10 alerts
    while (alertsContainer.children.length > 10) {
        alertsContainer.removeChild(alertsContainer.lastChild);
    }
}

function log(type, message) {
    const logEntry = document.createElement('div');
    logEntry.className = 'log-entry';
    
    const time = new Date().toLocaleTimeString();
    const typeClass = `log-type-${type.toLowerCase()}`;
    
    logEntry.innerHTML = `
        <span class="log-time">[${time}]</span>
        <span class="${typeClass}">[${type}]</span>
        <span>${message}</span>
    `;
    
    logContainer.insertBefore(logEntry, logContainer.firstChild);
    
    // Keep only last 50 log entries
    while (logContainer.children.length > 50) {
        logContainer.removeChild(logContainer.lastChild);
    }
}

function clearLog() {
    logContainer.innerHTML = '';
    log('INFO', 'Log cleared');
}

// Initialize
log('INFO', 'Weather Application Web Client loaded');
log('INFO', 'Enter server details and click Connect to start');

// Simulate periodic updates if subscribed
setInterval(() => {
    if (isConnected && isSubscribed && Math.random() > 0.7) {
        const cities = ['London', 'Tokyo', 'New York', 'Paris', 'Sydney'];
        const city = cities[Math.floor(Math.random() * cities.length)];
        const temp = (Math.random() * 20 + 10).toFixed(1);
        addAlert(`Weather Alert: Temperature in ${city} is now ${temp}°C`, 'broadcast');
    }
}, 15000); // Every 15 seconds
