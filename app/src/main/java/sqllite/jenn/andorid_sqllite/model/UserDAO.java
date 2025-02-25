package sqllite.jenn.andorid_sqllite.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import sqllite.jenn.andorid_sqllite.entities.User;

public class UserDAO {
    //Declaracion de atributos -> objetos
    private ManagerDBUser dbUser;
    private Context context;
    private View view;

    //2. Constructor

    public UserDAO(Context context, View view) {
        this.context = context.getApplicationContext();
        this.view = view;
        this.dbUser = new ManagerDBUser(this.context);
    }

   // aqui la actualizacion de usuario
    public void updateUser(User user) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = dbUser.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("use_names", user.getNames());
            values.put("use_last_names", user.getLastNames());
            values.put("use_user", user.getUser());
            values.put("use_password", user.getPassword());

            int rowsUpdated = sqLiteDatabase.update("users", values, "use_document = ?", new String[]{String.valueOf(user.getDocument())});

            if (rowsUpdated > 0) {
                Snackbar.make(view, "Usuario actualizado con éxito", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, "Error al actualizar el usuario", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error en la actualización: " + e.getMessage());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
    }



    //encapsulacion de metodo
    private void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = dbUser.getWritableDatabase();
            if (sqLiteDatabase != null) {
                final int STATUS = 1;
                ContentValues values = new ContentValues();
                values.put("use_document", user.getDocument());
                values.put("use_names", user.getNames());
                values.put("use_last_names", user.getLastNames());
                values.put("use_user", user.getUser());
                values.put("use_password", user.getPassword());
                values.put("use_status", STATUS);

                long response = sqLiteDatabase.insert("users", null, values);

                if (response != -1) {
                    Snackbar.make(view, "Usuario registrado con ID: " + response, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "Error al registrar el usuario", Snackbar.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error en la gestión de la BD: " + e.getMessage());
            Snackbar.make(view, "Error en la BD: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close(); // aqui cierro la BD solo si está abierta !!! y ya me funcionaa
            }
        }
    }


    //metodo de acceso a insertUser
    public void getInsertUser(User user) {
        insertUser(user); // Ya está corregido y maneja bien la base de datosssssssssss no l o debo tocarr
    }

    // consulta de todos los usuarios
    public ArrayList<User> getUsersList() {
        ArrayList<User> listUsers = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;

        try {
            sqLiteDatabase = dbUser.getReadableDatabase();
            if (sqLiteDatabase != null) {
                String query = "SELECT use_document, use_names, use_last_names, use_user, use_password FROM users WHERE use_status = 1";
                cursor = sqLiteDatabase.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    do {
                        User user = new User();
                        user.setDocument(cursor.getInt(0));
                        user.setNames(cursor.getString(1));
                        user.setLastNames(cursor.getString(2));
                        user.setUser(cursor.getString(3));
                        user.setPassword(cursor.getString(4));
                        listUsers.add(user);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error en la consulta de la BD: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close(); // esto cierra el cursor solo si no está ya cerrado
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close(); // esto cierra la BD solo si está abierta
            }
        }

        return listUsers;
    }


    // metodo para buscar un usuario por su documento
    public User getUserByDocument(String documento) {
        SQLiteDatabase sqLiteDatabase = dbUser.getReadableDatabase();
        User user = null;

        try {
            String query = "SELECT use_document, use_names, use_last_names, use_user, use_password FROM users WHERE use_document = ? AND use_status = 1";
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{documento});

            if (cursor.moveToFirst()) {
                user = new User();
                user.setDocument(cursor.getInt(0));
                user.setNames(cursor.getString(1));
                user.setLastNames(cursor.getString(2));
                user.setUser(cursor.getString(3));
                user.setPassword(cursor.getString(4));
            }

            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error en la búsqueda de usuario: " + e.getMessage());
        }

        return user;
    }



    //probando que funciona mejor

    //metodo para buscar usuario por id y usuario

    public ArrayList<User> buscarUsuarios(String usuario, String documento) {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbUser.getReadableDatabase();

        String query = "SELECT * FROM users WHERE use_user LIKE ? OR use_document LIKE ?";
        String[] args = new String[]{"%" + usuario + "%", "%" + documento + "%"};

        Cursor cursor = db.rawQuery(query, args);

        if (cursor.moveToFirst()) {
            do {
                int document = cursor.getInt(0);
                String names = cursor.getString(1);
                String lastNames = cursor.getString(2);
                String user = cursor.getString(3);
                String password = cursor.getString(4);

                userList.add(new User(document, names, lastNames, user, password));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }
    //metodo para eliminar usuario
    public void eliminarUsuario(User user) {
        SQLiteDatabase db = dbUser.getWritableDatabase();
        db.delete("users", "use_document = ?", new String[]{String.valueOf(user.getDocument())});
        db.close();
    }


}
