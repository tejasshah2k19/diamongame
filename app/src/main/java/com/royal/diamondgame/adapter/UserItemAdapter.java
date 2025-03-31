package com.royal.diamondgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.royal.diamondgame.R;
import com.royal.diamondgame.model.UserModel;

import java.util.List;

public class UserItemAdapter extends  BaseAdapter {

    private Context context;
    private List<UserModel> itemList;

    public UserItemAdapter(Context context, List<UserModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(com.royal.diamondgame.R.layout.list_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.tvItemName);
        TextView creditTextView = convertView.findViewById(R.id.tvItemCredit);

        UserModel item = itemList.get(position);
        nameTextView.setText(item.getFirstName());
        creditTextView.setText(item.getCredit());

        return convertView;
    }
}//class
