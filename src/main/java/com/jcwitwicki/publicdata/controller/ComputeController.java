package com.jcwitwicki.publicdata.controller;

        import com.jcwitwicki.publicdata.dao.CountryDaoImpl;
        import com.jcwitwicki.publicdata.model.Country;

        import java.util.ArrayList;
        import java.util.Comparator;
        import java.util.List;
        import java.util.NoSuchElementException;
        import java.util.function.Function;
        import java.util.stream.Collectors;

public class ComputeController {

    private static CountryDaoImpl dao = new CountryDaoImpl();

    public ComputeController() {}

    protected void viewStatistics() {

        System.out.printf("%nThose statistics only include countries which have data" +
                " reported for both indicators:%n");
        try {
            System.out.printf("%nLowest Internet Users Rate : %.2f - %s%n",
                    lowestRateComputation(internetUsersRateComparator()).getInternetUsersRate(),
                    lowestRateComputation(internetUsersRateComparator()).getCountryName());
            System.out.printf("Highest Internet Users Rate : %.2f  - %s%n",
                    highestRateComputation(internetUsersRateComparator()).getInternetUsersRate(),
                    highestRateComputation(internetUsersRateComparator()).getCountryName());
            System.out.printf("Average Internet Users Rate : %.2f%n%n", averageRateComputation(getInternetUsersRateList()));

            System.out.printf("Lowest Adult Literacy Rate : %.2f  - %s%n",
                    lowestRateComputation(adultLiteracyRateComparator()).getAdultLiteracyRate(),
                    lowestRateComputation(adultLiteracyRateComparator()).getCountryName());
            System.out.printf("Highest Adult Literacy Rate : %.2f  - %s%n",
                    highestRateComputation(adultLiteracyRateComparator()).getAdultLiteracyRate(),
                    highestRateComputation(adultLiteracyRateComparator()).getCountryName());
            System.out.printf("Average Adult Literacy Rate : %.2f%n%n", averageRateComputation(getAdultLiteracyRateList()));

            System.out.printf("Correlation coefficient between Internet Users" +
                    " and Adult Literacy: %.2f%n%n " ,correlationCoefficientComputation());


            System.out.printf("Correlation Coefficient is used in statistics to measure" +
                    " the correlation between two sets of data. It has a value between" +
                    " +1 and −1, where 1 is total positive linear correlation," +
                    " 0 is no linear correlation, and −1 is total negative linear correlation.%n");

        } catch (NoSuchElementException nse) {
            System.out.printf("%nPlease enter data first");
        }
    }

    private Country lowestRateComputation(Comparator<Country> countryComparator) {
        return eligibleCountriesForStatistics().stream()
                .min(countryComparator)
                .get();
    }

    private Country highestRateComputation(Comparator<Country> countryComparator) {
        return eligibleCountriesForStatistics().stream()
                .max(countryComparator)
                .get();
    }

    private double averageRateComputation(List<Double> rateList) {
        return rateList.stream()
                .mapToDouble(a -> a)
                .average()
                .getAsDouble();
    }

    private ArrayList<Double> getAdultLiteracyRateList() {
        List<Double> rateList = new ArrayList<>();
        eligibleCountriesForStatistics().forEach(country -> rateList.add(country.getAdultLiteracyRate()));
        return (ArrayList<Double>) rateList;
    }

    private ArrayList<Double> getInternetUsersRateList() {
        List<Double> rateList = new ArrayList<>();
        eligibleCountriesForStatistics().forEach(country -> rateList.add(country.getInternetUsersRate()));
        return (ArrayList<Double>) rateList;
    }

    private Comparator<Country> internetUsersRateComparator() {
        return Comparator.comparingDouble(Country::getInternetUsersRate);
    }

    private Comparator<Country> adultLiteracyRateComparator() {
        return Comparator.comparingDouble(Country::getAdultLiteracyRate);
    }

//  all calculated statistics should only include countries that have data reported for both indicators
    private List<Country> eligibleCountriesForStatistics() {
        List<Country> countryIterable = dao.fetchAllCountries();
        List<Country> eligibleCountries = new ArrayList<>();
        countryIterable.forEach(country -> {
            if (country.getInternetUsersRate()!=0.00 && country.getAdultLiteracyRate()!=0.00) {
                eligibleCountries.add(country);
            }
        });
        return eligibleCountries;
    }

    /*
        Pearson Correlation Coefficient Formula :

        r = (NΣxy - (Ex)(Ey)) / SQRT([NΣx2 - (Σx)2][NΣy2 - (Σy)2])

        r   = correlation coefficient
        N   = number of pairs of scores                 here the number of eligible countries
        Σxy = sum of the products of paired scores      here the sum of the products of internet users rate
                                                        and adult literacy rate
        Σx  = sum of x scores                           here the sum of the internet users rates
        Σy  = sum of y scores                           here the sum of the adult literacy rates
        Σx2 = sum of squared x scores                   here the sum of the squared internet users rates
        Σy2 = sum of squared y scores                   here the sum of the squared adult literacy rates

    */

    private double correlationCoefficientComputation() {
        int N = eligibleCountriesForStatistics().size();
        double Ex = sumOfRates(getInternetUsersRateList());
        double Ey = sumOfRates(getAdultLiteracyRateList());
        double Exy = sumOfRates(listProductOfXandY(getInternetUsersRateList(),getAdultLiteracyRateList()));
        double Ex2 = sumOfRates(listToSquare(getInternetUsersRateList()));
        double Ey2 = sumOfRates(listToSquare(getAdultLiteracyRateList()));

        double coefficient = ((N * Exy) - (Ex*Ey)) / Math.sqrt(((N * Ex2) - (Ex * Ex)) * ((N * Ey2) - (Ey * Ey)));
        return coefficient;
    }

    private double sumOfRates(List<Double> rateList) {
        return rateList.stream()
                .mapToDouble(a -> a)
                .sum();
    }

    private List<Double> listProductOfXandY(ArrayList<Double> rateList1, ArrayList<Double> rateList2) {
        List<Double> list = new ArrayList<>();
        for(int i=0;i<rateList1.size();i++) {
            list.add(rateList1.get(i) * rateList2.get(i));
        }
        return list;
    }

    private List<Double> listToSquare(ArrayList<Double> rateList) {
        Function<Double, Double> square = x -> x * x;
        return  rateList.stream()
                .map(square)
                .collect(Collectors.toList());
    }
}
