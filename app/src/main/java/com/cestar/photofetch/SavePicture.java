package com.cestar.photofetch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cestar.db.DataBaseHelper;

import java.io.File;

/**
 * Created by lasha on 2015-06-08.
 */
public class SavePicture extends AppCompatActivity {

    EditText e_name;
    EditText e_category;
    EditText e_comments;
    Button b_save;
    ImageView displayImage;
    String imgPath;

    // Declare Strings and save the TextViews
    String  str_name, str_category, str_comments, str_path, imagePath;
    Bitmap im;

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    byte[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savepicture);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imgPath = extras.getString("PATH");
        }

        //Create the spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.photo_category, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Start listening
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),parent.getItemIdAtPosition(position)+" selected", Toast.LENGTH_LONG).show();
                //System.out.println(parent.getItemAtPosition(position));
                str_category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Associate variable to Textviews and Button
        e_name = (EditText) findViewById(R.id.txt_name);
        e_category = (EditText) findViewById(R.id.txt_category);
        e_comments = (EditText) findViewById(R.id.txt_comments);
        displayImage = (ImageView) findViewById(R.id.display_image);

        b_save = (Button) findViewById(R.id.b_save);

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Load the fields into the String variables
                str_name = e_name.getText().toString();
                //str_category = e_category.getText().toString(); // commented since its getting from spinner
                str_comments = e_comments.getText().toString();


                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "MyCameraApp");
                str_path = mediaStorageDir.getPath() + File.separator
                        + "IMG_" + str_name + ".jpg"; // this can be linked with mainActivity

                //Check if all the fields are NOT null
                Boolean Sfield = false;
                Boolean Cfield = false;
                Boolean COfield = false;
                Boolean Pfield = false;

                if(str_name != null && str_name.length() > 0) Sfield = true;

                if(str_category != null && str_category.length() > 0) Cfield = true;

                if(str_comments != null && str_comments.length() > 0) COfield = true;

                if(str_path != null && str_path.length() > 0) Pfield = true;

                if(Sfield & Cfield & COfield & Pfield)
                {
                    System.out.println("all fields are OK.. READY to write to dB");
                    System.out.println(str_name + " " + str_category + " " + str_comments + " " + str_path);
                    write2dB();

                    //Go Back to main page
                    finish();
                    Toast msg = Toast.makeText(getBaseContext(), "Picture Saved Successfully", Toast.LENGTH_LONG);
                    msg.show();
                }
                else
                {
                    String Err = "One or more fields are NULL";
                    Toast msg = Toast.makeText(getBaseContext(), Err, Toast.LENGTH_LONG);
                    msg.show();
                }
            }
        });

        //show the image
        File imgFile = new  File(imgPath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            displayImage.setImageBitmap(myBitmap);
            displayImage.setRotation(90);
        }

    }

    public void write2dB() {

        DataBaseHelper dbHelper = new DataBaseHelper(this);

        try{

            dbHelper.createDataBase();  // create in the phone

        }catch (Exception e)
        {
            System.out.println("Exception went wrong when create db " + e.toString());
        }

        // just creating another TRY to open/close
        try{
            dbHelper.openDataBase();
            System.out.println("Going to write data");
            dbHelper.insertRecord(str_name, str_category, str_comments, imgPath);
            System.out.println("Data written");
            dbHelper.close();

        }catch (Exception e)
        {
            System.out.println("Exception went wrong when open and close" + e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_activity2, menu);
        getMenuInflater().inflate(R.menu.menu_savepicture, menu);
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
        else if (id == android.R.id.home)
        {
            //finish();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
