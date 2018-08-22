package dk.eboks.app.domain.models.channel.storebox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StoreboxReceiptItem(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("storeName")
        var storeName: String = "",
        @SerializedName("purchaseDate")
        var purchaseDate: Date? = null,
        @SerializedName("grantTotal") // todo fix this whenever api doodz fix their spelling
        var grandTotal: Double? = null,
        @SerializedName("logo")
        var logo: StoreboxMerchantLogo? = null
) : Parcelable