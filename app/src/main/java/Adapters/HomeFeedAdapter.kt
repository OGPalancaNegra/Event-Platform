package Adapters

import Models.Events
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventplatform.R
import com.squareup.picasso.Picasso

class HomeFeedAdapter(val context: Context, val mEvents:List<Events>, private val listener: OnItemClickListener): RecyclerView.Adapter<HomeFeedAdapter.CustomViewHolder>() {



    inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val eventTitle = view.findViewById<TextView>(R.id.event_title)
        val eventDateTime = view.findViewById<TextView>(R.id.event_date_time)
        val eventLocation = view.findViewById<TextView>(R.id.event_location)
        val eventImage = view.findViewById<ImageView>(R.id.event_image)
        val eventPrice = view.findViewById<TextView>(R.id.event_price)


        init {
            view.setOnClickListener(this)

        }

        fun bind (events: Events, context: Context) {

            eventTitle?.text = events.eventTitle
            eventDateTime?.text = events.eventDateTime
            Picasso.get().load(events.eventImageString).placeholder(R.drawable.ic_round_android_24).fit().centerCrop().into(eventImage)
            eventLocation?.text = events.eventLocation
            eventPrice?.text = "£"+ events.eventPrice


        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            // recyclerView.No_Poistion is a constant of -1 ...this line assures position is not invalid
            if (position != RecyclerView.NO_POSITION) {
                //val recipes: Recipes
                listener.onItemClick(position, mEvents)
            }

        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, events: List<Events>){

        }
    }

    override fun getItemCount() = mEvents.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //create a view

        //LayoutInflater= Instantiates a layout XML file into its corresponding View objects.

        val layoutInflater =  LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.home_feed, parent, false)

        return CustomViewHolder(cellForRow)

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder?.bind(mEvents[position], context)

    }




}