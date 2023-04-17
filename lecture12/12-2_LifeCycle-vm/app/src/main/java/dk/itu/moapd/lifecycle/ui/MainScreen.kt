/*
 * MIT License
 *
 * Copyright (c) 2023 Fabricio Batista Narcizo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.lifecycle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.itu.moapd.lifecycle.R


/**
 * Creates the main screen of the Life Cycle app.
 *
 * @param modifier An object used as the start of a modifier extension factory expression.
 * @param mainViewModel An instance of `Dummy` class.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {

    /**
     * Get a `observer` to monitor the states of all UI components in the application.
     */
    val mainUiState by mainViewModel.uiState.collectAsState()

    // A column that spans the entire screen.
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {

        // This compose shows the latest UI component used by the user.
        Text(
            text = stringResource(mainUiState.textId),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )

        // Adding two buttons in the screen.
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // True button.
            Button(
                onClick = {
                    mainViewModel.onTextChanged(R.string.true_text)
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(stringResource(R.string.true_button), textAlign = TextAlign.Center)
            }

            // False button.
            Button(
                onClick = {
                    mainViewModel.onTextChanged(R.string.false_text)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(stringResource(R.string.false_button), textAlign = TextAlign.Center)
            }
        }

        // Adding a checkbox with a text in the screen.
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp)
                .toggleable(
                    value = mainUiState.checked,
                    onValueChange = { mainViewModel.toggleChecked() },
                    role = Role.Checkbox
                )
        ) {
            Checkbox(
                checked = mainUiState.checked,
                onCheckedChange = null
            )
            Text(
                text = stringResource(R.string.checkbox_text),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
