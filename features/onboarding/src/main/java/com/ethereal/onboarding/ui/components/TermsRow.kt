package com.ethereal.onboarding.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.BlindDateTypography
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose

@Composable
fun TermsRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onOpenTermsOfService: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
) {
    val linkStyle = TextLinkStyles(
        style = SpanStyle(
            textDecoration = TextDecoration.Underline,
            color = NeonRose
        )
    )

    val linkListener = remember {
        object : LinkInteractionListener {
            override fun onClick(link: LinkAnnotation) {
                val tag = (link as? LinkAnnotation.Clickable)?.tag
                when (tag) {
                    "tos" -> onOpenTermsOfService()
                    "privacy" -> onOpenPrivacyPolicy()
                }
            }
        }
    }

    val annotated = buildAnnotatedString {
        append("I agree to the ")
        withLink(
            LinkAnnotation.Clickable(
                tag = "tos",
                styles = linkStyle,
                linkInteractionListener = linkListener
            )
        ) {
            append("Terms of Service")
        }
        append(" and ")
        withLink(
            LinkAnnotation.Clickable(
                tag = "privacy",
                styles = linkStyle,
                linkInteractionListener = linkListener
            )
        ) {
            append("Privacy Policy")
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(0.85f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)

        Spacer(Modifier.width(8.dp))

        Text(
            text = annotated,
            style = BlindDateTypography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            color = FogWhite,
        )
    }
}
