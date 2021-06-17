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
import kotlinx.android.synthetic.main.activity_eg_display_events.*
import kotlinx.android.synthetic.main.activity_eo_account_settings.*
import kotlinx.android.synthetic.main.activity_eo_display_events.*
import kotlinx.android.synthetic.main.activity_eo_display_events.create_event_back_icon
import org.w3c.dom.Text

class egDisplayEvents: AppCompatActivity() {

    private lateinit var mEvents: Events
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eg_display_events)

        create_event_back_icon.setOnClickListener {
            finish()
        }

        //val intentTing= intent.getParcelableExtra<Adapter>(ProfileFragment.INTENT_PARCELABLE)

        val eventImage = findViewById<ImageView>(R.id.event_image_view)

        //Picasso.get().load(R.drawable.ic_round_android_24).placeholder(R.drawable.ic_round_android_24).fit().centerCrop().into(recipeImage)

        val eventTitle = findViewById<TextView>(R.id.event_title_text_view)

        val eventDescription = findViewById<TextView>(R.id.event_description_text_view)
        val eventLocation = findViewById<TextView>(R.id.event_location_text_view)
        val eventDateTime = findViewById<TextView>(R.id.event_date_time_text_view)
        val eventPrice = findViewById<TextView>(R.id.event_price_text_view)


        //recipeTitle.text = intentTing.
        val eventTitleString = intent.getStringExtra("event_title")
        val eventDescriptionString = intent.getStringExtra("event_description")
        val eventLocationString = intent.getStringExtra("event_location")
        val eventPriceString = intent.getStringExtra("event_price")
        val eventImageString = intent.getStringExtra("event_image")
        val eventDateTimeString = intent.getStringExtra("event_date_time")


        val eventCreatorString = intent.getStringExtra("recipe_creator")

        eventTitle.text = eventTitleString
        eventDescription.text= eventDescriptionString
        eventLocation.text= eventLocationString
        eventPrice.text= "£$eventPriceString"
        Picasso.get().load(eventImageString).fit().centerCrop().into(eventImage)
        eventDateTime.text = eventDateTimeString

        //recipeCreator.text=recipeCreatorString



        buy_ticket_button.setOnClickListener {

            val intent = Intent(this, checkOutActivity::class.java)
            intent.putExtra("event_title", eventTitle.text as String?)
            intent.putExtra("event_date_time", eventDateTime.text as String?)
            intent.putExtra("event_location", eventLocationString)
            intent.putExtra("event_description", eventDescription.text as String?)
            intent.putExtra("event_price", eventPrice.text as String?)
            intent.putExtra("event_image", eventImageString)
            startActivity(intent)
        }

    }






}