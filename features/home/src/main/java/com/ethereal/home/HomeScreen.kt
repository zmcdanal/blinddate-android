package com.ethereal.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.home.components.PlannerSheetContent
import com.ethereal.model.data.DateDetails


import com.ethereal.ui.BlindDateBackground

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeScreenUiState by viewModel.homeScreenUiState.collectAsStateWithLifecycle()

    HomeScreen(
        homeScreenUiState = homeScreenUiState,
        recenterOnUser = viewModel::centerRadiusOnUser,
        onFindCityState = viewModel::getCityGeoPoint
    )
}

@Composable
fun HomeScreen(
    homeScreenUiState: HomeScreenUiState,
    onFindCityState: (String) -> Unit,
    recenterOnUser: () -> Unit
) {
    when (homeScreenUiState) {
        is HomeScreenUiState.Error -> {
            // TODO
        }

        is HomeScreenUiState.Loading -> {
            // TODO
        }

        is HomeScreenUiState.Ready -> {
            HomeScreenContent(
                dateDetails = homeScreenUiState.dateDetails,
                cityState = homeScreenUiState.dateDetails.mapData.cityState,
                onFindCityState = onFindCityState,
                recenterOnUser = recenterOnUser
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    dateDetails: DateDetails,
    cityState: String,
    onFindCityState: (String) -> Unit,
    recenterOnUser: () -> Unit
) {
    val windowInfo = LocalWindowInfo.current            // <- accurate window size (px)
    val density = LocalDensity.current

    // Sheet should cover ~60% by default
    val sheetCoversFraction = 0.60f
    val containerHeightPx = windowInfo.containerSize.height
    val sheetPeekHeight = with(density) {
        (containerHeightPx * (1f - sheetCoversFraction)).toDp()
    }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetDragHandle = { BottomSheetDefaults.DragHandle() },
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            PlannerSheetContent(
                cityState = cityState,
                onFindCityState = onFindCityState,
                selectedCuisine = setOf("mexican", "bbq"),
                onToggleCuisine = {},
                onStart = {},
                recenterOnUser = recenterOnUser
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        HomeMap(
            dateDetails = dateDetails,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            interactive = true
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun PreviewHomeScreen() {
    BlindDateTheme {
        BlindDateBackground {
            HomeRoute()
        }
    }
}

