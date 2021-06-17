package Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Orders(
    val orderTime: Long = 0L,
    val user_id: String = "",
    val title: String = "",
    val location: String = "",
    val datetime: String = "",
    val image: String = "",
    val sub_total_amount: String = ""
    //val total_amount: String = "",

)
