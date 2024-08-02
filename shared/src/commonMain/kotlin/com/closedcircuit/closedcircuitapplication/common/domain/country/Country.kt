package com.closedcircuit.closedcircuitapplication.common.domain.country

import kotlinx.serialization.Serializable

@Serializable
data class Country(val name: String, val flag: String, val code: String, val dialCode: String) {

    init {
        require(name.isNotBlank()) { "Invalid country- $name" }
    }

    override fun toString(): String {
        return "$flag $dialCode  $name"
    }

    fun displayShort(includeFlag: Boolean = true): String {
        return if (includeFlag) "$flag $dialCode"
        else dialCode
    }
}

@Serializable
data class CountryData(
    val defaultCountry: Country,
    val countries: List<Country>
) {

    fun findByName(name: String): Country {
        return countries.first { it.name.equals(name, true) }
    }

    fun getAvailableCountries(): List<Country> {
        return countries.filter {
            it.code == "NG" || it.code == "ZA"
        }
    }
}