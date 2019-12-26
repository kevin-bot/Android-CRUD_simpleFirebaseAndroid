package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.crudfirebase.Model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    EditText txtnombre,txtpellido,txtcorreo,txtcontrasena;
    ListView listV_persona;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtnombre = findViewById(R.id.txt_nombrePersona);
        txtpellido = findViewById(R.id.txt_ApellidoPersona);
        txtcontrasena = findViewById(R.id.txt_contrasenaPersona);
        txtcorreo = findViewById(R.id.txt_CorreoPersona);
        listV_persona = findViewById(R.id.lv_datosPersonas);

        inicializarfirebase();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombre = txtnombre.getText().toString();
        String correo = txtcorreo.getText().toString();
        String contrasena = txtcontrasena.getText().toString();
        String apellido = txtpellido.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:
                if(nombre.equals("") || contrasena.equals("") || apellido.equals("")  || correo.equals("")  ){
                    validadcion();

                }else{
                    String id= UUID.randomUUID().toString();
                    Persona mipersona = new Persona(id,nombre,apellido,correo,contrasena);
                    databaseReference.child("Persona").child(mipersona.getUid()).setValue(mipersona);

                    Toast.makeText(getApplicationContext(), "Agregado",Toast.LENGTH_LONG).show();
                    limpiarcajas();

                }
                break;


            case R.id.icon_save:

                Toast.makeText(getApplicationContext(), "save",Toast.LENGTH_LONG).show();
                break;

            case R.id.icon_delete:
                Toast.makeText(getApplicationContext(), "delete ",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void limpiarcajas() {
        txtnombre.setText("");
        txtcorreo.setText("");
        txtcontrasena.setText("");
        txtpellido.setText("");
    }

    private void validadcion() {
        String nombre = txtnombre.getText().toString();
        String correo = txtcorreo.getText().toString();
        String contrasena = txtcontrasena.getText().toString();
        String apellido = txtpellido.getText().toString();

        if(nombre.equals("")){
            txtnombre.setError("Required");
        }else if(apellido.equals("")){
            txtpellido.setError("Required");
        }else if(correo.equals("")){
            txtcorreo.setError("Required");
        }else if(contrasena.equals("")){
            txtcontrasena.setError("Required");
        }
    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
