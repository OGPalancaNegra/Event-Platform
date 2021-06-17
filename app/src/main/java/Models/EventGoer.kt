package Models

import com.google.firebase.database.Exclude

data class EventGoer(
    val name: String= "",
    val email: String= "",
    val imageString: String="",
    @Exclude val uid: String = "",
    val follows: Map<String, Boolean> = emptyMap()
    )