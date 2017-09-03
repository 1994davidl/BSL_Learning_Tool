package com.example.davidalaw.bsllearningtool.mModel_Controller;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import net.protyposis.android.mediaplayer.MediaSource;
import net.protyposis.android.mediaplayer.UriSource;

/**
 * @Link: **REFERENCE** Protyposis Mediaplayer library: https://github.com/protyposis/MediaPlayer-Extended
 *
 * This class is primarily used to loads, decodes and enables the play of the Video URL
 *
 * Created by DavidALaw on 24/07/2017.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     *
     * @param context
     * @param uri
     * @param callback
     */
    public static void uriToMediaSourceAsync(final Context context, Uri uri, MediaSourceAsyncCallbackHandler callback) {

        LoadMediaSourceAsyncTask loadingTask = new LoadMediaSourceAsyncTask(context, callback);

        try {
            loadingTask.execute(uri).get();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * @param context
     * @param uri
     * @return
     */
    private static MediaSource uriToMediaSource(Context context, Uri uri) {
        MediaSource source;
        source = new UriSource(context, uri);
        return source;
    }

    /**
     *
     */
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

    public interface MediaSourceAsyncCallbackHandler {
        void onMediaSourceLoaded(MediaSource mediaSource);
        void onException(Exception e);
    }
}
