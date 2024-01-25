package com.aad.mytodolist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String idUser;
    private FirebaseFirestore db;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    ListView listViewTareas;
    ArrayAdapter<String> adaptarTareas;
    List<String> listaTareas = new ArrayList<>();
    List<String> listaIdTareas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        idUser = mAuth.getCurrentUser().getEmail();
        listViewTareas = findViewById(R.id.listTareas);
        db = FirebaseFirestore.getInstance();
        actualizarUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Manejo de las opciones del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mas) {

            final EditText taskEditText = new EditText(this);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.nueva_tarea)
                    .setView(taskEditText)
                    .setPositiveButton(R.string.anadir, (dialogInterface, i) -> {

                        //Añadir tarea a Firebase

                        String miTarea = taskEditText.getText().toString().trim();
                        if (!miTarea.isEmpty()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("nombreTarea", miTarea);
                            data.put("usuario", idUser);

                            db.collection("Tareas")
                                    .add(data)
                                    .addOnSuccessListener(documentReference ->
                                            StyleableToast.makeText(MainActivity.this, "Tarea creada",
                                                    Toast.LENGTH_SHORT, R.style.tareaCreada).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(MainActivity.this, R.string.fallo_tarea, Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(MainActivity.this, R.string.tarea_vacia, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, null)
                    .create();
            dialog.show();
            return true;

        } else if (item.getItemId() == R.id.logout) {
            //Cerrar sesión en Firebase y Google
            mAuth.signOut();
            gsc.signOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    // Actualización con las tareas almacenadas en Firestore
    private void actualizarUI() {
        db.collection("Tareas")
                .whereEqualTo("usuario", idUser)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        return;
                    }
                    listaTareas.clear();
                    listaIdTareas.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        listaTareas.add(doc.getString("nombreTarea"));
                        listaIdTareas.add(doc.getId());
                    }

                    //Rellenar ListView con Adapter
                    if (listaTareas.size() == 0) {
                        listViewTareas.setAdapter(null);
                    } else {
                        adaptarTareas = new ArrayAdapter<>(MainActivity.this, R.layout.item_tarea,
                                R.id.textViewTarea, listaTareas);
                        listViewTareas.setAdapter(adaptarTareas);
                    }
                });
    }

    public void borrarTarea(View view) {
        View parent = (View) view.getParent();
        final TextView tareaTextView = parent.findViewById(R.id.textViewTarea);
        String tarea = tareaTextView.getText().toString();
        int posicion = listaTareas.indexOf(tarea);

        // Animación de borrado
        ObjectAnimator strikeThrough = ObjectAnimator.ofFloat(tareaTextView, "alpha", 1f, 0f);
        strikeThrough.setDuration(1000);
        strikeThrough.setInterpolator(new AccelerateDecelerateInterpolator());

        // Listener para ejecutar el borrado al acabar la animación
        strikeThrough.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                db.collection("Tareas").document(listaIdTareas.get(posicion)).delete();
            }
        });

        //Inicia la animación
        strikeThrough.start();
    }

    private void mostrarDialogoActualizarTarea(final int position, String tareaActual) {
        final EditText tareaEditText = new EditText(this);
        tareaEditText.setText(tareaActual);

        new AlertDialog.Builder(this)
                .setTitle(R.string.actualizar_tarea)
                .setView(tareaEditText)
                .setPositiveButton(R.string.guardar, (dialogInterface, i) -> {
                    // Actualizar la tarea en Firebase
                    String nuevaTarea = tareaEditText.getText().toString().trim();

                    if (!nuevaTarea.isEmpty()) {
                        String taskId = listaIdTareas.get(position);

                        Map<String, Object> data = new HashMap<>();
                        data.put("nombreTarea", nuevaTarea);

                        db.collection("Tareas")
                                .document(taskId)
                                .update(data)
                                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this,
                                        R.string.tarea_actualizada, Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(MainActivity.this,
                                        R.string.fallo_actualizar_tarea, Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(MainActivity.this, R.string.tarea_vacia, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    // Método para actualizar la tarea clickando sobre el texto
    public void onTextViewClick(View view) {
        int position = listViewTareas.getPositionForView(view);
        TextView textViewTarea = view.findViewById(R.id.textViewTarea);
        mostrarDialogoActualizarTarea(position, textViewTarea.getText().toString());
    }
}