package com.ethereal.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.room.util.copy
import com.ethereal.design.theme.BlindDateTheme
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose
import com.ethereal.ui.BlindDateBackground

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoggedIn: () -> Unit,
    onSignUp: () -> Unit,
) {
    LoginScreen(
       onLoggedIn = onLoggedIn,
        onSignUp = onSignUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    onLoggedIn: () -> Unit,
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

        UsernameField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PasswordField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Login Button
        Button(
            onClick = {
                onLoggedIn()
            },
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

/** Reusable rounded text field with neon-rose outline */
@Composable
fun UsernameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(60.dp),
        shape = RoundedCornerShape(25.dp),
        placeholder = {
            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyLarge,
                color = FogWhite.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Username",
                tint = NeonRose
            )
        },
        trailingIcon = null,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = FogWhite),
        visualTransformation = VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = FogWhite,
            unfocusedTextColor = FogWhite,
            disabledTextColor = FogWhite.copy(alpha = 0.5f),
            cursorColor = NeonRose,
            focusedBorderColor = NeonRose,
            unfocusedBorderColor = NeonRose.copy(alpha = 0.5f),
            disabledBorderColor = NeonRose.copy(alpha = 0.25f),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedLeadingIconColor = NeonRose,
            unfocusedLeadingIconColor = NeonRose.copy(alpha = 0.9f)
        )
    )
}

/** Reusable rounded text field with neon-rose outline */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(60.dp),
        shape = RoundedCornerShape(25.dp),
        placeholder = {
            Text(
                text = "Password",
                style = MaterialTheme.typography.bodyLarge,
                color = FogWhite.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Password",
                tint = NeonRose
            )
        },
        trailingIcon = {
            val icon =
                if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility
            val desc = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = icon,
                    contentDescription = desc,
                    tint = FogWhite.copy(alpha = 0.9f)
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = FogWhite),
        visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = FogWhite,
            unfocusedTextColor = FogWhite,
            disabledTextColor = FogWhite.copy(alpha = 0.5f),
            cursorColor = NeonRose,
            focusedBorderColor = NeonRose,
            unfocusedBorderColor = NeonRose.copy(alpha = 0.5f),
            disabledBorderColor = NeonRose.copy(alpha = 0.25f),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedLeadingIconColor = NeonRose,
            unfocusedLeadingIconColor = NeonRose.copy(alpha = 0.9f)
        )
    )
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