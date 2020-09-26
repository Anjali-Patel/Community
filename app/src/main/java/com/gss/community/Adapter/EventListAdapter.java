package com.gss.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gss.community.Activities.EventsDetailActivity;
import com.gss.community.Model.EventListModal;
import com.gss.community.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class EventListAdapter  extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<EventListModal> person = new ArrayList<>();


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Context context;
        // each data item is just a string in this case
        public TextView event,date,time;

        public MyViewHolder(View v) {
            super(v);
            event = v.findViewById(R.id.tv_location);
            date = v.findViewById(R.id.tv_created_at);

//            time = v.findViewById(R.id.tv3);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventListAdapter(ArrayList<EventListModal> person, Context context) {
        this.context=context;
        this.person = person;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);

        EventListAdapter.MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventListAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.event.setText(person.get(position).getEvent());
        holder.date.setText(person.get(position).getDate());
//        holder.time.setText(person.get(position).getTime());

//        Picasso.with(context).load().into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), EventsDetailActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("id", person.get(position).getId());
                context.startActivity(intent);


            }

        });

    }

    @Override
    public int getItemCount() {
        return person.size();
    }
}





