package com.ngocdiem.exerciseweek5;

/**
 * Created by NDiem on 4/2/2018.
 */

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    public void refresh() {

        realm.setAutoRefresh(true);
    }

    public void clearAll() {

        realm.beginTransaction();
        realm.delete(Task.class);
        realm.commitTransaction();
    }
    public RealmResults<Task> getTasks() {

        return realm.where(Task.class).findAll();
    }

    //query a single item with the given id
    public Task getTasks(String id) {

        return realm.where(Task.class).equalTo("id", id).findFirst();
    }
    public boolean hasTasks() {

        return !realm.where(Task.class).findAll().isEmpty();
    }

    //query example
    public RealmResults<Task> queryedTasks() {

        return realm.where(Task.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}