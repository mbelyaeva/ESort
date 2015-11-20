package com.example.brett.esort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Brett on 11/20/2015.
 */
public class OrgsListAdapter extends BaseAdapter {
    private ArrayList mData = new ArrayList();
    private LayoutInflater mInflater;

    public OrgsListAdapter(Context mContext) {
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
            // We retrieve the text from the array
            String text = (String) String.valueOf(getItem(position));

            // Get the TextView we want to edit
            TextView theTextView = (TextView) theView.findViewById(R.id.picTextRowText);
            // Put the next TV Show into the TextView
            theTextView.setText(text);
            // Get the ImageView in the layout
            ImageView theImageView = (ImageView) theView.findViewById(R.id.picTextRowPic);
            // We can set a ImageView like this
            theImageView.setImageResource(R.drawable.badasslogo);

            return theView;
            }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout view;
            if (convertView == null) {
                view = (LinearLayout) mInflater.inflate(R.layout.my_orgs_row, parent, false);
            } else {
                view = (LinearLayout) convertView;
            }
            view = makeView(view, position, parent);
            return view;
    }

}
