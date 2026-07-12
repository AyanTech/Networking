package ir.ayantech.networking

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import ir.ayantech.ayannetworking.v2.AyanApi
import ir.ayantech.ayannetworking.v2.api.onChangeState
import ir.ayantech.ayannetworking.v2.api.onFailure
import ir.ayantech.ayannetworking.v2.api.onSuccess
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

const val TAG = "TAG_NETWORKING"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ayanAPI: AyanApi = AyanApi
            .Builder(context = this, "my_base_url")
            .setLogLevel(LogLevel.LOG_ALL)
            .setInvokeUserToken {
                return@setInvokeUserToken "user_token"
            }
            .setTimeOutDuration(20.seconds)
            .build()

        findViewById<Button>(R.id.retryBtn).setOnClickListener {
            sampleApiCall(ayanApi = ayanAPI)
        }

    }

    private fun sampleApiCall(ayanApi: AyanApi) {
        val input = GetEndUserInquiryHistoryDetail.Input("my_type")
        val endpoint = "my_endpoint"

        lifecycleScope.launch {
            ayanApi.post<GetEndUserInquiryHistoryDetail.Input, GetEndUserInquiryHistoryDetail.Output>(
                body = input,
                endPint = endpoint
            ).collect { ayanAPIResult ->
                ayanAPIResult.onSuccess { response ->
                    val message =
                        "Success response: ${response.inquiryHistory.firstOrNull()?.value}"
                    Log.d(TAG, message)

                }
                ayanAPIResult.onFailure { ayanFailure ->
                    val message =
                        "ayanFailure | Type: ${ayanFailure.failureType} | Code: ${ayanFailure.failureCode}"
                    Log.d(TAG, message)

                }

                ayanAPIResult.onChangeState { state ->
                    Log.d(TAG, "onChangeState : $state")
                }

            }
        }
    }

}