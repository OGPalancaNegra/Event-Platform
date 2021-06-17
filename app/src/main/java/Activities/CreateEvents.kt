package Activities

import Models.Events
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_event.*

class CreateEvents: AppCompatActivity() {

    private lateinit var eventImageString: String
    private var eventImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        create_event_back_icon.setOnClickListener {
            finish()

        }


        val eventImageButton = findViewById<ImageButton>(R.id.EventImageButton)

        eventImageButton.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type ="image/*"
            startActivityForResult(imagePickerIntent, 0)

        }




        //perform create recipe function after user clicks on Save Recipe Icon

        saveEvent.setOnClickListener {
            createEvent()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0){
            //if user selected do else notify user that image was not with Activity.result_ok
            if (resultCode== Activity.RESULT_OK){



                if (data != null) {
                    eventImageUri = data.data
                }


                //RecipeImageButton.setImageURI(recipeImageUri)
                Picasso.get().load(eventImageUri).centerCrop().fit().into(EventImageButton)


            }

        } else {
            Toast.makeText(this,"IMAGE NOT SELECTED" , Toast.LENGTH_LONG).show()
        }


    }



    fun createEvent(){




        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val recipeImageRef = FirebaseStorage.getInstance().getReference("/user/$uid/images").child(
            eventImageUri?.lastPathSegment.toString()
        )

        eventImageUri?.let { recipeImageRef.putFile(it) }?.addOnCompleteListener { it ->
            if (it.isSuccessful){

                val uid = FirebaseAuth.getInstance().currentUser!!.uid

                recipeImageRef.downloadUrl.addOnSuccessListener {it ->

                    eventImageString = it.toString()

                    //create image database reference to add images to its own dat...nah

                    val databaseRefForImage = FirebaseDatabase.getInstance().getReference("eventImages/$uid/imageString").push()

                    val userDatabaseRef = FirebaseDatabase.getInstance().getReference("events/$uid")

                    //create condition to


                    userDatabaseRef.push().setValue(
                        Events(
                        uid = uid,
                        eventImageString = eventImageString,
                        eventTitle = et_event_title.text.toString(),
                        eventLocation = et_event_location.text.toString(),
                        eventPrice = et_event_price.text.toString(),
                        eventDescription = et_event_description.text.toString(),
                        eventDateTime = et_event_date_time.text.toString()

                    )
                    )
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val intent = Intent(this, eoMainActivity::class.java)
                                startActivity(intent)
                                finish()
                                //take to user profile?



                            } else {
                                Toast.makeText(this,"Event not Created" , Toast.LENGTH_LONG).show()


                            }




                        }

                }

                // Picasso.get().load(User().imageString).error(R.drawable.default_profile_image).centerCrop().fit().into(profilePhoto)

            }


        }


    }


}