package com.ethereal.home.components.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.BlindDateTheme

@Composable
fun DetailsSheet(
    priceLevel: Int,
    onPriceLevelChange: (Int) -> Unit,
    guests: Int,
    onGuestsChange: (Int) -> Unit,
    minRating: Int,
    onMinRatingChange: (Int) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Budget & Quality", style = MaterialTheme.typography.titleLarge)

        // $ .. $$$$
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("$", "$$", "$$$", "$$$$").forEachIndexed { idx, label ->
                val level = idx + 1
                FilterChip(
                    selected = priceLevel == level,
                    onClick = { onPriceLevelChange(level) },
                    label = { Text(label) }
                )
            }
        }

        // Guests (single counter)
        Counter("Guests", guests, onGuestsChange)

        // Minimum rating
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { rating ->
                FilterChip(
                    selected = minRating == rating,
                    onClick = { onMinRatingChange(rating) },
                    label = { Text("★ $rating") }
                )
            }
        }
    }
}

@Composable
private fun Counter(title: String, value: Int, onChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedIconButton(onClick = { onChange((value - 1).coerceAtLeast(1)) }) {
            Icon(Icons.Default.Remove, contentDescription = "decrease $title")
        }
        Text("$value", modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
        OutlinedIconButton(onClick = { onChange(value + 1) }) {
            Icon(Icons.Default.Add, contentDescription = "increase $title")
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewDetailsSheet() {
    BlindDateTheme {
        DetailsSheet(
            priceLevel = 4,
            onPriceLevelChange = {},
            minRating = 2,
            guests = 2,
            onGuestsChange = {},
            onMinRatingChange = {}
        )
    }
}
