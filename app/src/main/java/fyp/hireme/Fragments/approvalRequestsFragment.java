package fyp.hireme.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.R;
import fyp.hireme.Selection;

public class approvalRequestsFragment extends Fragment {
    RecyclerView users;
    SharedPreferences prefs;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.list,container,false);
        users =v.findViewById(R.id.projects_for_bids_list);
        users.setLayoutManager(new LinearLayoutManager(getActivity()));
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        return v;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.admin_menu,menu);
        MenuItem searchitem=menu.findItem(R.id.action_search);
        SearchView userSearch=(SearchView) MenuItemCompat.getActionView(searchitem);
        firebase_operations.getApprovalRequests(getActivity(),users,userSearch,this);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.signOut){
            FirebaseAuth.getInstance().signOut();
            prefs.edit().remove("user_role").apply();
            startActivity(new Intent(getActivity(), Selection.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            getActivity().finish();
        }
        return true;
    }
}
