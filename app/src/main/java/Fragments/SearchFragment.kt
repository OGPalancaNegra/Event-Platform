package Fragments

import Adapters.SearchAdapter
import Models.EventGoer
import Models.EventOrganizers
import ValueEventListenerAdapter
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventplatform.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*


class SearchFragment : Fragment(), SearchAdapter.Listener {
    private lateinit var mEventGoer: EventGoer
    private lateinit var mUsers: List<EventOrganizers>
    private lateinit var mFriendAdapter: SearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)


        val uid = FirebaseAuth.getInstance().currentUser!!.uid


        FirebaseDatabase.getInstance().getReference("EventGoers/$uid").addValueEventListener(ValueEventListenerAdapter{
            val userName = it.getValue(EventGoer::class.java)?.name
            view.username_text.text = "Hey ${userName?.capitalize()}"
        })


        mFriendAdapter = context?.let { SearchAdapter(this ,it,true) }!!

        view.search_recycler_view_2.adapter = mFriendAdapter
        view.search_recycler_view_2.layoutManager = LinearLayoutManager(context)


        val usersRef = FirebaseDatabase.getInstance().getReference("EventOrganizers").addValueEventListener(ValueEventListenerAdapter{
            val allEventOrganizers= it.children.map { it.getValue(EventOrganizers::class.java)!!.copy(uid = it.key!!) }
            val (userList, otherUsersList) = allEventOrganizers.partition { it.uid == uid }

            //get event goer to update follows node ting


            FirebaseDatabase.getInstance().getReference("EventGoers/$uid").addValueEventListener(ValueEventListenerAdapter{

                mEventGoer = it.getValue(EventGoer::class.java)!!
                mFriendAdapter.update(allEventOrganizers, mEventGoer.follows)

            })


           // mUser = userList.first()
         //   mUsers = otherUsersList

            //mFriendAdapter.update(allEventOrganizers, mUser.followers)

        })







        return view

    }

    override fun follow(uid: String) {
        setFollow (uid, true) {
            mFriendAdapter.followed(uid)
        }


    }

    override fun unfollow(uid: String) {
        setFollow(uid, false){
            mFriendAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: ()-> Unit) {
        val mUserUID = mEventGoer.uid

        //fun DatabaseReference.setValueTrueOrRemove(value: Boolean) =
      //      if (value) setValue(true) else removeValue()


        val followTask = FirebaseDatabase.getInstance().getReference("EventGoers/$mUserUID/follows/$uid")
        val setFollow = if (follow) followTask.setValue(true) else followTask.removeValue()

        val followerTask =
            FirebaseDatabase.getInstance().getReference("EventOrganizers/$uid/followers/$mUserUID")

        val setFollower = if (follow) followerTask.setValue(true) else followerTask.removeValue()


        fun <T> task(block: (TaskCompletionSource<T>) -> Unit): Task<T> {
            val taskSource = TaskCompletionSource<T>()
            block(taskSource)
            return taskSource.task
        }

        val eventFeedTasks= task<Void> {taskSource ->

            FirebaseDatabase.getInstance().getReference("events/$uid")
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val recipesMap = if (follow) {
                        it.children.map { it.key to it.value }.toMap()

                    } else {
                        it.children.map { it.key to null }.toMap()
                    }
                    FirebaseDatabase.getInstance().getReference("events/$mUserUID")
                        .updateChildren(recipesMap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                taskSource.setResult(it.result)
                            } else {
                                taskSource.setException(it.exception!!)

                            }
                        }

                })
        }
        Tasks.whenAll(eventFeedTasks)
        setFollow.continueWithTask({ setFollower }).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                Toast.makeText(context, "error", Toast.LENGTH_LONG)

            }
        }

    }






}