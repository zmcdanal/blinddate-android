package com.ethereal.onboarding.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ethereal.design.R as designR
import com.ethereal.design.theme.*
import com.ethereal.onboarding.OnboardingUiState
import com.ethereal.onboarding.R
import com.ethereal.ui.BlindDateBackground

private val LOCATION_PERMS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@Composable
fun OnboardingLocationGateScreen(
    uiState: OnboardingUiState,
    onAdvance: () -> Unit,
    onLocationGranted: () -> Unit
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val isPreview = LocalInspectionMode.current

    fun hasAnyPermission(): Boolean =
        LOCATION_PERMS.any {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

    // Local signal of a *recent* permission result (used for "blocked" UX)
    var lastSystemResult by remember { mutableStateOf<Map<String, Boolean>?>(null) }

    val onLocationGrantedUpdated by rememberUpdatedState(onLocationGranted)

    // Launcher → if granted, notify VM immediately
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        lastSystemResult = result
        val granted = result.values.any { it }
        if (granted && !uiState.locationEnabled && !isPreview) {
            onLocationGrantedUpdated()
        }
    }

    // Sync VM on first composition if already granted (e.g., returning user)
    LaunchedEffect(Unit) {
        if (!isPreview && hasAnyPermission() && !uiState.locationEnabled) {
            onLocationGrantedUpdated()
        }
    }

    // Re-check on resume (covers returning from App Settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && !isPreview) {
                if (hasAnyPermission() && !uiState.locationEnabled) {
                    onLocationGrantedUpdated()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Only compute rationale if we have a real Activity
    val showRationale = remember(activity, lastSystemResult, uiState.locationEnabled) {
        activity != null && LOCATION_PERMS.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
    }

    // Consider "blocked" only after we've actually asked once
    val isBlocked = !showRationale && lastSystemResult != null && !uiState.locationEnabled

    // Continue is driven by VM (and preview for design)
    val continueEnabled by remember(uiState.locationEnabled, isPreview) {
        derivedStateOf { uiState.locationEnabled || isPreview }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.onboarding_location_title),
            style = BlindDateTypography.headlineLarge,
            textAlign = TextAlign.Center,
            color = FogWhite,
            modifier = Modifier
                .fillMaxWidth(.9f)
                .padding(top = 16.dp)
        )

        Image(
            painter = painterResource(designR.drawable.location_map),
            contentDescription = stringResource(R.string.onboarding_location_illustration_cd),
            modifier = Modifier
                .size(300.dp)
                .padding(top = 16.dp, bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.onboarding_location_description),
            style = BlindDateTypography.bodyMedium,
            textAlign = TextAlign.Center,
            color = FogWhite.copy(.85f),
            modifier = Modifier.fillMaxWidth(.9f)
        )

        if (uiState.locationEnabled) {
            Spacer(Modifier.height(12.dp))
            AssistChip(
                onClick = {},
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = NeonRose,
                    labelColor = FogWhite
                ),
                label = { Text(stringResource(R.string.onboarding_location_enabled_chip)) }
            )
        }

        if (showRationale && !continueEnabled) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.onboarding_location_rationale),
                style = BlindDateTypography.bodySmall,
                textAlign = TextAlign.Center,
                color = FogWhite.copy(alpha = 0.75f),
                modifier = Modifier.fillMaxWidth(.9f)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Request permissions
        Button(
            onClick = { if (!isPreview) launcher.launch(LOCATION_PERMS) },
            enabled = !uiState.locationEnabled,
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonRose,
                contentColor = FogWhite
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp)
        ) { Text(stringResource(R.string.onboarding_location_enable)) }

        // "Don't ask again" path → App Settings
        if (isBlocked) {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = FogWhite),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp)
            ) { Text(stringResource(R.string.onboarding_location_open_settings)) }

            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.onboarding_location_required_note),
                style = BlindDateTypography.bodySmall,
                textAlign = TextAlign.Center,
                color = FogWhite.copy(alpha = 0.65f),
                modifier = Modifier.fillMaxWidth(.9f)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Continue (gated by VM)
        Button(
            onClick = onAdvance,
            enabled = continueEnabled,
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (continueEnabled) NeonRose else NeonRose.copy(alpha = 0.4f),
                contentColor = FogWhite,
                disabledContainerColor = NeonRose.copy(alpha = 0.25f),
                disabledContentColor = FogWhite.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp)
        ) { Text(stringResource(R.string.onboarding_location_continue)) }
    }
}

/** Safely unwrap an Activity from any Context (returns null in preview). */
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Preview
@Composable
private fun PreviewOnboardingLocationGateScreen() {
    BlindDateTheme {
        BlindDateBackground {
            OnboardingLocationGateScreen(
                uiState = OnboardingUiState(locationEnabled = true),
                onAdvance = {},
                onLocationGranted = {}
            )
        }
    }
}
