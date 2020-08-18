package fyp.hireme;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import fyp.hireme.Firebase_Operations.firebase_operations;

public class Notifications extends AppCompatActivity {
RecyclerView notificationsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notificationsList=findViewById(R.id.notificationsList);
        notificationsList.setLayoutManager(new LinearLayoutManager(this));
        firebase_operations.getNotifications(this,notificationsList, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

}
