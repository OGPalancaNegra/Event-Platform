package Fragments

import Adapters.OrderFeedAdapter
import Models.Orders
import ValueEventListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventplatform.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_orders.view.*
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*

class OrdersFragment : Fragment(), OrderFeedAdapter.OnItemClickListener {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_orders, container, false)


        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        //AppBar.visibility = View.GONE

        FirebaseDatabase.getInstance().getReference("orders/$uid").addValueEventListener(
            ValueEventListenerAdapter{
                val orders = it.children.map { it.getValue(Orders::class.java)!! }

                //val (personalRecipe, otherUserRecipes) = recipes.partition { it.uid == uid }

                    view.order_frag_recycler.layoutManager = LinearLayoutManager(context)
                    view.order_frag_recycler.setHasFixedSize(true)
                    view.order_frag_recycler.adapter =
                        context?.let { it1 -> OrderFeedAdapter(it1, orders, this) }

            })


        return view
    }


}
