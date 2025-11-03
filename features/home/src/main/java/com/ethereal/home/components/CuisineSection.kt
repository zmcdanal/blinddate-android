package com.ethereal.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.CuisineOption

@Composable
fun CuisineSection(
    title: String = "Cuisine / Region",
    options: List<CuisineOption>,
    selectedIds: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 3,
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { opt ->
                    CuisineCard(
                        option = opt,
                        selected = opt.id in selectedIds,
                        onClick = { onToggle(opt.id) }
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
    val shape = RoundedCornerShape(14.dp)

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
                containerColor = if (selected)
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
                else
                    MaterialTheme.colorScheme.surfaceContainerHighest
            ),
            modifier = Modifier
                .size(96.dp, 80.dp)
                .clickable(role = Role.Checkbox, onClick = onClick)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(option.emoji, style = MaterialTheme.typography.headlineSmall)
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