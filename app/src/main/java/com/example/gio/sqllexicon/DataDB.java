package com.example.gio.sqllexicon;

import android.content.Context;
import java.util.HashMap;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;
import java.lang.String;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;

public class DataDB {
    private static final HashMap<String, String> MAP;
    private static final String[] English_Letters_Array = {",", " ", "\r", "\n", "a", "A", "b", "B", "g", "G", "d", "D", "e", "E", "v", "V", "z", "Z", "T", "I",
            "i", "k", "K", "l", "L", "m", "M", "n", "N", "o", "O", "p", "P", "J", "r", "s", "t", "u", "U", "f", "F", "q", "Q", "R", "Y", "y", "S", "C", "c", "Z", "w",
            "W", "x", "j", "h", "H"};
    private static final String[] Georgian_Letters_Array = {",", " ", "\r", "\n", "ა", "ა", "ბ", "ბ", "გ", "გ", "დ", "დ", "ე", "ე", "ვ", "ვ", "ზ", "ზ", "თ", "ი", "ი",
            "კ", "კ", "ლ", "ლ", "მ", "მ", "ნ", "ნ", "ო", "ო", "პ", "პ", "ჟ", "რ", "ს", "ტ", "უ", "უ", "ფ", "ფ", "ქ", "ქ", "ღ", "ყ", "ყ", "შ", "ჩ", "ც", "ძ", "წ", "ჭ", "ხ", "ჯ", "ჰ", "ჰ"};

    static {
        MAP = new HashMap<>();
        for (int i = 0; i < Georgian_Letters_Array.length; i++) {
            MAP.put(English_Letters_Array[i], Georgian_Letters_Array[i]);
        }
    }

    private SQLiteDatabase db;
    private Cursor tableCursor;
    private final StringBuilder translatedWordsBuilder = new StringBuilder();
    private int idToWordsCounter, commaCounter;
    private final List<String> translatedWordsIds = new ArrayList<>();
    private final List<String> suggestedWordsIds = new ArrayList<>();
    public final List<String> englishDisplayList = new ArrayList<>();
    public final List<String> georgianDisplayList = new ArrayList<>();
    private Connection connection;

    public DataDB(Context context) {
        connection = new Connection(context);
        try {
            connection.createDatabase();
        } catch (IOException e) {
            Toast.makeText(context, "cannot create database", Toast.LENGTH_SHORT).show();
        }
    }

    public void getListsEngGeo(EditText x) {
        translatedWordsIds.clear();
        suggestedWordsIds.clear();
        georgianDisplayList.clear();
        englishDisplayList.clear();
        if (connection.checkDatabase()) {
            connection.openDatabase();
            db = connection.getWritableDatabase();
            tableCursor = db.rawQuery("SELECT id FROM eng WHERE LOWER(eng) LIKE LOWER(?)", new String[]{x.getText().toString() + '%'});
            while (tableCursor.moveToNext()) {
                suggestedWordsIds.add(tableCursor.getString(0));
            }
            tableCursor.moveToFirst();
            for (int i = 0; i < suggestedWordsIds.toArray().length; i++) {
                tableCursor = db.rawQuery("SELECT eng FROM eng WHERE id=?", new String[]{suggestedWordsIds.get(i)});
                while (tableCursor.moveToNext()) {
                    englishDisplayList.add(tableCursor.getString(0));
                }
            }
            tableCursor.moveToFirst();
            for (int j = 0; j < suggestedWordsIds.toArray().length; j++) {
                translatedWordsIds.clear();
                translatedWordsBuilder.delete(0, translatedWordsBuilder.length());
                tableCursor = db.rawQuery("SELECT geo_id FROM geo_eng WHERE eng_id=?", new String[]{suggestedWordsIds.get(j)});
                while (tableCursor.moveToNext()) {
                    translatedWordsIds.add(tableCursor.getString(0));
                }
                idToWordsCounter = translatedWordsIds.toArray().length;
                commaCounter = 0;
                for (; idToWordsCounter != 0; idToWordsCounter--) {

                    tableCursor = db.rawQuery("Select geo FROM geo WHERE id=?", new String[]{translatedWordsIds.get(idToWordsCounter - 1)});
                    tableCursor.moveToNext();
                    if (commaCounter > 0)
                        translatedWordsBuilder.append(",");
                    if (commaCounter != 0 && commaCounter % 2 == 0)
                        translatedWordsBuilder.append("\r\n");
                    translatedWordsBuilder.append(tableCursor.getString(0));
                    commaCounter++;
                }
                georgianDisplayList.add(georgianLettersConverter(translatedWordsBuilder.toString()));

            }
            connection.close();
        }
    }

    public void getListsGeoEng(EditText x) {
        translatedWordsIds.clear();
        suggestedWordsIds.clear();
        georgianDisplayList.clear();
        englishDisplayList.clear();
        if (connection.checkDatabase()) {
            connection.openDatabase();
            db = connection.getWritableDatabase();

            tableCursor = db.rawQuery("SELECT id FROM geo WHERE geo LIKE ?", new String[]{x.getText().toString() + '%'});
            while (tableCursor.moveToNext()) {
                suggestedWordsIds.add(tableCursor.getString(0));
            }
            tableCursor.moveToFirst();
            for (int i = 0; i < suggestedWordsIds.toArray().length; i++) {
                tableCursor = db.rawQuery("SELECT geo FROM geo WHERE id=?", new String[]{suggestedWordsIds.get(i)});
                while (tableCursor.moveToNext()) {
                    georgianDisplayList.add(georgianLettersConverter(tableCursor.getString(0)));
                }

            }
            tableCursor.moveToFirst();
            for (int j = 0; j < suggestedWordsIds.toArray().length; j++) {
                translatedWordsIds.clear();
                translatedWordsBuilder.delete(0, translatedWordsBuilder.length());
                tableCursor = db.rawQuery("SELECT eng_id FROM geo_eng WHERE geo_id=?", new String[]{suggestedWordsIds.get(j)});
                while (tableCursor.moveToNext()) {
                    translatedWordsIds.add(tableCursor.getString(0));
                }
                idToWordsCounter = translatedWordsIds.toArray().length;
                commaCounter = 0;
                for (; idToWordsCounter != 0; idToWordsCounter--) {

                    tableCursor = db.rawQuery("Select eng FROM eng WHERE id=?", new String[]{translatedWordsIds.get(idToWordsCounter - 1)});
                    tableCursor.moveToNext();
                    if (commaCounter > 0)
                        translatedWordsBuilder.append(",");
                    if (commaCounter != 0 && commaCounter % 2 == 0)
                        translatedWordsBuilder.append("\r\n");
                    translatedWordsBuilder.append(tableCursor.getString(0));
                    commaCounter++;


                }
                englishDisplayList.add(translatedWordsBuilder.toString());

            }
            connection.close();
        }
    }

    public String georgianLettersConverter(String x) {

        char[] array = x.toCharArray();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < x.length(); i++) {

            s.append(MAP.get(Character.toString(array[i])));
        }
        return s.toString();

    }


}
