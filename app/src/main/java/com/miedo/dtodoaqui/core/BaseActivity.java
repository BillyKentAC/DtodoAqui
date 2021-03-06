package com.miedo.dtodoaqui.core;


import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.miedo.dtodoaqui.R;

public class BaseActivity extends AppCompatActivity {


    private StateView stateView;

    public void showSnackMessage(String message, int colorResource) {
        View container = findViewById(R.id.container);
        if (container != null) {
            Snackbar snackbar = Snackbar.make(container, message, Snackbar.LENGTH_LONG);
            /*snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, colorResource));
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);*/
            snackbar.show();
        } else {
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            message, Toast.LENGTH_LONG);
            toast.show();
        }


    }

    public void hideInput() {

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public void showMessageError(String message) {
        // aun por definir el color del snackbar para el error
        showSnackMessage(message, R.color.colorPrimaryDark);
    }

    public void showMessage(String message) {
        showSnackMessage(message, com.google.android.material.R.color.error_color_material_light);
    }

    public void showToastMessage(String message) {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    }
                });

    }


    public void setUpStateView(View anotherView) {
        stateView = new StateView(anotherView);
    }

    public StateView getStateView() {
        return stateView;
    }
}
