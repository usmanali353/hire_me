package fyp.hireme.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.project;
import fyp.hireme.Model.user;
import fyp.hireme.R;
import fyp.hireme.Utils.utils;

public class projects_list_adapter extends RecyclerView.Adapter<projects_list_adapter.project_list_viewholder> {
    ArrayList<project> projects;
    Context context;
    ArrayList<String> projectIds;
    SharedPreferences prefs;
    user u;
    public projects_list_adapter(ArrayList<project> projects, Context context, ArrayList<String> projectIds) {
        this.projects = projects;
        this.context = context;
        this.projectIds = projectIds;
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
        u=new Gson().fromJson(prefs.getString("user_info",null),user.class);
    }

    @NonNull
    @Override
    public project_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new project_list_viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull project_list_viewholder holder, int position) {
      holder.title.setText(projects.get(position).getTitle());
      holder.description.setText(projects.get(position).getDescription());
      Picasso.get().load(projects.get(position).getImage()).into(holder.projectImage);
      holder.card.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if(u.getRole().equals("Worker")&&projects.get(position).getStatus().equals("New Project")){
                View place_bid_view=LayoutInflater.from(context).inflate(R.layout.place_bid_layout,null);
                MaterialEditText price=place_bid_view.findViewById(R.id.bid_price);
                AlertDialog place_bid_dialog=new AlertDialog.Builder(context)
                        .setTitle("Place Bid")
                        .setMessage("Enter Price for Bid")
                        .setPositiveButton("set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setView(place_bid_view).create();
                place_bid_dialog.show();
                place_bid_dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(price.getText().toString().isEmpty()){
                            price.setError("Enter Bid Price");
                        }else if(Integer.parseInt(price.getText().toString())==0&&Integer.parseInt(price.getText().toString())<1000){
                            price.setError("Bid Price too Low");
                        }else{

                            firebase_operations.AddBid(context,u.getName(), utils.getCurrentDate(),projectIds.get(position),Integer.parseInt(price.getText().toString()),"New Bid",place_bid_dialog);
                        }
                    }
                });

            }
          }
      });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class project_list_viewholder extends RecyclerView.ViewHolder{
  TextView title,description;
  ImageView projectImage;
  CardView card;
     public project_list_viewholder(@NonNull View itemView) {
         super(itemView);
         title=itemView.findViewById(R.id.title);
         description=itemView.findViewById(R.id.description);
         projectImage=itemView.findViewById(R.id.icon);
         card=itemView.findViewById(R.id.card);
     }
 }
}
