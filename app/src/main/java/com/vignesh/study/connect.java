package com.vignesh.study;

import android.os.AsyncTask;

/**
 * Created by vignesh on 13/08/2015.
 */
public abstract class connect extends AsyncTask<String, String, Void> {
    protected abstract void onPostExecute(String result);
}
