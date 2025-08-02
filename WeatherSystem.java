import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherSystem {

    // ANSI color codes
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE_BOLD = "\u001B[1;37m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Your working API key
        String apiKey = "5fff193e63d7f52b3e8ab6f18ac9a874";

        System.out.println(CYAN + "======================================" + RESET);
        System.out.println(WHITE_BOLD + "        WEATHER INFORMATION SYSTEM    " + RESET);
        System.out.println(CYAN + "======================================\n" + RESET);

        System.out.print(YELLOW + "Enter city name: " + RESET);
        String city = sc.nextLine();

        try {
            // Create API URL
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + apiKey + "&units=metric";

            // Connect to API
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String json = response.toString();

            // Extract values from JSON
            String temp = json.split("\"temp\":")[1].split(",")[0];
            String condition = json.split("\"description\":\"")[1].split("\"")[0];
            String humidity = json.split("\"humidity\":")[1].split(",")[0];
            String pressure = json.split("\"pressure\":")[1].split(",")[0];
            String windSpeed = json.split("\"speed\":")[1].split(",")[0];
            String cityName = json.split("\"name\":\"")[1].split("\"")[0];

            // Convert special characters to plain ASCII
            cityName = cityName.replaceAll("[^\\p{ASCII}]", "a");

            // Choose text label for weather condition
            String weatherSymbol;
            if (condition.contains("cloud")) {
                weatherSymbol = "Cloudy";
            } else if (condition.contains("clear")) {
                weatherSymbol = "Sunny";
            } else if (condition.contains("rain")) {
                weatherSymbol = "Rain";
            } else if (condition.contains("snow")) {
                weatherSymbol = "Snow";
            } else {
                weatherSymbol = "Weather";
            }

            // Display formatted output with colors
            System.out.println("\n" + CYAN + "--------------------------------------" + RESET);
            System.out.println(WHITE_BOLD + "   WEATHER REPORT FOR " + cityName.toUpperCase() + RESET);
            System.out.println(CYAN + "--------------------------------------" + RESET);
            System.out.printf("%-15s : %s%n", "Condition", GREEN + weatherSymbol + " (" + condition + ")" + RESET);
            System.out.printf("%-15s : %s°C%n", "Temperature", RED + temp + RESET);
            System.out.printf("%-15s : %s%%%n", "Humidity", BLUE + humidity + RESET);
            System.out.printf("%-15s : %s hPa%n", "Pressure", YELLOW + pressure + RESET);
            System.out.printf("%-15s : %s m/s%n", "Wind Speed", CYAN + windSpeed + RESET);
            System.out.println(CYAN + "--------------------------------------\n" + RESET);

        } catch (Exception e) {
            System.out.println(RED + "Error fetching weather data. Please check city name or API key." + RESET);
            e.printStackTrace();
        }

        sc.close();
    }
}
