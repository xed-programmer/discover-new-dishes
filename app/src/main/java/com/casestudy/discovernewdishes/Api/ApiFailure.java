package com.casestudy.discovernewdishes.Api;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

public class ApiFailure {
    public static void onFailure(Throwable t, Context context){
        if(t instanceof IOException)
            Toast.makeText(context,"No Internet Connection, Please check your connectivity", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,"Something went wrong!", Toast.LENGTH_LONG).show();
    }
}
