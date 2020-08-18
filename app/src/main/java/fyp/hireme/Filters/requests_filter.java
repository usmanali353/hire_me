package fyp.hireme.Filters;

import android.widget.Filter;

import java.util.ArrayList;

import fyp.hireme.Adapters.approvalRequestsAdapter;
import fyp.hireme.Adapters.user_list_adapter;
import fyp.hireme.Model.Requests;
import fyp.hireme.Model.user;

public class requests_filter extends Filter {
    ArrayList<Requests> filterlist;
    approvalRequestsAdapter showmoreadapter;
    public requests_filter(ArrayList<Requests> productsArrayList,approvalRequestsAdapter adapter){
        this.filterlist=productsArrayList;
        this.showmoreadapter=adapter;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null&&constraint.length()>0){
            constraint=constraint.toString().toUpperCase();
            ArrayList<Requests> filteredproductslist=new ArrayList<>();
            for(int i=0;i<filterlist.size();i++){
                if(filterlist.get(i).getEmail().toUpperCase().contains(constraint)){
                    filteredproductslist.add(filterlist.get(i));
                }
            }
            results.count=filteredproductslist.size();
            results.values=filteredproductslist;
        }else{
            results.values=filterlist;
            results.count=filterlist.size();
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        showmoreadapter.requests=(ArrayList<Requests>)filterResults.values;
        showmoreadapter.notifyDataSetChanged();
    }
}
