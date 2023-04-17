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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Creates a new row with `dummy` object data to add it into a `LazyColumn` or `LazyRow`.
 *
 * @param dummy An instance of `Dummy` class.
 * @param modifier An object used as the start of a modifier extension factory expression.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRow(
    dummy: DummyModel,
    modifier: Modifier = Modifier
) {

    // This composable is based on a Material Design Card.
    Card(modifier = modifier.padding(8.dp)) {
        Column {

            // An image to show the `dummy` content.
            Image(
                painterResource(R.drawable.media),
                stringResource(R.string.content_description_media),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp)
                    .alpha(0.5f)
            )

            // Show the text information about the current `dummy` object.
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.title, dummy.artistName),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.secondary_text, dummy.year, dummy.country),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = stringResource(R.string.supporting_text, dummy.text),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Adding three buttons in the card.
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* Do something */ }) {
                    Icon(
                        Icons.Outlined.ThumbUp,
                        contentDescription = stringResource(R.string.content_description_thumb_up)
                    )
                }
                IconButton(onClick = { /* Do something */ }) {
                    Icon(
                        Icons.Outlined.Favorite,
                        contentDescription = stringResource(R.string.content_description_favorite)
                    )
                }
                IconButton(onClick = { /* Do something */ }) {
                    Icon(
                        Icons.Outlined.Share,
                        contentDescription = stringResource(R.string.content_description_share)
                    )
                }
            }
        }
    }
}
