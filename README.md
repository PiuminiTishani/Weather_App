# Weather Application - Real-Time Weather Data Fetcher
**Network Programming Group Project - Group 03**

## 📋 Project Overview

A comprehensive Java network programming application that demonstrates client-server architecture, real-time data broadcasting, HTTP API integration, and network monitoring. The application fetches weather data from external APIs and broadcasts it to multiple connected clients in real-time.

## 🎯 Network Programming Concepts Demonstrated

- **Socket Programming** - Server-client communication using TCP sockets
- **Multithreading** - Concurrent client handling with thread pools
- **HTTP Networking** - RESTful API integration using HttpURLConnection
- **Stream Handling** - Input/Output streams for data transfer
- **Thread Synchronization** - Synchronized methods for thread-safe operations
- **Real-Time Broadcasting** - Push notifications to multiple clients
- **Network Monitoring** - Connection tracking and event logging

## 👥 Team Members & Individual Contributions

### Member 1: Server Setup & Multithreaded Client Handling
**File:** `WeatherServer.java`

**Concepts Implemented:**
- ServerSocket for accepting connections
- ExecutorService for thread pool management
- CopyOnWriteArrayList for thread-safe client management
- Graceful shutdown handling

**Key Features:**
- Multi-client connection handling
- Thread pool with 10 concurrent threads
- Command processing system
- Automatic weather updates

---

### Member 2: Weather API Integration
**File:** `WeatherDataFetcher.java`

**Concepts Implemented:**
- HttpURLConnection for HTTP requests
- BufferedReader and InputStreamReader for response reading
- Manual JSON parsing (pure Java)
- Request/Response handling

**Key Features:**
- OpenWeatherMap API integration
- Data caching mechanism (5-minute TTL)
- Fallback mock data system
- Error handling for API failures

---

### Member 3: Real-Time Data Broadcasting
**File:** `WeatherBroadcaster.java`

**Concepts Implemented:**
- PrintWriter for client communication
- Synchronized broadcast methods
- Concurrent collection handling
- Message formatting

**Key Features:**
- Broadcast to all connected clients
- Targeted broadcasting with filters
- Weather alerts system
- Broadcast statistics tracking

---

### Member 4: Client Application
**File:** `WeatherClient.java`

**Concepts Implemented:**
- Socket for server connection
- Separate listener thread for async reading
- BufferedReader for input handling
- Non-blocking I/O pattern

**Key Features:**
- Interactive command-line interface
- Continuous message reception
- Command input system
- Graceful disconnect

---

### Member 5: Simplified Network Monitoring
**File:** `NetworkMonitor.java`

**Concepts Implemented:**
- Connection tracking with synchronized methods
- File I/O for logging
- Periodic monitoring thread
- Event notification system

**Key Features:**
- Real-time connection monitoring
- API success/failure tracking
- Dual logging (console + file)
- Statistics reporting

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Weather Server                        │
│  ┌─────────────────┐  ┌──────────────────────────────┐ │
│  │  ServerSocket   │  │    ExecutorService           │ │
│  │  (Port 8080)    │  │    (Thread Pool)             │ │
│  └────────┬────────┘  └──────────────────────────────┘ │
│           │                                              │
│  ┌────────▼──────────────────────────────────────────┐ │
│  │          ClientHandler (Multiple Threads)         │ │
│  └───────────────────┬───────────────────────────────┘ │
│                      │                                   │
│  ┌───────────────────▼────────────────────────────────┐ │
│  │         WeatherDataFetcher (API Client)           │ │
│  │              ▼ HTTP Connection                     │ │
│  │    OpenWeatherMap API / Mock Data                 │ │
│  └────────────────────┬───────────────────────────────┘ │
│                       │                                  │
│  ┌────────────────────▼──────────────────────────────┐ │
│  │        WeatherBroadcaster (Publisher)            │ │
│  │    Broadcasts to all connected clients           │ │
│  └────────────────────┬───────────────────────────────┘ │
│                       │                                  │
│  ┌────────────────────▼──────────────────────────────┐ │
│  │      NetworkMonitor (Event Logger)               │ │
│  │  Tracks connections, API calls, events           │ │
│  └──────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
                        │
            ┌───────────┴────────────┐
            │                        │
    ┌───────▼────────┐      ┌───────▼────────┐
    │ WeatherClient  │      │ WeatherClient  │
    │  (Client 1)    │ ...  │  (Client N)    │
    └────────────────┘      └────────────────┘
```

## 🚀 How to Run

### Option 1: Using Batch File (Windows)
```bash
# Double-click run.bat or:
run.bat
```

### Option 2: Using PowerShell
```powershell
.\run.ps1
```

### Option 3: Manual Compilation
```bash
cd src
javac weatherapp\*.java
```

**Start Server:**
```bash
java weatherapp.WeatherServer
```

**Start Client (new terminal):**
```bash
java weatherapp.WeatherClient
```

## 📝 Available Client Commands

Once connected to the server, use these commands:

| Command | Description |
|---------|-------------|
| `weather <city>` | Get current weather for a city |
| `subscribe` | Subscribe to automatic updates |
| `unsubscribe` | Unsubscribe from updates |
| `list` | List available cities |
| `help` | Show help message |
| `quit` | Disconnect from server |

**Example:**
```
> weather London
> weather New York
> list
> quit
```

## 🌐 Supported Cities

London, New York, Tokyo, Paris, Sydney, Mumbai, Dubai, Singapore, Toronto, Berlin

*Note: Any city name can be tried. Invalid cities will return an error message.*

## 📊 Features Demonstration

### 1. **Multiple Client Connections**
```bash
# Terminal 1
java weatherapp.WeatherServer

