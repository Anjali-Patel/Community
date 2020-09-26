package com.gss.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gss.community.Activities.PersonDetailActivity;
import com.gss.community.Model.PersonModal;
import com.gss.community.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FindPersonAdapter  extends RecyclerView.Adapter<FindPersonAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<PersonModal> person = new ArrayList<>();

//    public FindPersonAdapter(FindPersonActivity findPersonActivity, ArrayList<PersonModal> memberDetailsArrayListTmp) {
//    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Context context;
        // each data item is just a string in this case
        public TextView name;
        public ImageView img;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            img = v.findViewById(R.id.img);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FindPersonAdapter(Context context,ArrayList<PersonModal> person) {
        this.context=context;
        this.person = person;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FindPersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.find_person, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(person.get(position).getName());
        Glide.with(context).load("http://demo1.geniesoftsystem.com/newweb/community/uploads/images/"+person.get(position).getImg()).error(R.drawable.profile).into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), PersonDetailActivity.class);
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




