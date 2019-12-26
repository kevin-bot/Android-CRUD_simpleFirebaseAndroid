package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.crudfirebase.Model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Persona> listapersona = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterpersona;

    EditText txtnombre,txtpellido,txtcorreo,txtcontrasena;
    ListView listV_persona;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Persona personaSeleccionada;
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

        listarpersonas();


        listV_persona.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSeleccionada = (Persona) parent.getItemAtPosition(position);

                txtnombre.setText(personaSeleccionada.getNombre());
                txtcorreo.setText(personaSeleccionada.getCorreo());
                txtcontrasena.setText(personaSeleccionada.getContrasena());
                txtpellido.setText(personaSeleccionada.getApellido());
            }
        });
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
                Persona mipersona = new Persona();
                mipersona.setUid(personaSeleccionada.getUid());
                mipersona.setNombre(txtnombre.getText().toString().trim());
                mipersona.setApellido(txtpellido.getText().toString().trim());
                mipersona.setCorreo(txtcorreo.getText().toString().trim());
                mipersona.setContrasena(txtcontrasena.getText().toString().trim());
                databaseReference.child("Persona").child(mipersona.getUid()).setValue(mipersona);
                Toast.makeText(getApplicationContext(), "save",Toast.LENGTH_LONG).show();
                limpiarcajas();
                break;

            case R.id.icon_delete:
                Persona mipersonaDelete = new Persona();
                mipersonaDelete.setUid(personaSeleccionada.getUid());
                databaseReference.child("Persona").child(mipersonaDelete.getUid()).removeValue();
                limpiarcajas();

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
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarpersonas() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//instancia de datos
                    listapersona.clear();
                    for( DataSnapshot objdataSnapshot: dataSnapshot.getChildren()) {
                        Persona mipersonaListar = objdataSnapshot.getValue(Persona.class);
                        listapersona.add(mipersonaListar);

                        arrayAdapterpersona = new ArrayAdapter<Persona>(MainActivity.this,android.R.layout.simple_list_item_1, listapersona);
                        listV_persona.setAdapter(arrayAdapterpersona);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
