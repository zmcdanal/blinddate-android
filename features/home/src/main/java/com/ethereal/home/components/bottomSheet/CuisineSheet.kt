package com.ethereal.home.components.bottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.reusables.DatePlanningSectionCard
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.CuisineOption

@Composable
fun CuisineSheet(
    title: String = "Cuisine & Vibe",
    options: List<CuisineOption>,
    selectedIds: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    DatePlanningSectionCard(
        title = title,
        subtitle = "Pick one or more cuisines you’re in the mood for. We’ll keep the mystery.",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 3
            ) {
                options.forEach { option ->
                    CuisineCard(
                        option = option,
                        selected = option.id in selectedIds,
                        onClick = { onToggle(option.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CuisineCard(
    option: CuisineOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(96.dp)
    ) {
        Card(
            shape = shape,
            border = BorderStroke(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) NeonRose else MaterialTheme.colorScheme.outlineVariant
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (selected) {
                    NeonRose.copy(alpha = 0.14f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                }
            ),
            modifier = Modifier
                .size(width = 96.dp, height = 82.dp)
                .clickable(
                    role = Role.Checkbox,
                    onClick = onClick
                )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.emoji,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = option.label,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
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
            onToggle = {}
        )
    }
}
