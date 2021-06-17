package Fragments

import Activities.egAccountSettings
import Activities.egDisplayEvents
import Adapters.HomeFeedAdapter
import Models.EventGoer
import Models.Events
import ValueEventListenerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*


class HomeFragment : Fragment(), HomeFeedAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeView= inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment


        homeView.account_set_image.setOnClickListener {
            val intent = Intent(context, egAccountSettings::class.java)
            startActivity(intent)
        }

        val uid = FirebaseAuth.getInstance().currentUser!!.uid


        FirebaseDatabase.getInstance().getReference("EventGoers/$uid").addValueEventListener(ValueEventListenerAdapter{
            val userName = it.getValue(EventGoer::class.java)?.name
            homeView.username_text.text = "Hey ${userName?.capitalize()}"
        })


        val databaseReference = FirebaseDatabase.getInstance().getReference("EventGoers")





        FirebaseDatabase.getInstance().getReference("events/$uid").addValueEventListener(
            ValueEventListenerAdapter{
                val events = it.children.map { it.getValue(Events::class.java)!! }

                //val (personalRecipe, otherUserRecipes) = recipes.partition { it.uid == uid }

                databaseReference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val mUser =  it.getValue(EventGoer::class.java)!!

                    //mUsers = it.children.map {it.getValue(User::class.java)!!}
                    //homeView.username_text?.setText(mUser.name, TextView.BufferType.EDITABLE)
                    //  homeView.recipe_chef.text = mUsers[]
                    homeView.home_frag_recycler.layoutManager = LinearLayoutManager(context)
                    homeView.home_frag_recycler.setHasFixedSize(true)
                    homeView.home_frag_recycler.adapter =
                        context?.let { it1 -> HomeFeedAdapter(it1, events, this) }


                })

            })






        return homeView

    }


    override fun onItemClick(position: Int, events: List<Events>) {
        Toast.makeText(context, events[position].eventTitle, Toast.LENGTH_SHORT).show()



        val intent = Intent(context, egDisplayEvents::class.java)
        intent.putExtra("event_title", events[position].eventTitle)
        intent.putExtra("event_date_time", events[position].eventDateTime)
        intent.putExtra("event_location", events[position].eventLocation)
        intent.putExtra("event_description", events[position].eventDescription)
        intent.putExtra("event_price", events[position].eventPrice)
        intent.putExtra("event_image", events[position].eventImageString)


        //intent.putExtra("recipe_serving_size", recipes[position].)


        // intent.putExtra("recipe_creator", mUser[position].username)

        startActivity(intent)


        // set user profile image on image view
        //get a list of the recipe uid strings


        //val clickedItem = personalRecipe[position]




    }


}