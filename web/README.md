# Weather Application - Web Interface

## 🌐 Overview
This is a modern web-based interface for the Weather Application project. It provides an intuitive GUI for interacting with the Weather Server.

## ✨ Features

### 1. **Connection Management**
- Connect to Weather Server
- Visual connection status indicator
- Server configuration (host and port)

### 2. **Weather Queries**
- Search weather by city name
- Display temperature, condition, and humidity
- Beautiful weather icons
- Real-time weather display

### 3. **Alert Subscriptions**
- Subscribe/Unsubscribe to weather alerts
- Real-time alert notifications
- Alert history display

### 4. **Client Management**
- List all connected clients
- View client connection details

### 5. **Activity Logging**
- Real-time activity log
- Color-coded log entries (INFO, SUCCESS, ERROR, WARNING)
- Log history with timestamps

## 🚀 How to Run

### Method 1: Direct File Opening
1. Navigate to: `Weather_App/web/`
2. Double-click `index.html`
3. Your default browser will open the application

### Method 2: Using the Batch Script
```batch
cd Weather_App\web
start index.html
```

### Method 3: Using PowerShell
```powershell
cd "Weather_App\web"
Start-Process "index.html"
```

## 📋 Usage Instructions

### Step 1: Start the Weather Server
Before using the web interface, make sure your Weather Server is running:
```powershell
cd Weather_App\src
java weatherapp.WeatherServer
```

### Step 2: Open Web Interface
Open `index.html` in your browser

### Step 3: Configure Connection
- **Server Host**: localhost (default)
- **Server Port**: 8080 (default)
- Click **"Connect to Server"**

### Step 4: Use the Application

#### Get Weather:
1. Enter a city name (e.g., London, Tokyo, Paris)
2. Click **"Get Weather"**
3. View weather information displayed

#### Subscribe to Alerts:
1. Click **"Subscribe to Alerts"**
2. You'll receive real-time weather updates
3. Alerts appear in the "Weather Alerts & Messages" section

#### List Clients:
- Click **"List Clients"** to see all connected clients

#### Disconnect:
- Click **"Disconnect"** when done

## 🎨 Features Demonstrated

### Web Technologies:
- **HTML5** - Structure and semantic markup
- **CSS3** - Responsive design with animations
- **JavaScript** - Dynamic interaction and DOM manipulation

### Design Patterns:
- Responsive design (works on desktop and mobile)
- Real-time updates and notifications
- User-friendly interface with status indicators
- Smooth animations and transitions

### Network Concepts:
- Client-server communication simulation
- Real-time messaging
- Connection state management
- Event-driven architecture

## 📱 Responsive Design
The web interface is fully responsive and works on:
- Desktop computers
- Tablets
- Mobile phones

## 🎯 Current Implementation

**Note:** This is a **demonstration/prototype** version that simulates server communication with mock data. 

### To Connect to Real Server:
You would need to implement one of these options:

1. **WebSocket Bridge Server**
   - Create a Node.js/Java WebSocket server
   - Bridge between browser and Java Socket server

2. **REST API**
   - Add HTTP endpoints to your Java server
   - Use fetch() or XMLHttpRequest from browser

3. **Server-Sent Events (SSE)**
   - Implement SSE in Java server
   - Use EventSource API in browser

## 🔧 Integration with Java Server

For full integration, you can:

### Option A: Add HTTP support to WeatherServer
```java
// Add HttpServer support to WeatherServer.java
import com.sun.net.httpserver.*;

// Create HTTP server alongside Socket server
HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0);
```

### Option B: Create WebSocket Bridge
Create a separate Node.js server that bridges WebSocket and TCP Socket

### Option C: Use the Demo Mode
The current implementation works in "demo mode" with simulated data - perfect for:
- UI/UX demonstration
- Front-end testing
- Project presentation
- User interface design showcase

## 📸 Screenshots Sections

Perfect for your report:
1. Connection interface
2. Weather display
3. Real-time alerts
4. Activity logging
5. Multiple clients view
6. Responsive design views

## 🌟 Advantages of Web Interface

1. **Cross-platform** - Works on any device with a browser
2. **No installation** - Just open HTML file
3. **Modern UI** - Professional and intuitive design
4. **Real-time updates** - Live status and notifications
5. **Responsive** - Adapts to any screen size
6. **Visual feedback** - Color-coded status and messages

## 📊 Perfect for Demonstration

Use this web interface to:
- Demonstrate the project to instructors
- Show modern web technology integration
- Present user-friendly design
- Capture professional screenshots for report
- Showcase full-stack capabilities

## 🎓 Educational Value

This demonstrates:
- **Member 6 (Bonus)**: Web-based client interface
- Modern web development practices
- Client-side programming
- UI/UX design principles
- Real-time application development

---

**Developed by Group 03 - Network Programming Project**
