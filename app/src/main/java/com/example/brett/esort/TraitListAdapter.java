package com.example.brett.esort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Brett on 12/3/2015.
 */
public class TraitListAdapter extends BaseAdapter {
    private ArrayList mData = new ArrayList();
    private LayoutInflater mInflater;

    public TraitListAdapter(Context mContext) {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void removeItems() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    protected LinearLayout makeView(LinearLayout theView, int position, ViewGroup parent) {
        String trait = (position + 1) + ". " + getItem(position);

        TextView orgName = (TextView) theView.findViewById(R.id.userName);
        orgName.setText(trait);

        return theView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view;
        if (convertView == null) {
            view = (LinearLayout) mInflater.inflate(R.layout.team_users_row, parent, false);
        } else {
            view = (LinearLayout) convertView;
        }
        view.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 160));
        view = makeView(view, position, parent);
        return view;
    }

}
