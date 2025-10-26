package com.ethereal.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.registerForAllProfilingResults
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.ui.BlindDateBackground
import reusables.PasswordField
import reusables.EmailField

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoggedIn: () -> Unit,
    onSignUp: () -> Unit,
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }
    }


    LoginScreen(
        onLogin = {
            viewModel.loginEmailPassword { result ->
                if (result) {
                    onLoggedIn()
                }
            }
        },
        onSignUp = onSignUp,
        uiState = uiState,
        onEmailTextChange = viewModel::onEmailChange,
        onPasswordTextChange = viewModel::onPasswordChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onEmailTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.blinddate_logo),
            contentDescription = "Blind Date Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .size(350.dp)
        )

        Spacer(Modifier.height(50.dp))

        EmailField(
            value = uiState.email,
            onValueChange = onEmailTextChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PasswordField(
            value = uiState.password,
            onValueChange = onPasswordTextChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Login Button
        Button(
            onClick = onLogin,
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
                    text = "Login",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    color = FogWhite,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Login arrow",
                    tint = FogWhite
                )
            }
        }

        /*
        * Account Creation Click
        * */
        Row {
            Text(
                "Don't have an account? ",
                style = MaterialTheme.typography.bodyLarge,
                color = FogWhite,
                modifier = Modifier.alignByBaseline()
            )
            TextButton(
                onClick = onSignUp,
                colors = ButtonDefaults.textButtonColors(contentColor = NeonRose),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.alignByBaseline()
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = NeonRose
                )
            }

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0B10)
@Composable
private fun PreviewLoginScreen() {
    BlindDateTheme {
        BlindDateBackground {
            LoginRoute(
                onLoggedIn = {},
                onSignUp = {}
            )
        }
    }
}