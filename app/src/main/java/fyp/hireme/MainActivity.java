package fyp.hireme;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fyp.hireme.Firebase_Operations.firebase_operations;

public class MainActivity extends AppCompatActivity {
 RecyclerView projects_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        projects_list=findViewById(R.id.projects_list);
        projects_list.setLayoutManager(new LinearLayoutManager(this));
        firebase_operations.getProjectsforCustomer(MainActivity.this,projects_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_project){
            startActivity(new Intent(MainActivity.this,add_project.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
