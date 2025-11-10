package com.ethereal.home.components.bottomSheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.BlindDateTheme
import androidx.compose.ui.Alignment
import com.ethereal.home.components.bottomSheet.slide_navigation.PlannerStep
import com.ethereal.model.data.CuisineOption

@Composable
fun PlannerSheetRedesign(
    cityState: String,
    onFindCityState: (String) -> Unit,
    selectedCuisine: Set<String>,
    onToggleCuisine: (String) -> Unit,
    recenterOnUser: () -> Unit,
    currentStep: PlannerStep,
    onNext: () -> Unit,
    onBack: () -> Unit,
    priceLevel: Int,
    onPriceLevelChange: (Int) -> Unit,
    guests: Int,
    onGuestsChange: (Int) -> Unit,
    minRating: Int,
    onMinRatingChange: (Int) -> Unit
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
        CuisineOption("med", "Mediterranean", "🥙"),
        CuisineOption("vegan", "Vegan", "🥗"),
    )
    // AnimatedContent gives you slide transitions; a pager works too.
    AnimatedContent(
        targetState = currentStep,
        transitionSpec = {
            // simple left-to-right slide; tweak as desired
            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
        },
        label = "PlannerStep"
    ) { step ->
        when (step) {
            PlannerStep.Location -> {
                // Your existing section pieces (split them apart)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Mystery Date Planner", style = MaterialTheme.typography.titleLarge)
                    LocationSheet(
                        cityState = cityState,
                        onFindCityState = onFindCityState,
                        recenterOnUser = recenterOnUser,
                    )
                    Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                        Text("Next: Radius")
                    }
                }
            }

            PlannerStep.Radius -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Pick a radius", style = MaterialTheme.typography.titleLarge)
                    // drop in your radius UI (slider/choices)
                    RadiusSheet(
                        miles = 15,
                        onMilesChange = {}
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f)
                        ) { Text("Back") }
                        Button(
                            onClick = onNext,
                            modifier = Modifier.weight(1f)
                        ) { Text("Next: Cuisine") }
                    }
                }
            }

            PlannerStep.Cuisine -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Choose cuisines", style = MaterialTheme.typography.titleLarge)
                    CuisineSheet(
                        options = tempTestOptions,
                        selectedIds = selectedCuisine,
                        onToggle = onToggleCuisine
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f)
                        ) { Text("Back") }
                        Button(
                            onClick = onNext,
                            modifier = Modifier.weight(1f)
                        ) { Text("Next: Date Details") }
                    }
                }
            }

            PlannerStep.Details -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Choose date details", style = MaterialTheme.typography.titleLarge)
                    DetailsSheet(
                        priceLevel = priceLevel,
                        onPriceLevelChange = onPriceLevelChange,
                        guests = guests,
                        onGuestsChange = onGuestsChange,
                        minRating = minRating,
                        onMinRatingChange = onMinRatingChange,
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f)
                        ) { Text("Back") }
                        Button(
                            onClick = onNext,
                            modifier = Modifier.weight(1f)
                        ) { Text("Find my spot") }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPlannerSheetContent() {
    BlindDateTheme {
        PlannerSheetRedesign(
            cityState = "",
            onFindCityState = {},
            selectedCuisine = setOf("american", "indian"),
            onToggleCuisine = {},
            currentStep = PlannerStep.Radius,
            onNext = {},
            onBack = {},
            recenterOnUser = {},
            priceLevel = 3,
            onPriceLevelChange = {},
            guests = 2,
            onGuestsChange = {},
            minRating = 2,
            onMinRatingChange = {}
        )
    }
}
