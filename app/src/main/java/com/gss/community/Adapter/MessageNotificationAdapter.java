package com.gss.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gss.community.Activities.MessageNotificationDetailActivity;
import com.gss.community.Model.MessageNotificationModal;
import com.gss.community.Model.NotificationModal;
import com.gss.community.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MessageNotificationAdapter extends RecyclerView.Adapter<MessageNotificationAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<MessageNotificationModal> person = new ArrayList<>();



    public MessageNotificationAdapter(FragmentActivity activity, ArrayList<MessageNotificationModal> person1) {
        this.context=activity;
        this.person=person1;

    }

//    public FindPersonAdapter(FindPersonActivity findPersonActivity, ArrayList<PersonModal> memberDetailsArrayListTmp) {
//    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Context context;
        // each data item is just a string in this case
        public TextView title,date;

        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            date=v.findViewById(R.id.date);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
//    public MessageNotificationAdapter(Context context, ArrayList<MessageNotificationModal> person) {
//        this.context=context;
//        this.person = person;
//    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageNotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_view, parent, false);

        MessageNotificationAdapter.MyViewHolder vh = new MessageNotificationAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageNotificationAdapter.MyViewHolder holder, final int i) {
        holder.title.setText(person.get(i).getTitle());
        holder.date.setText(person.get(i).getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MessageNotificationDetailActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("notification_type", person.get(i).getType());

                intent.putExtra("id", person.get(i).getId());
                context.startActivity(intent);


            }

        });

    }



    @Override
    public int getItemCount() {
        return person.size();
    }
}





