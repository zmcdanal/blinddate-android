package com.ethereal.home.components.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.ethereal.design.theme.NeonRose
import reusables.neonOutlinedTextFieldColors

@Composable
fun RadiusSheet(
    modifier: Modifier = Modifier,
    miles: Int,
    onMilesChange: (Int) -> Unit,
    minMiles: Int = 1,
    maxMiles: Int = 200,
    presetMiles: List<Int> = listOf(5, 10, 15, 20, 25)
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
private fun PreviewRadiusSheet() {
    BlindDateTheme {
        RadiusSheet(
            miles = 15,
            onMilesChange = {}
        )
    }
}