package com.ngocdiem.exerciseweek5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NDiem on 4/2/2018.
 */
public class TaskDetailsAdapter extends BaseAdapter {
    private ArrayList<Task> taskDetailsArrayList;
    private Context context;
    private LayoutInflater inflater;
    public TaskDetailsAdapter(Context context, ArrayList<Task> personDetailsArrayList) {
        this.context = context;
        this.taskDetailsArrayList = personDetailsArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return taskDetailsArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return taskDetailsArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
           // v = inflater.inflate(R.layout.inflate_list_item, null);
            holder = new Holder();
            holder.tvTaskName = (TextView) v.findViewById(R.id.tvTaskName);
//            holder.ivEditPesonDetail=(ImageView)v.findViewById(R.id.ivEditPesonDetail);
//            holder.ivDeletePerson=(ImageView)v.findViewById(R.id.ivDeletePerson);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        holder.tvTaskName.setText(taskDetailsArrayList.get(position).getName());
        return v;
    }
    class Holder {
        TextView tvTaskName;
    }
}