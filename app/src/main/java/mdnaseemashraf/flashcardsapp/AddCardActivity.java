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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import mdnaseemashraf.model.FlashCard;
import mdnaseemashraf.model.FlashCardSQliteOpenHelper;

public class AddCardActivity extends AppCompatActivity {

    private FloatingActionButton mFabAddCard;
    private EditText txtEnterCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        mFabAddCard = (FloatingActionButton) findViewById(R.id.fabAddCard);
        txtEnterCard = (EditText) findViewById(R.id.txtEnterCard);

        mFabAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData(getApplicationContext(), txtEnterCard.getText().toString());
                Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void addData(Context con, String data) {
        //Setup database link
        FlashCardSQliteOpenHelper helper = new FlashCardSQliteOpenHelper(con);
        SQLiteDatabase database = helper.getWritableDatabase();

        FlashCard fc = new FlashCard(data,"NULL", "Date");
        helper.saveCard(fc, database);
    }

}
