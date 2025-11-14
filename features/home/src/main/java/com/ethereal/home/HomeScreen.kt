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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.home.components.map.HomeMap
import com.ethereal.home.components.bottomSheet.PlannerSheet
import com.ethereal.home.components.bottomSheet.slide_navigation.PlannerStep
import com.ethereal.model.data.DateDetails


import com.ethereal.ui.BlindDateBackground
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeScreenUiState by viewModel.homeScreenUiState.collectAsStateWithLifecycle()
    val nav = viewModel.plannerNav

    HomeScreen(
        homeScreenUiState = homeScreenUiState,
        nav = nav,
        onFindCityState = viewModel::getCityGeoPoint,
        onSetRadius = viewModel::setRadius,
        recenterOnUser = viewModel::centerRadiusOnUser,
        onNext = { viewModel.dispatch(PlannerIntent.Next) },
        onBack = { viewModel.dispatch(PlannerIntent.Back) },
        setPriceLevel = viewModel::setPriceLevel,
        setGuests = viewModel::setGuests,
        setMinRating = viewModel::setMinRating,
        onToggleCuisine = viewModel::toggleCuisine
    )
}

@Composable
fun HomeScreen(
    homeScreenUiState: HomeScreenUiState,
    nav: PlannerNavState,
    onFindCityState: (String) -> Unit,
    onSetRadius: (Int) -> Unit,
    recenterOnUser: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    setPriceLevel: (Int) -> Unit,
    setGuests: (Int) -> Unit,
    setMinRating: (Int) -> Unit,
    onToggleCuisine: (String) -> Unit
) {
    when (homeScreenUiState) {
        is HomeScreenUiState.Ready -> {
            HomeScreenContent(
                dateDetails = homeScreenUiState.dateDetails,
                currentStep = nav.step,
                onFindCityState = onFindCityState,
                onSetRadius = onSetRadius,
                recenterOnUser = recenterOnUser,
                onNext = onNext,
                onBack = onBack,
                setPriceLevel = setPriceLevel,
                setGuests = setGuests,
                setMinRating = setMinRating,
                onToggleCuisine = onToggleCuisine,
                isBottomSheetVisible = homeScreenUiState.isBottomSheetVisible
            )
        }

        is HomeScreenUiState.Loading -> { /* TODO */
        }

        is HomeScreenUiState.Error -> { /* TODO */
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    dateDetails: DateDetails,
    currentStep: PlannerStep,
    onFindCityState: (String) -> Unit,
    onSetRadius: (Int) -> Unit,
    recenterOnUser: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    setPriceLevel: (Int) -> Unit,
    setGuests: (Int) -> Unit,
    setMinRating: (Int) -> Unit,
    onToggleCuisine: (String) -> Unit,
    isBottomSheetVisible: Boolean
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val sheetHeightFraction = 0.5f
    val containerHeightPx = windowInfo.containerSize.height
    val sheetPeekHeight = with(density) {
        (containerHeightPx * sheetHeightFraction).toDp()
    }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
    val scope = rememberCoroutineScope()

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            sheetState.expand()
        } else {
            sheetState.hide()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle()
        },
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            PlannerSheet(
                sheetHeight = sheetPeekHeight,
                dateDetails = dateDetails,
                onFindCityState = onFindCityState,
                onSetRadius = onSetRadius,
                onToggleCuisine = onToggleCuisine,
                recenterOnUser = recenterOnUser,
                currentStep = currentStep,
                onNext = onNext,
                onBack = onBack,
                onPriceLevelChange = setPriceLevel,
                onGuestsChange = setGuests,
                onMinRatingChange = setMinRating,
                onCollapseClick = {
                    scope.launch {
                        sheetState.hide()
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) {
        HomeMap(
            dateDetails = dateDetails,
            modifier = Modifier.fillMaxSize(),
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

