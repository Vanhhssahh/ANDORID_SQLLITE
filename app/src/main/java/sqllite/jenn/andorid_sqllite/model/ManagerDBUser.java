package sqllite.jenn.andorid_sqllite.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ManagerDBUser extends SQLiteOpenHelper {

    //Declaracion de atributos

    private static final String DATA_BASE="dbUsersAndroidActividadEnClase";
    private static final int VERSION = 1;
    private static final String TABLE_USERS ="users";

    private static final String QUERY_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ( " +
            "use_document INTEGER PRIMARY KEY, " +
            "use_names VARCHAR(150) NOT NULL, " +
            "use_last_names VARCHAR(150) NOT NULL, " +
            "use_user VARCHAR(100) NOT NULL, " +
            "use_password VARCHAR(25) NOT NULL, " +
            "use_status INTEGER NOT NULL );";


    //2. Constructor

    public ManagerDBUser(@Nullable Context context) {
        super(context, DATA_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(QUERY_TABLE_USERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        final  String DOWN_USER ="DROP TABLE IF EXISTS "+ TABLE_USERS;
        database.execSQL(DOWN_USER);
        onCreate(database);

    }
}
