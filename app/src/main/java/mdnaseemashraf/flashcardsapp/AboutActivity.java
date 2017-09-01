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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import mdnaseemashraf.colorpicker.ColorPickerDialog;
import mdnaseemashraf.model.FlashCard;
import mdnaseemashraf.model.FlashCardSQliteOpenHelper;

public class AddCardActivity extends AppCompatActivity {

    private FloatingActionButton mFabAddCard;
    private EditText txtEnterCard;
    private ImageView imageView;

    int forecolor = Color.parseColor("#000000");
    int backcolor = Color.parseColor("#FFFFFF");

    public static int CAMERA_REQUEST = 123;
    String mCurrentPhotoPath;
    private Uri mImageUri = Uri.EMPTY;
    String picPath;

    private static int RESULT_LOAD_IMAGE = 1;

    private CardView cView;

    //23+
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static void verifyPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if ((permission != PackageManager.PERMISSION_GRANTED) && (permission2 != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        mFabAddCard = (FloatingActionButton) findViewById(R.id.fabAddCard);
        txtEnterCard = (EditText) findViewById(R.id.txtEnterCard);

        cView = (CardView) findViewById(R.id.cardview2);

        imageView = (ImageView) findViewById(R.id.imageAddViewCard);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //23+
        verifyPermissions(this);

        picPath = "";

        mFabAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!picPath.isEmpty()||!picPath.equals(""))
                {
                    addData(getApplicationContext(), "Pic:"+picPath);
                }
                else {
                    addData(getApplicationContext(), txtEnterCard.getText().toString());
                }

                picPath = "";

                Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addData(Context con, String data) {
        FlashCardSQliteOpenHelper helper = new FlashCardSQliteOpenHelper(con);
        SQLiteDatabase database = helper.getWritableDatabase();

        FlashCard fc = new FlashCard(data,"NULL", "Date", forecolor, backcolor);
        helper.saveCard(fc, database);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.forecolor:

                showColorPickerDialogDemo(0);

                return true;

            case R.id.backcolor:

                showColorPickerDialogDemo(1);

                return true;

            case R.id.Attach:

                PopupMenu popup = new PopupMenu(AddCardActivity.this, findViewById(R.id.Attach));
                popup.getMenuInflater().inflate(R.menu.attachment_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().toString().equalsIgnoreCase("Camera")) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                Log.e("error", "error creating file"); Log.e("error-data", ex.toString());
                            }
                            if (photoFile != null) {
                                mImageUri = Uri.fromFile(photoFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(photoFile));

                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        }
                        else if(item.getTitle().toString().equalsIgnoreCase("Gallery")) {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }

                        return true;
                    }
                });

                popup.show();

                return true;
        }
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                txtEnterCard.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(Uri.parse(mImageUri.toString()));

                picPath = mImageUri.toString();

            }catch(Exception e){
                //Log.e("Error","Uri not set");
            }
        }else if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){

            txtEnterCard.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picPath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picPath));
        }
        else {
            mImageUri = Uri.EMPTY;
        }

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // filename
                ".png",         // extension
                storageDir      // folder
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void showColorPickerDialogDemo(final int inputtype) {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            public void onColorSelected(int color) {
                if(inputtype == 0)
                {
                    forecolor = color;
                }
                else
                {
                    backcolor = color;
                    cView.setBackgroundColor(color);
                }

                setColoredText(forecolor, backcolor);
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
