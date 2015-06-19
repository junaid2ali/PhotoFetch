package com.cestar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cestar.photofetch.R;
import com.cestar.photofetch.SavePicture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePictureFragment extends android.support.v4.app.Fragment {

    Context c;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    // storage for camera using URI components ( need because photo is returned from camera fragment?
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    //private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";
    // Required for camera to save image file on resume from camera app
    private String mCurrentPhotoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_take_picture, null);

        c = getActivity().getApplicationContext();

        dispatchTakePictureIntent();
        return v;
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // try to get a file to save the image into
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast toast = Toast.makeText(getActivity(), "No Camera", Toast.LENGTH_SHORT);
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            // get the extra, then get the bm froms the extra then pump the bitmap
            // in an image view

            Bundle extras = data.getExtras();
            Intent in = new Intent(getActivity(), SavePicture.class);
            in.putExtra("IMAGE", (Bitmap) extras.get("data"));
            in.putExtra("URL", mCurrentPhotoPath);
            startActivity(in);



            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            ///!!!!! need to save this image path and associate it with a label
            // Need to associate the label with the image file path here.
            //
        }

    }

    public Boolean hasCamera() {
        PackageManager packageManager = c.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        else
        {
            return true;
        }
    }

}
