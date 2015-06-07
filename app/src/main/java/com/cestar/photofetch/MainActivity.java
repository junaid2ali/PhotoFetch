package com.cestar.photofetch;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    // storage for camera using URI components ( need because photo is returned from camera fragment?
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    //private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";
    // Required for camera to save image file on resume from camera app
    private String mCurrentPhotoPath;
    //private Uri mCapturedImageURI;


    // widgetes
    private ImageView mImageView;
    private Button mPicButton;


    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Step 1 setup widgets
        mImageView = (ImageView) findViewById(R.id.imageView);
        mPicButton = (Button) findViewById(R.id.picButton);
        // Step 2 on click of "take picture button" send an intent to existing camera app
        // to handle picture taking.
        mPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }


    // Driver to :
    //  - get file
    //  - call pre-existing camera  applilcation with an intent to start camera activity
    private void dispatchTakePictureIntent() {

        // The camera application makes use of an activity.
        // we need to send communications from our application to camera cativit
        // via the "take Picture Intent"
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // First check if the camera isv available.
        // if can get the intent start the camera via the start Activity for Result
        // check package manager if camera exists and remeber that we
        // force the camera existence for appl to work in the manifest
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // try to get a file to save the image into
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast toast = Toast.makeText(this, "No Camera", Toast.LENGTH_SHORT);
                toast.show();
                System.out.println("File system error");
            }
            if (photoFile != null) {

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    ////////////////////////
    // This methond creates a file to store the image
    private File createImageFile() throws IOException {
        // create file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName, // unique name
                ".jpg", // extension
                storageDir); // path

        // Save a file: path for use with ACTION_VIEW intents which will be needed later
        mCurrentPhotoPath = "file:" + imageFile.getAbsolutePath();
        return imageFile;
    }

    /// On Activity Result is the method called once, the camera Application is taking the picture
    /////// Method to display a thumbnail of picture after the camera sends an intent
    /////// with the picture... To be overwritten by Charles & Lalit.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // get the extra, then get the bm froms the extra then pump the bitmap
            // in an image view
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

