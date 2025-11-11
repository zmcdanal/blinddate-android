package com.ethereal.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ethereal.common.Constants
import com.ethereal.common.NTuple3
import com.ethereal.common.Result
import com.ethereal.common.asResult
import com.ethereal.data.repository.CityGeocodingRepository
import com.ethereal.data.repository.DateDetailsRepository
import com.ethereal.data.repository.UserDataRepository
import com.ethereal.home.components.bottomSheet.slide_navigation.PlannerFlow
import com.ethereal.home.components.bottomSheet.slide_navigation.PlannerStep
import com.ethereal.home.components.bottomSheet.slide_navigation.index
import com.ethereal.home.components.bottomSheet.slide_navigation.nextOrSelf
import com.ethereal.home.components.bottomSheet.slide_navigation.prevOrSelf
import com.ethereal.home.location.LocationClient
import com.ethereal.model.data.DateDetails
import com.ethereal.model.data.GeoPoint
import com.ethereal.model.data.MapData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val cityGeocodingRepository: CityGeocodingRepository,
    private val dateDetailsRepository: DateDetailsRepository,
    private val locationClient: LocationClient,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _homeScreenUiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    private val radiusInput = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 32,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    var plannerNav by mutableStateOf(PlannerNavState())
        private set


    init {
        viewModelScope.launch {
            homeScreenUiState(
                userDataRepository = userDataRepository,
                dateDetailsRepository = dateDetailsRepository,
                locationClient = locationClient
            ).collect { state -> _homeScreenUiState.value = state }
        }

        radiusInput
            .debounce(Constants.RADIUS_DEBOUNCE_MS)
            .map { it.coerceIn(Constants.MIN_RADIUS_MILES, Constants.MAX_RADIUS_MILES) }
            .distinctUntilChanged()
            .onEach { clamped ->
                val current = (homeScreenUiState.value as? HomeScreenUiState.Ready)
                    ?.dateDetails?.mapData?.radiusMiles
                if (current != clamped) {
                    updateDetails { dateDetails ->
                        dateDetails.copy(
                            mapData = dateDetails.mapData.copy(
                                radiusMiles = clamped
                            )
                        )
                    }
                }
            }
            .launchIn(viewModelScope)

    }

    fun startDate(dateDetails: DateDetails) = try {
        viewModelScope.launch {
            async {
                dateDetailsRepository.save(dateDetails)
            }.await()
        }
    } catch (exception: Exception) {
        Log.d(TAG, "Unable to start date: ", exception)
    }

    private inline fun updateDetails(dateDetailsCopy: (DateDetails) -> DateDetails) {
        _homeScreenUiState.update { state ->
            if (state is HomeScreenUiState.Ready) state.copy(dateDetails = dateDetailsCopy(state.dateDetails))
            else state
        }
    }

    fun setRadius(value: Int) {
        radiusInput.tryEmit(value)
    }

    fun setPriceLevel(value: Int) = updateDetails {
        it.copy(priceLevel = value.coerceIn(1, 4))
    }

    fun setGuests(value: Int) = updateDetails {
        it.copy(guests = value.coerceAtLeast(1))
    }

    fun setMinRating(value: Int) = updateDetails {
        it.copy(minRating = value.coerceIn(1, 5))
    }

    fun toggleLoading() {
        _homeScreenUiState.update { state ->
            if (state is HomeScreenUiState.Ready) {
                val old = state.dateDetails
                val oldMap = old.mapData
                state.copy(
                    dateDetails = old.copy(
                        mapData = oldMap.copy(
                            mapLoading = !oldMap.mapLoading
                        )
                    )
                )
            } else state
        }
    }


    fun getCityGeoPoint(cityState: String) = viewModelScope.launch {
        try {
            toggleLoading()
            val geoPoint = cityGeocodingRepository.cityToLatLng(cityState)
            if (geoPoint != null) {
                _homeScreenUiState.update { state ->
                    if (state is HomeScreenUiState.Ready) {
                        val old = state.dateDetails
                        val oldMap = old.mapData
                        state.copy(
                            dateDetails = old.copy(
                                mapData = oldMap.copy(
                                    mapLoading = false,
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
        try {
            toggleLoading()
            val geo = locationClient.currentOnce(5_000L) ?: locationClient.lastKnownOnce()
            if (geo != null) {
                _homeScreenUiState.update { state ->
                    if (state is HomeScreenUiState.Ready) {
                        val old = state.dateDetails
                        val oldMap = old.mapData
                        state.copy(
                            dateDetails = old.copy(
                                mapData = oldMap.copy(
                                    mapLoading = false,
                                    userLocation = geo,
                                    cityState = ""
                                )
                            )
                        )
                    } else state
                }
            }
        } catch (exception: Exception) {
            Log.d(TAG, "Unable to center on user: ", exception)
        }
    }

    fun canBack(): Boolean = plannerNav.step != PlannerFlow.first()
    fun canNext(): Boolean = validate(plannerNav.step)

    fun dispatch(intent: PlannerIntent) {
        when (intent) {
            PlannerIntent.Back -> {
                val prev = plannerNav.step.prevOrSelf()
                plannerNav = plannerNav.copy(step = prev, direction = NavDir.Backward)
            }

            PlannerIntent.Next -> {
                if (!validate(plannerNav.step)) return
                val next = plannerNav.step.nextOrSelf()
                plannerNav = plannerNav.copy(step = next, direction = NavDir.Forward)
            }

            is PlannerIntent.Goto -> {
                val from = plannerNav.step.index()
                val to = intent.step.index()
                val dir = when {
                    to > from -> NavDir.Forward
                    to < from -> NavDir.Backward
                    else -> NavDir.None
                }
                // require validation to move forward to later steps
                if (to > from && !validate(plannerNav.step)) return
                plannerNav = plannerNav.copy(step = intent.step, direction = dir)
            }
        }
    }

    // ---- Validation per step (read from your Ready(uiState).dateDetails)
    private fun validate(step: PlannerStep): Boolean {
        val details = (homeScreenUiState.value as? HomeScreenUiState.Ready)?.dateDetails
            ?: return false

        return when (step) {
            PlannerStep.Location -> details.mapData.userLocation != null
            PlannerStep.Radius -> details.mapData.radiusMiles > 0.0
            PlannerStep.Details -> details.priceLevel in 1..4 &&
                    details.guests >= 1 &&
                    details.minRating in 1..5

            PlannerStep.Cuisine -> true
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun homeScreenUiState(
        userDataRepository: UserDataRepository,
        dateDetailsRepository: DateDetailsRepository,
        locationClient: LocationClient,
    ): Flow<HomeScreenUiState> {

        val userStream = userDataRepository.userData
        val currentDateDetails: Flow<DateDetails?> = flow {
            val details = dateDetailsRepository.getOrNull()
            emit(details)
        }
        val locationStreamNonNull: Flow<GeoPoint> = flow {
            val fresh = locationClient.currentOnce(timeoutMs = 2_500L)
            val loc = fresh ?: locationClient.lastKnownOnce()
            if (loc != null) emit(loc)
        }.distinctUntilChanged()

        return combine(
            userStream,
            currentDateDetails,
            locationStreamNonNull,
            ::NTuple3
        ).asResult()
            .map { homeResult ->
                when (homeResult) {
                    is Result.Error -> HomeScreenUiState.Error("Something went wrong")
                    is Result.Loading -> HomeScreenUiState.Loading
                    is Result.Success -> {
                        val (userData, currentDateDetails, userLocation) = homeResult.data
                        val mapData = MapData(
                            mapLoading = false,
                            userLocation = userLocation,
                            radiusMiles = userData.defaultRadius
                        )
                        val dateDetails = currentDateDetails ?: DateDetails(
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

enum class NavDir { None, Forward, Backward }

data class PlannerNavState(
    val step: PlannerStep = PlannerStep.Location,
    val direction: NavDir = NavDir.None
)

sealed interface PlannerIntent {
    data object Next : PlannerIntent
    data object Back : PlannerIntent
    data class Goto(val step: PlannerStep) : PlannerIntent
}