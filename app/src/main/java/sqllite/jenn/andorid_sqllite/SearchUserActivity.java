package sqllite.jenn.andorid_sqllite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import sqllite.jenn.andorid_sqllite.R;
import sqllite.jenn.andorid_sqllite.entities.User;
import sqllite.jenn.andorid_sqllite.model.UserDAO;


public class SearchUserActivity extends AppCompatActivity {

    //ahora los puedo declarar asi separandolos por comas y no hay tanto codigo
    private EditText etBuscarUsuario, etBuscarDocumento;
    private ListView listViewResultados;
    private Button btnBuscarUsuario, btnEliminarUsuario;
    private UserDAO userDAO;
    private User selectedUser; // aqui agarra el usuario seleccionado para eliminar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        etBuscarUsuario = findViewById(R.id.etBuscarUsuario);
        etBuscarDocumento = findViewById(R.id.etBuscarDocumento);
        listViewResultados = findViewById(R.id.listViewResultados);
        btnBuscarUsuario = findViewById(R.id.btnBuscarUsuario);
        btnEliminarUsuario = findViewById(R.id.btnEliminarUsuario);

        View rootView = findViewById(android.R.id.content); // Obtiene la vista raíz de la actividad
        userDAO = new UserDAO(this, rootView);

        btnBuscarUsuario.setOnClickListener(view -> buscarUsuario());

        btnEliminarUsuario.setOnClickListener(view -> {
            if (selectedUser != null) {
                eliminarUsuario(selectedUser);
            }
        });
    }
   //para buscar el usuariooo
    private void buscarUsuario() {
        String usuario = etBuscarUsuario.getText().toString();
        String documento = etBuscarDocumento.getText().toString();

        ArrayList<User> usuariosEncontrados = userDAO.buscarUsuarios(usuario, documento);

        if (!usuariosEncontrados.isEmpty()) {
            ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuariosEncontrados);
            listViewResultados.setAdapter(adapter);

            listViewResultados.setOnItemClickListener((parent, view, position, id) -> {
                selectedUser = usuariosEncontrados.get(position);
                Toast.makeText(this, "Usuario seleccionado: " + selectedUser.getNames(), Toast.LENGTH_SHORT).show();

                // AAAAAAAAAAAAAAAAAAAAAAAA
                etBuscarUsuario.setText(selectedUser.getUser());
                etBuscarDocumento.setText(String.valueOf(selectedUser.getDocument()));

                btnEliminarUsuario.setVisibility(View.VISIBLE);
            });

        } else {
            Toast.makeText(this, "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
            btnEliminarUsuario.setVisibility(View.GONE);
        }
    }
    //para eliminar el usuariooo
    private void eliminarUsuario(User user) {
        userDAO.eliminarUsuario(user);
        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
        buscarUsuario(); // Refrescar la lista
    }
}
