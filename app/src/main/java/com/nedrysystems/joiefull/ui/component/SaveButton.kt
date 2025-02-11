package com.nedrysystems.joiefull.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.ui.theme.joiefullBackground

@Composable
fun SaveButton(saveButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    val saveTextContentDescription = stringResource(R.string.save_content_description)
    Button(
        onClick = saveButtonClick,
        modifier = modifier.then(Modifier.padding(16.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = joiefullBackground)
    ) {
        Text(
            text = stringResource(R.string.save),
            color = Color.Black,
            modifier = Modifier.semantics { contentDescription = saveTextContentDescription })

    }
}