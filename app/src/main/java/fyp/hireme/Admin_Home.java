package fyp.hireme;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import java.util.ArrayList;

import fyp.hireme.Fragments.approvalRequestsFragment;
import fyp.hireme.Fragments.completedProjectsFragment;
import fyp.hireme.Fragments.ongoingProjects;
import fyp.hireme.Fragments.projects_for_bids;
import fyp.hireme.Fragments.user_list_fragment;

public class Admin_Home extends AppCompatActivity {
    ViewPager vp;
    TabLayout tb;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        vp=(ViewPager) findViewById(R.id.viewpager);
        tb=(TabLayout) findViewById(R.id.tabs);
        setviewpager();
        tb.setupWithViewPager(vp);
    }
    public void setviewpager(){
        viewpageradapter vpa=new viewpageradapter(getSupportFragmentManager());
        vpa.addfragment(new approvalRequestsFragment(),"Approval Requests");
        vpa.addfragment(new user_list_fragment(),"Users");
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
}