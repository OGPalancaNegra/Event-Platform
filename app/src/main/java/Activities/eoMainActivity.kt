package Activities

import Adapters.eoEventListAdapter
import Models.EventOrganizers
import Models.Events
import ValueEventListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventplatform.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_eo_main_activity.*

class eoMainActivity: AppCompatActivity(), eoEventListAdapter.OnItemClickListener {

    private lateinit var mUser: EventOrganizers


    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eo_main_activity)

        // open once user eo account settings once user clicks on menu ting

        val accountSetMenu = findViewById<ImageView>(R.id.account_set_image)
        accountSetMenu.setOnClickListener {
            val intent = Intent(this, eoAccountSettings::class.java)
            startActivity(intent)
        }

        // create events

        val createEventButton = findViewById<FloatingActionButton>(R.id.createEventsButton)
        createEventButton.setOnClickListener {
            val intent = Intent(this, CreateEvents::class.java)
            startActivity(intent)
        }


        // display number of events, followers and tickets sold
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().getReference("EventOrganizers/$uid/followers").addListenerForSingleValueEvent(ValueEventListenerAdapter{
            val followersCount = it.childrenCount
            val followerCountTxtView = findViewById<TextView>(R.id.user_follower_count)
            followerCountTxtView.text = followersCount.toString()

        })


        //display event organzier profile picture

        val databaseReference = FirebaseDatabase.getInstance().getReference("EventOrganizers/$uid")

        databaseReference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
            mUser = it.getValue(EventOrganizers::class.java)!!
            val usernameTxtView = findViewById<TextView>(R.id.username_text)
            usernameTxtView.setText(mUser.name, TextView.BufferType.EDITABLE)
            if (mUser.imageString?.isEmpty()!!) {
                val circularImageView = findViewById<ImageView>(R.id.circleImageView)
                circularImageView.setImageResource(R.drawable.default_profile_image);
            } else{
                Picasso.get().load(mUser.imageString).into(circleImageView)
            }

        })




        // display list of events and number of events
        FirebaseDatabase.getInstance().getReference("events/$uid").addValueEventListener(
            ValueEventListenerAdapter{
                val events = it.children.map { it.getValue(Events::class.java)!! }

                //val (personalRecipe, otherUserRecipes) = .partition { it.uid == uid }


                //get users

                databaseReference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    mUser = it.getValue(EventOrganizers::class.java)!!
                    val eventsCount = events.count()
                    val eventCountTxt = findViewById<TextView>(R.id.user_recipe_count)
                    eventCountTxt.text = eventsCount.toString()

                    val nameTxtView = findViewById<TextView>(R.id.username_text)
                    nameTxtView.setText(mUser.name, TextView.BufferType.EDITABLE)
                    recyclerView = findViewById<RecyclerView>(R.id.eo_recycler_view)
                    recyclerView.layoutManager= LinearLayoutManager(this)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter =
                        this?.let { it1 -> eoEventListAdapter(it1, events, mUser, this) }


                })



            })




    }

    override fun onItemClick(position: Int, events: List<Events>) {
        Toast.makeText(this, events[position].eventTitle, Toast.LENGTH_SHORT).show()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid


        val intent = Intent(this, eoDisplayEvents::class.java)
        intent.putExtra("event_title", events[position].eventTitle)
        intent.putExtra("event_price", events[position].eventPrice)
        intent.putExtra("event_location", events[position].eventLocation)
        intent.putExtra("event_date_time", events[position].eventDateTime)
        intent.putExtra("event_image", events[position].eventImageString)
        intent.putExtra("event_description", events[position].eventDescription)
        startActivity(intent)


        // set user profile image on image view
        //get a list of the recipe uid strings


        //val clickedItem = personalRecipe[position]




    }





}