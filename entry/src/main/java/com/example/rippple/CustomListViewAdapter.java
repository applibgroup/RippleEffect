/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Robin Chutaux
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.rippple;

import ohos.app.Context;
import java.util.ArrayList;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;

/**
 * Author :    Chutaux Robin
 * Date :      1/6/2015
 */
public class CustomListViewAdapter extends BaseItemProvider {

    private final ArrayList<String> textArrayList;

    private final LayoutScatter layoutInflater;

    public CustomListViewAdapter(final Context context) {
        this.layoutInflater = LayoutScatter.getInstance(context);
        this.textArrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return textArrayList.size();
    }

    @Override
    public String getItem(int position) {
        return textArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertView, ComponentContainer parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.parse(ResourceTable.Layout_row_item_view, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String param = CustomListViewAdapter.changeParamToString(textArrayList.get(position));
        viewHolder.textView.setText(param);
        return convertView;
    }

    public void updateList(ArrayList<String> stringArrayList) {
        this.textArrayList.clear();
        this.textArrayList.addAll(stringArrayList);
        this.notifyDataChanged();
    }

    private class ViewHolder {

        Text textView;

        public ViewHolder(Component v) {
            textView = (Text) v.findComponentById(ResourceTable.Id_text);
        }
    }

    public static String changeParamToString(CharSequence charSequence) {
        String convertToString = charSequence.toString();
        return convertToString;
    }
}