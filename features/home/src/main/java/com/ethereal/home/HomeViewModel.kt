package com.ethereal.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethereal.common.NTuple2
import com.ethereal.common.Result
import com.ethereal.common.asResult
import com.ethereal.data.repository.CityGeocodingRepository
import com.ethereal.data.repository.UserDataRepository
import com.ethereal.home.location.LocationClient
import com.ethereal.model.data.DateDetails
import com.ethereal.model.data.GeoPoint
import com.ethereal.model.data.MapData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    val cityGeocodingRepository: CityGeocodingRepository,
    val locationClient: LocationClient,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _homeScreenUiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    init {
        // Build the initial Ready state from user + location streams
        viewModelScope.launch {
            homeScreenUiState(
                userDataRepository = userDataRepository,
                locationClient = locationClient
            ).collect { state -> _homeScreenUiState.value = state }
        }
        getCityGeoPoint("Birmingham, AL")
    }

    fun getCityGeoPoint(cityState: String) = viewModelScope.launch {
        try {
            val geoPoint = cityGeocodingRepository.cityToLatLng(cityState)
            if (geoPoint != null) {
                _homeScreenUiState.update { state ->
                    if (state is HomeScreenUiState.Ready) {
                        val old = state.dateDetails
                        val oldMap = old.mapData
                        state.copy(
                            dateDetails = old.copy(
                                mapData = oldMap.copy(
                                    userLocation = geoPoint,
                                    cityState = cityState
                                )
                            )
                        )
                    } else state
                }
            }
        } catch (exception: Exception) {
            Log.d(TAG, "Unable to find LatLng from City/State: ", exception)
        }
    }

    fun centerRadiusOnUser() = viewModelScope.launch {
        val geo = locationClient.currentOnce(5_000L) ?: locationClient.lastKnownOnce()
        if (geo != null) {
            _homeScreenUiState.update { state ->
                if (state is HomeScreenUiState.Ready) {
                    val old = state.dateDetails
                    val oldMap = old.mapData
                    state.copy(dateDetails = old.copy(mapData = oldMap.copy(userLocation = geo)))
                } else state
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun homeScreenUiState(
        userDataRepository: UserDataRepository,
        locationClient: LocationClient,
    ): Flow<HomeScreenUiState> {

        val userStream = userDataRepository.userData

        val locationStreamNonNull: Flow<GeoPoint> = kotlinx.coroutines.flow.flow {
            val fresh = locationClient.currentOnce(timeoutMs = 2_500L)
            val loc = fresh ?: locationClient.lastKnownOnce()
            if (loc != null) emit(loc)
        }.distinctUntilChanged()

        return combine(
            userStream,
            locationStreamNonNull,
            ::NTuple2
        ).asResult()
            .map { homeResult ->
                when (homeResult) {
                    is Result.Error -> HomeScreenUiState.Error("Something went wrong")
                    is Result.Loading -> HomeScreenUiState.Loading
                    is Result.Success -> {
                        val (userData, userLocation) = homeResult.data
                        val mapData = MapData(
                            userLocation = userLocation,
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
    }
}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState
    data class Ready(
        val dateDetails: DateDetails,
    ) : HomeScreenUiState

    data class Error(val message: String) : HomeScreenUiState
}