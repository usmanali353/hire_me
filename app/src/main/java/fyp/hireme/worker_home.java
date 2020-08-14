package fyp.hireme;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;
import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Fragments.completedProjectsFragment;
import fyp.hireme.Fragments.ongoingProjects;
import fyp.hireme.Fragments.projects_for_bids;
import fyp.hireme.Model.user;
import fyp.hireme.Utils.utils;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class worker_home extends AppCompatActivity {
    ViewPager vp;
    TabLayout tb;
    SharedPreferences prefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        vp=(ViewPager) findViewById(R.id.viewpager);
        tb=(TabLayout) findViewById(R.id.tabs);
        setviewpager();
        tb.setupWithViewPager(vp);
    }
    public void setviewpager(){
        viewpageradapter vpa=new viewpageradapter(getSupportFragmentManager());
        vpa.addfragment(new ongoingProjects(),"On going");
        vpa.addfragment(new projects_for_bids(),"for Bids");
        vpa.addfragment(new completedProjectsFragment(),"Completed");
        vp.setAdapter(vpa);
       // vp.setOffscreenPageLimit(2);
    }

    class viewpageradapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> listofFragment;
        ArrayList<String>fragmenttitles;
        public viewpageradapter(FragmentManager fm) {
            super(fm);
            listofFragment=new ArrayList<Fragment>();
            fragmenttitles=new ArrayList<String>();
        }
        @Override
        public Fragment getItem(int position) {
            return listofFragment.get(position);
        }
        @Override
        public int getCount() {
            return listofFragment.size();
        }
        public void addfragment(Fragment f, String name){
            listofFragment.add(f);
            fragmenttitles.add(name);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmenttitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.worker_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.signOut){
            FirebaseAuth.getInstance().signOut();
            prefs.edit().remove("user_info").apply();
            startActivity(new Intent(worker_home.this,Selection.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }else if(item.getItemId()==R.id.change_profile){
            View v= LayoutInflater.from(worker_home.this).inflate(R.layout.update_profile,null);
            MaterialEditText name=v.findViewById(R.id.nametxt);
            MaterialEditText phone=v.findViewById(R.id.phonetxt);
            MaterialSpinner offered_service=v.findViewById(R.id.offered_service);
            user u=new Gson().fromJson(prefs.getString("user_info",null),user.class);
            name.setText(u.getName());
            phone.setText(u.getPhone());
            for(int i=0;i<getResources().getStringArray(R.array.offered_services).length;i++){
                if(getResources().getStringArray(R.array.offered_services)[i].equals(u.getOffered_service())){
                    offered_service.setSelection(i+1);
                }
            }
            AlertDialog changeProfileDialog =new AlertDialog.Builder(worker_home.this)
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
                    }else if(offered_service.getVisibility()==View.VISIBLE&&offered_service.getSelectedItem()==null){
                        offered_service.setError("Select Offered Service");
                    }else{
                        firebase_operations.updateProfile(worker_home.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),name.getText().toString(),phone.getText().toString(),offered_service.getSelectedItem().toString());
                    }
                }
            });
        }
        return true;
    }
}
