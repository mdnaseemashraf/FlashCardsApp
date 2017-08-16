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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mdnaseemashraf.model.FlashCard;
import mdnaseemashraf.model.FlashCardSQliteOpenHelper;
import swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity implements SwipeStack.SwipeStackListener {

    private Button mButtonLeft, mButtonRight;
    private FloatingActionButton mFab;

    private ArrayList<FlashCard> deck;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deck = new ArrayList<FlashCard>();
        fillWithData(getApplicationContext());

        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        mButtonLeft = (Button) findViewById(R.id.buttonSwipeLeft);
        mButtonRight = (Button) findViewById(R.id.buttonSwipeRight);
        mFab = (FloatingActionButton) findViewById(R.id.fabAdd);

        //Check note below
        //mButtonLeft.setOnClickListener(this);
        //mButtonRight.setOnClickListener(this);
        //mFab.setOnClickListener(this);

        mAdapter = new SwipeStackAdapter(deck);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        mSwipeStack.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {

                //mSwipeStack.getTopView().

                Toast.makeText(MainActivity.this, "Invoking Context", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        mButtonLeft.setEnabled(false);
        mButtonRight.setEnabled(false);
        mButtonLeft.setVisibility(View.GONE);
        mButtonRight.setVisibility(View.GONE);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.println(Log.ASSERT,"Main Activity","onResume() followed by fillWithData");

        //Works?
        //fillWithData(getApplicationContext());

        //Works
        fillWithData(getApplicationContext());
        mAdapter.notifyDataSetChanged();
    }

    private void fillWithData(Context con) {

        Log.println(Log.ASSERT,"MainActivity","fillWithData() invoked");

        //Setup database link
        FlashCardSQliteOpenHelper helper = new FlashCardSQliteOpenHelper(con);
        SQLiteDatabase database = helper.getWritableDatabase();

        /* NOTE: BUGGY CODE KILLS INITIALIZATION
        Initialise Task Array
        if(null == deck){
            deck = helper.fetchCards(database);
        }
        */
        deck = helper.fetchCards(database);
        Log.println(Log.ASSERT,"fillWithData()", deck.toString());
    }

    /* NOTE: Add View.OnClickListener to Activity
    @Override
    public void onClick(View v) {
        if (v.equals(mButtonLeft)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mButtonRight)) {
            mSwipeStack.swipeTopViewToRight();
        } else if (v.equals(mFab)) {
            mData.add(getString(R.string.dummy_fab));
            //TODO Build Custom Dialog to collect new Flashcard Data
            //TODO Save in SQLite DB
            mAdapter.notifyDataSetChanged();
        }
    }
    */

    //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuReset:
                mSwipeStack.resetStack();
                Snackbar.make(mFab, R.string.stack_reset, Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menuGitHub:
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://github.com/flschweiger/SwipeStack"));
                startActivity(browserIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position).getData();
        Toast.makeText(this, getString(R.string.view_swiped_right, swipedElement),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position).getData();
        Toast.makeText(this, getString(R.string.view_swiped_left, swipedElement),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
    }

    public class SwipeStackAdapter extends BaseAdapter {

        private ArrayList<FlashCard> deck;

        public SwipeStackAdapter(ArrayList<FlashCard> data) {
            this.deck = data;
        }

        @Override
        public int getCount() {
            return deck.size();
        }

        @Override
        public FlashCard getItem(int position) {
            return deck.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.card, parent, false);
            }

            TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(deck.get(position).getData());

            return convertView;
        }
    }
}
