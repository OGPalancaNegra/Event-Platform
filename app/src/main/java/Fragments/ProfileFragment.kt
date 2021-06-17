package Fragments

import Activities.egAccountSettings
import Adapters.AccountSettingsAdapter
import Models.EventGoer
import ValueEventListenerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*


class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        // Inflate the layout for this fragment

        view.account_set_image.setOnClickListener {
            val intent = Intent(context, egAccountSettings::class.java)
            startActivity(intent)
        }


        //display following and order count as well as user imaeb

        val uid = FirebaseAuth.getInstance().currentUser!!.uid


        //


        FirebaseDatabase.getInstance().getReference("EventGoers/$uid").addListenerForSingleValueEvent(ValueEventListenerAdapter{
            val eventGoer = it.getValue(EventGoer::class.java)
            view.username_text.text = "Hey ${eventGoer?.name?.capitalize()}"

            //Picasso.get().load(eventGoer?.imageString)?.into(view?.circleImageView)
            view?.circleImageView?.let { it1 ->
                Glide.with(this).load(eventGoer?.imageString).placeholder(R.drawable.default_profile_image).centerCrop().into(
                    it1
                )
            }

            view.user_following_count.text = eventGoer?.follows?.count().toString()

            FirebaseDatabase.getInstance().getReference("orders/$uid").addListenerForSingleValueEvent(ValueEventListenerAdapter{
                view.user_order_count.text = it.children.count().toString()

                view.ticket_count.text = it.children.count().toString()


            })





        })


        // display fragments in list view

        val listView = view.list_view

        val options = ArrayList<String>()

        //options.add("Following")
        options.add("Orders")

        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, options) }

        listView.adapter = adapter

        val viewPager = view.viewPager


        listView.setOnItemClickListener { adapterView, view, position, l ->

            setViewPager(position)
        }


        setUpFragment()


        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun setUpFragment() {
        val sectionStatePagerAdapter = AccountSettingsAdapter(childFragmentManager)
        /*sectionStatePagerAdapter.addFragment(
            FollowingFragment(),
            "Following Fragment"
        )*/

        sectionStatePagerAdapter.addFragment(
            OrdersFragment(),
            "Orders Fragment"
        )




    }

    fun setViewPager(fragNum: Int){
        val sectionStatePagerAdapter = AccountSettingsAdapter(childFragmentManager)
        /*sectionStatePagerAdapter.addFragment(
            FollowingFragment(),
            "Following Fragment"
        )*/

        sectionStatePagerAdapter.addFragment(
            OrdersFragment(),
            "Orders Fragment"
        )

        content_layout.visibility = View.GONE
        viewPager.adapter = sectionStatePagerAdapter
        viewPager.currentItem = fragNum
        viewPager.isSaveEnabled = false




    }





}