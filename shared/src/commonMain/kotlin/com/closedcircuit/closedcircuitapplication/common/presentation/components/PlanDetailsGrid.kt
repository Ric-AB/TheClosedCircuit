package com.closedcircuit.closedcircuitapplication.common.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PlanDetailsGrid(
    modifier: Modifier,
    sector: String,
    duration: String,
    estimatedCostPrice: String?,
    estimatedSellingPrice: String?,
    targetAmount: String?,
    totalFundsRaised: String?,
) {

    @Composable
    fun GridRow(item1: @Composable () -> Unit, item2: @Composable () -> Unit) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                item1()
            }

            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                item2()
            }
        }
    }


    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GridRow(
            item1 = {
                GridItem(
                    imageResource = SharedRes.images.ic_briefcase,
                    header = sector,
                    message = stringResource(SharedRes.strings.business_sector)
                )
            },
            item2 = {
                GridItem(
                    imageResource = SharedRes.images.ic_calendar,
                    header = duration,
                    message = stringResource(SharedRes.strings.plan_duration)
                )
            }
        )

        if (targetAmount != null && totalFundsRaised != null) {
            GridRow(
                item1 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_target_arrow,
                        header = targetAmount,
                        message = stringResource(SharedRes.strings.target_amount)
                    )
                },
                item2 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_funds,
                        header = totalFundsRaised,
                        message = stringResource(SharedRes.strings.total_funds_raised)
                    )
                }
            )
        }

        if (estimatedCostPrice != null && estimatedSellingPrice != null) {
            GridRow(
                item1 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_orange_bag,
                        header = estimatedCostPrice,
                        message = stringResource(SharedRes.strings.estimated_cost_price_per_unit_label)
                    )
                },
                item2 = {
                    GridItem(
                        imageResource = SharedRes.images.ic_purple_file,
                        header = estimatedSellingPrice,
                        message = stringResource(SharedRes.strings.estimated_selling_price_per_unit_label)
                    )
                }
            )
        }
    }
}

@Composable
private fun GridItem(imageResource: ImageResource, header: String, message: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = header,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = message,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}