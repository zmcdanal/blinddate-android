package com.ethereal.home.components.bottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.reusables.DatePlanningSectionCard
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.NeonRose

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Sheet title
        Text(
            text = "Date Details",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // === BUDGET / PRICE LEVEL CARD ===
        DatePlanningSectionCard (
            title = "Budget",
            subtitle = "How fancy are we feeling tonight?"
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                listOf("$", "$$", "$$$", "$$$$").forEachIndexed { idx, label ->
                    val level = idx + 1
                    BlindDateFilterChip(
                        selected = priceLevel == level,
                        onClick = { onPriceLevelChange(level) },
                        label = { Text(label) }
                    )
                }
            }
        }

        // === GUEST COUNT CARD ===
        DatePlanningSectionCard(
            title = "Guests",
            subtitle = "Who’s coming along on this adventure?"
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedIconButton(
                    onClick = { onGuestsChange((guests - 1).coerceAtLeast(1)) },
                    border = BorderStroke(1.dp, NeonRose.copy(alpha = 0.7f)),
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        contentColor = NeonRose
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease guest count"
                    )
                }

                Text(
                    text = "$guests",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .widthIn(min = 32.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedIconButton(
                    onClick = { onGuestsChange(guests + 1) },
                    border = BorderStroke(1.dp, NeonRose.copy(alpha = 0.7f)),
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        contentColor = NeonRose
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase guest count"
                    )
                }
            }
        }

        // === MINIMUM RATING CARD ===
        DatePlanningSectionCard(
            title = "Minimum rating",
            subtitle = "Pick how picky you want BlindDate to be."
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                (1..5).forEach { rating ->
                    BlindDateFilterChip(
                        selected = minRating == rating,
                        onClick = { onMinRatingChange(rating) },
                        label = { Text("★ $rating") }
                    )
                }
            }
        }
    }
}

@Composable
fun BlindDateFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        modifier = modifier,
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

@Preview(showBackground = true)
@Composable
private fun PreviewDetailsSheet() {
    BlindDateTheme {
        DetailsSheet(
            priceLevel = 3,
            onPriceLevelChange = {},
            guests = 2,
            onGuestsChange = {},
            minRating = 4,
            onMinRatingChange = {}
        )
    }
}
