package weatherapp;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * MEMBER 2 - Weather API Integration
 * 
 * Concepts Demonstrated:
 * - HTTP Networking (HttpURLConnection)
 * - REST API Integration
 * - JSON Parsing (Pure Java)
 * - Exception Handling
 */
public class WeatherDataFetcher {
    // Using OpenWeatherMap API (free tier)
    // Register at: https://openweathermap.org/api
    private static final String API_KEY = "10ddcc4dc4ddbbf83185b2a6faccbb40"; // Replace with real API key
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    
    private NetworkMonitor monitor;
    private Map<String, WeatherCache> cache;
    private static final long CACHE_DURATION = 300000; // 5 minutes
    
    /**
     * Weather data cache to avoid excessive API calls
     */
    private static class WeatherCache {
        String data;
        long timestamp;
        
        WeatherCache(String data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_DURATION;
        }
    }
    
    public WeatherDataFetcher(NetworkMonitor monitor) {
        this.monitor = monitor;
        this.cache = new HashMap<>();
    }
    
    /**
     * Fetch weather data for a specific city
     * Uses HttpURLConnection for HTTP networking
     */
    public String fetchWeatherData(String city) {
        // Check cache first
        WeatherCache cached = cache.get(city.toLowerCase());
        if (cached != null && !cached.isExpired()) {
            monitor.onApiSuccess("Weather data retrieved from cache for " + city);
            return cached.data;
        }
        
        try {
            // Build URL with parameters
            String urlString = String.format("%s?q=%s&appid=%s&units=metric",
                API_URL, URLEncoder.encode(city, "UTF-8"), API_KEY);
            
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // Configure connection
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response using BufferedReader and InputStreamReader
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
                
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                // Parse JSON response
                String weatherInfo = parseWeatherJson(response.toString(), city);
                
                // Cache the result
                cache.put(city.toLowerCase(), new WeatherCache(weatherInfo));
                
                // Notify monitor of success
                monitor.onApiSuccess("Weather data fetched successfully for " + city);
                
                return weatherInfo;
                
            } else if (responseCode == 401) {
                // Invalid API key - return mock data
                monitor.onApiFailure("Invalid API key - returning mock data");
                return getMockWeatherData(city);
                
            } else if (responseCode == 404) {
                monitor.onApiFailure("City not found: " + city);
                return "Error: City '" + city + "' not found. Please check the spelling.";
                
            } else {
                monitor.onApiFailure("HTTP error code: " + responseCode);
                return getMockWeatherData(city);
            }
            
        } catch (MalformedURLException e) {
            monitor.onApiFailure("Invalid URL: " + e.getMessage());
            return "Error: Invalid request";
            
        } catch (SocketTimeoutException e) {
            monitor.onApiFailure("API request timeout");
            return getMockWeatherData(city);
            
        } catch (IOException e) {
            monitor.onApiFailure("Network error: " + e.getMessage());
            return getMockWeatherData(city);
            
        } catch (Exception e) {
            monitor.onApiFailure("Unexpected error: " + e.getMessage());
            return "Error: Could not fetch weather data";
        }
    }
    
    /**
     * Parse JSON response manually (Pure Java - no external libraries)
     * Demonstrates string manipulation and data extraction
     */
    private String parseWeatherJson(String json, String city) {
        try {
            // Extract temperature
            String temp = extractJsonValue(json, "\"temp\":");
            
            // Extract feels_like
            String feelsLike = extractJsonValue(json, "\"feels_like\":");
            
            // Extract humidity
            String humidity = extractJsonValue(json, "\"humidity\":");
            
            // Extract description
            String description = extractJsonString(json, "\"description\":");
            
            // Extract wind speed
            String windSpeed = extractJsonValue(json, "\"speed\":");
            
            // Format the response
            StringBuilder result = new StringBuilder();
            result.append("\n╔════════════════════════════════════════════╗\n");
            result.append(String.format("║  Weather Report - %-24s ║\n", city));
            result.append("╠════════════════════════════════════════════╣\n");
            result.append(String.format("║  Temperature:    %6.1f°C                  ║\n", 
                Double.parseDouble(temp)));
            result.append(String.format("║  Feels Like:     %6.1f°C                  ║\n", 
                Double.parseDouble(feelsLike)));
            result.append(String.format("║  Humidity:       %6s%%                    ║\n", humidity));
            result.append(String.format("║  Conditions:     %-24s ║\n", 
                capitalize(description)));
            result.append(String.format("║  Wind Speed:     %6s m/s                 ║\n", windSpeed));
            result.append("╚════════════════════════════════════════════╝\n");
            
            return result.toString();
            
        } catch (Exception e) {
            // If parsing fails, return mock data
            return getMockWeatherData(city);
        }
    }
    
    /**
     * Extract numeric value from JSON string
     */
    private String extractJsonValue(String json, String key) {
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return "0";
        
        startIndex += key.length();
        int endIndex = startIndex;
        
        while (endIndex < json.length() && 
               (Character.isDigit(json.charAt(endIndex)) || 
                json.charAt(endIndex) == '.' || 
                json.charAt(endIndex) == '-')) {
            endIndex++;
        }
        
        return json.substring(startIndex, endIndex).trim();
    }
    
    /**
     * Extract string value from JSON
     */
    private String extractJsonString(String json, String key) {
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return "N/A";
        
        startIndex = json.indexOf("\"", startIndex + key.length()) + 1;
        int endIndex = json.indexOf("\"", startIndex);
        
        return json.substring(startIndex, endIndex);
    }
    
    /**
     * Capitalize first letter of each word
     */
    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        
        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
    
    /**
     * Return mock weather data when API is unavailable
     * This ensures the application continues to work even without API access
     */
    private String getMockWeatherData(String city) {
        Random random = new Random();
        double temp = 15 + random.nextInt(20);
        int humidity = 40 + random.nextInt(40);
        double windSpeed = 2 + random.nextInt(10);
        
        String[] conditions = {"Clear Sky", "Partly Cloudy", "Cloudy", "Light Rain", "Sunny"};
        String condition = conditions[random.nextInt(conditions.length)];
        
        StringBuilder result = new StringBuilder();
        result.append("\n╔════════════════════════════════════════════╗\n");
        result.append(String.format("║  Weather Report - %-24s ║\n", city));
        result.append("║  [DEMO DATA - API Key Required]            ║\n");
        result.append("╠════════════════════════════════════════════╣\n");
        result.append(String.format("║  Temperature:    %6.1f°C                  ║\n", temp));
        result.append(String.format("║  Feels Like:     %6.1f°C                  ║\n", temp - 2));
        result.append(String.format("║  Humidity:       %6d%%                    ║\n", humidity));
        result.append(String.format("║  Conditions:     %-24s ║\n", condition));
        result.append(String.format("║  Wind Speed:     %6.1f m/s                 ║\n", windSpeed));
        result.append("╚════════════════════════════════════════════╝\n");
        result.append("Note: Using simulated data. Add API key for real data.\n");
        
        return result.toString();
    }
    
    /**
     * Get weather for multiple cities
     */
    public Map<String, String> fetchMultipleCities(List<String> cities) {
        Map<String, String> results = new HashMap<>();
        
        for (String city : cities) {
            results.put(city, fetchWeatherData(city));
        }
        
        return results;
    }
    
    /**
     * Clear the cache
     */
    public void clearCache() {
        cache.clear();
        System.out.println("Weather data cache cleared");
    }
    
    /**
     * Test method
     */
    public static void main(String[] args) {
        NetworkMonitor monitor = new NetworkMonitor("logs/fetcher_test.log");
        WeatherDataFetcher fetcher = new WeatherDataFetcher(monitor);
        
        System.out.println("Testing Weather Data Fetcher...\n");
        
        String[] cities = {"London", "New York", "Tokyo", "InvalidCity123"};
        
        for (String city : cities) {
            System.out.println("Fetching weather for: " + city);
            String weather = fetcher.fetchWeatherData(city);
            System.out.println(weather);
            System.out.println();
        }
        
        monitor.shutdown();
    }
}
