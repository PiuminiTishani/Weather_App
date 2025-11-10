# How to Divide Work Among Team Members
## Weather Application - Group 03

---

## 📦 Complete Package Overview

The **WeatherApp_Complete** folder contains a FULLY WORKING application with all 5 components implemented. Here's how to divide it among your team:

---

## 👥 Work Division Strategy

### **Member 1: Server Setup & Multithreaded Client Handling**

**Your File:** `WeatherServer.java`

**What to Focus On:**
- Lines 1-70: Class setup, constructor, initialization
- Lines 71-110: Main server loop and client acceptance
- Lines 135-290: ClientHandler inner class
- Lines 291-330: Main method and shutdown

**What to Study:**
- `ServerSocket.accept()` - How server accepts connections
- `ExecutorService.newFixedThreadPool()` - Thread pool creation
- `CopyOnWriteArrayList` - Thread-safe collection
- `ClientHandler` implements `Runnable` - Thread execution

**For Report, Explain:**
- How ServerSocket works
- Why thread pool is used
- How commands are processed
- Thread safety mechanisms

**Testing Your Part:**
```bash
java weatherapp.WeatherServer
# Should start and wait for clients
```

---

### **Member 2: Weather API Integration**

**Your File:** `WeatherDataFetcher.java`

**What to Focus On:**
- Lines 1-45: API configuration and caching
- Lines 50-130: `fetchWeatherData()` method (main logic)
- Lines 135-180: JSON parsing methods
- Lines 235-275: Mock data generation

**What to Study:**
- `HttpURLConnection` - HTTP request setup
- `BufferedReader` + `InputStreamReader` - Reading response
- `extractJsonValue()` - Manual JSON parsing
- Cache mechanism - Reduces API calls

**For Report, Explain:**
- HTTP networking flow
- How JSON is parsed without libraries
- Why caching is important
- Mock data fallback strategy

**Testing Your Part:**
```bash
java weatherapp.WeatherDataFetcher
# Should fetch weather for multiple cities
```

---

### **Member 3: Real-Time Data Broadcasting**

**Your File:** `WeatherBroadcaster.java`

**What to Focus On:**
- Lines 1-30: Class setup and initialization
- Lines 32-68: `broadcast()` method (main functionality)
- Lines 91-140: Alert formatting
- Lines 142-162: Statistics methods

**What to Study:**
- `synchronized` keyword - Thread safety
- `CopyOnWriteArrayList` - Avoiding ConcurrentModificationException
- `PrintWriter` - Sending messages to clients
- Message formatting techniques

**For Report, Explain:**
- Why synchronization is needed
- How messages are sent to multiple clients
- Difference between broadcast and targeted broadcast
- Thread safety in concurrent environment

**Testing Your Part:**
Must test with WeatherServer (integrated component)

---

### **Member 4: Client Application**

**Your File:** `WeatherClient.java`

**What to Focus On:**
- Lines 1-35: Client setup and connection
- Lines 37-55: Starting listener thread
- Lines 57-90: User input handling
- Lines 130-150: ServerListener inner class (critical!)

**What to Study:**
- `Socket` connection to server
- Separate thread for listening (`ServerListener`)
- Non-blocking I/O pattern
- `BufferedReader` for user input

**For Report, Explain:**
- How client connects to server
- Why separate listener thread is needed
- Asynchronous message reception
- Command-line interface design

**Testing Your Part:**
```bash
# Need server running first
java weatherapp.WeatherClient
# Try: weather London, list, help, quit
```

---

### **Member 5: Simplified Network Monitoring**

**Your File:** `NetworkMonitor.java`

**What to Focus On:**
- Lines 1-85: Setup and initialization
- Lines 125-135: `onClientConnected()` method
- Lines 145-175: `onClientDisconnected()` method
- Lines 205-225: API monitoring methods
- Lines 290-330: Periodic monitoring thread

