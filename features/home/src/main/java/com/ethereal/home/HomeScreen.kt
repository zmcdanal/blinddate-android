package com.ethereal.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.BlindDateTypography

import com.ethereal.home.components.PlanDateVelvetRing


import com.ethereal.ui.BlindDateBackground
import com.ethereal.ui.BlindDateBottomBar

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    // TODO- uistate init
    HomeScreen()
}

@Composable
fun HomeScreen() {
    //TODO - ViewModel states
    HomeScreenContent()
}

@Composable
fun HomeScreenContent() {
    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        /**
         *
         *
         *
         *
         * This is simply a placeholder. Design will differ
         *
         *
         *
         *
         * */
        val ring = Brush.linearGradient(listOf(Color(0xFFFF4D8D), Color(0xFF4DEEFF)))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Hey Alex & Zac, ready to go?",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 60.dp)
            )
            // User Stats -------------
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, ring, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(.9f)
                ) {
                    // Static info for now
                    UserInfoStrip(
                        title = "Places Visited: ",
                        value = "0",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    UserInfoStrip(
                        title = "Distance Traveled: ",
                        value = "0m",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    UserInfoStrip(
                        title = "Favorite Food: ",
                        value = "Burgers",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    UserInfoStrip(
                        title = "Last Date: ",
                        value = "Feb. 9, 2025"
                    )
                }

            }

            Spacer(modifier = Modifier.height(20.dp))



            PlanDateVelvetRing(onClick = {})
        }
    }
}

@Composable
internal fun UserInfoStrip(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = title,
            style = BlindDateTypography.titleMedium,
        )

        Text(
            text = value,
            style = BlindDateTypography.titleMedium,
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

