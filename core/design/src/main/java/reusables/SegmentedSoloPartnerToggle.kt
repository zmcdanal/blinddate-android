package reusables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose

@Composable
fun SegmentedSoloPartnerToggle(isPartner: Boolean, onToggle: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(0.85f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        @Composable
        fun SegmentButton(text: String, selected: Boolean, click: () -> Unit) {
            Button(
                onClick = click,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected) NeonRose else Color.Transparent,
                    contentColor = FogWhite
                ),
                border = if (selected) null else BorderStroke(1.dp, NeonRose),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
            ) {
                Text(text, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
            }
        }
        SegmentButton("Solo", !isPartner) { onToggle(false) }
        SegmentButton("With Partner", isPartner) { onToggle(true) }
    }
}