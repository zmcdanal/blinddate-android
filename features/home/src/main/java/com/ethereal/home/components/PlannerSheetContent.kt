package com.ethereal.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.model.data.CuisineOption
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerSheetContent(
    cityState: String,
    onFindCityState: (String) -> Unit,
    selectedCuisine: Set<String>,
    onToggleCuisine: (String) -> Unit,
    recenterOnUser: () -> Unit,
    onStart: () -> Unit
) {
    val listState = rememberLazyListState()

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

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        stickyHeader {
            Text(
                "Mystery Date Planner",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            )
        }

        item { LocationSection(
            cityState = cityState,
            onFindCityState = onFindCityState,
            miles = 5,
            onMilesChange = {},
            recenterOnUser = recenterOnUser,
        ) }

        item(key = "cuisine") {
            CuisineSection(
                options = tempTestOptions,
                selectedIds = selectedCuisine,
                onToggle = onToggleCuisine
            )
        }

         item { PriceSection() }

        item(key = "cta") {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Find my spot") }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewPlannerSheetContent() {
    BlindDateTheme {
        PlannerSheetContent(
            cityState = "",
            onFindCityState = {},
            selectedCuisine = setOf("american", "indian"),
            onToggleCuisine = {},
            onStart = {},
            recenterOnUser = {}
        )
    }
}
