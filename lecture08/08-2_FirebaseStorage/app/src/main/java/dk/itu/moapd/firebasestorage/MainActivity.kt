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

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dk.itu.moapd.firebasestorage.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * An activity class with methods to manage the main activity of Firebase Authentication
 * application.
 */
class MainActivity : AppCompatActivity(), ItemClickListener {

    /**
     * A set of static attributes used in this activity class.
     */
    companion object {
        private lateinit var adapter: CustomAdapter
    }

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private lateinit var auth: FirebaseAuth

    /**
     * A Firebase reference represents a particular location in your Database and can be used for
     * reading or writing data to that Database location.
     */
    private lateinit var database: DatabaseReference

    /**
     * The entry point of the Firebase Storage SDK.
     */
    private lateinit var storage: FirebaseStorage

    /**
     * This object launches a new activity and receives back some result data.
     */
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        galleryResult(result)
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

        // Sets whether the decor view should fit root-level content views for `WindowInsetsCompat`.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Migrate from Kotlin synthetics to Jetpack view binding.
        // https://developer.android.com/topic/libraries/view-binding/migration
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a `Toolbar` to act as the `ActionBar` for this Activity window.
        setSupportActionBar(binding.toolbar)

        // Initialize Firebase Auth and connect to the Firebase Realtime Database and Firebase
        // Storage.
        auth = FirebaseAuth.getInstance()
        database = Firebase.database(DATABASE_URL).reference
        storage = Firebase.storage(BUCKET_URL)

