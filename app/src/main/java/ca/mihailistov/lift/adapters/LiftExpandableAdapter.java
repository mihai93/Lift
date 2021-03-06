package ca.mihailistov.lift.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import ca.mihailistov.lift.R;
import ca.mihailistov.lift.realm.RealmExercise;
import ca.mihailistov.lift.realm.RealmExerciseData;
import ca.mihailistov.lift.realm.RealmSet;
import io.realm.Realm;

/**
 * Created by mihai on 2016-11-26.
 */

public class LiftExpandableAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "LiftExpandableAdapter";
    private Realm realm;
    private final LayoutInflater inf;
    private List<RealmExercise> listExercises;
    private HashMap<RealmExercise, List<RealmSet>> listSetData;

    public LiftExpandableAdapter(Context context, List<RealmExercise> listExercises){
        realm = Realm.getDefaultInstance();
        this.listExercises = listExercises;
        inf = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount(){
        return this.listExercises.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (this.listExercises.get(i).realmSets == null)
            return 1;
        else
            return this.listExercises.get(i).realmSets.size();
    }

    @Override
    public RealmExerciseData getGroup (int i) {
        return this.listExercises.get(i).realmExerciseData;
    }

    @Override
    public RealmSet getChild(int i, int i1){
        Log.e(TAG, "Attempting to getChild...");
        RealmSet realmSet = null;
        try {
            realmSet = this.listExercises.get(i).realmSets.get(i1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (realmSet == null) {
            Log.e(TAG, "realmSet not found");
            realmSet = new RealmSet();
            realmSet.reps = 0;
            realmSet.weight = 0;
        }

        return realmSet;
    }

    @Override
    public long getGroupId(int i){
        return i;
    }

    @Override
    public long getChildId(int i, int i1){
        return i1;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            view = inf.inflate(R.layout.list_item_exercise, viewGroup, false);

            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.exercise_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (getGroup(i) != null)
            holder.text.setText(getGroup(i).name);

        ExpandableListView elv = (ExpandableListView) viewGroup;
        elv.expandGroup(i);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup){

        if (view == null){
            view = inf.inflate(R.layout.list_item_set, null);
        }

        TextView childSetNum = (TextView) view.findViewById(R.id.childSetNum);
        TextView childSetReps = (TextView) view.findViewById(R.id.childSetReps);
        TextView childSetWeight = (TextView) view.findViewById(R.id.childSetWeight);
        TextView lbsTv = (TextView) view.findViewById(R.id.lbsTv);
        TextView repsTv = (TextView) view.findViewById(R.id.repsTv);

        RealmSet childSet = getChild(i, i1);
        if (childSet.reps != 0 && childSet.weight != 0) {
            childSetNum.setText(String.valueOf(i1+1));
            childSetWeight.setText(String.valueOf(childSet.weight));
            childSetReps.setText(String.valueOf(childSet.reps));
            lbsTv.setVisibility(View.VISIBLE);
            repsTv.setVisibility(View.VISIBLE);
            childSetReps.setVisibility(View.VISIBLE);
            childSetWeight.setVisibility(View.VISIBLE);

        } else {
            childSetNum.setText("+");
            childSetWeight.setText("Click to add set.");
            childSetReps.setVisibility(View.GONE);
            lbsTv.setVisibility(View.GONE);
            repsTv.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1){
        return true;
    }

    private class ViewHolder {
        TextView text;
    }
}