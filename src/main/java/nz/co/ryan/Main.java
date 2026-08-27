package nz.co.ryan;
import java.util.Scanner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        // Initialise a scanner
        Scanner scanner = new Scanner(System.in);

        // Get users NZBN
        System.out.print("Enter an NZBN: ");
        String nzbn = scanner.nextLine();

        // Check the NZBN is exactly 13 digits long.
        if(!nzbn.matches("\\d{13}"))
        {
            System.out.println("Invalid NZBN. It must be 13 digits!");
            return;
        }

        // Access the api
        String baseUrl = "https://api.business.govt.nz/gateway/nzbn/v5/entities/";
        String requestUrl = baseUrl + nzbn;

        // omitted as we don't need to see everything
        //System.out.println("Requesting:");
        //System.out.println(requestUrl);

        String apiKey = System.getenv("NZBN_API_KEY");

        if(apiKey == null || apiKey.isBlank()){
            System.out.println("NZBN_API_KEY environment variable is not set.");
            return;
        }

        // Create request for api starts here
        // start building new HTTP req
        HttpRequest request = HttpRequest.newBuilder()
                // set where the HTTP request is heading
                .uri(URI.create(requestUrl))
                        // attach the MBIE api key
                        .header("Ocp-Apim-Subscription-key", apiKey)
                                // asks for request return to be in json format
                                .header("Accept", "application/json")
                                        .GET()
                                                // finish building the request
                                                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // omitted as we don't need to see everything, was for debugging
        //System.out.println("Status: " + response.statusCode());
        //System.out.println(response.body());

        //Parsing the response of the requests
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.body());

        switch(response.statusCode()){
            case 200:
                // success
                // read the values from the response and return them in a nice format
                String entityName = root.get("entityName").asText();
                String entityStatusDescription = root.get("entityStatusDescription").asText();
                String registrationDate = root.get("registrationDate").asText();

                System.out.println("NZBN: " + nzbn);
                System.out.println("Business name: " + entityName);
                System.out.println("Business status: " + entityStatusDescription);
                System.out.println("Business registration date: " + registrationDate);
                break;
            case 400:
                // bad request / failure
                System.out.println("Bad request. Check the NZBN or request format.");
                break;
            case 401:
                // authentication problem
                System.out.println("Authentication problem. Check your api key.");
                break;
            case 404:
                // NZBN not found
                System.out.println("NZBN was not found, re-check the the NZBN entered.");
                break;
            default:
                // Anything unexpected
                System.out.println("uh oh! Something unexpected has happened.");
                break;
        }
    }
}