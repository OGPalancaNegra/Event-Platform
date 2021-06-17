package Fragments

import Models.EventGoer
import ValueEventListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FollowingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_following, container, false)


        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        //display followings

        // to display following

        //event organizer uid = to uid from event goers follow ting display event ogarnzier







        return view
    }


}
