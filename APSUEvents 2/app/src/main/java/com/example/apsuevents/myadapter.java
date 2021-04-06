package com.example.apsuevents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<User, myadapter.myviewholder>
{
    public myadapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull User User)
    {

        holder.name.setText(User.getName());
        holder.type.setText(User.getEvent());
        holder.date.setText(User.getDate());
        holder.curCap.setText(User.getCurCapacity());
        holder.cap.setText("/ "+User.getCapacity());
        if(User.getPrivacy().equals("Closed")) {
            holder.privFlag = 1;
        }
        if(holder.privFlag == 0){
            holder.priv_img.setBackgroundResource(R.drawable.unlock);
        }
        else{
            holder.priv_img.setBackgroundResource(R.drawable.lock);
        }
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        int privFlag = 0;
        TextView name,type,date,cap,curCap;
        ImageView priv_img;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nametext);
            type=(TextView)itemView.findViewById(R.id.coursetext);
            date=(TextView)itemView.findViewById(R.id.datetext);
            cap=(TextView)itemView.findViewById(R.id.captext);
            curCap=(TextView)itemView.findViewById(R.id.curCap);
            priv_img=(ImageView)itemView.findViewById(R.id.privimg);
        }
    }
}

