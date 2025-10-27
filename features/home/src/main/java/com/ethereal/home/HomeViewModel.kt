package com.ethereal.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethereal.data.repository.UserDataRepository
import com.ethereal.home.location.LocationClient
import com.ethereal.model.data.DateDetails
import com.ethereal.model.data.GeoPoint
import com.ethereal.model.data.MapData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    locationClient: LocationClient,
) : ViewModel() {

    val homeScreenUiState = homeScreenUiState(
        userDataRepository = userDataRepository,
        locationClient = locationClient,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeScreenUiState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun homeScreenUiState(
        userDataRepository: UserDataRepository,
        locationClient: LocationClient,
    ): Flow<HomeScreenUiState> {

        val userStream = userDataRepository.userData

        // TODO: If lastKnown is also null -> show snackbar with retry or something else
        // Try current GPS once (with timeout), if null - fall back to lastKnown.
        val locationStream: Flow<GeoPoint?> =
            locationClient.currentOnce(timeoutMs = 2_500L)
                .flatMapLatest { current ->
                    if (current != null) flowOf(current)
                    else locationClient.lastKnownOnce()
                }
                .onStart { emit(null) }

        return combine(
            userStream,
            locationStream
        ) { userData, userLocation ->
            val isMapReady = userLocation != null
            val mapData = MapData(
                userLocation = userLocation,
                center = userLocation,
                radiusMeters = userData.defaultRadius,
                isEditingRadius = false,
                isMapReady = isMapReady
            )


            val dateDetails = DateDetails(
                genre = "",
                keywords = emptyList(),
                priceLevel = 2,
                fastFood = false,
                mapData = mapData
            )

            HomeScreenUiState.Ready(dateDetails)
        }
            .map { it as HomeScreenUiState }
            .onStart { emit(HomeScreenUiState.Loading) }
    }
}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState
    data class Ready(
        val dateDetails: DateDetails,
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}