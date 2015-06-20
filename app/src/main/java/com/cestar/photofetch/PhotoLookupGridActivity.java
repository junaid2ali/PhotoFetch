package com.cestar.photofetch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cestar.actor.PhotoImage;
import com.cestar.db.DataBaseHelper;
import com.cestar.gridviewhelper.AbsListViewBaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.ArrayList;


public class PhotoLookupGridActivity  extends AbsListViewBaseActivity{

   ArrayList<PhotoImage> images;
    DisplayImageOptions options;
    String category;

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_lookup_grid);

        getSupportActionBar().hide();

        //get bundle from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getString("CATEGORY");
        }

        images = new ArrayList<PhotoImage>();
        dbHelper = new DataBaseHelper(this);
        loadUpDB();

        options = new DisplayImageOptions.Builder()
			/*.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)*/
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        //set up gridview
        listView = (GridView) findViewById(R.id.gridview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Clicked");
                Intent in = new Intent(PhotoLookupGridActivity.this, ViewImageActivity.class);
                in.putExtra("IMAGE_URL",images.get(position).getPath());
                in.putExtra("TITLE", images.get(position).getName());
                in.putExtra("DESCRIPTION", images.get(position).getComments());
                startActivity(in);
            }
        });

        ((GridView) listView).setAdapter(new ImageAdapter());
    }

    static class ViewHolder {
        ImageView imageView;
        TextView imageTitle;
        ProgressBar progress;
    }


    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageTitle = (TextView) view.findViewById(R.id.image_title);
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progress = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
                //view.setTag(imageUrls.get(position).getTrackArtURL());
            } else {
                holder = (ViewHolder) view.getTag();
            }

            File imgFile = new  File(images.get(position).getPath());
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.imageView.setImageBitmap(RotateBitmap(myBitmap, 90));
            holder.imageTitle.setText(images.get(position).getName());
            holder.progress.setVisibility(View.INVISIBLE);
            //imageLoader.displayImage(imgFile, holder.imageView, options, new SimpleImageLoadingListener());

            return view;
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_photo_lookup_grid, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent in = new Intent(PhotoLookupGridActivity.this, ViewImageActivity.class);
//            in.putExtra("IMAGE_URL",images.get(0).getPath());
//            in.putExtra("TITLE", images.get(0).getName());
//            in.putExtra("DESCRIPTION", images.get(0).getComments());
//            startActivity(in);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public void loadUpDB(){

        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
            images = dbHelper.getImages();
        }
        catch(Exception e)
        {
            System.out.println("AHH not working " + e);
        }

        dbHelper.close();
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
