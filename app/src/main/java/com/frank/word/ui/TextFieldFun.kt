package com.frank.word.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.word.hideFunction
import com.frank.word.inputText
import com.frank.word.myFontSize

var isImeVisible by mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShowTextFieldFun(modifier: Modifier = Modifier) {
    val focusRequester = remember { FocusRequester() }
    val imeVisible = WindowInsets.Companion.isImeVisible
    LaunchedEffect(WindowInsets.isImeVisible) {
        isImeVisible = imeVisible
    }
    Row(modifier = modifier) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { hideFunction(it) },
            label = { Text("Enter text") },
            textStyle = TextStyle(
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = myFontSize.sp
            ),
            modifier = Modifier
                .background(Color.Black)
                .focusRequester(focusRequester)
                .focusable()
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}
