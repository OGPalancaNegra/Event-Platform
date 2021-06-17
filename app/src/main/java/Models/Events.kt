package Models

import com.google.firebase.database.Exclude

data class Events(
    val eventImageString: String="",
    val eventTitle: String ="",
    val eventLocation: String="",
    val eventDateTime: String="",
    val eventPrice: String="",
    val eventDescription: String="",
    @Exclude val uid: String? = null)


