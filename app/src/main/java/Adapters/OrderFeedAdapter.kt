package Adapters

import Models.Events
import Models.Orders
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventplatform.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class OrderFeedAdapter(val context: Context, val mOrders:List<Orders>, private val listener: OnItemClickListener): RecyclerView.Adapter<OrderFeedAdapter.CustomViewHolder>() {



    inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val orderTitle = view.findViewById<TextView>(R.id.order_event_name)
        val orderTime = view.findViewById<TextView>(R.id.order_date)
        val orderLocation = view.findViewById<TextView>(R.id.order_event_location)
        val orderTimeDate = view.findViewById<TextView>(R.id.order_event_date_time)


        init {
            view.setOnClickListener(this)

        }

        fun bind (orders: Orders, context: Context) {



            val dateFormat = "dd MMM yyyy HH:mm"
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            val calender: Calendar = Calendar.getInstance()
            calender.timeInMillis = orders.orderTime
            val orderDateTime = formatter.format(calender.time)

            orderTitle?.text = orders.title
             orderTime?.text = orderDateTime
            orderLocation?.text = orders.location
            orderTimeDate?.text = orders.datetime


        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            // recyclerView.No_Poistion is a constant of -1 ...this line assures position is not invalid
            if (position != RecyclerView.NO_POSITION) {
                //val recipes: Recipes
                listener.onItemClick(position, mOrders)
            }

        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, orders: List<Orders>){

        }
    }

    override fun getItemCount() = mOrders.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //create a view

        //LayoutInflater= Instantiates a layout XML file into its corresponding View objects.

        val layoutInflater =  LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.order_feed, parent, false)

        return CustomViewHolder(cellForRow)

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder?.bind(mOrders[position], context)

    }




}