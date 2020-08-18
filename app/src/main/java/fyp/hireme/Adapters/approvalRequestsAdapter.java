package fyp.hireme.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fyp.hireme.Filters.requests_filter;
import fyp.hireme.Filters.user_filter;
import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.Requests;
import fyp.hireme.Model.user;
import fyp.hireme.R;

public class approvalRequestsAdapter extends RecyclerView.Adapter<approvalRequestsAdapter.approvalRequestsViewholder> implements Filterable {
    public approvalRequestsAdapter(ArrayList<Requests> requests, ArrayList<String> requestIds, Context context, Fragment fragment) {
        this.requests = requests;
        this.requestIds = requestIds;
        this.context = context;
        this.filteredList=requests;
        this.fragment=fragment;
    }
    public ArrayList<Requests> requests;
ArrayList<String> requestIds;
Context context;
Fragment fragment;
    ArrayList<Requests> filteredList;
    requests_filter filter;
    @NonNull
    @Override
    public approvalRequestsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new approvalRequestsViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull approvalRequestsViewholder holder, int position) {
      holder.email.setText(requests.get(position).getEmail());
      holder.role.setText(requests.get(position).getRole());
      holder.date.setText(requests.get(position).getDate());
      holder.status.setText(requests.get(position).getStatus());
      holder.card.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PopupMenu popup = new PopupMenu(context, v);
              popup.getMenuInflater().inflate(R.menu.requests_popup,popup.getMenu());
              popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      if(item.getItemId()==R.id.view_profile){
                          firebase_operations.getProfile(context,requests.get(position).getUserId(),requests.get(position).getRole());
                      }else if(item.getItemId()==R.id.approveRejectRequest){
                          if(requests.get(position).getStatus().equals("New Request")){
                              AlertDialog.Builder requestDialog=new AlertDialog.Builder(context);
                              requestDialog.setTitle("Approve/Reject Request");
                              requestDialog.setMessage("What you want to do with this Request");
                              requestDialog.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      firebase_operations.changeRequestStatus(context,requestIds.get(position),"Verified",requests.get(position).getUserId(),fragment);
                                  }
                              }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      dialog.dismiss();
                                  }
                              }).setNeutralButton("Reject", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      firebase_operations.changeRequestStatus(context,requestIds.get(position),"Rejected",requests.get(position).getUserId(),fragment);
                                  }
                              }).show();
                          }
                      }
                      return true;
                  }
              });
              popup.show();
          }
      });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new requests_filter(filteredList,this);
        }
        return filter;
    }
    class approvalRequestsViewholder extends RecyclerView.ViewHolder{
     TextView date,email,role,status;
     CardView card;
        public approvalRequestsViewholder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            email=itemView.findViewById(R.id.email);
            role=itemView.findViewById(R.id.role);
            status=itemView.findViewById(R.id.status);
            card=itemView.findViewById(R.id.card);
        }
    }
}
