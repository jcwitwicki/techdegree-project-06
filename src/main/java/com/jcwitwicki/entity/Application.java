package com.jcwitwicki.entity;

import com.jcwitwicki.entity.dao.CountryDao;
import com.jcwitwicki.entity.model.Country;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Application {

    private static CountryDao dao = new CountryDao();
    private Prompter prompter = new Prompter();
    private List<Country> countryIterable;
    private Country country;
    private String countryCode;
    private String countryName;
    private double internetUsersRate;
    private double adultLiteracyRate;
    private Comparator<Country> comparator;
    private boolean alreadyUsed;
    private List<Double> rateList = new ArrayList<>();
    private double averageRate;

    public Application() throws IOException {
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

    protected void viewStatistics() {
        System.out.printf("%nThose statistics only include countries which have data" +
                            " reported for both indicators%n");
        try {
            System.out.printf("%nLowest Internet Users Rate : %.2f - %s%n",
                    lowestRateCalculation(internetUsersRateComparator()).getInternetUsersRate(),
                    lowestRateCalculation(internetUsersRateComparator()).getCountryName());
            System.out.printf("Highest Internet Users Rate : %.2f  - %s%n",
                    highestRateCalculation(internetUsersRateComparator()).getInternetUsersRate(),
                    highestRateCalculation(internetUsersRateComparator()).getCountryName());
            System.out.printf("Average Internet Users Rate : %.2f%n%n", averageRateCalculation(getInternetUsersRateList()));

            System.out.printf("Lowest Adult Literacy Rate : %.2f  - %s%n",
                    lowestRateCalculation(adultLiteracyRateComparator()).getAdultLiteracyRate(),
                    lowestRateCalculation(adultLiteracyRateComparator()).getCountryName());
            System.out.printf("Highest Adult Literacy Rate : %.2f  - %s%n",
                    highestRateCalculation(adultLiteracyRateComparator()).getAdultLiteracyRate(),
                    highestRateCalculation(adultLiteracyRateComparator()).getCountryName());
            System.out.printf("Average Adult Literacy Rate : %.2f%n%n", averageRateCalculation(getAdultLiteracyRateList()));

        } catch (NoSuchElementException nse) {
            System.out.printf("%nPlease enter data first");
        }
    }

    protected void addCountry() throws IOException {
        System.out.printf("%nAdding country %n");
        do {
            countryCode = prompter.insertCountryCode();
        } while (alreadyUsed(countryCode));
        do {
            countryName = prompter.insertCountryName();
        } while (alreadyUsed(countryName));
        internetUsersRate = prompter.insertInternetUsersRate();
        adultLiteracyRate = prompter.insertAdultLiteracyRate();

        Country country = new Country.CountryBuilder(countryCode, countryName)
                .withInternetUsersRate(internetUsersRate)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();

        CountryDao.save(country);
        System.out.printf("%n%s added to database %n", country.getCountryName());
    }

    protected void deleteCountry() throws IOException {
        System.out.printf("%nDeleting country %n");
        country = selectCountry();
        CountryDao.delete(country);
        System.out.printf("%n%s deleted from database %n", country.getCountryName());
    }

    protected void editCountry() throws IOException {
        System.out.printf("%nEditing country %n%n");
        country = selectCountry();
        country.setCountryName(prompter.insertCountryName());
        country.setInternetUsersRate(prompter.insertInternetUsersRate());
        country.setAdultLiteracyRate(prompter.insertAdultLiteracyRate());
        CountryDao.update(country);
        System.out.printf("%n%s updated in database %n", country.getCountryName());
    }

    private Country selectCountry() throws IOException {
        viewListCountries();
        Country countrySelected = null;
        String countryCode = prompter.insertCountryCode();
            for (Country country : countryIterable) {
                if (countryCode.equals(country.getCountryCode()))
                {
                    countrySelected = country;
                }
            }
            if (countrySelected==null) {
                System.out.printf("%nNo match%n");
                return selectCountry();
            }
        return countrySelected;
    }

    private List<Country> eligibleCountriesForStatistics() {
        countryIterable = dao.fetchAllCountries();
        List<Country> eligibleCountries = new ArrayList<>();

        countryIterable.forEach(country -> {
            if (country.getInternetUsersRate()!=0.00 && country.getAdultLiteracyRate()!=0.00) {
                eligibleCountries.add(country);
            }
        });
        return eligibleCountries;
    }

    private Comparator<Country> adultLiteracyRateComparator() {
        comparator = Comparator.comparingDouble(Country::getAdultLiteracyRate);
        return comparator;
    }

    private Comparator<Country> internetUsersRateComparator() {
        comparator = Comparator.comparingDouble(Country::getInternetUsersRate);
        return comparator;
    }

    private Country lowestRateCalculation(Comparator<Country> countryComparator) {
        Country lowestRate = eligibleCountriesForStatistics().stream()
                               .min(countryComparator)
                               .get();
        return lowestRate;
    }

    private Country highestRateCalculation(Comparator<Country> countryComparator) {
        Country highestRate = eligibleCountriesForStatistics().stream()
                                .max(countryComparator)
                                .get();
        return highestRate;
    }

    private double averageRateCalculation(List<Double> rateList) {
        averageRate = rateList.stream()
                        .mapToDouble(a -> a)
                        .average()
                        .getAsDouble();
        return averageRate;
    }

    private List<Double> getInternetUsersRateList() {
        eligibleCountriesForStatistics().forEach(country -> rateList.add(country.getInternetUsersRate()));
        return rateList;
    }

    private List<Double> getAdultLiteracyRateList() {
        eligibleCountriesForStatistics().forEach(country -> rateList.add(country.getAdultLiteracyRate()));
        return rateList;
    }

    private String checkIfNull(Double rate) {
        DecimalFormat twoDForm = new DecimalFormat("#.00");
        String result = String.valueOf(twoDForm.format(rate));
        if (rate==0.00) {
            result = "--";
        }
        return result;
    }

    private boolean alreadyUsed(String str) {
        alreadyUsed = false;
        countryIterable = dao.fetchAllCountries();
        countryIterable.forEach(country -> {
            if (str.equals(country.getCountryCode()) || str.equals(country.getCountryName())) {
                alreadyUsed = true;
                System.out.printf("%nAlready used%n");
            }
        });
        return alreadyUsed;
    }

}


