package com.cestar.photofetch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cestar.db.DataBaseHelper;

import java.util.ArrayList;


public class CategoryListsActivity extends ActionBarActivity {

    DataBaseHelper dbHelper;
    ArrayList<String> categories;

    ListView list;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_lists);

        categories = new ArrayList<String>();
        list = (ListView) findViewById(R.id.list_cat);
        adapter= new ListAdapter(this);
        DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
        myDbHelper = new DataBaseHelper(this);

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
