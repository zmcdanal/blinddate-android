package com.ethereal.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ethereal.ui.BlindDateBackground

@Composable
 fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
  Column(
   modifier = Modifier
    .fillMaxSize()
    .padding(horizontal = 24.dp, vertical = 48.dp),
   horizontalAlignment = Alignment.CenterHorizontally,
   verticalArrangement = Arrangement.Bottom
  ) {
   Text(
    text = "Hey Alex & Zac, ready to go?",
    style = MaterialTheme.typography.headlineMedium,
    color = Color.White,
    modifier = Modifier.padding(bottom = 60.dp)
   )

   //PlanDateButton(onClick = onPlanDateClick)
  }

}
