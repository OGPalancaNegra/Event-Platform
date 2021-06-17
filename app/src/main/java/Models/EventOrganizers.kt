package Models

import com.google.firebase.database.Exclude

data class EventOrganizers(
    val name: String= "",
    val email: String= "",
    val imageString: String="",
    @Exclude val uid: String = "",
    val followers: Map<String, Boolean> = emptyMap()
)