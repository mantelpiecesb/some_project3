package com.deitel.winfoxtesttask1.ui

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deitel.winfoxtesttask1.viewmodels.MyViewModel
import com.deitel.winfoxtesttask1.R
import com.deitel.winfoxtesttask1.data.MyRepository
import com.deitel.winfoxtesttask1.model.UserInfo
import com.deitel.winfoxtesttask1.databinding.ActivityMainBinding
import com.deitel.winfoxtesttask1.helpers.PhoneAuthHelper
import com.deitel.winfoxtesttask1.viewmodels.MyViewModelFactory
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.raywenderlich.android.daggerserverrepository.di.DaggerAppComponent

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MY_TAG"
    private var forceResendingToken : PhoneAuthProvider.ForceResendingToken? = null
    private var mCallBacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var mVerificationId : String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog
    private lateinit var phoneAuthHelper: PhoneAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.phoneLl.visibility = View.VISIBLE
        binding.codeLl.visibility = View.GONE

        val repository = DaggerAppComponent.create().repository()
        val viewModelFactory = MyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MyViewModel::class.java)
        viewModel.mMyResponse.observe(this, Observer { response ->
            if(response.isSuccessful) {
                phoneAuthHelper.checkUserStatus = response.code()
            } else {
                Log.d(TAG, "No retrofit results...")
            }
        })

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Подождите...")
        progressDialog.setCanceledOnTouchOutside(false)


        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential : PhoneAuthCredential) {
                phoneAuthHelper.signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                makeShortToast("${e.message}")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                mVerificationId = verificationId
                forceResendingToken = token
                progressDialog.dismiss()
                val checkUser = UserInfo(verificationId, getPhoneText())
                viewModel.checkUser(checkUser)
                binding.phoneLl.visibility = View.GONE
                binding.codeLl.visibility = View.VISIBLE
                makeShortToast("Код отправлен в СМС")
            }
        }

        phoneAuthHelper = PhoneAuthHelper(this, progressDialog, mCallBacks, firebaseAuth)

        binding.phoneContinueBtn.setOnClickListener {
            if(TextUtils.isEmpty(getPhoneText())) {
                makeShortToast("Введите номер")
            } else if(getPhoneText().substring(0,2) != "+7") {
                makeShortToast("Номер должен начинаться с +7")
            } else if(getPhoneText().length != 12) {
                makeShortToast("Неправильная длина номера")
            } else {
                phoneAuthHelper.startPhoneNumberVerification(getPhoneText())
            }
        }

        binding.resendCodeTv.setOnClickListener {
            if(TextUtils.isEmpty(getPhoneText())) {
                makeShortToast("Введите номер")
            } else {
                binding.resendCodeTv.isClickable = false
                object : CountDownTimer(120000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.resendCodeTv.text = "Повторить отправку через: " + millisUntilFinished / 1000 + " сек."
                    }
                    override fun onFinish() {
                        binding.resendCodeTv.text = getString(R.string.didn_t_get_otp_resend)
                        binding.resendCodeTv.isClickable = true
                    }
                }.start()
                phoneAuthHelper.resendVerificationCode(getPhoneText(), forceResendingToken)
            }
        }

        binding.codeSubmitBtn.setOnClickListener {
            val code = binding.codeEt.text.toString().trim()
            if(TextUtils.isEmpty(code)){
                makeShortToast("Введите код подтверждения")
            } else {
                phoneAuthHelper.verifyPhoneNumberWithCode(mVerificationId, code)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
    }

    fun makeShortToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun getPhoneText():String = binding.phonEt.text.toString().trim()

}