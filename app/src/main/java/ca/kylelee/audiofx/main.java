package ca.kylelee.audiofx;

import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import static android.view.View.*;

public class main extends AppCompatActivity implements OnClickListener {

    private final String PARCELABLE_SOUNDMANAGER = "PARCELABLE_SOUNDMANAGER"; //bundling key string for Soundmanager class
    SoundManager soundManager;
    DatabaseManager dbm;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private ArrayList<Object> lstChill,lstLounge,lstDriving,lstWorkout,lstHealing,lstParty; //ArrayList objects that stores values from SQlite Database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // Keep current state ( Media player ) when orientation changes, call a parcaelable object
        if(savedInstanceState == null) {
            soundManager = new SoundManager(getApplicationContext());
        }else if(savedInstanceState == null && soundManager.isPlay()){
            soundManager = new SoundManager(getApplicationContext());
        }else {
            soundManager = savedInstanceState.getParcelable(PARCELABLE_SOUNDMANAGER);
        }

        //invoke threadWorker class ( Inner class )
        new threadWorker().execute();

    }

    @Override
    public void onClick(View v) {

    }

    // inner class ( threadWorker using AsyncTask )
    class threadWorker extends AsyncTask<Void,Void,Void>{

        // Setup UI buttons
        ImageButton btnChill = (ImageButton) findViewById(R.id.btnChill);
        ImageButton btnLounge = (ImageButton) findViewById(R.id.btnLounge);
        ImageButton btnDriving = (ImageButton) findViewById(R.id.btnDriving);
        ImageButton btnWorkout = (ImageButton) findViewById(R.id.btnWorkout);
        ImageButton btnHealing = (ImageButton) findViewById(R.id.btnHealing);
        ImageButton btnParty = (ImageButton) findViewById(R.id.btnParty);
        ImageButton btnStop = (ImageButton) findViewById(R.id.btnStop);
        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);

        @Override
        protected void onPreExecute() {

            // Initialize UI Buttons
            btnChill.setOnClickListener(main.this);
            btnLounge.setOnClickListener(main.this);
            btnDriving.setOnClickListener(main.this);
            btnWorkout.setOnClickListener(main.this);
            btnHealing.setOnClickListener(main.this);
            btnParty.setOnClickListener(main.this);
            btnStop.setOnClickListener(main.this);
            btnAdd.setOnClickListener(main.this);


            volumeSeekbar = (SeekBar) findViewById(R.id.seekBar);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            audioManager = (AudioManager) getSystemService(main.AUDIO_SERVICE);

            // create an instance of DatabaseManager Class
            dbm = new DatabaseManager(main.this);


        }

        @Override
        protected Void doInBackground(Void...params) {

            //Calling a class method to get our seekbar wired up ( volume control )
            SoundManager.initSeekBar(volumeSeekbar, audioManager);

            //Creating / Copying Database into our device
            try {
                dbm.createDB();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Using .getInformation method to return a Arraylist of URL with different genres(passing in the name of genre-string)
            //each will be stored in seperate arraylist
            lstChill = new ArrayList<Object>(dbm.getInformation(getResources().getString(R.string.genreChill)));
            lstLounge = new ArrayList<Object>(dbm.getInformation(getResources().getString(R.string.genreLounge)));
            lstDriving = new ArrayList<Object>(dbm.getInformation(getResources().getString(R.string.genreDriving)));
            lstWorkout = new ArrayList<Object>(dbm.getInformation(getResources().getString(R.string.genreWorkout)));
            lstHealing = new ArrayList<Object>(dbm.getInformation(getResources().getString(R.string.genreHealing)));
            lstParty = new ArrayList<Object>(dbm.getInformation(getResources().getString(R.string.genreParty)));

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {

        }

        @Override
        protected void onPostExecute(Void params) {

            //Event handler (button click )
            btnChill.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //If list of genre (URLs) is not empty
                    if(lstChill.size() > 0) {
                        //Play live stream using url (always play 0 index URL)  .radioStaion medthod requires a string of URL
                        soundManager.radioStation((String) lstChill.get(0));
                        toast(getResources().getString(R.string.loading), 1);  //Toast message
                        // Shuffle and randomize the index order of URL list each time
                        Collections.shuffle(lstChill);
                    }else {
                        //If list is empty
                        toast(getResources().getString(R.string.emptyPlaylist), 0); // Toast message : empty url list
                    }

                }
            });

            btnLounge.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lstLounge.size() > 0) {
                        soundManager.radioStation((String) lstLounge.get(0));
                        toast(getResources().getString(R.string.loading), 1);
                        Collections.shuffle(lstLounge);
                    }else {
                        toast(getResources().getString(R.string.emptyPlaylist), 0);
                    }

                }
            });

            btnDriving.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lstDriving.size() > 0) {
                        soundManager.radioStation((String) lstDriving.get(0));
                        toast(getResources().getString(R.string.loading), 1);
                        Collections.shuffle(lstDriving);
                    }else {
                        toast(getResources().getString(R.string.emptyPlaylist), 0);
                    }

                }
            });

            btnWorkout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lstWorkout.size() > 0) {
                        soundManager.radioStation((String) lstWorkout.get(0));
                        toast(getResources().getString(R.string.loading), 1);
                        Collections.shuffle(lstWorkout);
                    }else {
                        toast(getResources().getString(R.string.emptyPlaylist), 0);
                    }

                }
            });

            btnHealing.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lstHealing.size() > 0) {
                        soundManager.radioStation((String) lstHealing.get(0));
                        toast(getResources().getString(R.string.loading), 1);
                        Collections.shuffle(lstHealing);
                    }else {
                        toast(getResources().getString(R.string.emptyPlaylist), 0);
                    }

                }
            });

            btnParty.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lstParty.size() > 0) {
                        soundManager.radioStation((String) lstParty.get(0));
                        toast(getResources().getString(R.string.loading), 1);
                        Collections.shuffle(lstParty);
                    } else {
                        toast(getResources().getString(R.string.emptyPlaylist), 0);
                    }

                }
            });

            // ADD button
            btnAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create a new intent - > opens up second activity (Popup)
                    Intent myIntent = new Intent(getApplicationContext(), AddStationPopUp.class);
                    //Start new activity
                    startActivity(myIntent);

                }
            });

            // Stop button
            btnStop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //STOP media player
                    soundManager.stop();
                    toast(getResources().getString(R.string.stopped), 0);

                }
            });

        }


    }

    //Creates Short/Long toast message
    private void toast(String text, int key){

        if(key == 0){
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
        }
        if(key == 1){
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
        }

    }




    //------------------------------------------------------------------SAVE CURRENT STATES / BUNDLE
    //------------------------------------------------------------------PUT
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCELABLE_SOUNDMANAGER, soundManager);

    }

    //------------------------------------------------------------------LOAD CURRENT STATES / BUNDLE
    //------------------------------------------------------------------GET
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }






}//END OF MAIN





