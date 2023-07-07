package com.example.educationplatform.utils.extensions

import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.res.ResourcesCompat.getColor
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.example.educationplatform.R

fun setDefaultSkeletonDrawable(view: ViewGroup, ignoredViews: List<Int> = emptyList()) {
    val ids = arrayOf(1, 2, 3)
    test(*ids.toIntArray())
    SkeletonDrawable.create(view)
        .builder()
        .setAnimateRestoreBounds(true)
        .setEnabled(true)
        .setAllowSavedState(true)
        .withIgnoredBones(*ignoredViews.toIntArray())
        .setStateTransitionDuration(200L)
        .setUseStateTransition(true)
        .withShimmerBuilder {
            this
                .setThickness(200f)
                .setTilt(-0.4f)
                .setInterpolator(LinearInterpolator())
                .setSharedInterpolator(true)
                .setSpeedMultiplier(1.1f)
                .setCount(3)
                .setColor(MutableColor.Companion.fromColor(getColor(view.context.resources, R.color.bone_ray_color, view.context.theme)))
        }
        .withBoneBuilder {
            this
                .setEnabled(true)
                .setColor(MutableColor.Companion.fromColor(getColor(view.context.resources, R.color.bone_color, view.context.theme)))
                .setCornerRadius(40f)
                .setAllowSavedState(true)
                .setDissectBones(true)
        }
}

fun test(vararg x: Int) {

}

