package com.cestar.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cestar.db.DataBaseHelper;
import com.cestar.photofetch.PhotoLookupGridActivity;
import com.cestar.photofetch.R;

import java.util.ArrayList;


public class CategoryListsFragment extends Fragment {

    DataBaseHelper dbHelper;
    ArrayList<String> categories;

    ListView list;
    private ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_category_lists, null);

        categories = new ArrayList<String>();
        list = (ListView) v.findViewById(R.id.list_cat);
        adapter= new ListAdapter(getActivity());


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getActivity(), PhotoLookupGridActivity.class);
                in.putExtra("CATEGORY", categories.get(position));
                startActivity(in);
            }
        });

        return v;
    }

    public class ListAdapter extends BaseAdapter {

        private Activity activity;
        private LayoutInflater inflater = null;

        public ListAdapter(Activity a) {
            activity = a;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return categories.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View view =convertView;
            if(convertView==null){
                view = inflater.inflate(R.layout.list_row_categories, null);
            }

            TextView tv = (TextView) view.findViewById(R.id.name_id);
            tv.setText(categories.get(position));
            return view;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        dbHelper = new DataBaseHelper(getActivity());

        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
            categories = dbHelper.getCategories();

        }
        catch(Exception e)
        {
            System.out.println("AHH not working " + e);
        }

        dbHelper.close();
        list.setAdapter(adapter);
    }
}
