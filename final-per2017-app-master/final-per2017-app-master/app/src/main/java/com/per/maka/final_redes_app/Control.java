package com.per.maka.final_redes_app;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import common.User;

public class Control extends AppCompatActivity implements Observer {

    //Comunicacion
    Communication com;

    //UI Elements
    Button left, down, right, up, ok;
    TextView currentUser;

    //Usuario
    private User user;
    private String comando;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);




        user= (User) getIntent().getSerializableExtra("user"); //Obtenemos el extra del usuario
        currentUser = (TextView) findViewById(R.id.currentUser);
        if(user!=null) {
            currentUser.setText(user.getName()); //Ponemos el nombre del usuario en el Text View para tener el identificador
        }

        //Iniciamos la comunicacion
        com= Communication.getInstance();
        //Borramos observadores previos
        com.deleteObservers();
        //Añadimos esta clse como observer de la comunicacion
        com.getInstance().addObserver(this);


        //UI Elements y sus acciones --> inicializando controles

        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);
        ok = (Button)findViewById(R.id.enter);

        //Comando para enviar
        comando = "";

        //Comandos

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comando = "up";
                com.send(comando);
                Log.d("Control", "Send UP!");
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comando = "down";
                com.send(comando);
                Log.d("Control", "Send DOWN!");
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comando = "up";
                com.send(comando);
                Log.d("Control", "Send LEFT!");
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comando = "right";
                com.send(comando);
                Log.d("Control", "Send RIGHT!");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comando = "ok";
                com.send(comando);
                Log.d("Control", "Send OK!");
            }
        });



    }

    @Override
    public void update(Observable o, Object data) {
        if(data instanceof String){
            Log.d("COMM", "update: LLEGO UN STRING");
            String tempVar = (String)data;
            if(tempVar.equals("next")){
                Intent myIntent = new Intent(Control.this, Shake.class);
                startActivityForResult(myIntent, 0);
            }
        }
    }
}
