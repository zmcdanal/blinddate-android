package com.ethereal.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
 fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
println("hey bobby here")
 Text("Hello World")
}