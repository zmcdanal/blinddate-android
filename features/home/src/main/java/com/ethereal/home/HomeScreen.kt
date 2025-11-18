package com.ethereal.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.home.ui.map.HomeMap
import com.ethereal.home.ui.bottomSheet.PlannerSheet
import com.ethereal.home.ui.bottomSheet.slide_navigation.PlannerStep
import com.ethereal.model.data.DateDetails


import com.ethereal.ui.BlindDateBackground
import com.google.android.gms.maps.model.LatLng
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
        onFastFoodAllowedChange = viewModel::setFastFoodAllowed,
        onToggleCuisine = viewModel::toggleCuisine,
        setCenterViaMapTap = viewModel::setCenterViaMapTap,
        onPickLocationOnMap = viewModel::setAllowUserToChooseCenter,
        toggleSheetState = viewModel::openCloseScaffold
    )
}

@Composable
fun HomeScreen(
    homeScreenUiState: HomeScreenUiState,
    nav: PlannerNavState,
    onFindCityState: (String) -> Unit,
    setCenterViaMapTap: (LatLng) -> Unit,
    onPickLocationOnMap: () -> Unit,
    onSetRadius: (Int) -> Unit,
    recenterOnUser: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    setPriceLevel: (Int) -> Unit,
    setGuests: (Int) -> Unit,
    setMinRating: (Int) -> Unit,
    onToggleCuisine: (String) -> Unit,
    toggleSheetState: (close: Boolean) -> Unit,
    onFastFoodAllowedChange: (Boolean) -> Unit,
) {
    when (homeScreenUiState) {
        is HomeScreenUiState.Ready -> {
            HomeScreenContent(
                dateDetails = homeScreenUiState.dateDetails,
                isMapLoading = homeScreenUiState.mapLoading,
                isBottomSheetGone = homeScreenUiState.isBottomSheetGone,
                currentStep = nav.step,
                onFindCityState = onFindCityState,
                onPickLocationOnMap = onPickLocationOnMap,
                onSetRadius = onSetRadius,
                recenterOnUser = recenterOnUser,
                onNext = onNext,
                onBack = onBack,
                setPriceLevel = setPriceLevel,
                setGuests = setGuests,
                setMinRating = setMinRating,
                onToggleCuisine = onToggleCuisine,
                onFastFoodAllowedChange = onFastFoodAllowedChange,
                isBottomSheetVisible = homeScreenUiState.isBottomSheetVisible,
                allowTapToChooseCenter = homeScreenUiState.allowTapToChooseCenter,
                onCenterChanged = setCenterViaMapTap,
                toggleSheetState = toggleSheetState,
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
    isMapLoading: Boolean,
    isBottomSheetGone: Boolean,
    currentStep: PlannerStep,
    onFindCityState: (String) -> Unit,
    onPickLocationOnMap: () -> Unit,
    onSetRadius: (Int) -> Unit,
    recenterOnUser: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    setPriceLevel: (Int) -> Unit,
    setGuests: (Int) -> Unit,
    setMinRating: (Int) -> Unit,
    onToggleCuisine: (String) -> Unit,
    onFastFoodAllowedChange: (Boolean) -> Unit,
    toggleSheetState: (close: Boolean) -> Unit,
    isBottomSheetVisible: Boolean,
    allowTapToChooseCenter: Boolean,
    onCenterChanged: (LatLng) -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // --- fixed half-screen height for the sheet ---
    val sheetHeightFraction = 0.5f
    val containerHeightPx = windowInfo.containerSize.height
    val sheetHeight = with(density) {
        (containerHeightPx * sheetHeightFraction).toDp()
    }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            sheetState.partialExpand()
        } else {
            sheetState.hide()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = sheetHeight,
            sheetSwipeEnabled = false,
            sheetDragHandle = { BottomSheetDefaults.DragHandle() },
            sheetContainerColor = MaterialTheme.colorScheme.surface,
            sheetContent = {
                PlannerSheet(
                    sheetHeight = sheetHeight,
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
                    onFastFoodAllowedChange = onFastFoodAllowedChange,
                    onCollapseClick = {
                        scope.launch { sheetState.hide() }
                    },
                    onPickLocationOnMap = onPickLocationOnMap
                )
            },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) {
            HomeMap(
                dateDetails = dateDetails,
                isMapLoading = isMapLoading,
                modifier = Modifier.fillMaxSize(),
                onCenterChanged = onCenterChanged,
                allowTapToChooseCenter = allowTapToChooseCenter
            )
        }

        // Arrow to collapse when sheet is partially expanded
        if (sheetState.currentValue == SheetValue.PartiallyExpanded && !isMapLoading && !isBottomSheetGone) {
            FilledTonalIconButton(
                onClick = { toggleSheetState(true) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    // push it up to just above the top edge of the sheet
                    .padding(bottom = sheetHeight + 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Hide date planner"
                )
            }
        }

        // Arrow to reopen when sheet is hidden
        if (sheetState.currentValue == SheetValue.Hidden && !isMapLoading && !isBottomSheetGone) {
            FilledTonalIconButton(
                onClick = { toggleSheetState(false) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExpandLess,
                    contentDescription = "Show date planner"
                )
            }
        }
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



