package ca.kylelee.audiofx;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Kyle on 11/30/2015.
 */

public class SoundManager implements Parcelable{


    private Context ctx;
    private SoundPool sPool;
    private float rate = 1.0f;
    private float masterVolume = 1.0f;
    private float leftVolume = 1.0f;
    private float rightVolume = 1.0f;
    private float balance = 0.5f;
    private ArrayList<String> listOfSound = new ArrayList<>();
    MediaPlayer mPlayer;


    private int maxVolume;
    private int curVolume;

    public SoundManager(Context context){

        sPool = new SoundPool(16,AudioManager.STREAM_MUSIC,0);
        ctx = context;

        mPlayer = new MediaPlayer();


    }

    protected SoundManager(Parcel in) {
        rate = in.readFloat();
        masterVolume = in.readFloat();
        leftVolume = in.readFloat();
        rightVolume = in.readFloat();
        balance = in.readFloat();
        listOfSound = in.createStringArrayList();
        maxVolume = in.readInt();
        curVolume = in.readInt();
    }

    public static final Creator<SoundManager> CREATOR = new Creator<SoundManager>() {
        @Override
        public SoundManager createFromParcel(Parcel in) {
            return new SoundManager(in);
        }

        @Override
        public SoundManager[] newArray(int size) {
            return new SoundManager[size];
        }
    };

    // lookup for a sound and get its ID
    public int load(int soundID){
        return sPool.load(ctx, soundID,1);

    }

    // play a sound with provided ID
    public void play(int soundID){
        sPool.play(soundID, leftVolume,rightVolume,1,0,rate);

    }
    public void play(int soundID,int loop){
        sPool.play(soundID, leftVolume,rightVolume,1,loop,rate);

    }


    public void setSpeed(float speed)
    {
        rate = speed;

        // Speed of zero is invalid
        if(rate < 0.01f)
            rate = 0.01f;

        // Speed has a maximum of 2.0
        if(rate > 2.0f)
            rate = 2.0f;
    }

   //invokes listRaw() object and store into an Arraylist
    public ArrayList<String> getlistRaw(){

        listRaw();
        return listOfSound;
    }
    //Returns a list of files that are currently existing on raw folder
    private void listRaw(){
        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            Log.d("kyle", "raw assets: " + fields[count].getName());
            listOfSound.add(fields[count].getName());
        }

    }

    //A custom class method that initializes a seekbar and wire up with the volume controller
    //set up a progressbar
    public static void initSeekBar(SeekBar seekbar, final AudioManager audioManager) {

        try
        {
            seekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Streaming a audio source with a string value of URL
    public void radioStation(String STREAM_URL){

        try {

            mPlayer.reset();
            mPlayer.setDataSource(STREAM_URL);
            mPlayer.prepareAsync();

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                        mp.start();


                }


            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //stop the Mediaplayer
    public void stop(){

        mPlayer.stop();
    }

    //is media player running ?
    public boolean isPlay(){

        if(mPlayer.isPlaying()){
            return true;
        }else{
            return false;
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(rate);
        dest.writeFloat(masterVolume);
        dest.writeFloat(leftVolume);
        dest.writeFloat(rightVolume);
        dest.writeFloat(balance);
        dest.writeStringList(listOfSound);
        dest.writeInt(maxVolume);
        dest.writeInt(curVolume);
    }
}
