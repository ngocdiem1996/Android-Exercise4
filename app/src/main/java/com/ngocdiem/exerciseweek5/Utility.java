package com.ngocdiem.exerciseweek5;

import android.widget.EditText;

/**
 * Created by NDiem on 4/2/2018.
 */

public class Utility {
    public static boolean isBlankField(EditText etPersonData)
    {
        return etPersonData.getText().toString().trim().equals("");
    }
}
