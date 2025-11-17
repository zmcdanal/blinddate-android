package com.ethereal.home.ui.bottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.blinddate.features.home.R
import com.ethereal.design.reusables.DatePlanningSectionCard
import com.ethereal.design.reusables.TrailingCheckmark
import com.ethereal.design.reusables.neonOutlinedTextFieldColors
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.DateDetails
import com.ethereal.model.data.MapData

private enum class LocationMode {
    CURRENT,
    CITY,
    MAP
}

@Composable
fun LocationSheet(
    dateDetails: DateDetails,
    cityState: String,
    onFindCityState: (String) -> Unit,
    recenterOnUser: () -> Unit,
    onPickLocationOnMap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isReady = dateDetails.mapData.userLocation != null
    val showFindCheck = isReady && dateDetails.mapData.cityState.isNotBlank()
    val showMyLocationCheck = isReady && dateDetails.mapData.cityState.isBlank()

    var cityStateHolder by remember { mutableStateOf(cityState) }
    val cityStatePattern = remember { Regex(".+,\\s*[A-Za-z]{2}") }
    val cityStateIsError =
        cityStateHolder.isNotBlank() && !cityStatePattern.matches(cityStateHolder)
    val canFind = !cityStateIsError && cityStateHolder.isNotBlank()

    // Initial mode based on what we already know
    var mode by remember {
        mutableStateOf(
            when {
                dateDetails.mapData.userLocation != null && dateDetails.mapData.cityState.isBlank() ->
                    LocationMode.CURRENT
                dateDetails.mapData.cityState.isNotBlank() ->
                    LocationMode.CITY
                else -> LocationMode.CURRENT
            }
        )
    }

    val locationString = if (dateDetails.mapData.cityState.isNotEmpty()) {
        "Location: ${dateDetails.mapData.cityState}"
    } else {
        stringResource(R.string.location)
    }

    DatePlanningSectionCard(
        title = locationString,
        subtitle = stringResource(R.string.start_from_your_current_spot_or_pick_a_city_and_state),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Mode selector – small, pill-y chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 360.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                LocationModeChip(
                    text = "Current location",
                    selected = mode == LocationMode.CURRENT,
                    onClick = { mode = LocationMode.CURRENT }
                )
                LocationModeChip(
                    text = "City & state",
                    selected = mode == LocationMode.CITY,
                    onClick = { mode = LocationMode.CITY }
                )
                LocationModeChip(
                    text = "Drop a pin",
                    selected = mode == LocationMode.MAP,
                    onClick = { mode = LocationMode.MAP }
                )
            }

            // Divider just for a subtle separation
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            when (mode) {
                LocationMode.CURRENT -> {
                    // === CURRENT LOCATION ===
                    Button(
                        onClick = recenterOnUser,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonRose,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 360.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.use_my_location),
                            style = MaterialTheme.typography.labelLarge
                        )
                        TrailingCheckmark(showMyLocationCheck)
                    }

                    Text(
                        text = stringResource(R.string.we_ll_only_use_this_to_find_restaurants_nearby),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LocationMode.CITY -> {
                    // === CITY + STATE ENTRY ===
                    OutlinedTextField(
                        value = cityStateHolder,
                        onValueChange = { cityStateHolder = it },
                        label = { Text("City, State") },
                        singleLine = true,
                        isError = cityStateIsError,
                        supportingText = {
                            when {
                                cityStateIsError -> Text("Format: City, ST")
                                cityStateHolder.isNotBlank() ->
                                    Text("Tap Find to center the map on this city.")
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { if (canFind) onFindCityState(cityStateHolder) }
                        ),
                        colors = neonOutlinedTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 360.dp)
                    )

                    Spacer(Modifier.height(4.dp))

                    Button(
                        onClick = { onFindCityState(cityStateHolder) },
                        enabled = canFind,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (canFind) NeonRose else NeonRose.copy(alpha = 0.4f),
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 360.dp)
                    ) {
                        Text("Find", style = MaterialTheme.typography.labelLarge)
                        TrailingCheckmark(showFindCheck)
                    }
                }

                LocationMode.MAP -> {
                    // === DROP A PIN / PICK ON MAP ===
                    OutlinedButton(
                        onClick = onPickLocationOnMap,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = NeonRose,
                            contentColor = FogWhite
                        ),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 360.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Pick a spot on the map",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Text(
                        text = "We’ll drop your mystery radius around the pin you place.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationModeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    SuggestionChip(
        onClick = onClick,
        label = { Text(text) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (selected)
                NeonRose.copy(alpha = 0.18f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            labelColor = if (selected)
                NeonRose
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) NeonRose else Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewLocationSheet() {
    BlindDateTheme {
        LocationSheet(
            dateDetails = DateDetails(mapData = MapData(userLocation = null, radiusMiles = 5)),
            cityState = "Birmingham, AL",
            onFindCityState = {},
            recenterOnUser = {},
            onPickLocationOnMap = {}
        )
    }
}
