package ir.ayantech.networking

import com.google.gson.annotations.SerializedName


class GetEndUserInquiryHistoryDetail {

    data class Input(
        @SerializedName("InquiryType")
        val inquiryType: String
    )

    data class Output(
        @SerializedName("InquiryHistory")
        val inquiryHistory: ArrayList<InquiryModel>,
        @SerializedName("TotalInquiryHistoryCount")
        val totalInquiryHistoryCount: Long
    )

    data class InquiryModel(
        @SerializedName("Description")
        val description: String,
        @SerializedName("IsFavorite")
        val isFavorite: Boolean,
        @SerializedName("IsElectronic")
        val isElectronic: Boolean,
        @SerializedName("ID")
        val id: Long,
        @SerializedName("Type")
        val type: String,
        @SerializedName("Value")
        val value: String
    )
}