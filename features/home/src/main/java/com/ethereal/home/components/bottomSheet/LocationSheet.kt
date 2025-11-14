package com.ethereal.home.components.bottomSheet

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ethereal.design.theme.NeonRose
import com.ethereal.model.data.DateDetails
import com.ethereal.model.data.MapData

@Composable
fun LocationSheet(
    dateDetails: DateDetails,
    cityState: String,
    onFindCityState: (String) -> Unit,
    recenterOnUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isReady = dateDetails.mapData.userLocation != null
    val showFindCheck = isReady && dateDetails.mapData.cityState.isNotBlank()
    val showMyLocationCheck = isReady && dateDetails.mapData.cityState.isBlank()

    var cityStateHolder by remember { mutableStateOf(cityState) }
    val cityStatePattern = remember { Regex(".+,\\s*[A-Za-z]{2}") } // "City, ST"
    val cityStateIsError =
        cityStateHolder.isNotBlank() && !cityStatePattern.matches(cityStateHolder)
    val canFind = !cityStateIsError && cityStateHolder.isNotBlank()

    DatePlanningSectionCard(
        title = stringResource(R.string.location),
        subtitle = stringResource(R.string.start_from_your_current_spot_or_pick_a_city_and_state),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Primary action: use current location
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

            // Tiny helper text under primary action
            Text(
                text = stringResource(R.string.we_ll_only_use_this_to_find_restaurants_nearby),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Divider "or"
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    stringResource(R.string.or),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            // City, State input
            OutlinedTextField(
                value = cityStateHolder,
                onValueChange = { cityStateHolder = it },
                label = { Text("City, State") },
                singleLine = true,
                isError = cityStateIsError,
                supportingText = {
                    if (cityStateIsError) {
                        Text("Format: City, ST")
                    } else if (cityStateHolder.isNotBlank()) {
                        Text("Press Find to center the map on this city.")
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

            // Find button
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
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLocationSheet() {
    BlindDateTheme {
        LocationSheet(
            dateDetails = DateDetails(mapData = MapData(userLocation = null, radiusMiles = 5)),
            cityState = "Birmingham, AL",
            onFindCityState = {},
            recenterOnUser = {}
        )
    }
}
