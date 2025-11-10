package com.ethereal.design.reusables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ethereal.design.theme.FogWhite
import com.ethereal.design.theme.NeonRose

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String? = null,
    modifier: Modifier = Modifier,
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.85f)
            .heightIn(min = 60.dp),
        shape = RoundedCornerShape(25.dp),
        isError = isError,
        supportingText = {
            if (isError && !errorText.isNullOrEmpty()) {
                Text(
                    errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
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
        colors = neonOutlinedTextFieldColors()
    )
}

/** Reusable rounded text field with neon-rose outline */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Password",
    isError: Boolean = false,
    errorText: String? = null,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.85f)
            .heightIn(min = 60.dp),
        shape = RoundedCornerShape(25.dp),
        isError = isError,
        supportingText = {
            if (isError && !errorText.isNullOrEmpty()) {
                Text(
                    errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        placeholder = {
            Text(
                text = placeholder,
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
        colors = neonOutlinedTextFieldColors()
    )
}

@Composable
fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Name",
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(0.85f)
            .heightIn(min = 60.dp),
        shape = RoundedCornerShape(25.dp),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = FogWhite.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Name",
                tint = NeonRose
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = FogWhite),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction?.invoke() },
            onNext = { onImeAction?.invoke() }
        ),
        colors = neonOutlinedTextFieldColors()
    )
}

@Composable
fun neonOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = NeonRose,
    unfocusedTextColor = FogWhite,
    disabledTextColor = FogWhite.copy(alpha = 0.5f),
    cursorColor = NeonRose,
    focusedBorderColor = NeonRose,
    unfocusedBorderColor = NeonRose.copy(alpha = 0.5f),
    disabledBorderColor = NeonRose.copy(alpha = 0.25f),
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    focusedLabelColor = NeonRose,
    unfocusedLabelColor = NeonRose,
    focusedLeadingIconColor = NeonRose,
    unfocusedLeadingIconColor = NeonRose.copy(alpha = 0.9f),
    focusedTrailingIconColor = NeonRose,
    unfocusedTrailingIconColor = NeonRose.copy(alpha = 0.9f),
)
