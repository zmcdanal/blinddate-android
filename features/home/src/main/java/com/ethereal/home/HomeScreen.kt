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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.BlindDateTypography
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose

import com.ethereal.home.components.PlanDateVelvetRing


import com.ethereal.ui.BlindDateBackground
import com.ethereal.ui.BlindDateBottomBar

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeScreenUiState by viewModel.homeScreenUiState.collectAsStateWithLifecycle()

    HomeScreen(
        homeScreenUiState = homeScreenUiState
    )
}

@Composable
fun HomeScreen(
    homeScreenUiState: HomeScreenUiState,
) {
    when (homeScreenUiState) {
        is HomeScreenUiState.Error -> {
            // TODO
        }

        is HomeScreenUiState.Loading -> {
            // TODO
        }

        is HomeScreenUiState.Ready -> {
            HomeScreenContent(
                homeScreenUiState = homeScreenUiState
            )
        }
    }
}

@Composable
fun HomeScreenContent(homeScreenUiState: HomeScreenUiState) {
    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

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

