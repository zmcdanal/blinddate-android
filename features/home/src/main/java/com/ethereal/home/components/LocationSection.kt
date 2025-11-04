package com.ethereal.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import reusables.neonOutlinedTextFieldColors

@Composable
fun LocationSection(
    cityState: String,
    onFindCityState: (String) -> Unit,
    miles: Int,
    onMilesChange: (Int) -> Unit,
    recenterOnUser: () -> Unit,
    modifier: Modifier = Modifier,
    minMiles: Int = 1,
    maxMiles: Int = 200,
    presetMiles: List<Int> = listOf(5, 10, 15, 20, 25)
) {
    var cityStateHolder by remember { mutableStateOf(cityState) }
    val cityStatePattern = remember { Regex(".+,\\s*[A-Za-z]{2}") } // "City, ST"
    val cityStateIsError =
        cityStateHolder.isNotBlank() && !cityStatePattern.matches(cityStateHolder)
    val canFind = !cityStateIsError && cityStateHolder.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Location & Radius",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        Button(
            onClick = recenterOnUser,
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonRose,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
            modifier = Modifier.width(240.dp)
        ) { Text("Use my location", style = MaterialTheme.typography.labelLarge) }

        // Divider "or"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                "or",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        OutlinedTextField(
            value = cityStateHolder,
            onValueChange = { cityStateHolder = it },
            label = { Text("City, State") },
            singleLine = true,
            isError = cityStateIsError,
            supportingText = { if (cityStateIsError) Text("Format: City, ST") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (canFind) onFindCityState(cityStateHolder) }
            ),
            colors = neonOutlinedTextFieldColors(),
            modifier = Modifier.width(240.dp)
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onFindCityState(cityStateHolder) },
            enabled = canFind,
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (canFind) NeonRose else NeonRose.copy(alpha = 0.4f),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
            modifier = Modifier.width(240.dp)
        ) { Text("Find", style = MaterialTheme.typography.labelLarge) }

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
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
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
            onStart = {},
            recenterOnUser = {},
            cityState = "Birmingham, AL",
            onFindCityState = {}
        )
    }
}