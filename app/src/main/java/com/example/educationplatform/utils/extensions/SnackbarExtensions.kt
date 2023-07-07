package com.example.educationplatform.utils.extensions

import android.view.Gravity
import android.widget.FrameLayout
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.style.RotatingCircle
import com.google.android.material.snackbar.Snackbar

private const val PROGRESS_BAR_MARGIN = 10
private const val PROGRESS_BAR_SIZE = 80
private const val SNACK_BAR_WIDTH = 900

fun Snackbar.setWithProgressBar() {
    val snackBarLayout = (view as Snackbar.SnackbarLayout)
    val snackBarLayoutParams = snackBarLayout.layoutParams as FrameLayout.LayoutParams

    snackBarLayoutParams.apply {
        width = SNACK_BAR_WIDTH
        gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
    }

    val lp = FrameLayout.LayoutParams(
        PROGRESS_BAR_SIZE,
        PROGRESS_BAR_SIZE,
        Gravity.CENTER_VERTICAL or Gravity.END
    )
    lp.apply {
        marginEnd = PROGRESS_BAR_MARGIN
        marginStart = PROGRESS_BAR_MARGIN
        topMargin = PROGRESS_BAR_MARGIN
        bottomMargin = PROGRESS_BAR_MARGIN
    }

    val spin = SpinKitView(context)
    val doubleBounce = RotatingCircle()
    spin.setIndeterminateDrawable(doubleBounce)
    spin.layoutParams = lp

    snackBarLayout.addView(spin)
}