package fyp.hireme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fyp.hireme.Model.project;
import fyp.hireme.R;

public class projects_list_adapter extends RecyclerView.Adapter<projects_list_adapter.project_list_viewholder> {
    ArrayList<project> projects;
    Context context;
    ArrayList<String> projectIds;

    public projects_list_adapter(ArrayList<project> projects, Context context, ArrayList<String> projectIds) {
        this.projects = projects;
        this.context = context;
        this.projectIds = projectIds;
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
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class project_list_viewholder extends RecyclerView.ViewHolder{
  TextView title,description;
  ImageView projectImage;
     public project_list_viewholder(@NonNull View itemView) {
         super(itemView);
         title=itemView.findViewById(R.id.title);
         description=itemView.findViewById(R.id.description);
         projectImage=itemView.findViewById(R.id.icon);
     }
 }
}
