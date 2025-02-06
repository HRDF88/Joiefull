package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nedrysystems.joiefull.ui.theme.joiefullBackground

@Composable
fun SaveButton(saveButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = saveButtonClick,
        modifier = modifier.then(Modifier.padding(16.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = joiefullBackground)
    ) {
        Text(text = "Enregistrer",color = Color.White)

    }
}