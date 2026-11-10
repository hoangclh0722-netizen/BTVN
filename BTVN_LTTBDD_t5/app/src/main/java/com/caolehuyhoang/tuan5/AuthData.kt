package com.caolehuyhoang.tuan5

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.crashlytics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed interface AuthEvent {
    data object Success : AuthEvent
    data class Failure(val message: String) : AuthEvent
}

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth

    private val _user = MutableStateFlow(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy

    private val _event = MutableStateFlow<AuthEvent?>(null)
    val event: StateFlow<AuthEvent?> = _event

    private val listener = FirebaseAuth.AuthStateListener {
        _user.value = it.currentUser
        Firebase.crashlytics.setUserId(it.currentUser?.uid.orEmpty())
    }

    init {
        auth.addAuthStateListener(listener)
    }

    fun signInWithGoogle(activity: Activity) = signIn("google") {
        val clientId = activity.resources
            .getIdentifier("default_web_client_id", "string", activity.packageName)
            .takeIf { it != 0 }
            ?.let(activity::getString)
            ?: error("Chưa bật đăng nhập Google trong Firebase Console")
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(GetSignInWithGoogleOption.Builder(clientId).build())
            .build()
        val credential = CredentialManager.create(activity).getCredential(activity, request).credential
        if (credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) error("Không nhận được tài khoản Google")
        val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
        auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null)).await()
    }

    fun signInAsGuest() = signIn("anonymous") { auth.signInAnonymously().await() }

    fun signOut() {
        auth.signOut()
    }

    fun eventShown() {
        _event.value = null
    }

    private fun signIn(method: String, block: suspend () -> Unit) {
        if (_busy.value) return
        viewModelScope.launch {
            _busy.value = true
            _event.value = try {
                block()
                Firebase.analytics.logEvent(
                    FirebaseAnalytics.Event.LOGIN,
                    android.os.Bundle().apply { putString(FirebaseAnalytics.Param.METHOD, method) }
                )
                AuthEvent.Success
            } catch (e: GetCredentialCancellationException) {
                AuthEvent.Failure("Bạn đã huỷ đăng nhập")
            } catch (e: NoCredentialException) {
                AuthEvent.Failure("Máy chưa có tài khoản Google nào")
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
                AuthEvent.Failure(e.message ?: "Đăng nhập thất bại")
            }
            _busy.value = false
        }
    }

    override fun onCleared() {
        auth.removeAuthStateListener(listener)
    }
}

fun FirebaseUser.displayNameOrGuest() =
    displayName?.takeIf { it.isNotBlank() } ?: if (isAnonymous) "Khách" else email.orEmpty()

fun initials(name: String): String {
    val words = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return words.takeLast(2).joinToString("") { it.first().uppercase() }.ifEmpty { "?" }
}
