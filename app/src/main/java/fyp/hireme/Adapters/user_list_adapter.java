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

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import fyp.hireme.Filters.user_filter;
import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.user;
import fyp.hireme.R;

public class user_list_adapter extends RecyclerView.Adapter<user_list_adapter.user_list_viewholder> implements Filterable {
    public user_list_adapter(ArrayList<user> users, ArrayList<String> usersId, Context context, Fragment fragment) {
        this.users = users;
        this.usersId = usersId;
        this.context = context;
        this.filteredList=users;
        this.fragment=fragment;
    }
  public  ArrayList<user> users;
ArrayList<String> usersId;
    ArrayList<user> filteredList;
Context context;
Fragment fragment;
user_filter filter;
    @NonNull
    @Override
    public user_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new user_list_viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull user_list_viewholder holder, int position) {
         holder.email.setText(users.get(position).getEmail());
        holder.role.setText(users.get(position).getRole());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.user_popup,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.view_profile){
                            firebase_operations.getProfile(context,usersId.get(position),users.get(position).getRole());
                        }else if(item.getItemId()==R.id.block_user){
                            new AlertDialog.Builder(context)
                                    .setTitle("Delete User")
                                    .setMessage("Are you sure you want to delete this user ?")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            firebase_operations.deleteUsers(context, usersId.get(position),fragment);
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
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
        return users.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new user_filter(filteredList,this);
        }
        return filter;
    }

    class user_list_viewholder extends RecyclerView.ViewHolder{
        TextView email,role;
        CardView card;
        public user_list_viewholder(@NonNull View itemView) {
            super(itemView);
            email=itemView.findViewById(R.id.email);
            role=itemView.findViewById(R.id.role);
            card=itemView.findViewById(R.id.card);
        }
    }
}
