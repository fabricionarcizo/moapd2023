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
package dk.itu.moapd.lazycomposables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import dk.itu.moapd.lazycomposables.ui.theme.LazyComposablesTheme


/**
 * An activity class with methods to manage the main activity of Lazy Composables application.
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a dummy dataset with 100 elements.
        val data = mutableListOf<DummyModel>()
        for (i in 1..100)
            data.add(
                DummyModel("Artist Name $i", 1982, "Brazil",
                    getString(R.string.lorem_ipsum_long))
            )

        // Composes the given composable into the given activity.  The content will become the root
        // view of the given activity.
        setContent {

            // Material Theming refers to the customization of this app.
            LazyComposablesTheme {

                // LazyColumns only compose and lay out items which are visible in the component's
                // viewport.
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paint(
                            painter = painterResource(id = R.drawable.background),
                            contentScale = ContentScale.FillWidth
                        )
                ){
                    // Add each item available in the dummies list.
                    items(data) { dummy ->
                        ListRow(dummy)
                    }
                }
            }
        }
    }
}
