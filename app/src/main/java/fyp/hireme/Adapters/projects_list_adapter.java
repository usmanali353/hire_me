package fyp.hireme.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.project;
import fyp.hireme.Model.user;
import fyp.hireme.R;
import fyp.hireme.Utils.utils;
import fyp.hireme.bidsList;
import fyp.hireme.dbhelper;

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
      holder.status.setText("Status "+projects.get(position).getStatus());
      holder.budget.setText("Budget: Rs "+projects.get(position).getBudget());
      Picasso.get().load(projects.get(position).getImage()).into(holder.projectImage);
      try{
          Geocoder geocoder = new Geocoder(context, Locale.getDefault());
          List<Address> addresses = geocoder.getFromLocation(projects.get(position).getLat(), projects.get(position).getLng(), 1);
          holder.location.setText(addresses.get(0).getLocality()+" - "+addresses.get(0).getCountryName());
      }catch(Exception e){
          e.printStackTrace();
          Log.e("exp",e.getMessage());
      }

      holder.card.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if(u.getRole().equals("Worker")&&projects.get(position).getStatus().equals("New Project")){
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.worker_popup,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.view_customer_profile){
                            firebase_operations.getProfile(context,projects.get(position).getCustomerId(),"Customer");
                        }else if(item.getItemId()==R.id.place_bid){
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
                                    }else if(Integer.parseInt(price.getText().toString())>projects.get(position).getBudget()){
                                        price.setError("Bid Price Should be Lower then Project Budget");
                                    }else{
                                        firebase_operations.checkBidsAlreadyExist(context,projectIds.get(position),FirebaseAuth.getInstance().getCurrentUser().getUid(),u.getName(),utils.getCurrentDate(),Integer.parseInt(price.getText().toString()),"New Bid",place_bid_dialog,projects.get(position).getTitle());
                                    }
                                }
                            });
                        }else if(item.getItemId()==R.id.fav_projects){
                           if(new dbhelper(context).check_if_already_exist(FirebaseAuth.getInstance().getCurrentUser().getUid(),projectIds.get(position))>0){
                               Toast.makeText(context,"Project Already in Favourates List",Toast.LENGTH_LONG).show();
                           }else{
                              if (new dbhelper(context).insert_projects(projectIds.get(position),projects.get(position).getImage(),projects.get(position).getTitle(),FirebaseAuth.getInstance().getCurrentUser().getUid(),projects.get(position).getDescription(),projects.get(position).getStatus(),String.valueOf(projects.get(position).getLat()),String.valueOf(projects.get(position).getLng()),projects.get(position).getRequiredService(),projects.get(position).getBudget())){
                                  Toast.makeText(context,"Project Added to favourates List",Toast.LENGTH_LONG).show();
                              }
                           }
                        }
                        return true;
                    }
                });
                popup.show();
            }else if(u.getRole().equals("Customer")&&projects.get(position).getStatus().equals("Allotted")){
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.customer_popup_rwp,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.view_worker_profile){
                            Log.e("allottedTo",projects.get(position).getAllottedTo());
                            firebase_operations.getProfile(context,projects.get(position).getAllottedTo(),"Worker");
                        }else if(item.getItemId()==R.id.rate){
                            View completeProjectView=LayoutInflater.from(context).inflate(R.layout.rate_work_layout,null);
                            AppCompatRatingBar rating=completeProjectView.findViewById(R.id.rating);
                            rating.setNumStars(5);
                            rating.setStepSize(1.0f);
                            MaterialEditText comments=completeProjectView.findViewById(R.id.comments);
                            AlertDialog completeProjectDialog= new AlertDialog.Builder(context)
                                    .setTitle("Rate the Work")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setView(completeProjectView).create();
                            completeProjectDialog.show();
                            completeProjectDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(rating.getRating()==0){
                                        Toast.makeText(context,"Please Rate",Toast.LENGTH_LONG).show();
                                    }else if(comments.getText().toString().isEmpty()){
                                        comments.setError("Please Comment on the work");
                                    }else{
                                        firebase_operations.rateComment(context,projectIds.get(position),String.valueOf(rating.getRating()),comments.getText().toString());
                                    }
                                }
                            });
                        }else if(item.getItemId()==R.id.fav_projects){
                            if(new dbhelper(context).check_if_already_exist(FirebaseAuth.getInstance().getCurrentUser().getUid(),projectIds.get(position))>0){
                                Toast.makeText(context,"Project Already in Favourates List",Toast.LENGTH_LONG).show();
                            }else{
                                if (new dbhelper(context).insert_projects(projectIds.get(position),projects.get(position).getImage(),projects.get(position).getTitle(),FirebaseAuth.getInstance().getCurrentUser().getUid(),projects.get(position).getDescription(),projects.get(position).getStatus(),String.valueOf(projects.get(position).getLat()),String.valueOf(projects.get(position).getLng()),projects.get(position).getRequiredService(),projects.get(position).getBudget())){
                                    Toast.makeText(context,"Project Added to favourates List",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        return true;
                    }
                });
                popup.show();


            }else if(u.getRole().equals("Worker")&&!projects.get(position).getStatus().equals("New Project")){
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.worker_popup_p,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.view_customer_profile){
                            firebase_operations.getProfile(context,projects.get(position).getCustomerId(),"Customer");
                        }else if(item.getItemId()==R.id.fav_projects){
                            if(new dbhelper(context).check_if_already_exist(FirebaseAuth.getInstance().getCurrentUser().getUid(),projectIds.get(position))>0){
                                Toast.makeText(context,"Project Already in Favourates List",Toast.LENGTH_LONG).show();
                            }else{
                                if (new dbhelper(context).insert_projects(projectIds.get(position),projects.get(position).getImage(),projects.get(position).getTitle(),FirebaseAuth.getInstance().getCurrentUser().getUid(),projects.get(position).getDescription(),projects.get(position).getStatus(),String.valueOf(projects.get(position).getLat()),String.valueOf(projects.get(position).getLng()),projects.get(position).getRequiredService(),projects.get(position).getBudget())){
                                    Toast.makeText(context,"Project Added to favourates List",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }else if(u.getRole().equals("Customer")){
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.customer_another_menu,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.go_to_bids){
                            context.startActivity(new Intent(context, bidsList.class).putExtra("project_id",projectIds.get(position)).putExtra("project_status",projects.get(position).getStatus()));
                        }else if(item.getItemId()==R.id.fav_projects){
                            if(new dbhelper(context).check_if_already_exist(FirebaseAuth.getInstance().getCurrentUser().getUid(),projectIds.get(position))>0){
                                Toast.makeText(context,"Project Already in Favourates List",Toast.LENGTH_LONG).show();
                            }else{
                                if (new dbhelper(context).insert_projects(projectIds.get(position),projects.get(position).getImage(),projects.get(position).getTitle(),FirebaseAuth.getInstance().getCurrentUser().getUid(),projects.get(position).getDescription(),projects.get(position).getStatus(),String.valueOf(projects.get(position).getLat()),String.valueOf(projects.get(position).getLng()),projects.get(position).getRequiredService(),projects.get(position).getBudget())){
                                    Toast.makeText(context,"Project Added to favourates List",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        return true;
                    }
                });
                popup.show();

            }
          }
      });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class project_list_viewholder extends RecyclerView.ViewHolder{
  TextView title,description,status,location,budget;
  ImageView projectImage;
  CardView card;
     public project_list_viewholder(@NonNull View itemView) {
         super(itemView);
         status=itemView.findViewById(R.id.status);
         title=itemView.findViewById(R.id.title);
         description=itemView.findViewById(R.id.description);
         projectImage=itemView.findViewById(R.id.icon);
         card=itemView.findViewById(R.id.card);
         location=itemView.findViewById(R.id.location);
         budget=itemView.findViewById(R.id.budget);
     }
 }
}
