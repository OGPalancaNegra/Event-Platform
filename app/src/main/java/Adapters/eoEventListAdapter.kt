package Adapters

import Models.EventOrganizers
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

class eoEventListAdapter(val context: Context,
                            val mEvents:List<Events>,
                            val mUser: EventOrganizers,
                            private val listener: OnItemClickListener):
    RecyclerView.Adapter<eoEventListAdapter.CustomViewHolder>() {


    // create a custom view holder class to supply adapter

    inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val titleTextView = view.findViewById<TextView>(R.id.event_title)
        val eventPrice = view.findViewById<TextView>(R.id.event_price)
        val eventLocation = view.findViewById<TextView>(R.id.event_location)
        val date_timeTextView = view.findViewById<TextView>(R.id.event_date_time)
        val eventImage = view.findViewById<ImageView>(R.id.event_image)

        init {
            view.setOnClickListener(this)

        }

        fun bind (events: Events, context: Context, user: EventOrganizers) {

            titleTextView?.text = events.eventTitle
            eventPrice?.text = "£${events.eventPrice}"
            Picasso.get().load(events.eventImageString).placeholder(R.drawable.ic_round_android_24).fit().centerCrop().into(eventImage)
            eventLocation?.text = events.eventLocation
            date_timeTextView?.text = events.eventDateTime

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
        val cellForRow = layoutInflater.inflate(R.layout.eo_event, parent, false)

        return CustomViewHolder(cellForRow)

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder?.bind(mEvents[position], context, mUser)

    }




}