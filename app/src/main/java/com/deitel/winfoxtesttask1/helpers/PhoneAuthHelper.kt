package com.deitel.winfoxtesttask1.helpers

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import com.deitel.winfoxtesttask1.ui.MainActivity
import com.deitel.winfoxtesttask1.ui.SecondActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthHelper(private var activity: MainActivity,
                      private var progressDialog : ProgressDialog,
                      private var mCallBacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks?,
                      private var firebaseAuth: FirebaseAuth
                      ) {


    var checkUserStatus = 0

    fun startPhoneNumberVerification(phone : String) {
        val options = initPhoneOptions("Подтверждение номера...", phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendVerificationCode(phone: String, token: PhoneAuthProvider.ForceResendingToken?) {
        val options = initPhoneOptions("Повторная отпрввка кода...", phone)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun initPhoneOptions(dialogMessage: String, phone: String): PhoneAuthOptions.Builder {
        progressDialog.setMessage(dialogMessage)
        progressDialog.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallBacks)
        return options
    }

    fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        progressDialog.setMessage("Подтверждение номера...")
        progressDialog.show()
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        progressDialog.setMessage("Авторизация...")
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                if(checkUserStatus == 200) {
                    val phone = firebaseAuth.currentUser.phoneNumber
                    activity.makeShortToast("Вы вошли с номера $phone")
                    activity.startActivity(Intent(activity, SecondActivity::class.java))
                    activity.finish()
                } else {
                    progressDialog.dismiss()
                    activity.makeShortToast("BackendAPI Error: not 200")
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                activity.makeShortToast("${e.message}")
            }
    }
}