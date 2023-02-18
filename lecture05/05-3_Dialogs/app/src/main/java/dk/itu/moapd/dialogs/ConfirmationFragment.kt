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
package dk.itu.moapd.dialogs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


/**
 * A fragment to show the `Confirmation Fragment`.
 *
 * The `MainActivity` has a `FragmentContainerView` area to replace dynamically the fragments used
 * by this project. You can use a bundle to share data between the main activity and this fragment.
 */
class ConfirmationFragment : Fragment() {

    /**
     * A set of private constants used in this class.
     */
    companion object {
        private val TAG = ConfirmationFragment::class.qualifiedName
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and
     * non-graphical fragments can return null. This will be called between `onCreate(Bundle)` and
     * `onViewCreated(View, Bundle)`. A default `View` can be returned by calling `Fragment(int)` in
     * your constructor. Otherwise, this method returns null.
     *
     * It is recommended to <strong>only</strong> inflate the layout in this method and move logic
     * that operates on the returned View to `onViewCreated(View, Bundle)`.
     *
     * If you return a `View` from here, you will later be called in `onDestroyView()` when the view
     * is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *      fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *      attached to. The fragment should not add the view itself, but this can be used to
     *      generate the `LayoutParams` of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *      saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView() method called.")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation, container, false)
    }

    /**
     * Called immediately after `onCreateView(LayoutInflater, ViewGroup, Bundle)` has returned, but
     * before any saved state has been restored in to the view. This gives subclasses a chance to
     * initialize themselves once they know their view hierarchy has been completely created. The
     * fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by `onCreateView(LayoutInflater, ViewGroup, Bundle)`.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *      saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val singleItems = arrayOf("First Item", "Second Item", "Third Item")
        val checkedItem = 1

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirmation_title))
            .setNeutralButton(getString(R.string.cancel)) { _, _ ->
                // TODO: Respond to neutral button press
                snackBar("Cancelled")
                showMainFragment()
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                // TODO: Respond to positive button press
                snackBar("Confirmed")
                showMainFragment()
            }
            // Single-choice items (initialized with checked item)
            .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                // TODO: Respond to item chosen
                snackBar(singleItems[which])
            }
            .show()
    }

    /**
     * Make a standard snack-bar that just contains text.
     */
    private fun snackBar(text: CharSequence,
                         duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar
            .make(requireActivity().findViewById(R.id.content_main), text, duration)
            .show()
    }

    /**
     * Return the user to the main fragment automatically.
     */
    private fun showMainFragment() {
        findNavController().navigate(
            R.id.show_fragment_main
        )
    }

}
