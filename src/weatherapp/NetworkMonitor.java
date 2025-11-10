package weatherapp;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * MEMBER 5 - Simplified Network Monitoring
 * 
 * Concepts Demonstrated:
 * - Connection Monitoring
 * - Synchronized Methods (Thread Safety)
 * - File I/O (Logging)
 * - Event Notification System
 */
public class NetworkMonitor {
    
    // Thread-safe list to track active clients
    private final List<ClientInfo> activeClients;
    
    // Log file writer
    private PrintWriter logWriter;
    private final String logFilePath;
    
    // Date formatter for log entries
    private final SimpleDateFormat dateFormat;
    
    // Statistics
    private int totalConnectionsEver;
    private int totalDisconnections;
    private int totalApiFailures;
    private int totalApiSuccesses;
    
    /**
     * Inner class to hold client information
     */
    public static class ClientInfo {
        private final Socket socket;
        private final String clientId;
        private final long connectionTime;
        
        public ClientInfo(Socket socket, String clientId) {
            this.socket = socket;
            this.clientId = clientId;
            this.connectionTime = System.currentTimeMillis();
        }
        
        public Socket getSocket() {
            return socket;
        }
        
        public String getClientId() {
            return clientId;
        }
        
        public long getConnectionTime() {
            return connectionTime;
        }
        
        public String getAddress() {
            return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        }
    }
    
    /**
     * Constructor - initializes the monitor with default log file
     */
    public NetworkMonitor() {
        this("logs/network_monitor.log");
    }
    
    /**
     * Constructor - initializes the monitor with custom log file
     */
    public NetworkMonitor(String logFilePath) {
        this.activeClients = new CopyOnWriteArrayList<>();
        this.logFilePath = logFilePath;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.totalConnectionsEver = 0;
        this.totalDisconnections = 0;
        this.totalApiFailures = 0;
        this.totalApiSuccesses = 0;
        
        initializeLogFile();
    }
    
    /**
     * Initialize the log file
     */
    private void initializeLogFile() {
        try {
            // Create logs directory if it doesn't exist
            File logFile = new File(logFilePath);
            File parentDir = logFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            logWriter = new PrintWriter(new FileWriter(logFilePath, true), true);
            logEvent("INFO", "NetworkMonitor initialized - Log file: " + logFilePath);
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
        }
    }
    
    /**
     * Log an event to both console and file
     * Synchronized for thread safety
     */
    private synchronized void logEvent(String level, String message) {
        String timestamp = dateFormat.format(new Date());
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Log to console
        System.out.println(logEntry);
        
        // Log to file
        if (logWriter != null) {
            logWriter.println(logEntry);
            logWriter.flush();
        }
    }
    
    /**
     * Register a new client connection
     * Synchronized to prevent concurrent modification
     */
    public synchronized void onClientConnected(Socket clientSocket) {
        String clientId = "Client-" + (totalConnectionsEver + 1);
        ClientInfo clientInfo = new ClientInfo(clientSocket, clientId);
        
        activeClients.add(clientInfo);
        totalConnectionsEver++;
        
        logEvent("CONNECTION", String.format("%s connected from %s | Total active: %d", 
                clientId, clientInfo.getAddress(), activeClients.size()));
        
        // Send alert notification
        sendAlert("New client connected: " + clientId);
    }
    
    /**
     * Register a client disconnection
     * Synchronized to prevent concurrent modification
     */
    public synchronized void onClientDisconnected(Socket clientSocket) {
        ClientInfo disconnectedClient = null;
        
        // Find and remove the client
        for (ClientInfo client : activeClients) {
            if (client.getSocket().equals(clientSocket)) {
                disconnectedClient = client;
                activeClients.remove(client);
                break;
            }
        }
        
        if (disconnectedClient != null) {
            totalDisconnections++;
            long connectionDuration = System.currentTimeMillis() - disconnectedClient.getConnectionTime();
            long durationSeconds = connectionDuration / 1000;
            
            logEvent("DISCONNECTION", String.format("%s disconnected from %s | Duration: %ds | Total active: %d", 
                    disconnectedClient.getClientId(), 
                    disconnectedClient.getAddress(),
                    durationSeconds,
                    activeClients.size()));
            
            // Send alert notification
            sendAlert("Client disconnected: " + disconnectedClient.getClientId());
        }
    }
    
    /**
     * Register a client disconnection by client ID
     */
    public synchronized void onClientDisconnected(String clientId) {
        ClientInfo disconnectedClient = null;
        
        // Find and remove the client by ID
        for (ClientInfo client : activeClients) {
            if (client.getClientId().equals(clientId)) {
                disconnectedClient = client;
                activeClients.remove(client);
                break;
            }
        }
        
        if (disconnectedClient != null) {
            totalDisconnections++;
            long connectionDuration = System.currentTimeMillis() - disconnectedClient.getConnectionTime();
            long durationSeconds = connectionDuration / 1000;
            
            logEvent("DISCONNECTION", String.format("%s disconnected | Duration: %ds | Total active: %d", 
                    clientId, durationSeconds, activeClients.size()));
            
            sendAlert("Client disconnected: " + clientId);
        }
    }
    
