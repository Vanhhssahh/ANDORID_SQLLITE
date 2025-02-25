package sqllite.jenn.andorid_sqllite;

//NOMBRE: Jennifer Vanessa Organista Paz
//ID: 785957

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import sqllite.jenn.andorid_sqllite.entities.User;
import sqllite.jenn.andorid_sqllite.model.UserDAO;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContraseña;
    private ListView listUsers;
    SQLiteDatabase sqLiteDatabase;
    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnLimpiar;
    private Button btnListUsers;
    private int documento;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String pass;
    private UserDAO userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        this.context = getApplicationContext();

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        begin();

        // Inicializar botones
        btnGuardar = findViewById(R.id.btnRegistrar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnListUsers = findViewById(R.id.btnListar);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        // Asignar eventos
        if (btnGuardar != null) {
            btnGuardar.setOnClickListener(this::createUser);
        }
        //añadi esto al boton buscar mejor (recordar para continuar) !!!
        if (btnBuscar != null) {
            btnBuscar.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, sqllite.jenn.andorid_sqllite.SearchUserActivity.class);
                startActivity(intent);
            });
        }
        if (btnListUsers != null) {
            btnListUsers.setOnClickListener(this::listUserShow);
        }
        if (btnLimpiar != null) {
            btnLimpiar.setOnClickListener(this::clearFields);
        }
    }

    private void listUserShow(View view) {
        UserDAO userDao = new UserDAO(this.context, findViewById(R.id.listView));
        ArrayList<User> userList = userDao.getUsersList();

        if (userList != null && !userList.isEmpty()) {
            ArrayAdapter<User> adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1, userList);
            listUsers.setAdapter(adapter);


            listUsers.setOnItemClickListener((parent, view1, position, id) -> {
                User selectedUser = (User) parent.getItemAtPosition(position);

                etUsuario.setText(selectedUser.getUser());
                etDocumento.setText(String.valueOf(selectedUser.getDocument()));
                etNombres.setText(selectedUser.getNames());
                etApellidos.setText(selectedUser.getLastNames());
                etContraseña.setText(selectedUser.getPassword());

                Toast.makeText(this, "Usuario seleccionado: " + selectedUser.getNames(), Toast.LENGTH_SHORT).show();
            });





        } else {
            listUsers.setAdapter(null);
        }
    }

    private void createUser(View view) {
        catchData();
        UserDAO dao = new UserDAO(this.context, view);
        User existingUser = dao.getUserByDocument(String.valueOf(this.documento));

        if (existingUser != null) {
            existingUser.setNames(this.nombres);
            existingUser.setLastNames(this.apellidos);
            existingUser.setUser(this.usuario);
            existingUser.setPassword(this.pass);
            dao.updateUser(existingUser);
            Snackbar.make(view, "Usuario actualizado correctamente", Snackbar.LENGTH_LONG).show();
        } else {
            User newUser = new User(this.documento, this.nombres, this.apellidos, this.usuario, this.pass);
            dao.getInsertUser(newUser);
            Snackbar.make(view, "Usuario registrado correctamente", Snackbar.LENGTH_LONG).show();
        }
        listUserShow(view);
    }

    private void clearFields(View view) {
        etDocumento.setText("");
        etUsuario.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etContraseña.setText("");
        Toast.makeText(this.context, "Campos limpiados", Toast.LENGTH_SHORT).show();
    }

    private void catchData() {
        this.documento = Integer.parseInt(etDocumento.getText().toString());
        this.nombres = etNombres.getText().toString();
        this.apellidos = etApellidos.getText().toString();
        this.usuario = etUsuario.getText().toString();
        this.pass = etContraseña.getText().toString();
    }

    private void begin() {
        this.btnGuardar = findViewById(R.id.btnRegistrar);
        this.btnListUsers = findViewById(R.id.btnListar);
        this.etNombres = findViewById(R.id.etNombres);
        this.etApellidos = findViewById(R.id.etApellidos);
        this.etDocumento = findViewById(R.id.etDocumento);
        this.etUsuario = findViewById(R.id.etUsuario);
        this.etContraseña = findViewById(R.id.etContraseña);
        this.listUsers = findViewById(R.id.listView);
    }
}
