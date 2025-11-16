package com.ethereal.home.ui.bottomSheet

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.blinddate.features.home.R
import com.ethereal.common.Constants
import com.ethereal.design.reusables.DatePlanningSectionCard
import com.ethereal.design.reusables.neonOutlinedTextFieldColors
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.NeonRose

@Composable
fun RadiusSheet(
    modifier: Modifier = Modifier,
    miles: Int,
    onMilesChange: (Int) -> Unit
) {
    val presetMiles = Constants.presetRadiusMiles

    DatePlanningSectionCard(
        title = stringResource(R.string.search_radius),
        subtitle = "How far are you willing to drive for this date?",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Numeric input
            OutlinedTextField(
                value = if (miles == 0) "" else miles.toString(),
                onValueChange = { milesString ->
                    if (milesString.isEmpty()) {
                        onMilesChange(0)
                    } else {
                        try {
                            val milesInt = milesString.toInt()
                            onMilesChange(
                                milesInt.coerceIn(
                                    Constants.MIN_RADIUS_MILES,
                                    Constants.MAX_RADIUS_MILES
                                )
                            )
                        } catch (exception: Exception) {
                            Log.d("RadiusSheet", "milesString to milesInt error: ", exception)
                            onMilesChange(0)
                        }
                    }
                },
                singleLine = true,
                label = { Text("Custom distance") },
                suffix = { Text("mi") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                colors = neonOutlinedTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 260.dp)
            )

            Text(
                text = "Tip: 10–20 miles is a sweet spot for most date nights.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            // Preset chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presetMiles.forEach { preset ->
                    val selected = miles == preset
                    androidx.compose.material3.FilterChip(
                        selected = selected,
                        onClick = { onMilesChange(preset) },
                        label = { Text("$preset mi") },
                        shape = RoundedCornerShape(999.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selected,
                            borderColor = NeonRose.copy(alpha = 0.6f),
                            selectedBorderColor = NeonRose,
                            disabledBorderColor = NeonRose.copy(alpha = 0.25f),
                            disabledSelectedBorderColor = NeonRose.copy(alpha = 0.25f)
                        ),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Transparent,
                            selectedContainerColor = NeonRose.copy(alpha = 0.18f),
                            labelColor = MaterialTheme.colorScheme.onSurface,
                            selectedLabelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRadiusSheet() {
    BlindDateTheme {
        RadiusSheet(
            miles = 15,
            onMilesChange = {}
        )
    }
}
