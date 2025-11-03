package com.ethereal.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.NeonRose

@Composable
fun PriceSection(modifier: Modifier = Modifier) {

    var priceButtonSelected by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = "Price",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)
        )

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
                // Price $
            PriceButton(
                priceLevel = 1,
                selected = priceButtonSelected == 1,
                onClick = {
                    priceButtonSelected = 1
                }
            )

            // Price $$
            PriceButton(
                priceLevel = 2,
                selected = priceButtonSelected == 2,
                onClick = {
                    priceButtonSelected = 2
                }
            )

            // Price $$$
            PriceButton(
                priceLevel = 3,
                selected = priceButtonSelected == 3,
                onClick = {
                    priceButtonSelected = 3
                }
            )

            // Price $$$$
            PriceButton(
                priceLevel = 4,
                selected = priceButtonSelected == 4,
                onClick = {
                    priceButtonSelected = 4
                }
            )
        }
    }
}

@Composable
fun PriceButton(
    priceLevel: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = "$".repeat(priceLevel)
    val shape = RoundedCornerShape(999.dp)

    if (selected) {
        Button(
            onClick = onClick,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonRose,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = modifier.heightIn(min = 36.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = shape,
            border = BorderStroke(1.5.dp, NeonRose),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = NeonRose
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = modifier.heightIn(min = 36.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}
