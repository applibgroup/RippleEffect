/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.rippple;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;
import java.util.ArrayList;

/**
 * Custom List view adapter class for RippleView sample app.
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

    /**
     * Updating given list.
     *
     * @param stringArrayList string array lists
     */
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