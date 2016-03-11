package com.example.gio.sqllexicon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection extends SQLiteOpenHelper {
    private static final String DB_PATH = "/data/data/com.example.gio.sqllexicon/databases/";
    private static final String DB_NAME = "lexicon.sqlite";
    private SQLiteDatabase myDataBase;
    private Context myContext = null;
    private static final int version = 1;

    public Connection(Context context) {
        super(context, DB_NAME, null, version);
        this.myContext = context;
    }
    //am metodis gamozaxebit sheiqmneba carieli baza default misamartshi romelsac shemdeg gadavawert chven bazas
    public void createDatabase() throws IOException {
        boolean dbExits = checkDatabase();
        if (!dbExits) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Failed to copy the database");
            }
        }


    }

    public void openDatabase() {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void copyDatabase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);//lokaluri bazis gaxsna shetanis nakadistvis
        String outFileName = DB_PATH + DB_NAME; // axalsheqmnil carieli bazis misamarti
        OutputStream myOutput = new FileOutputStream(outFileName);//carieli bazis gaxsna rogorc gamotanis nakadi
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();


    }

    public boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e) {
            Toast.makeText(myContext, "database doesnt exist yet", Toast.LENGTH_SHORT).show();

        }
        if (checkDB != null) {

            checkDB.close();
        }
        return checkDB != null;

    }

    public synchronized void close() {
        if (myDataBase != null) {

            myDataBase.close();
        }
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
