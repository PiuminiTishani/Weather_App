# Quick Start Guide
## Weather Application - Network Programming

### 🚀 Super Quick Start (3 Steps)

#### Step 1: Compile Everything
```bash
cd WeatherApp_Complete
run.bat
```
Or:
```powershell
.\run.ps1
```

#### Step 2: Start the Server
Choose option **1** from the menu

The server will start on port 8080 and wait for clients.

#### Step 3: Start Client(s)
Open **new terminal(s)** and run:
```bash
cd WeatherApp_Complete
run.bat
```
Choose option **2** to start a client.

You can start multiple clients!

---

## 📺 Full Demo (Copy & Paste)

### Terminal 1 - Server
```powershell
cd "c:\Users\lakshan roshana\Desktop\NetworkProgramming\WeatherApp_Complete\src"
javac weatherapp\*.java
java weatherapp.WeatherServer
```

### Terminal 2 - Client 1
```powershell
cd "c:\Users\lakshan roshana\Desktop\NetworkProgramming\WeatherApp_Complete\src"
java weatherapp.WeatherClient
```
*When prompted: Press Enter twice (use defaults)*

Then type:
```
weather London
weather New York
list
help
```

### Terminal 3 - Client 2
```powershell
cd "c:\Users\lakshan roshana\Desktop\NetworkProgramming\WeatherApp_Complete\src"
java weatherapp.WeatherClient
```

Then type:
```
weather Tokyo
subscribe
weather Paris
```

### Terminal 4 - Monitor Logs
```powershell
cd "c:\Users\lakshan roshana\Desktop\NetworkProgramming\WeatherApp_Complete"
Get-Content -Path "logs\server_monitor.log" -Wait
```

---

## 🧪 Test Individual Components

### Test 1: NetworkMonitor (Member 5)
```bash
cd src
javac weatherapp\NetworkMonitor.java
java weatherapp.NetworkMonitor
```
**Expected:** See simulated events and log file created

### Test 2: WeatherDataFetcher (Member 2)
```bash
cd src
javac weatherapp\WeatherDataFetcher.java weatherapp\NetworkMonitor.java
java weatherapp.WeatherDataFetcher
```
**Expected:** See weather data for multiple cities

### Test 3: Full Server + Multiple Clients
1. Start server (Terminal 1)
2. Start client 1 (Terminal 2)
3. Start client 2 (Terminal 3)
4. Type commands in both clients
5. Watch server terminal for logs

---

## ✅ Verification Checklist

After running, verify these features:

- [ ] Server starts successfully on port 8080
- [ ] Client can connect to server
- [ ] Client receives welcome message
- [ ] `weather London` returns weather data
- [ ] Multiple clients can connect simultaneously
- [ ] Server logs show in console
- [ ] Log file created in `logs/server_monitor.log`
- [ ] Client can disconnect with `quit` command
- [ ] Server continues running after client disconnects

---

## 🐛 Troubleshooting

### Problem: "javac is not recognized"
**Solution:** Install JDK and add to PATH
```powershell
# Check if Java is installed
java -version
javac -version
```

### Problem: "Address already in use"
**Solution:** Port 8080 is busy. Kill the process:
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Problem: Client can't connect
**Solution:** 
1. Make sure server is running first
2. Check firewall settings
3. Verify port is 8080

### Problem: Compilation errors
**Solution:** Compile in correct order:
```bash
cd src
javac weatherapp\NetworkMonitor.java
javac weatherapp\WeatherDataFetcher.java
javac weatherapp\WeatherBroadcaster.java
javac weatherapp\WeatherServer.java
javac weatherapp\WeatherClient.java
```

---

## 📊 Expected Output Examples

### When Server Starts:
```
╔════════════════════════════════════════════╗
║   Weather Server - Network Programming    ║
║             Group 03                       ║
╚════════════════════════════════════════════╝
Server started on port: 8080
Thread pool size: 10
Waiting for clients to connect...

[2025-11-10 16:30:15] [INFO] NetworkMonitor initialized - Log file: logs/server_monitor.log
[2025-11-10 16:30:15] [INFO] Periodic monitoring started with 30s interval
```

### When Client Connects:
```
✓ Connected to Weather Server at localhost:8080

╔════════════════════════════════════════════╗
║     Welcome to Weather Server!             ║
║     Network Programming - Group 03         ║
╚════════════════════════════════════════════╝

Available commands:
  weather <city>  - Get weather for a city
  subscribe       - Subscribe to auto-updates
  unsubscribe     - Unsubscribe from updates
  list            - List available cities
  help            - Show this help
  quit            - Disconnect

═══════════════════════════════════════════
  You can now type commands
═══════════════════════════════════════════

>
```

### When Requesting Weather:
```
> weather London

╔════════════════════════════════════════════╗
║  Weather Report - London                   ║
║  [DEMO DATA - API Key Required]            ║
╠════════════════════════════════════════════╣
║  Temperature:     23.4°C                   ║
║  Feels Like:      21.4°C                   ║
║  Humidity:         65%                     ║
║  Conditions:      Partly Cloudy            ║
║  Wind Speed:       5.2 m/s                 ║
╚════════════════════════════════════════════╝
Note: Using simulated data. Add API key for real data.

>
```

---

## 🎯 Demo Script for Presentation

Use this script to demonstrate all features:

```
1. Start server (show it's listening)
2. Connect Client 1 (show welcome message)
3. Type "list" (show available cities)
4. Type "weather London" (show weather data)
5. Connect Client 2 in new terminal (show multi-client support)
6. In Client 2, type "weather Tokyo"
7. Show server terminal (both clients logged)
8. Check logs/server_monitor.log (show monitoring)
9. Type "quit" in Client 1 (show graceful disconnect)
10. Show server terminal (disconnect logged)
11. Ctrl+C on server (show graceful shutdown + statistics)
```

---

## 📁 What You Should Submit

For your assignment, submit:

```
WeatherApp_Complete/
├── src/weatherapp/          (All 5 Java files)
├── logs/                    (Sample log files)
├── screenshots/             (Create this - add screenshots)
├── README.md               (Project documentation)
├── REPORT.md or REPORT.pdf (Your assignment report)
├── run.bat
└── run.ps1
```

---

## 📝 Tips for Report Writing

Include in your report:

1. **Screenshots** of:
   - Server starting
   - Client connecting
   - Weather command output
   - Multiple clients connected
   - Log files
   - Statistics output

2. **Code Snippets** showing:
   - Each member's key implementation
   - Network concepts used
   - Thread safety mechanisms

3. **Explanation** of:
   - How socket programming works in your app
   - How multithreading is used
   - How data is broadcasted
   - How monitoring works

---

## 🎓 Dividing Work Among Team Members

Each member can work on their assigned file:

- **Member 1:** WeatherServer.java (330 lines)
- **Member 2:** WeatherDataFetcher.java (300 lines)
- **Member 3:** WeatherBroadcaster.java (180 lines)
- **Member 4:** WeatherClient.java (220 lines)
- **Member 5:** NetworkMonitor.java (365 lines)

Everyone can test individually, then integrate together!

---

**Good luck with your project! 🎉**
