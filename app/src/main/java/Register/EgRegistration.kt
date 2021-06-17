package Register

import Activities.egLogin
import Activities.egNavigation
import Models.EventGoer
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_eg_register.*
import kotlinx.android.synthetic.main.activity_eg_register.et_email
import kotlinx.android.synthetic.main.activity_eg_register.et_password

class egRegistration: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eg_register)


        val registerButton = findViewById<Button>(R.id.register_button)



        txt_registered.setOnClickListener {
            startActivity(Intent(this, egLogin::class.java))

        }

        registerButton.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {
        val name = et_name.text.toString().toLowerCase()
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        
        when {
            TextUtils.isEmpty(name) -> Toast.makeText(
                this,
                "name not entered",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(email) -> Toast.makeText(
                this,
                "Email not entered",
                Toast.LENGTH_LONG
            ).show()
            TextUtils.isEmpty(password) -> Toast.makeText(
                this,
                "Password not entered",
                Toast.LENGTH_LONG
            ).show()

            else -> {

                //create a progress diaglog
                val progressDialog = ProgressDialog(this)
                //set the title of the progress dialog "sign up" by implementing the setTitle() function


                progressDialog.setTitle("Register")
                // set "hold up bruda" message to progress bar by implementing the setMessage function
                progressDialog.setMessage("Hold up bruda...")
                // make removing of progress bar by touching on the outside false by using
                progressDialog.setCanceledOnTouchOutside(false)

                //show the progress dialog
                progressDialog.show()

                //create instance of firebase auth to enable us to create user with Email and Password

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {


                            saveUserInfo(name, email, progressDialog)


                        } else {

                            // make a toast with the it/task complettion exception
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Error: @message", Toast.LENGTH_LONG).show()
                            //   Log.d(TAG, "Authentication Failed. User info not saved")
                            //in case of error dismiss progress dialog
                            FirebaseAuth.getInstance().signOut()
                            progressDialog.dismiss()


                        }


                    }


            }

        }




    }

    private fun saveUserInfo(name: String, email: String, progressDialog: ProgressDialog) {

        // ?: added to add string incase of emptiness
        val uid = FirebaseAuth.getInstance().uid ?: ""
        //val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val databaseRef = FirebaseDatabase.getInstance().getReference("EventGoers/$uid")

        val user = EventGoer(name, email, "", uid)

        databaseRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful){

                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Account Created",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, egNavigation::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            else {


                val message = task.exception!!.toString()
                Toast.makeText(this, "Error: @message", Toast.LENGTH_LONG).show()
                //   Log.d(TAG, "Authentication Failed. User info not saved")
                //in case of error dismiss progress dialog
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()


            }
        }

    }


}