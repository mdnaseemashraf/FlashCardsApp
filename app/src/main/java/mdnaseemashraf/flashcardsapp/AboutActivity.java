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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import mdnaseemashraf.colorpicker.ColorPickerDialog;
import mdnaseemashraf.model.FlashCard;
import mdnaseemashraf.model.FlashCardSQliteOpenHelper;

import static mdnaseemashraf.flashcardsapp.AddCardActivity.CAMERA_REQUEST;
import static mdnaseemashraf.flashcardsapp.AddCardActivity.verifyPermissions;
import static mdnaseemashraf.flashcardsapp.R.id.backcolor;
import static mdnaseemashraf.flashcardsapp.R.id.btnGit;
import static mdnaseemashraf.flashcardsapp.R.id.forecolor;
import static mdnaseemashraf.flashcardsapp.R.id.txtEnterCard;

public class AboutActivity extends AppCompatActivity {

    ImageButton btnGithub;
    ImageButton btnLinkedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnGithub = (ImageButton) findViewById(R.id.btnGit);
        btnLinkedin = (ImageButton) findViewById(R.id.btnLinkedin);

        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://goo.gl/i9GAFn"));
                startActivity(browserIntent);
            }
        });

        btnLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://goo.gl/fsBafw"));
                startActivity(browserIntent);
            }
        });
    }
}
