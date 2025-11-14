package com.ethereal.home.components.bottomSheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.home.components.bottomSheet.slide_navigation.PlannerStep
import com.ethereal.model.data.CuisineOption
import com.ethereal.model.data.DateDetails
import com.ethereal.model.data.MapData

@Composable
fun PlannerSheet(
    sheetHeight: Dp,
    dateDetails: DateDetails,
    onFindCityState: (String) -> Unit,
    onSetRadius: (Int) -> Unit,
    onToggleCuisine: (String) -> Unit,
    recenterOnUser: () -> Unit,
    currentStep: PlannerStep,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onPriceLevelChange: (Int) -> Unit,
    onGuestsChange: (Int) -> Unit,
    onMinRatingChange: (Int) -> Unit,
    onCollapseClick: () -> Unit,
) {
    val tempTestOptions = listOf(
        CuisineOption("american", "American", "🍔"),
        CuisineOption("mexican", "Mexican", "🌮"),
        CuisineOption("korean", "Korean", "🍜"),
        CuisineOption("italian", "Italian", "🍝"),
        CuisineOption("japanese", "Japanese", "🍣"),
        CuisineOption("thai", "Thai", "🥟"),
        CuisineOption("indian", "Indian", "🍛"),
        CuisineOption("bbq", "BBQ", "🥩"),
        CuisineOption("mediterranean", "Mediterranean", "🥙"),
        CuisineOption("vegan", "Vegan", "🥗"),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = sheetHeight)
    ) {
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    // Going forward (Next)
                    slideInHorizontally { fullWidth -> fullWidth } togetherWith
                            slideOutHorizontally { fullWidth -> -fullWidth }
                } else {
                    // Going backward (Back)
                    slideInHorizontally { fullWidth -> -fullWidth } togetherWith
                            slideOutHorizontally { fullWidth -> fullWidth }
                }
            },
            label = "PlannerStep"
        ) { step ->
            when (step) {
                PlannerStep.Location -> {
                    PlannerStepScaffold(
                        title = "Mystery Date Planner",
                        showBack = false,
                        nextLabel = "Next: Radius",
                        onBack = onBack,
                        onNext = onNext
                    ) {
                        LocationSheet(
                            dateDetails = dateDetails,
                            cityState = dateDetails.mapData.cityState,
                            onFindCityState = onFindCityState,
                            recenterOnUser = recenterOnUser,
                        )
                    }
                }

                PlannerStep.Radius -> {
                    var radiusMiles by remember {
                        mutableIntStateOf(dateDetails.mapData.radiusMiles)
                    }

                    PlannerStepScaffold(
                        title = "Pick a radius",
                        showBack = true,
                        nextLabel = "Next: Cuisine",
                        onBack = onBack,
                        onNext = onNext
                    ) {
                        RadiusSheet(
                            miles = radiusMiles,
                            onMilesChange = {
                                radiusMiles = it
                                onSetRadius(it)
                            }
                        )
                    }
                }

                PlannerStep.Cuisine -> {
                    PlannerStepScaffold(
                        title = "Choose cuisines",
                        showBack = true,
                        nextLabel = "Next: Date details",
                        onBack = onBack,
                        onNext = onNext
                    ) {
                        CuisineSheet(
                            options = tempTestOptions,
                            selectedIds = dateDetails.cuisineIds,
                            onToggle = onToggleCuisine
                        )
                    }
                }

                PlannerStep.Details -> {
                    PlannerStepScaffold(
                        title = "Choose date details",
                        showBack = true,
                        nextLabel = "Find my spot",
                        onBack = onBack,
                        onNext = onNext
                    ) {
                        DetailsSheet(
                            priceLevel = dateDetails.priceLevel,
                            onPriceLevelChange = onPriceLevelChange,
                            guests = dateDetails.guests,
                            onGuestsChange = onGuestsChange,
                            minRating = dateDetails.minRating,
                            onMinRatingChange = onMinRatingChange,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Shared layout for each planner step:
 * - Title at top
 * - Scrollable content in the middle
 * - Nav buttons pinned at the bottom
 */
@Composable
private fun PlannerStepScaffold(
    title: String,
    showBack: Boolean,
    nextLabel: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .padding(top = 12.dp)
    ) {
        // Header
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Scrollable content area
        Column(
            modifier = Modifier
                .weight(1f, fill = true)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }

        // Nav buttons pinned to bottom
        PlannerStepNavButtons(
            showBack = showBack,
            nextLabel = nextLabel,
            onBack = onBack,
            onNext = onNext
        )
    }
}

/**
 * Shared Back / Next button row.
 * - If showBack = false → single full-width Next button.
 * - If showBack = true → Back + Next as a split row.
 */
@Composable
private fun PlannerStepNavButtons(
    showBack: Boolean,
    nextLabel: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    backLabel: String = "Back"
) {
    if (!showBack) {
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(nextLabel)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text(backLabel)
            }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f)
            ) {
                Text(nextLabel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPlannerSheetContent() {
    BlindDateTheme {
        PlannerSheet(
            sheetHeight = 400.dp,
            dateDetails = DateDetails(mapData = MapData(userLocation = null, radiusMiles = 5)),
            onFindCityState = {},
            onSetRadius = {},
            onToggleCuisine = {},
            currentStep = PlannerStep.Details,
            onNext = {},
            onBack = {},
            recenterOnUser = {},
            onPriceLevelChange = {},
            onGuestsChange = {},
            onMinRatingChange = {},
            onCollapseClick = {}
        )
    }
}
