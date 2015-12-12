package ca.kylelee.audiofx;

import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AddStationPopUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Key string values for saving instances of current state
    private final String KEY_STATION = "KEY_STATION";
    private final String KEY_URL = "KEY_URL";
    private final String KEY_SPINNER ="KEY_SPINNER";

    private ImageButton btnCancel;
    private ImageButton btnSave;
    private EditText txtStation;
    private EditText txtUrl;
    private Spinner spinnerGenre;
    private static String newGenre;
    ArrayAdapter<CharSequence> myAdapter;



    DatabaseManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        new threadWorker().execute();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newGenre = spinnerGenre.getSelectedItem().toString();  //Storing a string value from spinner
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    // inner class ( threadWorker using AsyncTask )
    class threadWorker extends AsyncTask<Void,Void,Void> {




        @Override
        protected void onPreExecute() {
            // get references to view object
            btnCancel = (ImageButton) findViewById(R.id.btnCancel);
            btnSave = (ImageButton) findViewById(R.id.btnSave);
            txtStation = (EditText) findViewById(R.id.txtStation);
            txtUrl = (EditText) findViewById(R.id.txtUrl);
            spinnerGenre = (Spinner) findViewById(R.id.spinnerGenre);



        }

        @Override
        protected Void doInBackground(Void...params) {

            // setup array adapter from array of different genres
            // preset spinner list usgin arrayadater
            myAdapter = ArrayAdapter.createFromResource(AddStationPopUp.this, R.array.GENRE, android.R.layout.simple_spinner_item);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dbManager = new DatabaseManager(getBaseContext());



            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {



        }

        @Override
        protected void onPostExecute(Void params) {

            //setup event listener to spinnerview
            //populate spinnerview
            spinnerGenre.setOnItemSelectedListener(AddStationPopUp.this);
            spinnerGenre.setAdapter(myAdapter);

            // setup event listener
            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Create a new Intent (main activity)
                   // Intent myIntent = new Intent(getApplicationContext(), main.class);
                    //start a new activity
                   // startActivity(myIntent);

                   finish();

                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //if there's no input from user
                    //force focus
                    //set error message on txtStation , txtURL
                    //do not move forward
                    //else then run the code - inserting user's input into SQLite database
                    //show a toast message and clear interface.
                    if(txtStation.getText().toString().length() <= 0 ) {
                        txtStation.requestFocus();
                        txtStation.setError(getResources().getString(R.string.txtStationErrorCheck));
                    }else if(txtUrl.getText().toString().length() <= 0 ) {
                        txtUrl.requestFocus();
                        txtUrl.setError(getResources().getString(R.string.txtUrlErrorCheck));
                    }else {
                        dbManager.insertDB(newGenre, txtStation.getText(), txtUrl.getText());
                        Toast.makeText(AddStationPopUp.this, getResources().getString(R.string.dbAdded), Toast.LENGTH_SHORT).show();
                        txtStation.setText("");
                        txtUrl.setText("");
                    }
                }
            });



        }


    }



    //------------------------------------------------------------------SAVE CURRENT STATES / BUNDLE
    //------------------------------------------------------------------PUT
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(KEY_STATION, String.valueOf(txtStation.getText()));
        outState.putString(KEY_URL, String.valueOf(txtUrl.getText()));
        outState.putInt(KEY_SPINNER,spinnerGenre.getSelectedItemPosition());

    }

    //------------------------------------------------------------------LOAD CURRENT STATES / BUNDLE
    //------------------------------------------------------------------GET
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtStation.setText(savedInstanceState.getString(KEY_STATION));
        txtUrl.setText(savedInstanceState.getString(KEY_URL));
        spinnerGenre.setSelection(savedInstanceState.getInt(KEY_SPINNER));

    }



}//END OF APP

