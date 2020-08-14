package fyp.hireme.Adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.favourite_projects;
import fyp.hireme.Model.project;
import fyp.hireme.R;
import fyp.hireme.dbhelper;

public class fav_projects_adapter extends RecyclerView.Adapter<fav_projects_adapter.fav_projects_viewholder> {
    public fav_projects_adapter(ArrayList<favourite_projects> projects, Context context) {
        this.projects = projects;
        this.context = context;
    }

    ArrayList<favourite_projects> projects;
Context context;
    @NonNull
    @Override
    public fav_projects_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new fav_projects_viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull fav_projects_viewholder holder, int position) {
        holder.title.setText(projects.get(position).getTitle());
        holder.description.setText(projects.get(position).getDescription());
        holder.status.setText("Status "+projects.get(position).getStatus());
        Picasso.get().load(projects.get(position).getImage()).into(holder.projectImage);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete from Favourite List")
                        .setMessage("are you sure you want to Delete this Project ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer rows = new dbhelper(context).delete(projects.get(position).getProjectId());
                                if (rows > 0) {
                                    projects.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context,"Item not Removed From Cart", Toast.LENGTH_LONG).show();
                                }


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
        return projects.size();
    }

    class fav_projects_viewholder extends RecyclerView.ViewHolder{
        TextView title,description,status;
        ImageView projectImage;
        CardView card;
        public fav_projects_viewholder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.status);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            projectImage=itemView.findViewById(R.id.icon);
            card=itemView.findViewById(R.id.card);
        }
    }
}
