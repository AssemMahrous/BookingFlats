package com.example.bookingflats.basemodule.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bookingflats.basemodule.BuildConfig
import com.example.bookingflats.basemodule.R
import com.example.bookingflats.basemodule.base.data.model.ApplicationMessage
import org.koin.core.qualifier.Qualifier
import java.util.*
import kotlin.reflect.KClass
import com.example.bookingflats.basemodule.base.di.AppConfigurationModule.isDebug
import okhttp3.Headers
import okhttp3.internal.toHeaderList
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.text.SimpleDateFormat

private val sdfWithSeconds = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    .apply { timeZone = TimeZone.getTimeZone("UTC") }
private val sdfWithoutSeconds = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
    .apply { timeZone = TimeZone.getTimeZone("UTC") }

fun Any?.isNull(): Boolean = this == null

fun Any?.isNotNull(): Boolean = this != null

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun isDebug(block: () -> Unit) {
    if (isDebug && BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) block()
}

inline fun isNotDebug(block: () -> Unit) {
    if (!isDebug || !BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) block()
}

inline fun isDebugOrBeta(block: () -> Unit) {
    if (isDebug) block()
}

fun <T : Any> Class<T>?.getKClass(): KClass<T>? = this?.kotlin

inline fun <reified T> getKoinInstance(qualifier: Qualifier? = null): Lazy<T> {
    return lazy {
        return@lazy object : KoinComponent {
            val value by inject<T>(qualifier)
        }.value
    }
}

val <T> T.exhaustive: T get() = this

@JvmOverloads
fun NavController.navigateTo(id: Int, bundle: Bundle? = null, navOptions: NavOptions? = null) {
    navigate(id, bundle, navOptions ?: getNavOptionsBuilder().build())
}

@JvmOverloads
fun NavController.navigateTo(id: Int, bundle: Bundle? = null, popCurrentDestination: Boolean) {
    currentDestination?.takeIf { popCurrentDestination }?.let { currentDestination ->
        navigate(
            id,
            bundle,
            getNavOptionsBuilder()
                .setPopUpTo(currentDestination.id, true)
                .build()
        )
    } ?: navigate(id, bundle, getNavOptionsBuilder().build())
}

@JvmOverloads
fun NavController.navigateTo(deepLink: Uri, navOptions: NavOptions? = null) {
    if (graph.hasDeepLink(deepLink)) navigate(deepLink, navOptions
        ?: getNavOptionsBuilder().build())
}

@JvmOverloads
fun NavController.navigateTo(deepLink: Uri, popCurrentDestination: Boolean) {
    currentDestination?.takeIf { popCurrentDestination }?.let { currentDestination ->
        navigateTo(
            deepLink,
            getNavOptionsBuilder()
                .setPopUpTo(currentDestination.id, true)
                .build()
        )
    } ?: navigateTo(deepLink)
}

@JvmOverloads
fun NavController.navigateTo(deepLink: String?, navOptions: NavOptions? = null) {
    deepLink?.let { navigateTo(Uri.parse(it), navOptions) }
}

fun NavController.navigateTo(deepLink: String?, popCurrentDestination: Boolean) {
    currentDestination?.takeIf { popCurrentDestination }?.let { currentDestination ->
        navigateTo(
            deepLink,
            getNavOptionsBuilder()
                .setPopUpTo(currentDestination.id, true)
                .build()
        )
    } ?: navigateTo(deepLink)
}

@JvmOverloads
fun NavController.navigateTo(navDirections: NavDirections, navOptions: NavOptions? = null) {
    navigate(navDirections, navOptions ?: getNavOptionsBuilder().build())
}

fun NavController.popBackStackOrFinish(fragment: Fragment) {
    if (!popBackStack()) fragment.requireActivity().finish()
}

fun getNavOptionsBuilder() = NavOptions.Builder()
    .setEnterAnim(R.anim.nav_default_enter_anim)
    .setExitAnim(R.anim.nav_default_exit_anim)
    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
    .setPopExitAnim(R.anim.nav_default_pop_exit_anim)

fun <T> Fragment.setNavigationResult(key: String, value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> Fragment.getNavigationResult(@IdRes id: Int, key: String, onResult: (result: T) -> Unit) {
    val navBackStackEntry = findNavController().getBackStackEntry(id)

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(key)) {
            val result = navBackStackEntry.savedStateHandle.get<T>(key)
            result?.let(onResult)
            navBackStackEntry.savedStateHandle.remove<T>(key)
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry.lifecycle.removeObserver(observer)
        }
    })
}

fun Any.className(): String = this::class.java.simpleName

fun isMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

inline fun TODO_IF_DEBUG(reason: String? = null) = isDebug {
    throw reason?.let { throw NotImplementedError("An operation is not implemented: $it") }
        ?: throw NotImplementedError()
}

fun List<ApplicationMessage.MessageAction>.getActions()
        : Triple<ApplicationMessage.MessageAction?, ApplicationMessage.MessageAction?,
        ApplicationMessage.MessageAction?> {
    val positiveAction = firstOrNull { it is ApplicationMessage.MessageAction.Positive }
    val negativeAction = firstOrNull { it is ApplicationMessage.MessageAction.Negative }
    val neutralAction = firstOrNull { it is ApplicationMessage.MessageAction.Neutral }
    return Triple(positiveAction, negativeAction, neutralAction)
}

fun Fragment.closeKeyboard() {
    if (isAdded)
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .run {
                if (!requireActivity().isFinishing) {
                    hideSoftInputFromWindow(this@closeKeyboard.view?.windowToken, 0)
                }
            }
}

fun Activity.closeKeyboard() {
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .run {
            var view: View? = currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) view = View(this@closeKeyboard)
            hideSoftInputFromWindow(view.windowToken, 0)
        }
}

fun Boolean?.isNullOrFalse() = this == null || !this

fun Date?.toStringFormat(): String? {
    return when {
        this != null -> sdfWithSeconds.format(this)
        else -> null
    }
}

fun String?.toDate(): Date? {
    return when {
        !this.isNullOrEmpty() -> try {
            sdfWithSeconds.parse(this)
        } catch (e: Exception) {
            sdfWithoutSeconds.parse(this)
        }
        else -> Date()
    }
}

fun <T : Any> T.toEvent() = Event(this)

fun Headers?.toHashMap() =
    hashMapOf<String, String>().apply {
        this@toHashMap?.toHeaderList()?.forEach { header ->
            this[header.name.utf8()] = header.value.utf8()
        }
    }