package com.example.rippple.slice;

import com.andexert.rippleeffect.LogUtil;
import com.example.rippple.CustomListViewAdapter;
import com.example.rippple.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;

import java.util.ArrayList;

public class MainAbilitySlice extends AbilitySlice {

    private static final String TAG = "MainAbilitySlice";
    private ArrayList<String> sourcesArrayList = new ArrayList<String>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        IntentParams savedInstanceState = intent.getParams();
        setUIContent(ResourceTable.Layout_ability_main);

        sourcesArrayList.add("Samsung");
        sourcesArrayList.add("Android");
        sourcesArrayList.add("Google");
        sourcesArrayList.add("Asus");
        sourcesArrayList.add("Apple");
        sourcesArrayList.add("Samsung");
        sourcesArrayList.add("Android");
        sourcesArrayList.add("Google");
        sourcesArrayList.add("Asus");
        sourcesArrayList.add("Apple");
        sourcesArrayList.add("Samsung");
        sourcesArrayList.add("Android");
        sourcesArrayList.add("Google");
        sourcesArrayList.add("Asus");
        sourcesArrayList.add("Apple");

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
