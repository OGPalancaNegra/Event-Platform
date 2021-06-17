package Activities

import Login.eoLogin
import Register.egRegistration
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_eg_login.*

class egLogin: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eg_login)


        val unregisteredTxt = findViewById<TextView>(R.id.txt_unregistered)

        unregisteredTxt.setOnClickListener {
            startActivity(Intent(this, egRegistration::class.java))
            finish()
        }

        event_organizer_text_view.setOnClickListener {
            startActivity(Intent(this, eoLogin::class.java))
            finish()
        }


        login_button.setOnClickListener {
                loginUser()



        }


    }




    private fun loginUser(){

        val emailTextView = findViewById<EditText>(R.id.et_email)
        val passwordTextView = findViewById<EditText>(R.id.et_password)

        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()

        when {
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
                val progressDialog = ProgressDialog(this)
                //set the title of the progress dialog "sign up" by implementing the setTitle() function


                progressDialog.setTitle("Login")
                // set "hold up bruda" message to progress bar by implementing the setMessage function
                progressDialog.setMessage("Hold up bruda...")
                // make removing of progress bar by touching on the outside false by using
                progressDialog.setCanceledOnTouchOutside(false)

                //show the progress dialog
                progressDialog.show()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        progressDialog.dismiss()

                        //send user to main application

                        val intent = Intent(this, egNavigation::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }

                    else {
                        val message = it.exception!!.toString()
                        Toast.makeText(this, "Error: @message", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }

                }



            }

        }




    }



    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null)
        {
            val intent = Intent(this, egNavigation::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }


}