    /**
     * Report a weather API fetch failure
     * Synchronized for thread-safe statistics update
     */
    public synchronized void onApiFailure(String reason) {
        totalApiFailures++;
        logEvent("API_FAILURE", String.format("Weather API fetch failed | Reason: %s | Total failures: %d", 
                reason, totalApiFailures));
        
        // Send alert notification
        sendAlert("Weather API fetch failed: " + reason);
    }
    
    /**
     * Report a successful API fetch
     * Synchronized for thread-safe statistics update
     */
    public synchronized void onApiSuccess(String details) {
        totalApiSuccesses++;
        logEvent("API_SUCCESS", "Weather API fetch successful | " + details);
    }
    
    /**
     * Send alert notification
     * Can be extended to send to external monitoring systems
     */
    private void sendAlert(String alertMessage) {
        // For now, just log as an alert
        // Can be extended to send notifications to connected clients or external systems
        logEvent("ALERT", alertMessage);
    }
    
    /**
     * Get current number of active clients
     */
    public synchronized int getActiveClientCount() {
        return activeClients.size();
    }
    
    /**
     * Get list of active clients (thread-safe copy)
     */
    public synchronized List<ClientInfo> getActiveClients() {
        return new ArrayList<>(activeClients);
    }
    
    /**
     * Get network statistics
     */
    public synchronized String getStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("\n╔════════════════════════════════════════════╗\n");
        stats.append("║        Network Statistics                  ║\n");
        stats.append("╠════════════════════════════════════════════╣\n");
        stats.append(String.format("║  Active Clients:      %-20d║\n", activeClients.size()));
        stats.append(String.format("║  Total Connections:   %-20d║\n", totalConnectionsEver));
        stats.append(String.format("║  Total Disconnects:   %-20d║\n", totalDisconnections));
        stats.append(String.format("║  API Successes:       %-20d║\n", totalApiSuccesses));
        stats.append(String.format("║  API Failures:        %-20d║\n", totalApiFailures));
        stats.append("╚════════════════════════════════════════════╝\n");
        return stats.toString();
    }
    
    /**
     * Print current active clients
     */
    public synchronized void printActiveClients() {
        logEvent("INFO", "Current active clients: " + activeClients.size());
        for (ClientInfo client : activeClients) {
            long connectedFor = (System.currentTimeMillis() - client.getConnectionTime()) / 1000;
            logEvent("INFO", String.format("  - %s at %s (connected for %ds)", 
                    client.getClientId(), client.getAddress(), connectedFor));
        }
    }
    
    /**
     * Periodic monitoring task - runs in a separate thread
     * Demonstrates background thread for periodic checks
     */
    public void startPeriodicMonitoring(int intervalSeconds) {
        Thread monitorThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                    
                    // Log periodic status
                    logEvent("MONITORING", String.format("Periodic check | Active clients: %d | API failures: %d", 
                            activeClients.size(), totalApiFailures));
                    
                    // Check for stale connections (optional)
                    checkStaleConnections();
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        monitorThread.setDaemon(true);
        monitorThread.setName("NetworkMonitor-PeriodicCheck");
        monitorThread.start();
        
        logEvent("INFO", "Periodic monitoring started with " + intervalSeconds + "s interval");
    }
    
    /**
     * Check for stale/closed connections
     * Synchronized for thread-safe list access
     */
    private synchronized void checkStaleConnections() {
        List<ClientInfo> staleClients = new ArrayList<>();
        
        for (ClientInfo client : activeClients) {
            if (client.getSocket().isClosed() || !client.getSocket().isConnected()) {
                staleClients.add(client);
            }
        }
        
        // Remove stale connections
        for (ClientInfo staleClient : staleClients) {
            activeClients.remove(staleClient);
            logEvent("WARNING", "Removed stale connection: " + staleClient.getClientId());
        }
    }
    
    /**
     * Shutdown the monitor and close resources
     */
    public synchronized void shutdown() {
        logEvent("INFO", "NetworkMonitor shutting down...");
        
        // Print final statistics
        System.out.println(getStatistics());
        
        // Close log writer
        if (logWriter != null) {
            logWriter.close();
        }
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║     NetworkMonitor - Member 5              ║");
        System.out.println("║     Standalone Test Mode                   ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        NetworkMonitor monitor = new NetworkMonitor("logs/test_monitor.log");
        
        // Start periodic monitoring
        monitor.startPeriodicMonitoring(10);
        
        // Simulate some events
        System.out.println("\n--- Simulating API events ---\n");
        
        monitor.onApiSuccess("Fetched weather for London - Temperature: 25°C");
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        monitor.onApiFailure("Connection timeout to weather service");
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        monitor.onApiSuccess("Fetched weather for New York - Temperature: 18°C");
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Print statistics
        System.out.println(monitor.getStatistics());
        
        // Keep running for a bit to see periodic monitoring
        System.out.println("Monitoring for 10 seconds...\n");
        try { Thread.sleep(10000); } catch (InterruptedException e) {}
        
        monitor.shutdown();
        
        System.out.println("\nTest complete! Check logs/test_monitor.log for details");
    }
}
