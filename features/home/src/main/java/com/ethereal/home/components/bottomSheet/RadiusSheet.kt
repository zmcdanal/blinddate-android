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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.NeonRose
import com.ethereal.design.reusables.neonOutlinedTextFieldColors

@Composable
fun RadiusSheet(
    modifier: Modifier = Modifier,
    miles: Int,
    onMilesChange: (Int) -> Unit
) {
    val presetMiles = Constants.presetRadiusMiles

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.search_radius),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = miles.toString(),
            onValueChange = { milesString ->
                val milesInt = milesString.filter { it.isDigit() }.toIntOrNull()
                if (milesInt != null) onMilesChange(milesInt.coerceIn(Constants.MIN_RADIUS_MILES, Constants.MAX_RADIUS_MILES))
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