package sqllite.jenn.andorid_sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLite extends SQLiteOpenHelper {
    public ConexionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //mejor organizacion para que quepa en mi pantalla
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE users (" +
                "use_document INTEGER PRIMARY KEY, " +
                "use_names TEXT NOT NULL, " +
                "use_last_names TEXT NOT NULL, " +
                "use_user TEXT NOT NULL, " +
                "use_password TEXT NOT NULL, " +
                "use_status INTEGER NOT NULL)";
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

}
