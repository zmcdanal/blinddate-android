package com.ethereal.home.components.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ethereal.design.reusables.TrailingCheckmark
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.NeonRose
import com.ethereal.design.reusables.neonOutlinedTextFieldColors
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Location & Radius",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        Button(
            onClick = recenterOnUser,
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonRose,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
            modifier = Modifier.width(240.dp)
        ) {
            Text("Use my location", style = MaterialTheme.typography.labelLarge)
            TrailingCheckmark(showMyLocationCheck)
        }

        // Divider "or"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                "or",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        OutlinedTextField(
            value = cityStateHolder,
            onValueChange = { cityStateHolder = it },
            label = { Text("City, State") },
            singleLine = true,
            isError = cityStateIsError,
            supportingText = { if (cityStateIsError) Text("Format: City, ST") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (canFind) onFindCityState(cityStateHolder) }
            ),
            colors = neonOutlinedTextFieldColors(),
            modifier = Modifier.width(240.dp)
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onFindCityState(cityStateHolder) },
            enabled = canFind,
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (canFind) NeonRose else NeonRose.copy(alpha = 0.4f),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
            modifier = Modifier.width(240.dp)
        ) {
            Text("Find", style = MaterialTheme.typography.labelLarge)
            TrailingCheckmark(showFindCheck)
        }

        Spacer(Modifier.height(16.dp))

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