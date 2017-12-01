/*
 * Copyright (C) 2016 Frederik Schweiger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Created by Naseem Ashraf on 16-08-2017.
 */

package mdnaseemashraf.flashcardsapp;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import mdnaseemashraf.colorpicker.ColorPickerDialog;
import mdnaseemashraf.model.FlashCard;
import mdnaseemashraf.model.FlashCardSQliteOpenHelper;

public class EditCardActivity extends AppCompatActivity {

    private FloatingActionButton mFabSaveCard;
    private FloatingActionButton mFabDeleteCard;
    private EditText txtEnterCard;

    private CardView cView;

    long intent_id;
    String intent_data;
    String intent_context;
    String intent_date;
    int intent_fcolor = Color.parseColor("#000000");
    int intent_bcolor = Color.parseColor("#FFFFFF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        mFabSaveCard = (FloatingActionButton) findViewById(R.id.fabSaveCard);
        mFabDeleteCard = (FloatingActionButton) findViewById(R.id.fabDeleteCard);
        txtEnterCard = (EditText) findViewById(R.id.txtEnterCard);

        cView = (CardView) findViewById(R.id.cardview2);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intents = getIntent();
        intent_id = intents.getExtras().getLong("id");
        intent_data = intents.getExtras().getString("data");
        intent_context = intents.getExtras().getString("context");
        intent_date = intents.getExtras().getString("date");
        intent_fcolor = intents.getExtras().getInt("fcolor");
        intent_bcolor = intents.getExtras().getInt("bcolor");

        cView.setBackgroundColor(intent_bcolor);

        txtEnterCard.setVisibility(View.VISIBLE);
        txtEnterCard.setEnabled(true);
        txtEnterCard.setText(intent_data);

        setColoredText(intent_fcolor, intent_bcolor);

        mFabSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent_data = txtEnterCard.getText().toString();

                FlashCard tempFC = new FlashCard(intent_id, intent_data, intent_context,
                        intent_date, intent_fcolor, intent_bcolor);

                updateData(getApplicationContext(), tempFC);
                Intent intent = new Intent(EditCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mFabDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FlashCard tempFC = new FlashCard(intent_id, intent_data, intent_context,
                        intent_date, intent_fcolor, intent_bcolor);

                removeData(getApplicationContext(), tempFC);
                Intent intent = new Intent(EditCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateData(Context con, FlashCard inFC) {
        FlashCardSQliteOpenHelper helper = new FlashCardSQliteOpenHelper(con);
        SQLiteDatabase database = helper.getWritableDatabase();

        helper.updateCard(inFC, database);
    }

    private void removeData(Context con, FlashCard inFC) {
        FlashCardSQliteOpenHelper helper = new FlashCardSQliteOpenHelper(con);
        SQLiteDatabase database = helper.getWritableDatabase();

        helper.deleteCard(inFC.getId(),database);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.edit_card_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.forecolorEdit:

                showColorPickerDialogDemo(0);
                return true;

            case R.id.backcolorEdit:

                showColorPickerDialogDemo(1);
                return true;
        }
        return false;
    }

    private void showColorPickerDialogDemo(final int inputtype) {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor,
                new ColorPickerDialog.OnColorSelectedListener() {

            public void onColorSelected(int color) {
                if(inputtype == 0) {
                    intent_fcolor = color;
                }
                else {
                    intent_bcolor = color;
                    cView.setBackgroundColor(color);
                }
                setColoredText(intent_fcolor, intent_bcolor);
            }
        });

        colorPickerDialog.show();
    }

    private void setColoredText(String fulltext, int forecolor1, int backcolor1) {
        txtEnterCard.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) txtEnterCard.getText();
        str.setSpan(new ForegroundColorSpan(forecolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new BackgroundColorSpan(backcolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtEnterCard.setText(fulltext, TextView.BufferType.SPANNABLE);
        str = (Spannable) txtEnterCard.getText();
        str.setSpan(new ForegroundColorSpan(forecolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new BackgroundColorSpan(backcolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void setColoredText(int forecolor1, int backcolor1) {

        Spannable str = (Spannable) txtEnterCard.getText();
        str.setSpan(new ForegroundColorSpan(forecolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new BackgroundColorSpan(backcolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        str = (Spannable) txtEnterCard.getText();
        str.setSpan(new ForegroundColorSpan(forecolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new BackgroundColorSpan(backcolor1), 0, txtEnterCard.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
