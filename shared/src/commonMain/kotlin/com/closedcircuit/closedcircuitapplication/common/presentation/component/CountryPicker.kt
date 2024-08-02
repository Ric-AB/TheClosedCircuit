package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.domain.country.Country
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.common.presentation.util.conditional
import kotlinx.collections.immutable.ImmutableList

@Stable
data class PhoneNumberState(
    val inputField: InputField,
    val country: Country,
    val countryOptions: ImmutableList<Country>
) {
    fun updatePhone(phone: String): PhoneNumberState {
        inputField.onValueChange(phone)
        return this
    }

    fun updateCountry(country: Country): PhoneNumberState {
        return copy(country = country)
    }
}

@Composable
fun CountryPicker(
    modifier: Modifier = Modifier,
    data: Country,
    options: ImmutableList<Country>,
    onCountrySelected: ((Country) -> Unit)?
) {
    val enabled = remember { onCountrySelected != null }
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.conditional(
            condition = enabled,
            ifTrue = {
                Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { expanded = !expanded }
                )
            }
        ).padding(start = 16.dp)
    ) {
        Text(
            text = data.displayShort(),
            style = LocalTextStyle.current,
            fontWeight = FontWeight.Medium,
        )

        if (enabled) {
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }

    DialogMenu(
        visible = expanded,
        selectedItem = data,
        items = options,
        onItemSelected = { _, country -> onCountrySelected?.invoke(country) },
        onDismissRequest = { expanded = false }
    )
}