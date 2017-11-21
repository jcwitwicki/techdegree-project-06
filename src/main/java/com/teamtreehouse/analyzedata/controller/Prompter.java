package com.teamtreehouse.analyzedata.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Prompter {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Map<Integer, String> menu = new LinkedHashMap<>();
    private int choice;
    private String prompt;

    public Prompter() throws IOException {
    }

    private void loadMenu() {
        menu.put(1, "View the data table");
        menu.put(2, "View the statistics");
        menu.put(3, "Add a country");
        menu.put(4, "Edit a country");
        menu.put(5, "Delete a country");
        menu.put(0, "Exit");
    }

    private int promptAction() throws IOException {
        System.out.printf("%nYour options are: %n%n");
        for (Map.Entry<Integer, String> entry : menu.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        System.out.printf("%nYour choice : ");
        choice = Integer.parseInt(reader.readLine());
        return choice;
    }

   public void promptMenu() throws IOException {
        DataController dataController = new DataController();
        ComputeController computeController = new ComputeController();
        loadMenu();
        choice=0;
        do {
            try {
                choice = promptAction();

                switch (choice) {
                    case 1:
                        dataController.viewListCountries();
                        break;

                    case 2:
                        computeController.viewStatistics();
                        break;

                    case 3:
                        dataController.addCountry();
                        break;

                    case 4:
                        dataController.editCountry();
                        break;

                    case 5:
                        dataController.deleteCountry();
                        break;

                    case 0:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Unknown choice. Please try again.");
                }
            } catch (NumberFormatException nfe) {
                System.out.printf("%nPlease enter a digit");
                promptMenu();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } while (choice!=0);
    }

    protected String insertCountryCode() throws IOException {
        String countryCode;
        do {
            System.out.printf("%nPlease enter the code of the country (3 letters): ");
            countryCode = reader.readLine().toUpperCase();
        } while ((countryCode.length() != 3) || (!countryCode.matches("[a-zA-Z]+")));
        return countryCode;
    }

    protected String insertCountryName() throws IOException {
        String countryName;
        do {
            System.out.printf("%nPlease enter the name of the country: ");
            countryName = reader.readLine().trim();
        } while (!countryName.matches("[a-zA-Z, -]+"));
        return countryName;
    }

    protected double insertInternetUsersRate() throws IOException {
        prompt = "Please enter the internet users rate (between 0.00 and 100.00, please enter 0 for unknown value): ";
        return insertRate(prompt);
    }

    protected double insertAdultLiteracyRate() throws IOException {
        prompt = "Please enter the adult literacy rate (between 0.00 and 100.00, please enter 0 for unknown value): ";
        return insertRate (prompt);
    }

    protected double insertRate(String prompt) throws IOException {
        double rate = -1.00;
        do {
            try {
                System.out.printf("%n%s",prompt);
                rate = Double.parseDouble(reader.readLine());
            } catch (NumberFormatException nfe) {
                System.out.printf("%nPlease enter a number%n");
            }
        } while (rate < 0.00 || rate > 100.00);
        return rate;
    }
}
