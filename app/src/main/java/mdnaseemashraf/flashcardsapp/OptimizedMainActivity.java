/**
 * Created by MD Naseem Ashraf on 16-08-2017.
 * md.naseem.ashraf@gmail.com
 */

package mdnaseemashraf.flashcardsapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mdnaseemashraf.model.FlashCard;
import mdnaseemashraf.model.FlashCardSQliteOpenHelper;
import swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity implements SwipeStack.SwipeStackListener {

    private Button mButtonLeft, mButtonRight;
    private FloatingActionButton mFab;
    private FloatingActionButton mFabEdit;

    private ArrayList<FlashCard> deck;
    private int deckIndex;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;

    boolean imgStat;

    int foreground = Color.parseColor("#000000");
    int background = Color.parseColor("#FFFFFF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deck = new ArrayList<FlashCard>();
        deckIndex = 0;
        fillWithData(getApplicationContext());

        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        mButtonLeft = (Button) findViewById(R.id.buttonSwipeLeft);
        mButtonRight = (Button) findViewById(R.id.buttonSwipeRight);
        mFab = (FloatingActionButton) findViewById(R.id.fabAdd);
        mFabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);

        //Check note below
        //mButtonLeft.setOnClickListener(this);
        //mButtonRight.setOnClickListener(this);
        //mFab.setOnClickListener(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new SwipeStackAdapter(deck);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

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

        mFabEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.fabEdit));
                popup.getMenuInflater().inflate(R.menu.edit_this_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().toString().equalsIgnoreCase("Delete")) {
                            FlashCardSQliteOpenHelper helper = new FlashCardSQliteOpenHelper(getApplicationContext());
                            SQLiteDatabase database = helper.getWritableDatabase();

                            helper.deleteCard(deck.get(deckIndex).getId(),database);

                            Intent inter = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(inter);
                            finish();
                        }
                        else if(item.getTitle().toString().equalsIgnoreCase("Edit")) {
                            boolean isPics = false;
                            if(deck.get(deckIndex).getData().contains("Pic:"))
                            {
                                isPics = true;
                            }

                            if((!isPics)&&(deckIndex != -1)){

                                Intent intent = new Intent(MainActivity.this, EditCardActivity.class);

                                intent.putExtra("id",deck.get(deckIndex).getId())
                                        .putExtra("data",deck.get(deckIndex).getData())
                                        .putExtra("context",deck.get(deckIndex).getContextData())
                                        .putExtra("date",deck.get(deckIndex).getDateCreated())
                                        .putExtra("fcolor",deck.get(deckIndex).getForeColor())
                                        .putExtra("bcolor",deck.get(deckIndex).getBackColor());

                                startActivity(intent);
                                finish();
                            }
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Works
        fillWithData(getApplicationContext());
        mAdapter.notifyDataSetChanged();
    }

    private void fillWithData(Context con) {
        FlashCardSQliteOpenHelper helper = new FlashCardSQliteOpenHelper(con);
        SQLiteDatabase database = helper.getWritableDatabase();

        deck = helper.fetchCards(database);
        deckIndex = 0;
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
            mAdapter.notifyDataSetChanged();
        }
    }*/

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
                        Intent.ACTION_VIEW, Uri.parse("https://github.com/mdnaseemashraf"));
                startActivity(browserIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position).getData();
        deckIndex = position+1;
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position).getData();
        deckIndex = position+1;
    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
        deckIndex = -1;
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

            ImageView imgView = (ImageView) convertView.findViewById(R.id.imageViewCard);

            TextView textV = (TextView) convertView.findViewById(R.id.textViewCard);
            String datum = deck.get(position).getData();

            boolean blIsPic = false;

            if(datum.contains("Pic:"))
            {
                blIsPic = true;
                datum = datum.substring(4,datum.length());
                textV.setVisibility(View.GONE);
                imgView.setVisibility(View.VISIBLE);

                imgStat = true;//TODO Check for topview or allviews?

                imgView.setImageURI(Uri.parse(datum));
            }
            else {
                imgStat = false;//TODO Check for topview or allviews?
            }

            background = deck.get(position).getBackColor();
            foreground = deck.get(position).getForeColor();

            CardView cardView = (CardView) convertView.findViewById(R.id.theCard);
            cardView.setBackgroundColor(background);

            if(!blIsPic) {
                textV.setText(datum, TextView.BufferType.SPANNABLE);
                Spannable str = (Spannable) textV.getText();
                str.setSpan(new ForegroundColorSpan(foreground), 0, textV.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new BackgroundColorSpan(background), 0, textV.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textV.setText(datum, TextView.BufferType.SPANNABLE);
                str = (Spannable) textV.getText();
                str.setSpan(new ForegroundColorSpan(foreground), 0, textV.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                str.setSpan(new BackgroundColorSpan(background), 0, textV.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return convertView;
        }
    }
}
