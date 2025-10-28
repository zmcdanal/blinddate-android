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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeScreenUiState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun homeScreenUiState(
        userDataRepository: UserDataRepository,
        locationClient: LocationClient,
    ): Flow<HomeScreenUiState> {

        val userStream = userDataRepository.userData

        val locationStreamNonNull: Flow<GeoPoint> =
            locationClient.currentOnce(timeoutMs = 2_500L)
                .flatMapLatest { current ->
                    if (current != null) flowOf(current) else locationClient.lastKnownOnce()
                }
                .filterNotNull()
                .distinctUntilChanged()

        return combine(userStream, locationStreamNonNull) { userData, userLocation ->
            val mapData = MapData(
                userLocation = userLocation,
                center = userLocation,
                radiusMiles = userData.defaultRadius,
                isEditingRadius = false,
                isMapReady = true
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
    }


}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState
    data class Ready(
        val dateDetails: DateDetails,
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}