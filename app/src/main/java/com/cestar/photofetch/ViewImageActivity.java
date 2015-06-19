package com.cestar.photofetch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class ViewImageActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Bitmap mBitmap;
    private String imgPath;
    private String title;
    private String description;

    private TextView titleTV;
    private TextView descriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imgPath = extras.getString("PATH");
            title = extras.getString("TITLE");
            description = extras.getString("DESCRIPTION");
        }

        mImageView = (ImageView) findViewById(R.id.viewImageIV);
        titleTV = (TextView) findViewById(R.id.im_title);
        descriptionTV  = (TextView) findViewById(R.id.im_description);

        titleTV.setText(title.toUpperCase());
        descriptionTV.setText(description.toUpperCase());

        try {

                File imgFile = new  File(imgPath);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mImageView.setImageBitmap(myBitmap);
                mImageView.setRotation(90);

        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_image, menu);
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
