package com.closedcircuit.closedcircuitapplication.common.data.country

import com.closedcircuit.closedcircuitapplication.common.domain.country.Country
import com.closedcircuit.closedcircuitapplication.common.domain.country.CountryData
import com.closedcircuit.closedcircuitapplication.common.domain.country.CountryRepository

class CountryRepositoryImpl(
    private val countryData: CountryData
) : CountryRepository {

    override fun getAllCountries(): List<Country> {
        return countryData.countries
    }

    override fun getDefaultCountry(): Country {
        return countryData.defaultCountry
    }

    override fun getAvailableCountries(): List<Country> {
        return countryData.getAvailableCountries()
    }

    override fun findByName(name: String): Country {
        return countryData.findByName(name)
    }
}