package com.example.gio.sqllexicon;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DataDB dictionaryDatabase;
    private EditText inputText;
    private  Spinner languageSpinner;
    private String[] spinnerName=new String[2];


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        spinnerName[0]=getString(R.string.eng_geo);
        spinnerName[1]=getString(R.string.geo_eng);
        dictionaryDatabase = new DataDB(MainActivity.this);
        initRecyclerView();
        initSpinList();
        inputText = (EditText) findViewById(R.id.enter_text);
        inputText.setGravity(Gravity.START);
        inputText.addTextChangedListener(editTextListener);
    }


    AdapterView.OnItemSelectedListener langViewListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) languageSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.cardview_dark_background));
            if (languageSpinner.getSelectedItem().toString() == spinnerName[1]) {
                inputText.setText("");
                Typeface type = Typeface.createFromAsset(getAssets(), "acadnusx.ttf");
                inputText.setTypeface(type);
            }
            if (languageSpinner.getSelectedItem().toString() == spinnerName[0]) {
                inputText.setText("");
                inputText.setTypeface(Typeface.DEFAULT);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    TextWatcher editTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            RecyclerView recycler1 = (RecyclerView) findViewById(R.id.recycler);
            recycler1.setAdapter(null);
            if (languageSpinner.getSelectedItem().toString() == spinnerName[0]) {
                if (s.length() > 2) {
                    dictionaryDatabase.getListsEngGeo(inputText);
                    CustomAdapter adap = new CustomAdapter(dictionaryDatabase.englishDisplayList, dictionaryDatabase.georgianDisplayList);
                    recycler1.setAdapter(adap);

                }
            }
            else {
                if (s.length() > 2) {
                    dictionaryDatabase.getListsGeoEng(inputText);
                    CustomAdapter adap = new CustomAdapter(dictionaryDatabase.georgianDisplayList, dictionaryDatabase.englishDisplayList);
                    recycler1.setAdapter(adap);

                }


            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private void initRecyclerView(){
        RecyclerView recycler;
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager layoutManager;
        recycler = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(dictionaryDatabase.englishDisplayList, dictionaryDatabase.georgianDisplayList);
        recycler.setAdapter(adapter);
    }

    private void initSpinList(){
        String spinList[] = new String[2];
        spinList[0] = getString(R.string.eng_geo);
        spinList[1] = getString(R.string.geo_eng);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinList);
        languageSpinner=(Spinner) findViewById(R.id.spinner);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setOnItemSelectedListener(langViewListener);
    }

}
