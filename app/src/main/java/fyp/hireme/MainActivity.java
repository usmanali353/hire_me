package fyp.hireme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fr.ganfra.materialspinner.MaterialSpinner;
import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.user;

public class MainActivity extends AppCompatActivity {
 RecyclerView projects_list;
 SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
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
        }else if(item.getItemId()==R.id.signOut){
            FirebaseAuth.getInstance().signOut();
            prefs.edit().remove("user_info").apply();
            startActivity(new Intent(MainActivity.this,Selection.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }else if(item.getItemId()==R.id.change_profile){
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.update_profile,null);
            MaterialEditText name=v.findViewById(R.id.nametxt);
            MaterialEditText phone=v.findViewById(R.id.phonetxt);
            MaterialSpinner offered_service=v.findViewById(R.id.offered_service);
            offered_service.setVisibility(View.GONE);
            user u=new Gson().fromJson(prefs.getString("user_info",null),user.class);
            name.setText(u.getName());
            phone.setText(u.getPhone());
            AlertDialog changeProfileDialog =new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Change Profile")
                    .setMessage("Provide Valid Info")
                     .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                         }
                     }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setView(v).create();
            changeProfileDialog.show();
            changeProfileDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(name.getText().toString().isEmpty()) {
                        name.setError("Enter your Name");
                    }else if(phone.getText().toString().isEmpty()){
                        phone.setError("Enter Your Phone");
                    }else{
                        firebase_operations.updateProfile(MainActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),name.getText().toString(),phone.getText().toString(),null);
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
