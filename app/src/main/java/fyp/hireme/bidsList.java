package fyp.hireme;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import fyp.hireme.Firebase_Operations.firebase_operations;

public class bidsList extends AppCompatActivity {
  RecyclerView bidsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bidsList=findViewById(R.id.bids_list);
        bidsList.setLayoutManager(new LinearLayoutManager(this));
        firebase_operations.getBidsforProject(bidsList.this,getIntent().getStringExtra("project_id"),bidsList,getIntent().getStringExtra("project_status"));
    }
}