package com.closedcircuit.closedcircuitapplication.common.domain.country

interface CountryRepository {

    fun getAllCountries(): List<Country>

    fun getDefaultCountry(): Country

    fun getAvailableCountries(): List<Country>

    fun findByName(name: String): Country
}