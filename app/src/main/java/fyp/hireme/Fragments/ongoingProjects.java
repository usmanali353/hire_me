package fyp.hireme.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.R;

public class ongoingProjects extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.list,container,false);
        RecyclerView bids_list =v.findViewById(R.id.projects_for_bids_list);
        bids_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebase_operations.getProjectsforBids(getActivity(),bids_list);
        return v;
    }
}
