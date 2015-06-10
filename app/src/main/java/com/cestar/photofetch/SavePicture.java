package com.cestar.photofetch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cestar.db.DataBaseHelper;

/**
 * Created by lasha on 2015-06-08.
 */
public class SavePicture extends ActionBarActivity {

    EditText e_name;
    EditText e_category;
    EditText e_comments;
    Button b_save;

    // Declare Strings and save the TextViews
    String  str_name, str_category, str_comments, str_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savepicture);

        // Associate variable to Textviews and Button
        e_name = (EditText) findViewById(R.id.txt_name);
        e_category = (EditText) findViewById(R.id.txt_category);
        e_comments = (EditText) findViewById(R.id.txt_comments);

        b_save = (Button) findViewById(R.id.b_save);

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_name = e_name.getText().toString();
                str_category = e_category.getText().toString();
                str_comments = e_comments.getText().toString();
                str_path = "path/path/path/fileName.jpg"; // this can be linked with mainActivity

                //Check if all the variable are NOT null
                write2dB();

/*
                if(str_name !=null && str_name.length() == 0 &&
                    str_category !=null && str_name.length() == 0 &&
                    str_comments !=null && str_name.length() == 0 &&
                    str_path !=null && str_name.length() == 0)
                {
                    write2dB();
                }
                else
                {
                    String Err = "One or more fields are NULL";
                    Toast msg = Toast.makeText(getBaseContext(), Err, Toast.LENGTH_LONG);

                    msg.show();

                }
                */



            }
        });


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
            dbHelper.insertRecord(str_name, str_category, str_comments, str_path);
            System.out.println("Data written");
            dbHelper.close();

        }catch (Exception e)
        {
            System.out.println("Exception went wrong when open and close" + e.toString());
        }

    }
}
