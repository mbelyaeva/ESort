package com.example.brett.esort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

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

    public void addItem(final ParseObject item) {
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
        ParseObject org = (ParseObject)getItem(position);

        // We retrieve the text from the array
        String name = "" + org.getString("name");
        String code = "Join Code: " + (org.getInt("code"));

        TextView orgName = (TextView) theView.findViewById(R.id.orgRowName);
        orgName.setText(name);

        TextView orgCode = (TextView) theView.findViewById(R.id.orgRowCode);
        orgCode.setText(code);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.orgRowPic);
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
            view.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 160));
            view = makeView(view, position, parent);
            return view;
    }

}