**What to Study:**
- `synchronized` methods - Thread safety
- File I/O with `PrintWriter` - Logging
- `CopyOnWriteArrayList` - Thread-safe client list
- Background daemon thread for periodic checks

**For Report, Explain:**
- Event-driven monitoring system
- Synchronized vs unsynchronized methods
- File logging implementation
- Periodic monitoring mechanism

**Testing Your Part:**
```bash
java weatherapp.NetworkMonitor
# Should show simulated events and create log file
```

---

## 📝 Individual Tasks Checklist

### For Each Member:

#### Week 1: Understanding
- [ ] Read and understand your assigned file
- [ ] Run the complete application to see how it works
- [ ] Test your individual component
- [ ] Take screenshots of your component running

#### Week 2: Documentation
- [ ] Write explanation of concepts used in your file
- [ ] Create code snippets highlighting key implementations
- [ ] Document challenges and solutions specific to your part
- [ ] Prepare presentation slides for your component

#### Week 3: Report
- [ ] Write your section in the report
- [ ] Add screenshots with captions
- [ ] Explain the network programming concepts you used
- [ ] Review and edit for clarity

---

## 📊 Report Sections - Who Writes What

| Section | Responsible Member(s) |
|---------|----------------------|
| 1. Project Title | All (agree together) |
| 2. Group Members | All (each writes their own part) |
| 3. System Overview | Member 1 (with input from all) |
| 4. Concepts - Socket Programming | Member 1 & 4 |
| 4. Concepts - Multithreading | Member 1 & 5 |
| 4. Concepts - HTTP Networking | Member 2 |
| 4. Concepts - Stream Handling | Member 3 & 4 |
| 4. Concepts - Synchronization | Member 3 & 5 |
| 5. Screenshots | All (each contributes 2-3) |
| 6. Challenges | All (each writes their own) |
| 7. Conclusion | Member 1 (compile from all inputs) |

---

## 🎯 Meeting Schedule Suggestion

### Meeting 1 (Day 1):
**Agenda:** Initial Setup
- Run the complete application together
- Each member tests their component
- Discuss how components interact
- Assign report sections

### Meeting 2 (Day 3):
**Agenda:** Progress Review
- Each member presents their component understanding
- Share screenshots
- Discuss challenges faced
- Review draft explanations

### Meeting 3 (Day 5):
**Agenda:** Integration
- Compile all sections into one report
- Ensure consistent formatting
- Review for completeness
- Finalize screenshots and diagrams

### Meeting 4 (Day 7):
**Agenda:** Final Review
- Proofread entire report
- Test the application one more time
- Prepare presentation (if needed)
- Package submission files

---

## 📂 Files Distribution

### Each Member Should Have:

```
YourName_Component/
├── [YourFile].java           ← Your component
├── screenshots/              ← 3-4 screenshots of your part
├── explanation.md            ← Your detailed explanation
└── code_snippets.md          ← Key code from your file
```

### Final Submission (Compiled by Member 1):

```
WeatherApp_Complete/
├── src/weatherapp/
│   ├── WeatherServer.java
│   ├── WeatherDataFetcher.java
│   ├── WeatherBroadcaster.java
│   ├── WeatherClient.java
│   └── NetworkMonitor.java
├── screenshots/              ← All screenshots combined
├── logs/                     ← Sample log files
├── README.md                 ← Project overview
├── REPORT.pdf               ← Final report (IMPORTANT!)
├── run.bat
└── run.ps1
```

---

## 💡 Tips for Success

### For Individual Work:
1. **Understand, don't just copy** - Read the code line by line
2. **Run independently** - Test your component alone first
3. **Document as you go** - Take notes while studying the code
4. **Ask questions** - If you don't understand something, ask team
5. **Be specific** - In report, explain YOUR specific implementation

