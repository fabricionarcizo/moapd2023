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
package dk.itu.moapd.gettingstarted

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dk.itu.moapd.gettingstarted.ui.theme.GettingStartedTheme


/**
 * An activity class with methods to manage the main activity of Getting Started application.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting. This is where most initialization should go: calling
     * `setContentView(int)` to inflate the activity's UI, using `findViewById()` to
     * programmatically interact with widgets in the UI, calling
     * `managedQuery(android.net.Uri, String[], String, String[], String)` to retrieve cursors for
     * data being displayed, etc.
     *
     * You can call `finish()` from within this function, in which case `onDestroy()` will be
     * immediately called after `onCreate()` without any of the rest of the activity lifecycle
     * (`onStart()`, `onResume()`, onPause()`, etc) executing.
     *
     * <em>Derived classes must call through to the super class's implementation of this method. If
     * they do not, an exception will be thrown.</em>
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     * down then this Bundle contains the data it most recently supplied in `onSaveInstanceState()`.
     * <b><i>Note: Otherwise it is null.</i></b>
     */
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Composes the given composable into the given activity.  The content will become the root
        // view of the given activity.
        setContent {

            // Material Theming refers to the customization of this app.
            GettingStartedTheme {

                // UI states.
                var name by remember { mutableStateOf("") }
                var text by rememberSaveable { mutableStateOf("") }
                var isTextVisible by rememberSaveable { mutableStateOf(false) }
                val keyboardController = LocalSoftwareKeyboardController.current

                // Create an UI layout that spans the entire screen.
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable{ keyboardController?.hide() }
                ) {

                    // Create a new row to add a `TextField` and a `Button`.
                    Row (verticalAlignment = Alignment.CenterVertically) {

                        // A text field to type the user's name.
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                if (!it.contains("\n"))
                                    name = it
                            },
                            label = { Text(stringResource(R.string.edit_name)) },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 48.dp)
                                .weight(1f)
                        )

                        // A button to send the name to the text component.
                        Button(
                            onClick = {
                                text = name.ifEmpty { "World" }
                                isTextVisible = true
                                keyboardController?.hide()
                            },
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 48.dp)
                        ) {
                            Text(stringResource(R.string.button_send), textAlign = TextAlign.Center)
                        }

                    }

                    // Only shows the text component when the user interacts with the button.
                    if (isTextVisible)

                        // Shows the user's name in a text component.
                        Text(
                            text = stringResource(R.string.text_name, text),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                                .fillMaxWidth()
                        )
                }
            }
        }
    }
}
