package fyp.hireme.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import fyp.hireme.Adapters.projects_list_adapter;
import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.user;
import fyp.hireme.R;

public class projects_for_bids extends Fragment {
    SharedPreferences prefs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.list,container,false);
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        user u=new Gson().fromJson(prefs.getString("user_info",null),user.class);
        RecyclerView bids_list =v.findViewById(R.id.projects_for_bids_list);
        bids_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebase_operations.getProjectsforBids(getActivity(),bids_list,u.getOffered_service());
        return v;
    }
}
