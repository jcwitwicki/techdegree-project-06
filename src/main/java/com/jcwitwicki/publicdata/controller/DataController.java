package com.jcwitwicki.publicdata.controller;

import com.jcwitwicki.publicdata.dao.CountryDaoImpl;
import com.jcwitwicki.publicdata.model.Country;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class DataController {

    private CountryDaoImpl dao = new CountryDaoImpl();
    private Prompter prompter = new Prompter();
    private List<Country> countryIterable;
    private Country country;
    private String countryCode;
    private String countryName;
    private double internetUsersRate;
    private double adultLiteracyRate;
    private boolean isAlreadyUsed;

    public DataController() throws IOException {
    }

    protected void viewListCountries() {
        countryIterable = dao.fetchAllCountries();
        Collections.sort(countryIterable);
        String alignFormat = "%-7s %-36s %6s %23s %n";
        System.out.printf("%nCode    Country                      Internet Users          Adult Literacy %n");
        System.out.printf("---------------------------------------------------------------------------%n");
        for (Country country : countryIterable) {
            System.out.printf(alignFormat, country.getCountryCode(),
                                           country.getCountryName(),
                                           checkIfNull(country.getInternetUsersRate()),
                                           checkIfNull(country.getAdultLiteracyRate()));
        }
    }

    protected void addCountry() throws IOException {
        System.out.printf("%nAdding country %n");
        do {
            countryCode = prompter.insertCountryCode();
        } while (checkIfCountryAlreadyUsed(countryCode));
        do {
            countryName = prompter.insertCountryName();
        } while (checkIfCountryAlreadyUsed(countryName));
        internetUsersRate = prompter.insertInternetUsersRate();
        adultLiteracyRate = prompter.insertAdultLiteracyRate();

        Country country = new Country.CountryBuilder(countryCode, countryName)
                .withInternetUsersRate(internetUsersRate)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();

        dao.save(country);
        System.out.printf("%n%s added to database %n", country.getCountryName());
    }

    protected void deleteCountry() throws IOException {
        System.out.printf("%nDeleting country %n");
        country = selectCountry();
        dao.delete(country);
        System.out.printf("%n%s deleted from database %n", country.getCountryName());
    }

    protected void editCountry() throws IOException {
        System.out.printf("%nEditing country %n%n");
        country = selectCountry();
        country.setCountryName(prompter.insertCountryName());
        country.setInternetUsersRate(prompter.insertInternetUsersRate());
        country.setAdultLiteracyRate(prompter.insertAdultLiteracyRate());
        dao.update(country);
        System.out.printf("%n%s updated in database %n", country.getCountryName());
    }

    private Country selectCountry() throws IOException {
        viewListCountries();
        Country countrySelected;
        do {
            String countryCode = prompter.insertCountryCode();
            countrySelected = dao.fetchCountryByCode(countryCode);
            if (countrySelected==null) {
                System.out.printf("%nNo match%n");
            }
        } while (countrySelected==null);
        return countrySelected;
    }

    private String checkIfNull(Double rate) {
        DecimalFormat twoDForm = new DecimalFormat("#.00");
        String result = String.valueOf(twoDForm.format(rate));
        if (rate==0.00) {
            result = "--";
        }
        return result;
}

    private boolean checkIfCountryAlreadyUsed(String str) {
        isAlreadyUsed = false;
        countryIterable = dao.fetchAllCountries();
        countryIterable.forEach(country -> {
            if (str.equals(country.getCountryCode()) || str.equals(country.getCountryName())) {
                isAlreadyUsed = true;
                System.out.printf("%nAlready used%n");
            }
        });
        return isAlreadyUsed;
    }

}


