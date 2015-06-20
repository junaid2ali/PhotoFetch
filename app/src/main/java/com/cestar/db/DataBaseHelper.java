package com.cestar.db;

/**
 * Created by lasha on 2015-06-08.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.cestar.actor.PhotoImage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

//import com.charlesli.actor.HighScore;

/**
 * Created by lasha on 2015-06-06.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.cestar.photofetch/databases/";

    private static String DB_NAME = "photoFetch.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /*
    public ArrayList<HighScore> getHighScores() {
        ArrayList<HighScore> scores = new ArrayList<HighScore>();

        String query = "SELECT * from scores";

        Cursor cursor = myDataBase.rawQuery(query, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                System.out.println("Name is " + cursor.getString(0)); // here 0 is column no. i.e. name
                scores.add(new HighScore(cursor.getString(0), cursor.getInt(1)));
            } while (cursor.moveToNext());
        }

        return  scores;

    }
*/


    public ArrayList<String> getCategories()
    {
        System.out.println("done first");
        ArrayList<String> categories = new ArrayList<String>();

        String query = "SELECT DISTINCT category from picturedB ORDER BY category ASC";

        Cursor cursor = myDataBase.rawQuery(query, new String[]{});

        System.out.println("done");
        if (cursor.moveToFirst()) {
            do{
                categories.add(cursor.getString(0));
                System.out.println("The best " + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public ArrayList<PhotoImage> getImages(String cat)
    {

        ArrayList<PhotoImage> categories = new ArrayList<PhotoImage>();

        String query = "SELECT * from picturedB WHERE category = '" + cat;

        Cursor cursor = myDataBase.rawQuery(query, new String[]{});


        if (cursor.moveToFirst()) {
            do{
                categories.add(new PhotoImage(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                System.out.println(cursor.getString(0) +  cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return categories;
    }



    public void insertRecord(String str_name, String str_category, String str_comments, String str_path)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        System.out.println("name= " + str_name + " category= " + str_category + " comments= " + str_comments + " path= " + str_path);

        values.put("name", str_name);
        values.put("category",str_category);
        values.put("comments",str_comments);
        values.put("path",str_path);
        database.insert("picturedB", null, values);
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        //myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
