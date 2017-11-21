package com.teamtreehouse.analyzedata.dao;

import com.teamtreehouse.analyzedata.model.Country;

import java.util.List;

public interface CountryDao  {

    void save(Country country);
    void update(Country country);
    void delete(Country country);
    List<Country> fetchAllCountries();
    Country fetchCountryByCode(String countryCode);

}
