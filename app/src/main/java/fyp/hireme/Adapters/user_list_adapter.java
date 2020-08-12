package fyp.hireme.Adapters;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.user;
import fyp.hireme.R;

public class user_list_adapter extends RecyclerView.Adapter<user_list_adapter.user_list_viewholder> {
    public user_list_adapter(ArrayList<user> users, ArrayList<String> usersId, Context context) {
        this.users = users;
        this.usersId = usersId;
        this.context = context;
    }
    ArrayList<user> users;
ArrayList<String> usersId;
Context context;
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
                new AlertDialog.Builder(context)
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete this user ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebase_operations.deleteUsers(context, usersId.get(position));
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
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
