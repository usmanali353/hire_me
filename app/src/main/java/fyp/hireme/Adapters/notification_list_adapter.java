package fyp.hireme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fyp.hireme.Model.Notifications;
import fyp.hireme.R;

public class notification_list_adapter extends RecyclerView.Adapter<notification_list_adapter.notification_list_viewholder> {
  ArrayList<Notifications> notifications;
  ArrayList<String> notificationIds;
  Context context;

    public notification_list_adapter(ArrayList<Notifications> notifications, ArrayList<String> notificationIds, Context context) {
        this.notifications = notifications;
        this.notificationIds = notificationIds;
        this.context = context;
    }

    @NonNull
    @Override
    public notification_list_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new notification_list_viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull notification_list_viewholder holder, int position) {
         holder.title.setText(notifications.get(position).getTitle());
        holder.date.setText(notifications.get(position).getDate());
        holder.message.setText(notifications.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class notification_list_viewholder extends RecyclerView.ViewHolder{
          TextView date,title,message;
        public notification_list_viewholder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            title=itemView.findViewById(R.id.title);
            message=itemView.findViewById(R.id.description);
        }
    }
}
