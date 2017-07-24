package com.example.davidalaw.bsllearningtool.mFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

public class VideoViewFragment extends Fragment {

    private static final String TAG = " VideoViewFragment";

    private DBHandler mDBHandler;

    private TextView mTextView;
    private String signSelected;

    ProgressDialog mProgressDialog;
    VideoView mVideoView;

    private String VideoURL;

    public VideoViewFragment(String signSelected) {

        this.signSelected = signSelected;
    }

    public String getSignSelected() {
        return signSelected;
    }

    public void setSignSelected(String signSelected) {
        this.signSelected = signSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, container, false);
        getData();

        mVideoView = (VideoView)view.findViewById(R.id.videoView2);
        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setTitle("Streaming tutorial");

        mProgressDialog.setMessage("Bufferring...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        mProgressDialog.show();

        try {
            MediaController mediaController = new MediaController(getActivity());

            mediaController.setAnchorView(mVideoView);
            Uri video = Uri.parse(VideoURL);
            mVideoView.setMediaController(mediaController);
            mVideoView.setVideoURI(video);
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("Error : ", e.getMessage());

        }

        videofocus();



        return view;
    }

    public void videofocus () {

        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mProgressDialog.dismiss();
                mVideoView.start();

            }
        });

    }


    private void getData() {

        Log.d(TAG, "Populate BSL Notation View ");


        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (getSignSelected().equals(cursor.getString(2))) {
                if (cursor.getString(9).equals("http://youtube.com")) {
                    VideoURL = "http://content.jwplatform.com/videos/V4kviIT2-xaGbYRw4.mp4";
                } else {
                    VideoURL = cursor.getString(9);
                }
            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
