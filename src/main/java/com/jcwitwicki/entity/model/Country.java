package com.jcwitwicki.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Country implements Comparable<Country> {

    @Id
    private String countryCode;

    @Column
    private String countryName;

    @Column
    private double internetUsersRate;

    @Column
    private double adultLiteracyRate;

    // Default constructor for JPA
    public Country(){}

    public Country(CountryBuilder builder) {
        this.countryCode = builder.countryCode;
        this.countryName = builder.countryName;
        this.internetUsersRate = builder.internetUsersRate;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    @Override
    public String toString() {

        return countryCode + "|" + countryName + "|" + internetUsersRate + "|" + adultLiteracyRate;

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public double getInternetUsersRate() { return internetUsersRate; }

    public void setInternetUsersRate(double internetUsersRate) {
        this.internetUsersRate = internetUsersRate;
    }

    public double getAdultLiteracyRate() { return adultLiteracyRate; }

    public void setAdultLiteracyRate(double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    public int compareTo(Country o) {
        return countryName.compareToIgnoreCase(o.getCountryName());
    }

    public static class CountryBuilder {
        private String countryCode;
        private String countryName;
        private double internetUsersRate;
        private double adultLiteracyRate;

        public CountryBuilder(String countryCode, String countryName) {
            this.countryCode = countryCode;
            this.countryName = countryName;
        }

        public CountryBuilder withInternetUsersRate(Double internetUsersRate) {
            this.internetUsersRate = internetUsersRate;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(Double adultLiteracyRate) {
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build() {
            return new Country(this);
        }

    }
}
