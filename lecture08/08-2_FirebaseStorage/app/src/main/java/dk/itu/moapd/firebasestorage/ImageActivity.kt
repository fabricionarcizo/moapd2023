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
package dk.itu.moapd.firebasestorage

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dk.itu.moapd.firebasestorage.databinding.ActivityImageBinding


/**
 * An activity class with methods to manage the image activity of Firebase Storage application.
 */
class ImageActivity : AppCompatActivity() {

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private lateinit var binding: ActivityImageBinding

    /**
     * Create a listener to be executed when the Glide finishes downloading the image, and stops the
     * progress bar.
     */
    private val progressListener = object : RequestListener<Drawable> {

        /**
         * Called when an exception occurs during a load, immediately before `onLoadFailed()`. Will
         * only be called if we currently want to display an image for the given model in the given
         * target.  It is recommended to create a single instance per activity/fragment rather than
         * instantiate a new object for each call to `Glide.with(fragment/activity).load()` to avoid
         * object churn.
         *
         * Although you can't start an entirely new load, it is safe to change what is displayed in
         * the `Target` at this point, as long as you return `true` from the method to prevent
         * `onLoadFailed(Drawable)` from being called.
         *
         * @param e The maybe `null` exception containing information about why the request failed.
         * @param model The model we were trying to load when the exception occurred.
         * @param target The `Target` we were trying to load the image into.
         * @param isFirstResource `true` if this exception is for the first resource to load.
         *
         * @return `true` to prevent `onLoadFailed(Drawable)` from being called on `target`,
         *      typically because the listener wants to update the `target` or the object the
         *      `target` wraps itself or `false` to allow `onLoadFailed(Drawable)` to be called on
         *      `target`.
         */
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                  isFirstResource: Boolean): Boolean {
            binding.progressBar.visibility = View.GONE
            return false
        }

        /**
         * Called when a load completes successfully, immediately before `onResourceReady()`.
         *
         * @param resource The resource that was loaded for the target.
         * @param model The specific model that was used to load the image.
         * @param target The target the model was loaded into.
         * @param dataSource The `DataSource` the resource was loaded from.
         * @param isFirstResource `true` if this is the first resource to in this load to be loaded
         *      into the target.  For example when loading a thumbnail and a full-sized image, this
         *      will be `true` for the first image to load and `false` for the second.
         *
         * @return `true` to prevent `onResourceReady(Drawable)` from being called on `target`,
         *      typically because the listener wants to update the {@code target} or the object the
         *      `target` wraps itself or `false` to allow `onResourceReady(Drawable)` to be called
         *      on `target`.
         */
        override fun onResourceReady(resource: Drawable?, model: Any?,
                                     target: Target<Drawable>?, dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean {
            binding.progressBar.visibility = View.GONE
            return false
        }
    }

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

        // Migrate from Kotlin synthetics to Jetpack view binding.
        // https://developer.android.com/topic/libraries/view-binding/migration
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Download and show the image.
        intent.getStringExtra("URL")?.let {
            setImageView(it)
        }
    }

    /**
     * Download and set the selected image into the ImageView.
     *
     * @param url The public URL of selected image.
     */
    private fun setImageView(url: String) {

        // Code for showing progress bar while uploading.
        binding.progressBar.visibility = View.VISIBLE

        // Download ans set the selected image.
        Glide.with(this)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(progressListener)
            .into(binding.imageView)
    }

}
