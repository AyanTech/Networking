package ir.ayantech.networking

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import ir.ayantech.ayannetworking.v2.AyanApi
import ir.ayantech.ayannetworking.v2.api.onChangeState
import ir.ayantech.ayannetworking.v2.api.onFailure
import ir.ayantech.ayannetworking.v2.api.onSuccess
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

const val TAG = "TAG_NETWORKING"

class MainActivity : AppCompatActivity() {

    private var txtStatus: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn_fetch)
        txtStatus = findViewById(R.id.txt_status)

        //http://ghabzinohelper.com/webservices/core.svc/help/operations/DonationServiceGerReferrerTypeList
        //https://application.billingsystem.ayantech.ir/WebServices/Core.svc/DonationServiceGerReferrerTypeList

        val ayanAPI: AyanApi = AyanApi
            .Builder(
                context = this,
                "https://application.billingsystem.ayantech.ir/WebServices/Core.svc/"
            )
            .setLogLevel(LogLevel.LOG_ALL)
            .setInvokeUserToken {
                return@setInvokeUserToken "user_token"
            }
            .setTimeOutDuration(20.seconds)
            .build()

        btn.setOnClickListener {
            sampleApiCall(ayanApi = ayanAPI)
        }

    }

    private fun sampleApiCall(ayanApi: AyanApi) {
        val input = DonationServiceGerReferrerTypeList.Input()
        val endpoint = "DonationServiceGerReferrerTypeList"

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ayanApi.post<DonationServiceGerReferrerTypeList.Input, DonationServiceGerReferrerTypeList.Output>(
                    body = input,
                    endPint = endpoint
                ).collect { ayanAPIResult ->
                    ayanAPIResult.onSuccess { response ->
                        txtStatus?.text =
                            getString(R.string.success).plus(response.referrerTypeList.firstOrNull()?.showName)

                    }
                    ayanAPIResult.onFailure { ayanFailure ->
                        val message =
                            "ayanFailure | Type: ${ayanFailure.failureType} | Code: ${ayanFailure.failureCode}"
                        Log.d(TAG, message)
                        txtStatus?.text = getString(R.string.failure).plus(ayanFailure.failureType)
                    }

                    ayanAPIResult.onChangeState { state ->
                        if (state == ApiCallStatus.LOADING) {
                            txtStatus?.text = getString(R.string.loading)
                        }
                        Log.d(TAG, "onChangeState : $state")
                    }

                }
            }
        }
    }

}