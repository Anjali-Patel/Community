package com.gss.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gss.community.Activities.EventsDetailActivity;
import com.gss.community.Activities.NotificationDetailActivity;
import com.gss.community.Model.EventListModal;
import com.gss.community.Model.HorizontalModel;
import com.gss.community.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class HorizontalAdapter extends  RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {
private final Context context;
private ArrayList<HorizontalModel> person = new ArrayList<>();

    public HorizontalAdapter(NotificationDetailActivity context, ArrayList<HorizontalModel> person) {

        this.context=context;
        this.person = person;
    }


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class MyViewHolder extends RecyclerView.ViewHolder {
    Context context;
    // each data item is just a string in this case
    public ImageView  image;

    public MyViewHolder(View v) {
        super(v);
        image = v.findViewById(R.id.image);

//            time = v.findViewById(R.id.tv3);



    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public HorizontalAdapter(ArrayList<HorizontalModel> person, Context context) {
        this.context=context;
        this.person = person;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_adapter, parent, false);

        HorizontalAdapter.MyViewHolder vh = new HorizontalAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HorizontalAdapter.MyViewHolder holder, final int position) {
//
//        holder.image.setImageResource(person.get(position).getImage());
//        holder.date.setText(person.get(position).getDate());
        //        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context.getApplicationContext(), EventsDetailActivity.class);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//
//                intent.putExtra("id", person.get(position).getId());
//                context.startActivity(intent);
//
//
//            }
//
//        });

    }

    @Override
    public int getItemCount() {
        return person.size();
    }
}





