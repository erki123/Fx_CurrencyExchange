package com.example.fx_currencyexchange;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloController {

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> currencyFrom;

    @FXML
    private ComboBox<String> currencyTo;

    @FXML
    private Label resultLabel;

    @FXML
    public void initialize() {
        // Initialize your ComboBoxes here
        currencyFrom.getItems().addAll("USD", "EUR", "GBP", "AUD", "DKK", "HUF", "SEK");
        currencyTo.getItems().addAll("USD", "EUR", "GBP", "AUD", "DKK", "HUF", "SEK");
    }

    @FXML
    private void onAmountInputChanged() {
        String text = amountField.getText();
        if (!text.matches("\\d*(\\.\\d*)?")) {
            amountField.setText(text.replaceAll("[^\\d.]", ""));
        }
    }

    @FXML
    protected void handleConvert() {
        String fromCurrency = currencyFrom.getValue();
        String toCurrency = currencyTo.getValue();
        String amount = amountField.getText();

        // Call the API and update resultLabel
        String result = getConversionRate(fromCurrency, toCurrency, amount);
        resultLabel.setText(result);
    }

    private String getConversionRate(String from, String to, String amountString) {
        String apiKey = "fca_live_Zk1BErROpqR8ODROErDIkYdvv3rqu26iuMDDu4Wz";
        String requestURL = "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Response: " + response.toString());

                JSONParser parser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());


                JSONObject dataObject = (JSONObject) jsonResponse.get("data");
                if (dataObject == null) {
                    return "Error: 'data' not found in JSON response";
                }

                if (!dataObject.containsKey(from) || !dataObject.containsKey(to)) {
                    return "Error: One of the specified currencies is not found in the response";
                }

                double fromRate = Double.parseDouble(dataObject.get(from).toString());
                double toRate = Double.parseDouble(dataObject.get(to).toString());
                double amount = Double.parseDouble(amountString);

                double convertedAmount = (amount / fromRate) * toRate;
                return String.format("%.2f %s = %.2f %s", amount, from, convertedAmount, to);
            } else {
                return "Failed to fetch currency data: HTTP error code " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }





}