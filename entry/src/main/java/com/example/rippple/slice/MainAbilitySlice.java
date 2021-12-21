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

package com.example.rippple.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import com.andexert.rippleeffect.LogUtil;
import com.example.rippple.CustomListViewAdapter;
import com.example.rippple.ResourceTable;
import java.util.ArrayList;

/**
 * MainAbilitySlice class for RippleView sample app.
 */
public class MainAbilitySlice extends AbilitySlice {

    private static final String TAG = "MainAbilitySlice";
    private static final String TEXT_SAMSUNG = "Samsung";
    private static final String TEXT_ANDROID = "Android";
    private static final String TEXT_GOOGLE = "Google";
    private static final String TEXT_ASUS = "Asus";
    private static final String TEXT_APPLE = "Apple";
    private ArrayList<String> sourcesArrayList = new ArrayList<>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);

        sourcesArrayList.add(TEXT_SAMSUNG);
        sourcesArrayList.add(TEXT_ANDROID);
        sourcesArrayList.add(TEXT_GOOGLE);
        sourcesArrayList.add(TEXT_ASUS);
        sourcesArrayList.add(TEXT_APPLE);
        sourcesArrayList.add(TEXT_SAMSUNG);
        sourcesArrayList.add(TEXT_ANDROID);
        sourcesArrayList.add(TEXT_GOOGLE);
        sourcesArrayList.add(TEXT_ASUS);
        sourcesArrayList.add(TEXT_APPLE);
        sourcesArrayList.add(TEXT_SAMSUNG);
        sourcesArrayList.add(TEXT_ANDROID);
        sourcesArrayList.add(TEXT_GOOGLE);
        sourcesArrayList.add(TEXT_ASUS);
        sourcesArrayList.add(TEXT_APPLE);

        ListContainer listView = (ListContainer) findComponentById(ResourceTable.Id_list_view);
        CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(this);
        customListViewAdapter.updateList(sourcesArrayList);
        listView.setItemProvider(customListViewAdapter);
        listView.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                LogUtil.info(TAG, "ListView tap item : " + i);
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
