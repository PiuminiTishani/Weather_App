package weatherapp;

import java.io.*;
import java.util.*;

/**
 * MEMBER 3 - Real-Time Data Broadcasting
 * 
 * Concepts Demonstrated:
 * - Client-Server Communication
 * - Stream Handling (OutputStream/PrintWriter)
 * - Thread Synchronization
 * - Concurrent Collection Access
 */
public class WeatherBroadcaster {
    private List<WeatherServer.ClientHandler> clients;
    private NetworkMonitor monitor;
    private int broadcastCount;
    
    public WeatherBroadcaster(List<WeatherServer.ClientHandler> clients, 
                             NetworkMonitor monitor) {
        this.clients = clients;
        this.monitor = monitor;
        this.broadcastCount = 0;
    }
    
    /**
     * Broadcast message to all connected clients
     * Synchronized to avoid concurrent modification issues
     */
    public synchronized void broadcast(String message) {
        if (clients.isEmpty()) {
            System.out.println("No clients connected to broadcast to");
            return;
        }
        
        int successCount = 0;
        int failCount = 0;
        
        // Create a copy to avoid ConcurrentModificationException
        List<WeatherServer.ClientHandler> clientsCopy = new ArrayList<>(clients);
        
        for (WeatherServer.ClientHandler client : clientsCopy) {
            try {
                if (client.isActive()) {
                    client.sendMessage(message);
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                System.err.println("Failed to send to client: " + e.getMessage());
            }
        }
        
        broadcastCount++;
        
        // Log broadcast statistics
        String logMessage = String.format(
            "Broadcast #%d completed: %d successful, %d failed, %d total clients",
            broadcastCount, successCount, failCount, clients.size()
        );
        
        System.out.println(logMessage);
        monitor.onApiSuccess(logMessage);
    }
    
    /**
     * Broadcast to specific group of clients (filtered)
     */
    public synchronized void broadcastToGroup(String message, 
                                             Predicate<WeatherServer.ClientHandler> filter) {
        int count = 0;
        
        for (WeatherServer.ClientHandler client : clients) {
            try {
                if (client.isActive() && filter.test(client)) {
                    client.sendMessage(message);
                    count++;
                }
            } catch (Exception e) {
                System.err.println("Failed to send to client: " + e.getMessage());
            }
        }
        
        System.out.println("Targeted broadcast sent to " + count + " clients");
    }
    
    /**
     * Send weather alert to all clients
     */
    public void broadcastAlert(String alertType, String alertMessage) {
        String formattedAlert = formatAlert(alertType, alertMessage);
        broadcast(formattedAlert);
    }
    
    /**
     * Format alert message with visual indicators
     */
    private String formatAlert(String alertType, String message) {
        StringBuilder alert = new StringBuilder();
        alert.append("\n");
        alert.append("╔════════════════════════════════════════════╗\n");
        alert.append(String.format("║  ⚠️  ALERT: %-32s ║\n", alertType));
        alert.append("╠════════════════════════════════════════════╣\n");
        
        // Word wrap the message to fit in the box
        String[] words = message.split(" ");
        StringBuilder line = new StringBuilder("║  ");
        
        for (String word : words) {
            if (line.length() + word.length() + 1 > 44) {
                // Pad the line to 46 characters and add closing border
                while (line.length() < 44) {
                    line.append(" ");
                }
                line.append(" ║\n");
                alert.append(line);
                line = new StringBuilder("║  ");
            }
            line.append(word).append(" ");
        }
        
        // Add remaining content
        while (line.length() < 44) {
            line.append(" ");
        }
        line.append(" ║\n");
        alert.append(line);
        
        alert.append("╚════════════════════════════════════════════╝\n");
        
        return alert.toString();
    }
    
    /**
     * Broadcast weather update with timestamp
     */
    public void broadcastWeatherUpdate(String city, String weatherData) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss")
            .format(new java.util.Date());
        
        String message = String.format(
            "\n[%s] Weather Update for %s:\n%s",
            timestamp, city, weatherData
        );
        
        broadcast(message);
    }
    
    /**
     * Get broadcast statistics
     */
    public synchronized int getBroadcastCount() {
        return broadcastCount;
    }
    
    /**
     * Get number of active clients
     */
    public synchronized int getActiveClientCount() {
        int count = 0;
        for (WeatherServer.ClientHandler client : clients) {
            if (client.isActive()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Shutdown broadcaster
     */
    public synchronized void shutdown() {
        broadcast("\n[SERVER] Server is shutting down. Goodbye!\n");
        System.out.println("Broadcaster shutdown complete");
    }
    
    /**
     * Simple predicate interface for filtering
     */
    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T t);
    }
    
    /**
     * Test the broadcaster
     */
    public static void main(String[] args) {
        System.out.println("WeatherBroadcaster - Testing");
        System.out.println("This class is designed to work with WeatherServer");
        System.out.println("Run WeatherServer to see broadcasting in action");
    }
}
