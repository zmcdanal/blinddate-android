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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.BlindDateTypography
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Hey Alex & Zac, ready to go on a Blind Date?",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.padding(bottom = 60.dp)
            )

            // User Stats -------------
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UserInfoCard(
                        title = "Places Visited",
                        value = "0",
                        modifier = Modifier
                            .weight(1f)
                    )
                    UserInfoCard(
                        title = "Distance Traveled",
                        value = "0 mi",
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UserInfoCard(
                        title = "Favorite Food",
                        value = "Burgers",
                        modifier = Modifier
                            .weight(1f)
                    )
                    UserInfoCard(
                        title = "Last Date",
                        value = "Feb 9, 2025",
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* TODO: action */ },
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonRose,
                    contentColor = FogWhite,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = FogWhite
                ),
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp)
                    .height(60.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = "Plan Date",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        color = FogWhite,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Next",
                        tint = FogWhite
                    )
                }
            }

        }
    }
}

// Add this composable near your other UI bits
@Composable
private fun UserInfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, NeonRose, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .wrapContentHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
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

