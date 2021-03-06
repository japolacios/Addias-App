package com.per.maka.final_redes_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Initiate extends AppCompatActivity implements ShakeEventManager.ShakeListener {

    private ShakeEventManager sd;
    private int shakeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate);
    }

    @Override
    public void onShake() {

        if(shakeCount<5){
            shakeCount++;
        } else {
            if(shakeCount>=5){
                Intent myIntent = new Intent(Initiate.this, Control.class); /** Class name here */
                startActivityForResult(myIntent, 0);
            }
        }

        //What imma do when im shaked like a boss!!!
        Log.d("SHAKE", "onShake: SHAED!!!! ");
        Toast.makeText(this, "Shake IT BABY!!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sd.register();

    }


    @Override
    protected void onPause() {
        super.onPause();
       sd.deregister();
    }
}
