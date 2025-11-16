package com.ethereal.home.ui.bottomSheet

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.reusables.DatePlanningSectionCard
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.CuisineOption

@Composable
fun CuisineSheet(
    title: String = "Cuisine & vibe",
    options: List<CuisineOption>,
    selectedIds: Set<String>,
    onToggle: (String) -> Unit,
    fastFoodAllowed: Boolean,
    onFastFoodAllowedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    DatePlanningSectionCard(
        title = title,
        subtitle = "Tap a few that match tonight’s mood.",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Fast food toggle row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Fast food is okay",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = if (fastFoodAllowed) {
                            "We’ll include drive-thru and quick bites."
                        } else {
                            "We’ll avoid fast food chains."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Switch(
                    checked = fastFoodAllowed,
                    onCheckedChange = onFastFoodAllowedChange,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = NeonRose,
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            // Compact pill-style multi-select chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { option ->
                    CuisineFilterChip(
                        option = option,
                        selected = option.id in selectedIds,
                        onClick = { onToggle(option.id) }
                    )
                }
            }

            // Tiny helper / feedback text
            if (selectedIds.isNotEmpty()) {
                Text(
                    text = "${selectedIds.size} selected • You can keep it loose.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "You can pick a few or leave it open.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CuisineFilterChip(
    option: CuisineOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Stable random direction per chip (left or right tilt)
    val tiltDirection = remember(option.id) {
        if (kotlin.random.Random.nextBoolean()) 1f else -1f
    }

    val targetTilt = if (selected) 5f * tiltDirection else 0f

    val rotationZ by animateFloatAsState(
        targetValue = targetTilt,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cuisineTilt"
    )

    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            this.rotationZ = rotationZ
        },
        shape = RoundedCornerShape(999.dp),
        label = {
            Text(
                text = "${option.emoji}  ${option.label}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = NeonRose.copy(alpha = 0.5f),
            selectedBorderColor = NeonRose,
            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledSelectedBorderColor = NeonRose.copy(alpha = 0.3f)
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
            selectedContainerColor = NeonRose.copy(alpha = 0.18f),
            labelColor = MaterialTheme.colorScheme.onSurface,
            selectedLabelColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewCuisineSheet() {
    val tempTestOptions = listOf(
        CuisineOption("american", "American", "🍔"),
        CuisineOption("mexican", "Mexican", "🌮"),
        CuisineOption("korean", "Korean", "🍜"),
        CuisineOption("italian", "Italian", "🍝"),
        CuisineOption("japanese", "Japanese", "🍣"),
        CuisineOption("thai", "Thai", "🥟"),
        CuisineOption("indian", "Indian", "🍛"),
        CuisineOption("bbq", "BBQ", "🥩"),
        CuisineOption("med", "Mediterranean", "🥙"),
        CuisineOption("vegan", "Vegan", "🥗"),
    )

    BlindDateTheme {
        CuisineSheet(
            options = tempTestOptions,
            selectedIds = setOf("mexican", "thai"),
            onToggle = {},
            fastFoodAllowed = true,
            onFastFoodAllowedChange = {}
        )
    }
}
