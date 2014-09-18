package com.aviatainc.webviewwithnativenav;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrivera on 9/17/2014.
 */
public class MenuItemsAdapter extends ArrayAdapter<String> {

    private List<String> menuItems = new ArrayList<String>();

    public MenuItemsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

        TypedArray menuResources = context.getResources().obtainTypedArray(R.array.menu_items);

        TypedArray itemDef;
        for (int i = 0; i < menuResources.length(); i++) {
            int resId = menuResources.getResourceId(i, -1);
            if (resId < 0) {
                continue;
            }
            itemDef = context.getResources().obtainTypedArray(resId);
            menuItems.add(itemDef.getString(0));
        }

    }

    public String getLink(Context context, int number) {
        //return getResources().getStringArray(R.array.drawerlinkitems)[number];

        ArrayList<String> menuLinks = new ArrayList<String>();
        TypedArray menuResources = context.getResources().obtainTypedArray(R.array.menu_items);

        TypedArray itemDef;
        for (int i = 0; i < menuResources.length(); i++) {
            int resId = menuResources.getResourceId(i, -1);
            if (resId < 0) {
                continue;
            }
            itemDef = context.getResources().obtainTypedArray(resId);
            menuLinks.add(itemDef.getString(1));
        }

        return menuLinks.get(number);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int i) {
        return menuItems.get(i);
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    public void setNewMenuItems(List<String> newMenuItems) {
        this.menuItems = newMenuItems;
        notifyDataSetChanged();
    }

}
