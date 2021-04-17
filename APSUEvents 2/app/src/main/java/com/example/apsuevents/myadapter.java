package com.example.apsuevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class myadapter extends FirebaseRecyclerAdapter<User, myadapter.myviewholder>
{
    final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
    final String cur_email = user1.getEmail();
    public myadapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final User User) {
        if(cur_email.equals(User.getEventHost()))
            holder.rel_id.setVisibility(View.GONE);
        else if (User.getCurCapacity().equals(User.getCapacity()))
            holder.rel_id.setVisibility(View.GONE);
        else {
            holder.name.setText(User.getName());
            holder.type.setText(User.getEvent());
            holder.date.setText(User.getDate());
            holder.curCap.setText(User.getCurCapacity());
            holder.cap.setText("/ " + User.getCapacity());
            if (User.getPrivacy().equals("Closed")) {
                holder.privFlag = 1;
            }
            if (holder.privFlag == 0) {
                holder.priv_img.setBackgroundResource(R.drawable.unlock);
            } else {
                holder.priv_img.setBackgroundResource(R.drawable.lock);
            }
            holder.rel_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openevent(v, User);
                }
            });
        }
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    static class myviewholder extends RecyclerView.ViewHolder
    {
        int privFlag = 0;
        TextView name,type,date,cap,curCap;
        ImageView priv_img;
        RelativeLayout rel_id;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nametext);
            type=(TextView)itemView.findViewById(R.id.coursetext);
            date=(TextView)itemView.findViewById(R.id.datetext);
            cap=(TextView)itemView.findViewById(R.id.captext);
            curCap=(TextView)itemView.findViewById(R.id.curCap);
            priv_img=(ImageView)itemView.findViewById(R.id.privimg);
            rel_id=(RelativeLayout)itemView.findViewById(R.id.rel_id);
        }
    }

    public void openevent(View view, User user) {
        Intent open = new Intent(view.getContext(), EventPage.class);
        open.putExtra("Title",user.name);
        open.putExtra("CurCap", user.cur_cap);
        open.putExtra("Capacity", user.capacity);
        open.putExtra("Date", user.date);
        open.putExtra("Time", user.time);
        open.putExtra("Desc", user.description);
        open.putExtra("CurCap", user.cur_cap);
        open.putExtra("Priv", user.privacy);
        open.putExtra("Pswd", user.password);
        open.putExtra("Place", user.coordinates);
        open.putExtra("Attendance", user.attendee);
        open.putExtra("Call", user.contactHost);
        view.getContext().startActivity(open);
    }



}

