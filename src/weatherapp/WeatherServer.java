package weatherapp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * MEMBER 1 - Server Setup & Multithreaded Client Handling
 * 
 * Concepts Demonstrated:
 * -Socket Programming (ServerSocket)
 * -Multithreading (ExecutorService)
 * -Concurrent client handling
 * -Thread-safe collections
 */
public class WeatherServer {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;
    
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private List<ClientHandler> clientHandlers;
    private WeatherBroadcaster broadcaster;
    private NetworkMonitor networkMonitor;
    private WeatherDataFetcher weatherFetcher;
    private volatile boolean running;
    
    public WeatherServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.clientHandlers = new CopyOnWriteArrayList<>();
        this.running = true;
        
        // Initialize components
        this.networkMonitor = new NetworkMonitor("logs/server_monitor.log");
        this.weatherFetcher = new WeatherDataFetcher(networkMonitor);
        this.broadcaster = new WeatherBroadcaster(clientHandlers, networkMonitor);
        
        // Start periodic monitoring
        this.networkMonitor.startPeriodicMonitoring(30);
        
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  Multi-Client Weather Broadcasting System ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("Server started on port: " + port);
        System.out.println("Thread pool size: " + THREAD_POOL_SIZE);
        System.out.println("Waiting for clients to connect...\n");
    }
    
    /**
     *Start accepting client connections
     */
    public void start() {
        // Start weather update broadcaster in background
        new Thread(() -> {
            while (running) {
                try {
                    // Fetch and broadcast weather every 30 seconds
                    Thread.sleep(30000);
                    String weatherData = weatherFetcher.fetchWeatherData("London");
                    if (weatherData != null) {
                        broadcaster.broadcast("AUTO_UPDATE: " + weatherData);
                    }
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    System.err.println("Error in auto-update: " + e.getMessage());
                }
            }
        }).start();
        
        // Accept client connections
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                
                // Notify monitor about new connection
                networkMonitor.onClientConnected(clientSocket);
                
                // Create client handler
                ClientHandler handler = new ClientHandler(
                    clientSocket, 
                    this, 
                    networkMonitor,
                    weatherFetcher
                );
                
                clientHandlers.add(handler);
                
                // Handle client in thread pool
                executorService.execute(handler);
                
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Remove a client handler from the list
     */
    public void removeClient(ClientHandler handler) {
        clientHandlers.remove(handler);
    }
    
    /**
     * Get the broadcaster instance
     */
    public WeatherBroadcaster getBroadcaster() {
        return broadcaster;
    }
    
    /**
     * Shutdown the server gracefully
     */
    public void shutdown() {
        System.out.println("\n\nShutting down Weather Server...");
        running = false;
        
        // Close all client connections
        for (ClientHandler handler : clientHandlers) {
            handler.close();
        }
        
        // Shutdown executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        
        // Close server socket
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
        
        // Shutdown monitor
        networkMonitor.shutdown();
        
        System.out.println("Server shutdown complete!");
    }
    
    /**
     * Inner class to handle each client connection
     * Uses Runnable for thread execution
     */
    static class ClientHandler implements Runnable {
        private Socket socket;
        private WeatherServer server;
        private NetworkMonitor monitor;
        private WeatherDataFetcher fetcher;
        private BufferedReader in;
        private PrintWriter out;
        private String clientId;
        private volatile boolean active;
        
        public ClientHandler(Socket socket, WeatherServer server, 
                           NetworkMonitor monitor, WeatherDataFetcher fetcher) {
            this.socket = socket;
            this.server = server;
            this.monitor = monitor;
            this.fetcher = fetcher;
            this.active = true;
            this.clientId = "Client-" + socket.getPort();
        }
        
        @Override
        public void run() {
            try {
                // Setup I/O streams
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                // Send welcome message
                out.println("╔════════════════════════════════════════════╗");
                out.println("║     Welcome to Weather Server!             ║");
                out.println("║  Multi-Client Weather Broadcasting System ║");
                out.println("╚════════════════════════════════════════════╝");
                out.println("\nAvailable commands:");
                out.println("  weather <city>  - Get weather for a city");
                out.println("  subscribe       - Subscribe to auto-updates");
                out.println("  unsubscribe     - Unsubscribe from updates");
                out.println("  list            - List available cities");
                out.println("  help            - Show this help");
                out.println("  quit            - Disconnect\n");
                
                // Handle client requests
                String line;
                while (active && (line = in.readLine()) != null) {
                    handleCommand(line.trim());
                }
                
            } catch (IOException e) {
                if (active) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            } finally {
                close();
            }
        }
        
        /**
         * Handle client commands
         */
        private void handleCommand(String command) {
            if (command.isEmpty()) {
                return;
            }
            
            String[] parts = command.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();
            
            try {
                switch (cmd) {
                    case "weather":
                        if (parts.length > 1) {
                            String city = parts[1];
                            String weather = fetcher.fetchWeatherData(city);
                            if (weather != null) {
                                out.println(weather);
                            } else {
                                out.println("Error: Could not fetch weather for " + city);
                            }
                        } else {
                            out.println("Usage: weather <city>");
                        }
                        break;
                        
                    case "subscribe":
                        out.println("✓ Subscribed to automatic weather updates");
                        break;
                        
                    case "unsubscribe":
                        out.println("✓ Unsubscribed from automatic updates");
                        break;
                        
                    case "list":
                        out.println("Available cities:");
                        out.println("  - London, New York, Tokyo, Paris, Sydney");
                        out.println("  - Mumbai, Dubai, Singapore, Toronto, Berlin");
                        break;
                        
                    case "help":
                        out.println("Available commands:");
                        out.println("  weather <city>  - Get weather for a city");
                        out.println("  subscribe       - Subscribe to auto-updates");
                        out.println("  list            - List available cities");
                        out.println("  help            - Show this help");
                        out.println("  quit            - Disconnect");
                        break;
                        
                    case "quit":
                        out.println("Goodbye! Disconnecting...");
                        active = false;
                        break;
                        
                    default:
                        out.println("Unknown command: " + cmd + " (type 'help' for commands)");
                }
            } catch (Exception e) {
                out.println("Error processing command: " + e.getMessage());
            }
        }
        
        /**
         * Send message to this client
         */
        public void sendMessage(String message) {
            if (out != null && active) {
                out.println(message);
            }
        }
        
        /**
         * Close the client connection
         */
        public void close() {
            active = false;
            
            // Notify monitor
            monitor.onClientDisconnected(socket);
            
            // Remove from server's client list
            server.removeClient(this);
            
            // Close resources
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                System.err.println("Error closing client resources: " + e.getMessage());
            }
        }
        
        public boolean isActive() {
            return active;
        }
    }
    
    /**
     * Main method to start the server
     */
    public static void main(String[] args) {
        try {
            int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
            WeatherServer server = new WeatherServer(port);
            
            // Add shutdown hook for graceful exit
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.shutdown();
            }));
            
            // Start the server
            server.start();
            
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
