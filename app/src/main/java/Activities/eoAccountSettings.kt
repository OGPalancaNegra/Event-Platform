package Activities

import Login.eoLogin
import Models.EventOrganizers
import ValueEventListenerAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventplatform.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_eo_account_settings.*

class eoAccountSettings: AppCompatActivity() {

    private lateinit var mEventOrganizers: EventOrganizers
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eo_account_settings)
        val backArrow = findViewById<ImageView>(R.id.edit_profile_backicon)


        backArrow.setOnClickListener {
            finish()
            //supportFragmentManager.beginTransaction().replace(R.id.home_frag_container, ProfileFragment())
            //supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ProfileFragment())
        }


        //Picasso.get().load(R.drawable.default_profile_image).fit().centerCrop().into(view.profilePhoto)



        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val databaseReference = FirebaseDatabase.getInstance().getReference("EventOrganizers/$uid")

        databaseReference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
            mEventOrganizers = it.getValue(EventOrganizers::class.java)!!
            edit_email_text_view2.setText(mEventOrganizers.email, TextView.BufferType.EDITABLE)
            edit_name_text_view2.setText(mEventOrganizers.name, TextView.BufferType.EDITABLE)

            if (mEventOrganizers.imageString?.isEmpty()!!) {
                profilePhoto2.setImageResource(R.drawable.default_profile_image);
            } else{
                Picasso.get().load(mEventOrganizers.imageString).into(profilePhoto2);
            }

        })

        val edit_profile_save_image = findViewById<ImageView>(R.id.edit_profile_save_image)


        //code to save changes

        edit_profile_save_image.setOnClickListener {
            //uploadImageToFirebase()
            updateProfile()

        }


        //code to change profile picture

        val changeProfilePhoto = findViewById<TextView>(R.id.changeProfilePhoto)
        changeProfilePhoto.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type ="image/*"
            startActivityForResult(imagePickerIntent, 0)

        }



        // code to sign out

        val signoutbutton = findViewById<Button>(R.id.signoutbtn)
        signoutbutton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            //send user to login activity

            val intent = Intent(this, eoLogin::class.java)
            //clear the
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }



    }



    //code to change profile photo

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

                val imageRef = FirebaseStorage.getInstance().getReference("/EventOrganizers/$uid/images")

                imageUri = data?.data

                val profilePhoto = findViewById<ImageView>(R.id.profilePhoto2)

                profilePhoto.setImageURI(imageUri)





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


    private fun updateProfile() {

        val user= EventOrganizers(name = edit_name_text_view2.text.toString(),email = edit_email_text_view2.text.toString())

        // validate user * create toasts to deal with empy input field

        val error = correctUser(user)

        if (error == null) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid

            val databaseReference = FirebaseDatabase.getInstance().getReference("EventOrganizers/$uid")
            databaseReference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
                val mEventOrganizers = it.getValue(EventOrganizers::class.java)})

            if (user.email == mEventOrganizers.email) {
                updateUser(user)


            } else {
                //reauthenticate and update email in auth
                val password = edit_profile_password2.text.toString()
                val email = edit_email_text_view2.text.toString()
                val credential = EmailAuthProvider.getCredential(email, password)
                FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential).addOnCompleteListener {
                    if (it.isSuccessful){
                        FirebaseAuth.getInstance().currentUser!!.updateEmail(mEventOrganizers.email).addOnCompleteListener {
                            if (it.isSuccessful){
                                updateUser(user)
                            } else {
                                val message = it.exception!!.toString()
                                Toast.makeText(
                                    this,
                                    "Error: @message",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }
                }
            }

        }
        //validate(user)
    }

    private fun updateUser(eventOrganizers: EventOrganizers) {
        val updateMap = mutableMapOf<String, Any>()
        if (eventOrganizers.name != mEventOrganizers.name) updateMap["name"] = eventOrganizers.name
        if (eventOrganizers.email != mEventOrganizers.email) updateMap["email"] = eventOrganizers.email


        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().getReference("EventOrganizers/$uid").updateChildren(updateMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Profile Saved",
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
    }

    fun correctUser(eventOrganizers: EventOrganizers): String? =
        when {

            eventOrganizers.name.isEmpty() -> "Please Enter Name"
            eventOrganizers.email.isEmpty() -> "Please Enter Email"

            else -> null

        }

}