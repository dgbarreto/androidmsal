package com.avanade.b2cauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.microsoft.identity.client.*
import com.microsoft.identity.client.IPublicClientApplication.ISingleAccountApplicationCreatedListener
import com.microsoft.identity.client.exception.MsalException

class MainActivity : AppCompatActivity() {
    private lateinit var mSingleAccountApp : ISingleAccountPublicClientApplication
    private lateinit var mAccount : IAccount
    private lateinit var mTextMessage : TextView
    private lateinit var mButtonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTextMessage = findViewById(R.id.message)
        mButtonLogin = findViewById(R.id.buttonLogin)
        mButtonLogin.setOnClickListener { logIn() }

        PublicClientApplication.createSingleAccountPublicClientApplication(
            this,
            R.raw.auth_config_single_account,
            object: ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication?) {
                    mSingleAccountApp = application!!
                    mTextMessage.text = "Application loaded"
                    //loadAccount()
                }

                override fun onError(exception: MsalException?) {
                    //displayError
                    mTextMessage.text = exception?.message
                }
            }
        )
    }

    fun logIn(){
        mSingleAccountApp.signIn(this, null, getScopes(), getAuthInteractiveCallback())
    }

    private fun getScopes() : Array<String>{
        return "read".split(" ").toTypedArray()
    }

    private fun getAuthInteractiveCallback() : AuthenticationCallback{
        return object: AuthenticationCallback{
            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                mTextMessage.text = authenticationResult?.account?.username
            }

            override fun onError(exception: MsalException?) {
                mTextMessage.text = exception?.message
            }

            override fun onCancel() {
                mTextMessage.text = "operação cancelada"
            }
        }
    }
}