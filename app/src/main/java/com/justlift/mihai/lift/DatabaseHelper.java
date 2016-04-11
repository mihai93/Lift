package com.justlift.mihai.lift;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mihai on 16-04-10.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

//    private static List<String> listDataHeader;
//    private static HashMap<String, List<String>> listDataChild;
    private static String DB_PATH = "/data/data/com.justlift.mihai.lift/databases/";
    private static String DB_NAME = "liftData";
    private SQLiteDatabase myDatabase;
    private final Context myContext;

    private static List<String> listDataHeader;
    private static HashMap<String, List<String>> listDataChild;

    // Database Info
    private static final String DATABASE_NAME = "liftData";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_EXERCISES = "exercises";
    private static final String TABLE_WORKOUTS = "workouts";
    private static final String TABLE_SETS = "sets";
    private static final String TABLE_WORKOUT_SETS = "workoutSets";
    private static final String TABLE_WORKOUT_TITLES = "workoutTitles";
    private static final String TABLE_TEMPLATE_TITLES = "templateTitles";
    private static final String TABLE_TEMPLATE_SETS = "templateSets";

    // Exercises Table Columns
    private static final String KEY_EXERCISE_ID = "_id";
    private static final String KEY_EXERCISE_NAME = "name";
    private static final String KEY_EXERCISE_MUSCLE = "muscle";

    // Workouts Table Columns
    private static final String KEY_WORKOUT_ID = "_id";
    private static final String KEY_WORKOUT_DATE = "date";

    // Sets Table Columns
    private static final String KEY_SET_ID = "_id";
    private static final String KEY_SET_EXERCISE_ID_FK = "exerciseId";
    private static final String KEY_SET_NUM = "num";
    private static final String KEY_SET_REPS = "reps";
    private static final String KEY_SET_WEIGHT = "weight";

    // Workout Sets Table Columns
    private static final String KEY_WORKOUT_SET_WORKOUT_ID_FK = "workoutId";
    private static final String KEY_WORKOUT_SET_SET_ID_FK = "setId";

    // Workout Titles Table Columns
    private static final String KEY_WORKOUT_TITLE_WORKOUT_ID_FK = "workoutId";
    private static final String KEY_WORKOUT_TITLE = "title";

    // Template Titles Table Columns
    private static final String KEY_TEMPLATE_ID = "_id";
    private static final String KEY_TEMPLATE_TITLE = "title";

    // Template Sets Table Columns
    private static final String KEY_TEMPLATE_SET_TEMPLATE_ID_FK = "templateId";
    private static final String KEY_TEMPLATE_SET_SET_ID_FK = "setId";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void createDatabase() throws IOException{

        boolean dbExist = checkDatabase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            File f = new File(DB_PATH);
            if (!f.exists()) {
                f.mkdir();
            }
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                this.close();
                copyDatabase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    private boolean checkDatabase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDatabase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDatabase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDatabase != null)
            myDatabase.close();

        super.close();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public static List<String> returnHeader(int fragmentNum) {
//        int dayWrtFirstDayOfWeek = fragmentNum - 7;
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
//        cal.add(Calendar.DAY_OF_WEEK, dayWrtFirstDayOfWeek);
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//        // Get yyyy-MM-dd format for current fragment's date
//        String date = df.format(cal.getTime());
//
//        return listDataHeader;
//    }

//    public String returnExerciseName()
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "Select * FROM " + TABLE_EXERCISES + " WHERE "
//                + KEY_EXERCISE_ID + " = 1";
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c != null)
//            c.moveToFirst();
//
//        String exerciseName;
//        exerciseName = c.getString(c.getColumnIndex(KEY_EXERCISE_NAME));
//        return exerciseName;
//
//    }

    public String getDate(int fragmentNum){
        int dayWrtFirstDayOfWeek = fragmentNum - 7;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, dayWrtFirstDayOfWeek);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Get yyyy-MM-dd format for current fragment's date
        return df.format(cal.getTime());
    }

    public boolean workoutExists(int fragmentNum)
    {
        boolean exists;
        String date = getDate(fragmentNum);

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE "
                + KEY_WORKOUT_DATE + " = '" + date + "'";

        Log.e("DatabaseHelper", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() <=0)
        {
            Log.e("DatabaseHelper", "Workout doesn't exist on " + date + "!");
            exists = false;
        } else{
            Log.e("DatabaseHelper", "Workout already exists on " + date + "!");
            exists = true;
        }

        return exists;
    }

    // called when trying to add exercise and workout doing exist for current date
    public void createWorkout(int fragmentNum){
        SQLiteDatabase db = this.getReadableDatabase();

        long rowId;
        ContentValues values = new ContentValues();
        values.put(KEY_WORKOUT_DATE, getDate(fragmentNum));
        rowId = db.insert(TABLE_WORKOUTS, null, values);

        Log.e("DatabaseHelper","New workout created on " + getDate(fragmentNum)
            + ". Row ID: " + rowId);
    }

    public long getWorkoutId(int fragmentNum){
        String date = getDate(fragmentNum);

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE "
                + KEY_WORKOUT_DATE + " = '" + date + "'";

        Log.e("DatabaseHelper", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        long rowId = c.getLong(c.getColumnIndex(KEY_WORKOUT_ID));
        Log.e("DatabaseHelper","The rowId for the workout is _id = " + rowId);
        return rowId;
    }

    public void addExerciseToTable(String exerciseName)
    {
        String exerciseNamePulledFromDb;
        long rowId;
        SQLiteDatabase db = this.getReadableDatabase();

        rowId = 0;

        // if exercise doesn't exist
        if (!exerciseExists(exerciseName))
        {
            Log.e("DatabaseHelper", "Exercise doesn't yet exist in DB! Going to create entry now");
            // Exercise doesn't exist yet in exercises table, add to table and get corresponding id
            // create sets entry with exercise id
            ContentValues values = new ContentValues();
            values.put(KEY_EXERCISE_NAME, exerciseName);
            rowId = db.insert(TABLE_EXERCISES, null, values);
            Log.e("DatabaseHelper", "Successfully created entry in table! id = " + Long.toString(id));
        } else {
            Log.e("DatabaseHelper", "Exercise already exists in database! No need to add");
        }
    }

    public long getExerciseId(String exerciseName){
        long rowId;
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EXERCISES + " WHERE "
                + KEY_EXERCISE_NAME + " = '" + exerciseName + "'";

        Log.e("DatabaseHelper", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        rowId = c.getLong(c.getColumnIndex(KEY_EXERCISE_ID));
        Log.e("DatabaseHelper","The rowId for the exercise is _id = " + rowId);
        return rowId;
    }

    public void addExercise(int fragmentNum, String exerciseName){
        boolean firstAdd;
        long exerciseId;
        long workoutId;
        // Check if exercise exists in exercise table
        if (!exerciseExists(exerciseName)){
            // doesn't exist, add to exercise table
            addExerciseToTable(exerciseName);
        }

        exerciseId = getExerciseId(exerciseName);

        if(!workoutExists(fragmentNum)){
            createWorkout(fragmentNum);
        }

        workoutId = getWorkoutId(fragmentNum);


    }

    public long createNewExerciseSet(long exerciseId){
        SQLiteDatabase db = this.getReadableDatabase();

        long rowId;
        ContentValues values = new ContentValues();
        values.put(KEY_SET_EXERCISE_ID_FK, exerciseId);
        values.put(KEY_SET_NUM, 1);
        values.put(KEY_SET_REPS, 8);
        values.put(KEY_SET_WEIGHT, )
        rowId = db.insert(TABLE_SETS, null, values);

        return rowId;
    }

    // determines if an exercise already exists in the table
    public boolean exerciseExists(String exerciseName)
    {
        boolean exists = true;
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EXERCISES + " WHERE "
                + KEY_EXERCISE_NAME + " = '" + exerciseName + "'";

        Log.e("DatabaseHelper", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() <=0)
        {
            exists = false;
        }

        return exists;
    }
}