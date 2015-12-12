package ca.kylelee.audiofx;

/**
 * Created by Kyle on 12/3/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static String DEST_PATH = "/data/data/ca.kylelee.audiofx/databases/";
    private static String DB_NAME = "radioStations";
    private static String FULL_PATH = DEST_PATH + DB_NAME;
    private static final int DB_VERSION = 1;
    private static final String TAG = "kyle";
    private static final String KEY_STATION = "station";
    private static final String KEY_URL = "url";
    private static final String KEY_GENRE = "genre";
    private static final String TBL_NAME = "tblRadio";
    private SQLiteDatabase db;

    private final Context ctx;

    //returns database name on our device
    public String getDbName(){return DB_NAME;}

    public DatabaseManager(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;
    }

    //creating a database - check is db exists first and if not copy from assets folder to physical device...
    public void createDB() throws IOException {

        boolean dbExist = isExist();

        if(dbExist){

        }else{

            this.getReadableDatabase();

            try {

                copyDB();

            } catch (IOException e) {

            }
        }

    }

    //boolean medthod checks if db exists
    private boolean isExist(){

        SQLiteDatabase checkDB = null;

        try{
            checkDB = SQLiteDatabase.openDatabase(FULL_PATH, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){


        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null;
    }


    //Copying database file from assets folder to physical device
    private void copyDB() throws IOException{


        InputStream input = ctx.getAssets().open(DB_NAME);
        OutputStream output = new FileOutputStream(FULL_PATH);


        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer))>0){
            output.write(buffer, 0, length);
        }

        //Close the streams
        //output.flush();
        output.close();
        input.close();

    }

    public void openDB() throws SQLException {

        db = SQLiteDatabase.openDatabase(FULL_PATH, null, SQLiteDatabase.OPEN_READONLY);

    }


    @Override
    public synchronized void close() {

        if(db != null)
            db.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //method that returns a ArrayList of genre from SQlite Database - a parameter required(string-genre)
    public ArrayList<String> getInformation(String genre){

        String sqlCommand = "SELECT * FROM tblRadio WHERE genre='" + genre+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlCommand, null);

        ArrayList<String> information = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {

                information.add(String.valueOf(cursor.getString(3))+"\n");

            } while (cursor.moveToNext());
        }

        db.close();

        return information;

    }

    //Inserting into SQlite database with given values (genre , name of station , URL)
    public void insertDB(String genre, Editable station, Editable url){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues myValues = new ContentValues();
        myValues.put(KEY_GENRE,String.valueOf(genre));
        myValues.put(KEY_STATION,String.valueOf(station));
        myValues.put(KEY_URL,String.valueOf(url));
        db.insert(TBL_NAME, null, myValues);
        db.close();

    }





}

