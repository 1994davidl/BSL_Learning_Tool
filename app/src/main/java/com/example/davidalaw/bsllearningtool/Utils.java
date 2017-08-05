package com.example.davidalaw.bsllearningtool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.protyposis.android.mediaplayer.MediaSource;
import net.protyposis.android.mediaplayer.UriSource;
import net.protyposis.android.mediaplayer.dash.AdaptationLogic;
import net.protyposis.android.mediaplayer.dash.DashSource;
import net.protyposis.android.mediaplayer.dash.SimpleRateBasedAdaptationLogic;

/**
 * Code is sourced from Protyposis Mediaplayer library: https://github.com/protyposis/MediaPlayer-Extended
 *
 * This class is primarily used to Convert the Video URL into a media source in order to play the
 * video file in the media player.
 *
 * Created by DavidALaw on 24/07/2017.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static MediaSource uriToMediaSource(Context context, Uri uri) {
        MediaSource source = null;
        source = new UriSource(context, uri);
        return source;
    }

    public static void uriToMediaSourceAsync(final Context context, Uri uri, MediaSourceAsyncCallbackHandler callback) {
        LoadMediaSourceAsyncTask loadingTask = new LoadMediaSourceAsyncTask(context, callback);

        try {
            loadingTask.execute(uri).get();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static class LoadMediaSourceAsyncTask extends AsyncTask<Uri, Void, MediaSource> {

        private Context mContext;
        private MediaSourceAsyncCallbackHandler mCallbackHandler;
        private MediaSource mMediaSource;
        private Exception mException;

        public LoadMediaSourceAsyncTask(Context context, MediaSourceAsyncCallbackHandler callbackHandler) {
            mContext = context;
            mCallbackHandler = callbackHandler;
        }

        @Override
        protected MediaSource doInBackground(Uri... params) {
            try {
                mMediaSource = Utils.uriToMediaSource(mContext, params[0]);
                return mMediaSource;
            } catch (Exception e) {
                mException = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(MediaSource mediaSource) {
            if(mException != null) {
                mCallbackHandler.onException(mException);
            } else {
                mCallbackHandler.onMediaSourceLoaded(mMediaSource);
            }
        }
    }

    public static interface MediaSourceAsyncCallbackHandler {
        void onMediaSourceLoaded(MediaSource mediaSource);
        void onException(Exception e);
    }
}
