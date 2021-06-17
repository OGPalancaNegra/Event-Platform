package Activities

import Models.EventOrganizers
import Models.Events
import ValueEventListenerAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.eventplatform.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_eo_account_settings.*
import kotlinx.android.synthetic.main.activity_eo_display_events.*

class eoDisplayEvents: AppCompatActivity() {

    private lateinit var mEvents: Events
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eo_display_events)

        create_event_back_icon.setOnClickListener {
            finish()
        }

        //val intentTing= intent.getParcelableExtra<Adapter>(ProfileFragment.INTENT_PARCELABLE)

        val eventImage = findViewById<ImageButton>(R.id.EventImageButton)

        //Picasso.get().load(R.drawable.ic_round_android_24).placeholder(R.drawable.ic_round_android_24).fit().centerCrop().into(recipeImage)

        val eventTitle = findViewById<EditText>(R.id.et_event_title)

        val eventDescription = findViewById<EditText>(R.id.et_event_description)
        val eventLocation = findViewById<EditText>(R.id.et_event_location)
        val eventDateTime = findViewById<EditText>(R.id.et_event_date_time)
        val eventPrice = findViewById<EditText>(R.id.et_event_price)

        //recipeTitle.text = intentTing.

        val eventTitleString = intent.getStringExtra("event_title")
        val eventDescriptionString = intent.getStringExtra("event_description")
        val eventLocationString = intent.getStringExtra("event_location")
        val eventPriceString = intent.getStringExtra("event_price")
        val eventImageString = intent.getStringExtra("event_image")
        val eventDateTimeString = intent.getStringExtra("event_date_time")

        val eventCreatorString = intent.getStringExtra("recipe_creator")

        eventTitle.setText(eventTitleString)
        eventDescription.setText(eventDescriptionString)
        eventLocation.setText(eventLocationString)
        eventPrice.setText(eventPriceString)
        Picasso.get().load(eventImageString).fit().centerCrop().into(eventImage)
        eventDateTime.setText(eventDateTimeString)






        //recipeCreator.text=recipeCreatorString




        //edit event

        EventImageButton.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type ="image/*"
            startActivityForResult(imagePickerIntent, 0)
        }


        saveEvent.setOnClickListener {
            //editEvent()

        }


        delete_event_button.setOnClickListener {

           // val uid = FirebaseAuth.getInstance().currentUser!!.uid


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //check if request code the same


        if (requestCode == 0){
            //if user selected do else notify user that image was not with Activity.result_ok
            if (resultCode== Activity.RESULT_OK){

                //get acess to image with the data intent thats passed in as paramter

                //get location of photo
                //photoUri = data?.data
                //view?.profilePhoto?.setImageURI(photoUri)

                //Picasso.get().load(photoUri).fit().centerCrop().into(view?.profilePhoto)


                //val filename = UUID.randomUUID().toString()
                val uid = FirebaseAuth.getInstance().currentUser!!.uid

                val imageRef = FirebaseStorage.getInstance().getReference("/user/$uid/images")

                imageUri = data?.data
                EventImageButton.setImageURI(imageUri)





                imageUri?.let { imageRef.putFile(it) }?.addOnCompleteListener { it ->
                    if (it.isSuccessful){

                        imageRef.downloadUrl.addOnSuccessListener {it ->

                            val imageString2 = it.toString()

                            val databaseRefForImage = FirebaseDatabase.getInstance().getReference("EventOrganizers/$uid/imageString")

                            val databaseRef = FirebaseDatabase.getInstance().getReference("/EventOrganizers/$uid")


                            //mUser = mUser.copy(imageString = it.toString())

                            //val user = User(uid, edit_name_text_view.text.toString(), edit_username_text_view.text.toString(), edit_email_text_view.text.toString(),imageString2)

                            //User().imageString= imageString2

                            databaseRefForImage.setValue(imageString2)

                            // Picasso.get().load(User().imageString).error(R.drawable.default_profile_image).centerCrop().fit().into(profilePhoto)

                        }


                    } else {
                        Toast.makeText(this,"IMAGE NOT PUT IN FILE" , Toast.LENGTH_LONG)

                    }

                }






                //FirebaseStorage.getInstance().reference.child("users/$uid/photo").pu



            } else {
                Toast.makeText(this,"IMAGE NOT SELECTED" , Toast.LENGTH_LONG)
            }


        }
    }


    private fun editEvent() {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseReference = FirebaseDatabase.getInstance().getReference("events/$uid")
        databaseReference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
            mEvents = it.getValue(Events::class.java)!!
            val event= Events(eventTitle = et_event_title.text.toString(), eventDateTime = et_event_date_time.text.toString(),eventPrice = et_event_price.text.toString(), eventLocation = et_event_location.text.toString(), eventDescription = et_event_description.text.toString())

            // validate user * create toasts to deal with empy input field

            val updateMap = mutableMapOf<String, Any>()
            if (event.eventTitle != mEvents.eventTitle) updateMap["eventTitle"] = event.eventTitle
            if (event.eventLocation != mEvents.eventLocation) updateMap["eventLocation"] = event.eventLocation
            if (event.eventDateTime != mEvents.eventDateTime) updateMap["eventDateTime"] = event.eventDateTime
            if (event.eventDescription != mEvents.eventDescription) updateMap["eventDescription"] = event.eventDescription
            if (event.eventPrice != mEvents.eventPrice) updateMap["eventPrice"] = event.eventPrice



            FirebaseDatabase.getInstance().getReference("events/$uid").updateChildren(updateMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Events Saved",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this, eoMainActivity::class.java)
                        startActivity(intent)




                    } else {
                        val message = it.exception!!.toString()
                        Toast.makeText(
                            this,
                            "Error: @message",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }


        })



    }





}