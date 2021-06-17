package Activities

import Models.Events
import Models.Orders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_check_out.*

class checkOutActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        //store and display event info onto screen and activity
        val eventTitle = findViewById<TextView>(R.id.event_title)
        val eventImage = findViewById<ImageView>(R.id.event_image)
        val eventLocation = findViewById<TextView>(R.id.event_location)
        val eventDateTime = findViewById<TextView>(R.id.event_date_time)
        val eventPrice = findViewById<TextView>(R.id.event_price)


        //recipeTitle.text = intentTing.
        val eventTitleString = intent.getStringExtra("event_title")
        val eventDescriptionString = intent.getStringExtra("event_description")
        val eventLocationString = intent.getStringExtra("event_location")
        val eventPriceString = intent.getStringExtra("event_price")
        val eventImageString = intent.getStringExtra("event_image")
        val eventDateTimeString = intent.getStringExtra("event_date_time")

        eventTitle.text = eventTitleString
        eventLocation.text= eventLocationString
        eventPrice.text= "$eventPriceString"
        Picasso.get().load(eventImageString).fit().centerCrop().into(eventImage)
        eventDateTime.text = eventDateTimeString

        booking_total.text = "$eventPriceString"

        findViewById<Button>(R.id.checkout_button).setOnClickListener {
            createOrder()
        }







    }


    private fun createOrder() {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val order = Orders(
            System.currentTimeMillis(),
            uid,
            event_title.text.toString(),
            event_location.text.toString(),
            event_date_time.text.toString(),
            R.drawable.barcode.toString(),
            event_price.text.toString()
        )


        //store in firebase database


        FirebaseDatabase.getInstance().getReference("orders/$uid").push().setValue(order)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val intent = Intent(this, egNavigation::class.java)
                    startActivity(intent)
                    finish()
                    //take to user profile?



                } else {
                    Toast.makeText(this,"Order not created" , Toast.LENGTH_LONG)


                }
            }
    }

}