        // Define the add button behavior.
        binding.floatingActionButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            galleryLauncher.launch(galleryIntent)
        }

        // Setup the recycler view.
        binding.contentMain.apply {

            // Define the recycler view layout manager.
            val padding = 2
            val columns = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> 2
                else -> 4
            }

            // Define the recycler view layout manager and adapter.
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity, columns)
            recyclerView.itemAnimator = null
            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(padding, padding, padding, padding)
                }
            })

        }

        // Create the search query.
        auth.currentUser?.let {
            val query = database.child("images")
                .child(it.uid)
                .orderByChild("createdAt")

            // A class provide by FirebaseUI to make a query in the database to fetch appropriate data.
            val options = FirebaseRecyclerOptions.Builder<Image>()
                .setQuery(query, Image::class.java)
                .setLifecycleOwner(this)
                .build()

            // Create the custom adapter to bind a list of image objects.
            adapter = CustomAdapter(this, options)
            binding.contentMain.recyclerView.adapter = adapter
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You should place your menu
     * items in to menu.  This is only called once, the first time the options menu is displayed.
     * To update the menu every time it is displayed, see `onPrepareOptionsMenu(Menu)`.  The default
     * implementation populates the menu with standard system menu items.  These are placed in the
     * `Menu#CATEGORY_SYSTEM` group so that they will be correctly ordered with application-defined
     * menu items.  Deriving classes should always call through to the base implementation.  You can
     * safely hold on to menu (and any items created from it), making modifications to it as
     * desired, until the next time `onCreateOptionsMenu()` is called.  When you add items to the
     * menu, you can implement the Activity's `onOptionsItemSelected(MenuItem)` method to handle
     * them there.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return `true` for the menu to be displayed; if you return `false` it will
     *      not be shown.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * This hook is called whenever an item in your options menu is selected.  The default
     * implementation simply returns `false` to have the normal processing happen (calling the
     * item's `Runnable` or sending a message to its `Handler` as appropriate).  You can use this
     * method for any items for which you would like to do processing without those other
     * facilities.  Derived classes should call through to the base class for it to perform the
     * default menu handling.
     *
     * @param item The menu item that was selected.  This value cannot be `null`.
     *
     * @return Return `false` to allow normal menu processing to proceed, `true` to consume it here.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_image -> {
                val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                }
                galleryLauncher.launch(galleryIntent)
                true
            }
            // Show image's subtitle.
            R.id.action_subtitle -> {
                adapter.toggleDisplayDate()
                true
            }
            // Firebase Sign Out.
            R.id.action_sign_out -> {
                auth.signOut()
                startLoginActivity()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Called after `onCreate()` method; or after `onRestart()` method when the activity had been
     * stopped, but is now again being displayed to the user. It will usually be followed by
     * `onResume()`. This is a good place to begin drawing visual elements, running animations, etc.
     *
     * You can call `finish()` from within this function, in which case `onStop()` will be
     * immediately called after `onStart()` without the lifecycle transitions in-between
     * (`onResume()`, `onPause()`, etc) executing.
     *
     * <em>Derived classes must call through to the super class's implementation of this method. If
     * they do not, an exception will be thrown.</em>
     */
    override fun onStart() {
        super.onStart()

        // Check if the user is not logged and redirect her/him to the LoginActivity.
        if (auth.currentUser == null)
            startLoginActivity()
    }

    /**
     * This method starts the login activity which allows the user log in or sign up to the Firebase
     * Authentication application.
     *
     * Before accessing the main activity, the user must log in the application through a Firebase
     * Auth backend service. The method starts a new activity using explicit intent and used the
     * method `finish()` to disable back button.
     */
    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * This method will be executed when the user press an item in the `RecyclerView` for a short
     * time.
     *
     * @param image An instance of `Image` class.
     */
    override fun onItemClickListener(image: Image) {
        val intent = Intent(this, ImageActivity::class.java).apply {
            putExtra("URL", image.url)
        }
        startActivity(intent)
    }

    /**
     * This method will be executed when the user press an item in the `RecyclerView` for a long
     * time.
     *
     * @param image An instance of `Image` class.
     * @param position The position of selected view holder in the `RecyclerView`.
     */
    override fun onItemLongClickListener(image: Image, position: Int) {
        MaterialAlertDialogBuilder(this)
            .setMessage(resources.getString(R.string.delete))
            .setNegativeButton(resources.getString(R.string.decline)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, _ ->
                deleteImage(image, position)
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * When the second activity finishes (i.e., the photo gallery intent), it returns a result to
     * this activity. If the user selects an image correctly, we can get a reference of the selected
     * image and send it to the Firebase Storage.
     *
     * @param result A container for an activity result as obtained form `onActivityResult()`.
     */
    private fun galleryResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            // Create the folder structure save the selected image in the bucket.
            auth.currentUser?.let {
                val filename = UUID.randomUUID().toString()
                val image = storage.reference.child("images/${it.uid}/$filename")
                val thumbnail = storage.reference.child("images/${it.uid}/${filename}_thumbnail")
                result.data?.data?.let { uri ->
                    uploadImageToBucket(uri, image, thumbnail)
                }
            }
        }
    }

    /**
     * This method uploads the original and the thumbnail images to the Firebase Storage, and
     * creates a reference of uploaded images in the database.
     *
     * @param uri The URI of original image.
     * @param image The original image's storage reference in the Firebase Storage.
     * @param thumbnail The thumbnail image's storage reference in the Firebase Storage.
     */
    private fun uploadImageToBucket(uri: Uri, image: StorageReference, thumbnail: StorageReference) {
        // Code for showing progress bar while uploading.
        binding.contentMain.progressBar.visibility = View.VISIBLE

        // Upload the original image.
        image.putFile(uri).addOnSuccessListener { imageUrl ->

            // Upload the thumbnail image.
            thumbnail.putFile(createThumbnail(uri)).addOnSuccessListener {

                // Save the image reference in the database.
                imageUrl.metadata?.reference?.downloadUrl?.addOnSuccessListener { imageUri ->
                    saveImageInDatabase(imageUri.toString(), image.path)
                    binding.contentMain.progressBar.visibility = View.GONE
                }
            }
        }
    }

    /**
     * This method creates a squared thumbnail of the uploaded image. We are going to use the
     * thumbnail to show the images into the `RecyclerView`.
     *
     * @param uri The immutable URI reference of uploaded image.
     * @param size The image resolution used to create the thumbnail (Default: 300).
     *
     * @return The immutable URI reference of created thumbnail image.
     */
    private fun createThumbnail(uri: Uri, size: Int = 300): Uri {
        val decode = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        val thumbnail = ThumbnailUtils.extractThumbnail(
            decode, size, size, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
        return getImageUri(thumbnail)
    }

    /**
     * This method saves the bitmap in the temporary folder and return its immutable URI reference.
     *
     * @param image The thumbnail bitmap created in memory.
     *
     * @return The immutable URI reference of created thumbnail image.
     */
    private fun getImageUri(image: Bitmap): Uri {
        val file = File(cacheDir, "thumbnail")
        val outStream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.close()
        return Uri.fromFile(file)
    }

    /**
     * This method saves a reference of uploaded image in the database. The Firebase Storage does
     * NOT have a option to observe changes in the bucket an automatically updates the application.
     * We must use a database to have this feature in our application.
     *
     * @param url The public URL of uploaded image.
     * @param path The private URL of uploaded image on Firebase Storage.
     */
    private fun saveImageInDatabase(url: String, path: String) {
        val timestamp = System.currentTimeMillis()
        val image = Image(url, path, timestamp)

        // In the case of authenticated user, create a new unique key for the object in the
        // database.
        auth.currentUser?.let { user ->
            val uid = database.child("images")
                .child(user.uid)
                .push()
                .key

            // Insert the object in the database.
            uid?.let {
                database.child("images")
                    .child(user.uid)
                    .child(it)
                    .setValue(image)
            }
        }
    }

    /**
     * This method deletes a reference of uploaded image in the database, and the original and
     * thumbnail images from the Firebase Storage.
     *
     * @param image An instance of `Image` class.
     * @param position The image position in the `RecyclerView`.
     */
    private fun deleteImage(image: Image, position: Int) {
        // Remove an item from the Firebase Realtime database.
        adapter.getRef(position).removeValue().addOnSuccessListener {

            // Remove the thumbnail image.
            storage.reference.child("${image.path}_thumbnail")
                .delete().addOnSuccessListener {

                    // Remove the original image.
                    storage.reference.child("${image.path}")
                        .delete().addOnSuccessListener {
                            snackBar("Item deleted successfully")
                        }
                }
        }
    }

    /**
     * Make a standard snack-bar that just contains text.
     */
    private fun snackBar(text: CharSequence,
                         duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar
            .make(findViewById(android.R.id.content), text, duration)
            .show()
    }

}
