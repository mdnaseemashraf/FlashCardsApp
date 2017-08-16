package mdnaseemashraf.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Naseem Ashraf on 17-08-2017.
 */

public class FlashCardSQliteOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DB_NAME = "flashcards_db.sqlite";
    public static final String CARDS_TABLE = "cards";
    public static final String CARD_ID = "id";
    public static final String CARD_DATA = "data";
    public static final String CARD_CONTEXT = "context";
    public static final String CARD_DATE = "date";

    public FlashCardSQliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, VERSION);
    }

    public FlashCardSQliteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //not needed
    }

    //Create Table
    private void createTable(SQLiteDatabase db) {
        db.execSQL(
                "create table " + CARDS_TABLE + " (" +
                        CARD_ID + " integer primary key autoincrement not null," +
                        CARD_DATA + " text," +
                        CARD_CONTEXT + " text," +
                        CARD_DATE + " text" +
                        ");"
        );
    }

    public ArrayList<FlashCard> fetchCards(SQLiteDatabase sqldatabase)
    {
        Log.println(Log.ASSERT,"SQL-Helper","fetchCards() invoked");

        ArrayList<FlashCard> currentDeck = new ArrayList<FlashCard>();				//ArrayList to contain Tasks

        //Setting up Cursor to Table in Database with SQL
        Cursor cardsCursor = sqldatabase.query(
                CARDS_TABLE,
                new String[] {CARD_ID,CARD_DATA,CARD_CONTEXT, CARD_DATE},
                null, null, null, null, String.format("%s,%s,%s", CARD_CONTEXT, CARD_DATA, CARD_DATE));

        cardsCursor.moveToFirst();

        //Read data from Cursor
        if(! cardsCursor.isAfterLast())						//Check end of table
        {
            do{
                long id = cardsCursor.getLong(0);
                String data = cardsCursor.getString(1);
                String card_context = cardsCursor.getString(2);
                String card_date = cardsCursor.getString(3);

                FlashCard card = new FlashCard(id, data, card_context, card_date);
                currentDeck.add(card);
            } while(cardsCursor.moveToNext());				//Looking through every item in the cursor

            Log.println(Log.ASSERT,"SQL-Helper","Current Deck : " + currentDeck.toString());

            cardsCursor.close();							//Close Cursor
        }

        if(currentDeck.size()==0||currentDeck.isEmpty()||currentDeck.equals(null)){

            Log.println(Log.ASSERT,"SQL-Helper","Fetch Deck failed.");
            FlashCard blankCard = new FlashCard(0,"No Cards to Show", "NULL", "BLANK");
            currentDeck.add(blankCard);
            return currentDeck;
        }

        Log.println(Log.ASSERT,"SQL-Helper","returning Deck on fetchCards()");
        return currentDeck;
    }

    //Add New Card
    public void saveCard(FlashCard fc, SQLiteDatabase sqldatabase){
        assert(null != fc);						//Check card to be not Null

        Log.println(Log.ASSERT,"SQL-Helper","saveCard() invoked");

        ContentValues values = new ContentValues();
        values.put(CARD_DATA, fc.getData());
        values.put(CARD_CONTEXT, fc.getContextData());
        values.put(CARD_DATE, fc.getDateCreated());
        long id = sqldatabase.insert(CARDS_TABLE, null, values);
        fc.setId(id);

        Log.println(Log.ASSERT,"SQL-Helper","fetchCards() returned id:" + id);
    }

    //Save card to ArrayList
    public void updateCard(FlashCard fc, SQLiteDatabase sqldatabase){
        assert(null != fc);							//Check card to be not Null

        ContentValues values = new ContentValues();
        values.put(CARD_DATA, fc.getData());
        values.put(CARD_CONTEXT, fc.getContextData());
        values.put(CARD_DATE, fc.getDateCreated());
        long id = fc.getId();

        String where = String.format("%s = %d", CARD_ID, id);

        sqldatabase.update(CARDS_TABLE, values, where, null);
    }

    //Delete card
    public void deleteCard(long id, SQLiteDatabase sqldatabase){
        String where = String.format("%s in (%s)", CARD_ID, id);
        sqldatabase.delete(CARDS_TABLE, where, null);
    }
}