### For Team Work:
1. **Communicate regularly** - Use WhatsApp/Discord/Teams
2. **Share progress** - Update others on what you've done
3. **Review each other's work** - Peer review before final submission
4. **Stay consistent** - Use same formatting, terminology
5. **Meet deadlines** - Don't delay individual tasks

### For the Report:
1. **Use technical terms** - Socket, Thread, Synchronization, etc.
2. **Include code snippets** - Show key implementations
3. **Explain WHY, not just WHAT** - Why you used ExecutorService, etc.
4. **Add diagrams** - Architecture, flow charts, sequence diagrams
5. **Be professional** - Proper grammar, formatting, citations

---

## 🎓 Grading Criteria (Typical)

| Aspect | Weight | What to Focus On |
|--------|--------|------------------|
| **Code Quality** | 30% | All 5 components working, well-commented |
| **Concepts Used** | 25% | Clear demonstration of network programming |
| **Individual Contribution** | 20% | Each member's distinct implementation |
| **Report Quality** | 15% | Well-written, detailed, professional |
| **Screenshots/Demo** | 10% | Clear outputs showing functionality |

---

## ✅ Final Checklist

Before submission, verify:

- [ ] All 5 Java files compile without errors
- [ ] Server starts successfully
- [ ] Client can connect and execute commands
- [ ] Multiple clients can connect simultaneously
- [ ] Weather data is fetched and displayed
- [ ] Log file is created with events
- [ ] All screenshots are clear and labeled
- [ ] Report has all required sections
- [ ] Each member's contribution is documented
- [ ] Code is properly commented
- [ ] README.md is complete
- [ ] Submission package is properly organized

---

## 🚀 Quick Command Reference

```bash
# Compile everything
cd WeatherApp_Complete/src
javac weatherapp\*.java

# Run server (Terminal 1)
java weatherapp.WeatherServer

# Run client (Terminal 2)
java weatherapp.WeatherClient

# Test individual components
java weatherapp.NetworkMonitor
java weatherapp.WeatherDataFetcher
```

---

## 📞 Communication Template

**For Team WhatsApp/Discord:**

```
Member Name: [Your Name]
Component: [Your File Name]
Status: [In Progress / Completed / Need Help]
Progress: [What you've done today]
Blockers: [Any issues you're facing]
Next Steps: [What you'll do next]
ETA: [When you'll finish]
```

Example:
```
Member Name: John
Component: WeatherClient.java
Status: Completed
Progress: Finished understanding the code, tested it, took screenshots
Blockers: None
Next Steps: Writing my section in the report
ETA: Tomorrow
```

---

## 🎯 Success Criteria

Your project is successful when:

✅ Application runs without errors
✅ All 5 components are functional
✅ Multiple clients can connect
✅ Weather data is fetched and displayed
✅ Broadcasting works
✅ Monitoring logs events properly
✅ Report is comprehensive and well-written
✅ Each member understands their component
✅ Screenshots show all features
✅ Code is well-commented and clean

---

**Remember:** This is a TEAM project. Support each other, communicate well, and deliver quality work!

**Good luck! 🎉**

---

## Quick Questions & Answers

**Q: Do we need to modify the code?**  
A: No! The code is complete and working. Just understand it and document it.

**Q: Can we split files differently?**  
A: No, each file is designed for one member. Don't mix responsibilities.

**Q: What if someone doesn't do their part?**  
A: Communicate early! If issues persist, inform your instructor.

**Q: How long should individual explanation be?**  
A: 2-3 pages per member in the report, covering concepts + implementation.

**Q: Do we need to create new features?**  
A: No, unless instructor asks. Focus on understanding and documenting existing features.

**Q: What if the API key doesn't work?**  
A: The application has mock data fallback. It will work without real API.

**Q: How to take good screenshots?**  
A: Full screen, clear text, show actual working features, add captions.

**Q: Should we print the code in report?**  
A: Include key snippets (5-20 lines), not entire files. Full code goes in appendix.

---

**END OF GUIDE**
