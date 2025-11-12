package weatherapp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * MEMBER 4 - Client Application
 * 
 * Concepts Demonstrated:
 * - Socket Programming (Socket)
 * - Multithreading (Listener Thread)
 * - Stream Handling (InputStream/OutputStream)
 * - User Interface (Console)
 */
public class WeatherClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private volatile boolean running;
    private Thread listenerThread;
    
    public WeatherClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.scanner = new Scanner(System.in);
        this.running = true;
        
        System.out.println("✓ Connected to Weather Server at " + host + ":" + port);
    }
    
    /**
     * Start the client - spawn listener thread and handle user input
     */
    public void start() {
        // Start listener thread to receive messages from server
        listenerThread = new Thread(new ServerListener());
        listenerThread.setDaemon(false);
        listenerThread.start();
        
        // Wait a moment for welcome message
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Handle user input
        handleUserInput();
    }
    
    /**
     * Handle user input and send to server
     */
    private void handleUserInput() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  You can now type commands");
        System.out.println("═══════════════════════════════════════════\n");
        
        while (running) {
            try {
                System.out.print("> ");
                String input = scanner.nextLine();
                
                if (input.trim().isEmpty()) {
                    continue;
                }
                
                // Send to server
                out.println(input);
                
                // Check if user wants to quit
                if (input.trim().equalsIgnoreCase("quit")) {
                    running = false;
                    break;
                }
                
            } catch (Exception e) {
                if (running) {
                    System.err.println("Error reading input: " + e.getMessage());
                }
                break;
            }
        }
        
        disconnect();
    }
    
    /**
     * Disconnect from server
     */
    public void disconnect() {
        running = false;
        
        try {
            if (listenerThread != null && listenerThread.isAlive()) {
                listenerThread.interrupt();
            }
            
            if (scanner != null) scanner.close();
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
            
            System.out.println("\n✓ Disconnected from server");
            
        } catch (IOException e) {
            System.err.println("Error during disconnect: " + e.getMessage());
        }
    }
    
    /**
     * Inner class - Listener thread to continuously read from server
     * Demonstrates multithreading for non-blocking I/O
     */
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while (running && (message = in.readLine()) != null) {
                    // Print server message
                    System.out.println(message);
                    
                    // Show prompt again after server message
                    if (running) {
                        System.out.print("> ");
                    }
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("\n✗ Connection to server lost: " + e.getMessage());
                    running = false;
                }
            }
        }
    }
    
    /**
     * Main method with enhanced CLI
     */
    public static void main(String[] args) {
        // Print banner
        printBanner();
        
        Scanner scanner = new Scanner(System.in);
        
        // Get connection details
        System.out.print("Enter server host (default: localhost): ");
        String host = scanner.nextLine().trim();
        if (host.isEmpty()) {
            host = DEFAULT_HOST;
        }
        
        System.out.print("Enter server port (default: 8080): ");
        String portInput = scanner.nextLine().trim();
        int port = DEFAULT_PORT;
        if (!portInput.isEmpty()) {
            try {
                port = Integer.parseInt(portInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port, using default: " + DEFAULT_PORT);
            }
        }
        
        // Connect and start
        WeatherClient client = null;
        try {
            System.out.println("\nConnecting to " + host + ":" + port + "...");
            client = new WeatherClient(host, port);
            
            // Add shutdown hook
            final WeatherClient finalClient = client;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                finalClient.disconnect();
            }));
            
            client.start();
            
        } catch (UnknownHostException e) {
            System.err.println("✗ Error: Unknown host '" + host + "'");
            System.err.println("  Please check the hostname and try again");
        } catch (ConnectException e) {
            System.err.println("✗ Error: Could not connect to server");
            System.err.println("  Make sure the server is running on " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Print welcome banner
     */
    private static void printBanner() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        Weather Client Application         ║");
        System.out.println("║  Multi-Client Weather Broadcasting System ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
    
    /**
     * Quick start method for testing
     */
    public static void quickStart() throws IOException {
        WeatherClient client = new WeatherClient(DEFAULT_HOST, DEFAULT_PORT);
        client.start();
    }
}
