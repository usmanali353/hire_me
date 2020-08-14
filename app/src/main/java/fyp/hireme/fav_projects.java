package fyp.hireme;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import fyp.hireme.Firebase_Operations.firebase_operations;

public class fav_projects extends AppCompatActivity {
RecyclerView fav_projects_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_projects);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fav_projects_list=findViewById(R.id.projects_list);
        fav_projects_list.setLayoutManager(new LinearLayoutManager(this));
        firebase_operations.getFavProjects(this,fav_projects_list);
    }

}
