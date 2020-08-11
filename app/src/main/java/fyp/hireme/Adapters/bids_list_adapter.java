package fyp.hireme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fyp.hireme.Model.Bid;
import fyp.hireme.R;

public class bids_list_adapter extends RecyclerView.Adapter<bids_list_adapter.bids_list_viewholder> {
    Context context;
    ArrayList<Bid> bids;
    ArrayList<String> bidId;

    public bids_list_adapter(Context context, ArrayList<Bid> bids, ArrayList<String> bidId) {
        this.context = context;
        this.bids = bids;
        this.bidId = bidId;
    }

    @NonNull
    @Override
    public bids_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new bids_list_viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bids_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull bids_list_viewholder holder, int position) {
        holder.mechanicName.setText(bids.get(position).getMechanic_name());
        holder.bidDate.setText(bids.get(position).getBid_date());
        holder.bidStatus.setText(bids.get(position).getStatus());
        holder.bidPrice.setText(String.valueOf(bids.get(position).getBid_price()));
    }

    @Override
    public int getItemCount() {
        return bids.size();
    }

    class bids_list_viewholder extends RecyclerView.ViewHolder{
      TextView mechanicName,bidPrice,bidStatus,bidDate;
        public bids_list_viewholder(@NonNull View itemView) {
            super(itemView);
            mechanicName=itemView.findViewById(R.id.mechanic_name);
            bidPrice=itemView.findViewById(R.id.bid_price);
            bidStatus=itemView.findViewById(R.id.status);
            bidDate=itemView.findViewById(R.id.date);
        }
    }
}
