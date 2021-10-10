package com.example.commentsold.data.network.model

import android.os.Parcelable
import com.example.commentsold.utils.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val admin_id: Int,
    val brand: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val note: String,
    val product_name: String,
    val product_type: String,
    val shipping_price: Int,
    val style: String,
    val updated_at: String,
    val url: String? = null
) : Parcelable {
    fun shippingPriceString() = shipping_price.toString()
    fun idString() = id.toString()

    private fun getMockImageUrl() = "${Constants.MOCK_IMAGE_URL}?${(1..50).random()}"

    fun getImageUrl(): String {
        return if (url.isNullOrBlank())
            getMockImageUrl()
        else
            url
    }
}