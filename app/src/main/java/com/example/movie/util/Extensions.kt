package com.example.movie.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.model.search.ResultMultiSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun AppCompatImageView.glidePoster(imagePath: String, glideRequestManager: RequestManager) =
    glideRequestManager
        .load(imagePath)
        .placeholder(R.drawable.progress_circle)
        .fitCenter()
        .into(this)

fun AppCompatImageView.glideTint(imagePath: String, glideRequestManager: RequestManager) =
    glideRequestManager
        .load(imagePath)
        .optionalCenterCrop()
        .into(this)

fun AppCompatImageView.glideProfile(imagePath: String, glideRequestManager: RequestManager) =
    glideRequestManager
        .load(imagePath)
        .optionalCircleCrop()
        .into(this)

fun AppCompatImageView.glidePosterDetails(imagePath: String, glideRequestManager: RequestManager) =
    glideRequestManager
        .load(imagePath)
        .placeholder(R.drawable.progress_circle)
        .optionalFitCenter()
        .into(this)

fun String.getProgress(): String {
    val stringBuilder = StringBuilder()
    var k = 0
    this.forEach {
        if (it != '.' && k < 2) {
            stringBuilder.append(it)
            k++
        }
    }
    return stringBuilder.toString()
}

fun List<ResultMultiSearch>.getInfo(): String {
    val stringBuilder = StringBuilder()
    this.forEach {
        if (it.name != null && it.name !== "") {
            stringBuilder.append(it.name + ", ")
        } else if (it.title != null && it.title !== "") {
            stringBuilder.append(it.title + ", ")
        }
    }
    return stringBuilder.toString()
}

fun Throwable.toastError(context: Context) {
    if (this.cause is ApiException.NetworkException) {
        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT)
            .show()
    } else {
        Toast.makeText(context, this.message, Toast.LENGTH_SHORT).show()
    }
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun <T> Flow<T>.launchWhenStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}
