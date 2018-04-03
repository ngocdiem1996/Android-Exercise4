package com.ngocdiem.exerciseweek5;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PlusOneFragment extends Fragment {
    private Button mPlusOneButton;
    private static int id = 1;
    private Realm myRealm;
    private ListView lvTaskList;
    private static ArrayList<Task> taskDetailsModelArrayList = new ArrayList<>();
    private TaskDetailsAdapter taskDetailsAdapter;
    private android.app.AlertDialog.Builder subDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus_one, container, false);
        mPlusOneButton = (Button) view.findViewById(R.id.btnAdd);
        mPlusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrUpdateTaskDetailsDialog(null,-1);
            }
        });
        try {
            myRealm = Realm.getDefaultInstance();

        } catch (Exception e) {

            // Get a Realm instance for this thread
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            myRealm = Realm.getInstance(config);

        }
//        setTaskDetailsAdapter();
//        getAllUsers();
        return view;


    }

        private void setTaskDetailsAdapter() {
        taskDetailsAdapter = new TaskDetailsAdapter(getActivity(), taskDetailsModelArrayList);
        lvTaskList.setAdapter(taskDetailsAdapter);
    }
    public void addOrUpdateTaskDetailsDialog(final Task model,final int position) {
        //subdialog
//        new AlertDialog.Builder(getActivity().getApplicationContext())
//                .setTitle("Delete entry")
//                .setMessage("Are you sure you want to delete this entry?")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //  deleteSuggestions(position);
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();

        //maindialog
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.prompt_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(getActivity());
        mainDialog.setView(promptsView);
        final EditText etAddTaskName = (EditText) promptsView.findViewById(R.id.etAddTaskName);
        if (model != null) {
            etAddTaskName.setText(model.getName());
        }
        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = mainDialog.create();
        dialog.show();
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isBlankField(etAddTaskName)) {
                    Task taskDetailsModel = new Task();
                    taskDetailsModel.setName(etAddTaskName.getText().toString());
                    if (model == null)
                        addDataToRealm(taskDetailsModel);
                    else
                        updateTaskDetails(taskDetailsModel, position, model.getId());
                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });
    }
    private void addDataToRealm(Task model) {
        myRealm.beginTransaction();
        Task taskDetailsModel = myRealm.createObject(Task.class);
        taskDetailsModel.setId(id);
        taskDetailsModel.setName(model.getName());
        taskDetailsModelArrayList.add(taskDetailsModel);
        myRealm.commitTransaction();
        taskDetailsAdapter.notifyDataSetChanged();
        id++;
    }
    public void deleteTask(int Id, int position) {
        RealmResults<Task> results = myRealm.where(Task.class).equalTo("id", Id).findAll();
        myRealm.beginTransaction();
        results.remove(0);
        myRealm.commitTransaction();
        taskDetailsModelArrayList.remove(position);
        taskDetailsAdapter.notifyDataSetChanged();
    }
    public Task searchTask(int Id) {
        RealmResults<Task> results = myRealm.where(Task.class).equalTo("id", Id).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return results.get(0);
    }
    public void updateTaskDetails(Task model,int position,int ID) {
        Task editTaskDetails = myRealm.where(Task.class).equalTo("id", ID).findFirst();
        myRealm.beginTransaction();
        editTaskDetails.setName(model.getName());
        myRealm.commitTransaction();
        taskDetailsModelArrayList.set(position, editTaskDetails);
        taskDetailsAdapter.notifyDataSetChanged();
    }
    private void getAllUsers() {
        RealmResults<Task> results = myRealm.where(Task.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            taskDetailsModelArrayList.add(results.get(i));
        }
        if(results.size()>0)
            id = myRealm.where(Task.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        taskDetailsAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        taskDetailsModelArrayList.clear();
        myRealm.close();
    }
}