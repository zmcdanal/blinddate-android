package com.ethereal.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationSearching
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import reusables.NameField
import reusables.neonOutlinedTextFieldColors
import kotlin.math.roundToInt

@Composable
fun LocationSection(
    zip: String,
    onZipChange: (String) -> Unit,
    miles: Int,
    onMilesChange: (Int) -> Unit,
    onUseMyLocation: () -> Unit,
    modifier: Modifier = Modifier,
    minMiles: Int = 1,
    maxMiles: Int = 200,
    presetMiles: List<Int> = listOf(5, 10, 15, 20, 25)
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = "Location & Radius",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onUseMyLocation,
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonRose,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) { Text("Use my location", style = MaterialTheme.typography.labelLarge) }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    "or",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            val zipIsError = zip.isNotEmpty() && zip.length != 5
            OutlinedTextField(
                value = zip,
                onValueChange = { onZipChange(it.filter(Char::isDigit).take(5)) },
                label = { Text("ZIP code") },
                singleLine = true,
                isError = zipIsError,
                supportingText = { if (zipIsError) Text("Enter 5 digits") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                colors = neonOutlinedTextFieldColors(),
                modifier = Modifier.width(140.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Search radius",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = miles.toString(),
            onValueChange = { s ->
                val n = s.filter { it.isDigit() }.toIntOrNull()
                if (n != null) onMilesChange(n.coerceIn(minMiles, maxMiles))
            },
            singleLine = true,
            label = { Text("Distance") },
            suffix = { Text("mi") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = neonOutlinedTextFieldColors(),
            modifier = Modifier.width(140.dp)
        )

        Spacer(Modifier.height(12.dp))


        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presetMiles.forEach { preset ->
                val selected = miles == preset
                FilterChip(
                    selected = selected,
                    onClick = { onMilesChange(preset.coerceIn(minMiles, maxMiles)) },
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
                        labelColor = Color.Black,
                        selectedLabelColor = Color.Black,
                        iconColor = NeonRose.copy(alpha = 0.9f),
                        selectedLeadingIconColor = FogWhite,
                        selectedTrailingIconColor = FogWhite,
                        disabledContainerColor = Color.Transparent,
                        disabledLabelColor = FogWhite.copy(alpha = 0.4f),
                        disabledLeadingIconColor = NeonRose.copy(alpha = 0.25f),
                        disabledTrailingIconColor = NeonRose.copy(alpha = 0.25f)
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewPlannerSheetContent() {
    BlindDateTheme {
        PlannerSheetContent(
            selectedCuisine = setOf("american", "indian"),
            onToggleCuisine = {},
            onStart = {}
        )
    }
}