# Terminal 2
java weatherapp.WeatherClient

# Terminal 3
java weatherapp.WeatherClient
```

### 2. **Real-Time Broadcasting**
Server automatically broadcasts weather updates every 30 seconds to all connected clients.

### 3. **Network Monitoring**
Check `logs/server_monitor.log` for:
- Connection events
- Disconnection events
- API success/failure logs
- Periodic monitoring checks

### 4. **API Integration**
The application fetches real weather data from OpenWeatherMap API. When API is unavailable, it uses realistic mock data.

## 📁 Project Structure

```
WeatherApp_Complete/
├── src/
│   └── weatherapp/
│       ├── WeatherServer.java         (Member 1)
│       ├── WeatherDataFetcher.java    (Member 2)
│       ├── WeatherBroadcaster.java    (Member 3)
│       ├── WeatherClient.java         (Member 4)
│       └── NetworkMonitor.java        (Member 5)
├── logs/
│   └── server_monitor.log
├── run.bat
├── run.ps1
└── README.md
```

## 🔧 Configuration

### Change Server Port
Edit `WeatherServer.java`:
```java
private static final int PORT = 8080; // Change this
```

### Add Weather API Key
Edit `WeatherDataFetcher.java`:
```java
private static final String API_KEY = "your_api_key_here";
```

Get free API key from: https://openweathermap.org/api

## 🧪 Testing Each Component

### Test Member 1 (Server):
```bash
java weatherapp.WeatherServer
```

### Test Member 2 (API Fetcher):
```bash
java weatherapp.WeatherDataFetcher
```

### Test Member 3 (Broadcaster):
Run with server (integrated component)

### Test Member 4 (Client):
```bash
java weatherapp.WeatherClient
```

### Test Member 5 (Monitor):
```bash
java weatherapp.NetworkMonitor
```

## 📸 Sample Outputs

### Server Output:
```
╔════════════════════════════════════════════╗
║   Weather Server - Network Programming    ║
║             Group 03                       ║
╚════════════════════════════════════════════╝
Server started on port: 8080
Thread pool size: 10
Waiting for clients to connect...

[2025-11-10 16:30:15] [INFO] NetworkMonitor initialized
[2025-11-10 16:30:20] [CONNECTION] Client-1 connected from 127.0.0.1:54321 | Total active: 1
[2025-11-10 16:30:25] [API_SUCCESS] Weather data fetched successfully for London
```

### Client Output:
```
╔════════════════════════════════════════════╗
║     Welcome to Weather Server!             ║
║     Network Programming - Group 03         ║
╚════════════════════════════════════════════╝

Available commands:
  weather <city>  - Get weather for a city
  subscribe       - Subscribe to auto-updates
  list            - List available cities
  help            - Show this help
  quit            - Disconnect

> weather London

╔════════════════════════════════════════════╗
║  Weather Report - London                   ║
╠════════════════════════════════════════════╣
║  Temperature:     15.5°C                   ║
║  Feels Like:      13.2°C                   ║
║  Humidity:         72%                     ║
║  Conditions:      Partly Cloudy            ║
║  Wind Speed:       4.5 m/s                 ║
╚════════════════════════════════════════════╝
```

## 🚧 Challenges Faced & Solutions

### Challenge 1: Thread Safety in Broadcasting
**Problem:** ConcurrentModificationException when broadcasting to clients while new clients connect.

**Solution:** Used `CopyOnWriteArrayList` for the client list and synchronized broadcast methods.

### Challenge 2: API Rate Limiting
**Problem:** OpenWeatherMap free tier has request limits.

**Solution:** Implemented caching mechanism with 5-minute TTL and fallback mock data system.

### Challenge 3: Client Disconnect Detection
**Problem:** Server couldn't detect when clients disconnected ungracefully.

**Solution:** Added periodic monitoring thread to check for stale connections and remove them.

### Challenge 4: JSON Parsing Without Libraries
**Problem:** Requirement to use pure Java without external JSON libraries.

**Solution:** Implemented manual JSON parsing using string manipulation and pattern extraction.

## 📚 Key Learning Outcomes

1. ✅ Understanding of TCP socket programming
2. ✅ Thread management and synchronization
3. ✅ HTTP client implementation
4. ✅ Real-time data broadcasting
5. ✅ Network event monitoring and logging
6. ✅ Client-server architecture design
7. ✅ Concurrent programming patterns
8. ✅ Error handling and fallback mechanisms

## 🎓 Conclusion

This project successfully demonstrates core Java network programming concepts through a practical real-world application. Each team member implemented distinct features using different networking concepts:

- **Member 1** showcased multithreaded server design with thread pools
- **Member 2** demonstrated HTTP networking and API integration
- **Member 3** implemented real-time broadcasting with stream handling
- **Member 4** created an interactive client with async message handling
- **Member 5** built a comprehensive monitoring system with logging

The application is fully functional, handles multiple concurrent clients, integrates with external APIs, and provides real-time weather updates with robust error handling and monitoring capabilities.

## 🔗 Resources

- Java Socket Programming: https://docs.oracle.com/javase/tutorial/networking/sockets/
- OpenWeatherMap API: https://openweathermap.org/api
- Java Concurrency: https://docs.oracle.com/javase/tutorial/essential/concurrency/

## 📄 License

Educational project for Network Programming course - Group 03

---

**Developed with ☕ by Group 03**
**Network Programming - 2025**
