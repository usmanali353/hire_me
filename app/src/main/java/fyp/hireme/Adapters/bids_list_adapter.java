package fyp.hireme.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.Bid;
import fyp.hireme.Model.user;
import fyp.hireme.R;

public class bids_list_adapter extends RecyclerView.Adapter<bids_list_adapter.bids_list_viewholder> {
    Context context;
    ArrayList<Bid> bids;
    ArrayList<String> bidId;
    SharedPreferences prefs;
    user u;
    String projectId,projectStatus;
    public bids_list_adapter(Context context, ArrayList<Bid> bids, ArrayList<String> bidId,String projectId,String projectStatus) {
        this.context = context;
        this.bids = bids;
        this.bidId = bidId;
        this.projectId=projectId;
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
        this.projectStatus=projectStatus;
        u=new Gson().fromJson(prefs.getString("user_info",null),user.class);
    }

    @NonNull
    @Override
    public bids_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new bids_list_viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bids_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull bids_list_viewholder holder, int position) {
        holder.mechanicName.setText(bids.get(position).getMechanic_name());
        holder.bidDate.setText("Date "+bids.get(position).getBid_date());
        holder.bidStatus.setText("Status "+bids.get(position).getStatus());
        holder.bidPrice.setText("Rs "+String.valueOf(bids.get(position).getBid_price()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u.getRole().equals("Customer")&&bids.get(position).getStatus().equals("New Bid")&&projectStatus.equals("New Project")){
                    new AlertDialog.Builder(context)
                            .setTitle("Accept Bid")
                            .setMessage("are you sure you want to accept this bid ?")
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebase_operations.acceptBid(context,bidId.get(position),projectId,bids.get(position).getMechanicId());
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bids.size();
    }

    class bids_list_viewholder extends RecyclerView.ViewHolder{
      TextView mechanicName,bidPrice,bidStatus,bidDate;
      CardView card;
        public bids_list_viewholder(@NonNull View itemView) {
            super(itemView);
            mechanicName=itemView.findViewById(R.id.mechanic_name);
            bidPrice=itemView.findViewById(R.id.price);
            bidStatus=itemView.findViewById(R.id.status);
            bidDate=itemView.findViewById(R.id.date);
            card=itemView.findViewById(R.id.card);
        }
    }
}
