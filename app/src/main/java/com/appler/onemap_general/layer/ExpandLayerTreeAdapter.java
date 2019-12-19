package com.appler.onemap_general.layer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.appler.onemap_general.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExpandLayerTreeAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<LayerTreeData> layerTreeDataList;

    private List<Map<Integer, Boolean>> listIsChecked = new ArrayList<Map<Integer, Boolean>>();// 存放已被选中的switch

    private WebView webView;
    private SaveLayerCheckBean saveLayerCheckBean;

    public ExpandLayerTreeAdapter(Context context, List<LayerTreeData> layerTreeDataList, WebView webView, SaveLayerCheckBean saveLayerCheckBean) {
        this.context = context;
        this.layerTreeDataList = layerTreeDataList;
        this.webView = webView;
        this.saveLayerCheckBean = saveLayerCheckBean;
        layoutInflater = LayoutInflater.from(context);

        //默认选中第一个
        if (saveLayerCheckBean.layerCheckedMap.size() == 0) {
            for (int i = 0; i < layerTreeDataList.size(); i++) {
                Map<Integer, Boolean> map = new HashMap<>();
                Map<Integer, Integer> map1 = new HashMap<>();
                for (int j = 0; j < layerTreeDataList.get(i).getLayerItemData().size(); j++) {
                    map.put(j, false);
                    map1.put(j, 0);
                }
                saveLayerCheckBean.layerCheckedMap.add(i, map);
            }

            //默认选中第一个
            Map map = saveLayerCheckBean.layerCheckedMap.get(0);
            map.put(0, true);
            saveLayerCheckBean.layerCheckedMap.set(0, map);

        }
        for (int i = 0; i < layerTreeDataList.size(); i++) {
            Map<Integer, Boolean> map = new HashMap<>();
            for (int j = 0; j < layerTreeDataList.get(i).getLayerItemData().size(); j++) {
                map.put(j, false);
            }
            listIsChecked.add(i, map);
        }

        Map map = listIsChecked.get(0);
        map.put(0, true);
        listIsChecked.set(0, map);
        LayerItemData layerData = layerTreeDataList.get(0).getLayerItemData().get(0);
        layerData.setType(1);
    }

    @Override
    public int getGroupCount() {
        return layerTreeDataList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return layerTreeDataList.get(i).getLayerItemData().size();
    }

    @Override
    public Object getGroup(int i) {
        return layerTreeDataList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return layerTreeDataList.get(i).getLayerItemData().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layer_group_item, viewGroup, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_groupname = view.findViewById(R.id.tv_group);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }

        LayerTreeData modelLayerPropertyData = layerTreeDataList.get(groupPosition);
        groupViewHolder.tv_groupname.setText(modelLayerPropertyData.getGroupName());

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final ChildViewHolder childViewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layer_child_item, viewGroup, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv_layer_name = view.findViewById(R.id.tv_layer_name);
            childViewHolder.cb_layer = view.findViewById(R.id.cb_layer);
            childViewHolder.rb_layer = view.findViewById(R.id.rb_layer);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }


        final List<LayerItemData> layerItemDataList = layerTreeDataList.get(groupPosition).getLayerItemData();
        final LayerItemData layerItemData = layerItemDataList.get(childPosition);
        final String layerType = layerItemData.getLayerType();

        final Map<String, String> name = layerItemData.getName();
        String key = "";
        String value = "";
        for (Map.Entry<String, String> entry : name.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (key != null) {
                break;
            }
        }
        childViewHolder.tv_layer_name.setText(value);

        childViewHolder.cb_layer.setOnCheckedChangeListener(null);

        final String finalKey = key;
        childViewHolder.cb_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childViewHolder.cb_layer.isChecked()) {
                    saveLayerCheckBean.layerCheckedMap.get(groupPosition).put(childPosition, true);
                    onLayerZhiDingTrue.zhidingTrue(childPosition, layerTreeDataList, groupPosition);

                    if (layerType.equals(LayerUtils.LAYER_TYPE_WAP)) {
                        webView.loadUrl("javascript:layer('" + finalKey + "','true')");
                    } else {
                        webView.loadUrl("javascript:loadLayerShp('" + finalKey + "',true)");
                    }

                    Map map = listIsChecked.get(groupPosition);
                    map.put(childPosition, true);
                    listIsChecked.set(groupPosition, map);
                } else {
                    saveLayerCheckBean.layerCheckedMap.get(groupPosition).put(childPosition, false);
                    onLayerZhiDingFalse.zhidingFalse(childPosition, layerItemDataList);

                    if (layerType.equals(LayerUtils.LAYER_TYPE_WAP)) {
                        webView.loadUrl("javascript:layer('" + finalKey + "','false')");
                    } else {
                        webView.loadUrl("javascript:loadLayerShp('" + finalKey + "',false)");
                    }

                    Map map = listIsChecked.get(groupPosition);
                    map.put(childPosition, false);
                    listIsChecked.set(groupPosition, map);
                }
            }
        });


        /**
         *判断置顶按钮 显示以及颜色
         * */
        //选中
        if (listIsChecked.get(groupPosition).get(childPosition)) {
            childViewHolder.cb_layer.setChecked(true);

            childViewHolder.rb_layer.setVisibility(View.VISIBLE);
            //选中并且置顶
            if (layerItemData.getType() == 1) {
                childViewHolder.rb_layer.setTextColor(Color.parseColor("#FFFD4080"));
            } else {
                //选中不置顶
                childViewHolder.rb_layer.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            //不选中
            childViewHolder.cb_layer.setChecked(false);
            childViewHolder.rb_layer.setVisibility(View.INVISIBLE);
        }

        childViewHolder.rb_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayerZhiDingTrue.zhidingTrue(childPosition, layerTreeDataList, groupPosition);
                webView.loadUrl("javascript:zhiding('" + finalKey + "')");
            }
        });

        return view;
    }


    class GroupViewHolder {
        TextView tv_groupname;
    }


    class ChildViewHolder {

        TextView tv_layer_name;
        CheckBox cb_layer;
        RadioButton rb_layer;
    }

    private OnLayerZhiDingTrue onLayerZhiDingTrue;

    public void setOnLayerZhiDingTrue(OnLayerZhiDingTrue onLayerZhiDingTrue) {
        this.onLayerZhiDingTrue = onLayerZhiDingTrue;
    }

    public interface OnLayerZhiDingTrue {
        void zhidingTrue(int childPosition, List<LayerTreeData> modelLayerPropertyDataList, int groupPosition);
    }


    private OnLayerZhiDingFalse onLayerZhiDingFalse;

    public void setOnLayerZhiDingFalse(OnLayerZhiDingFalse onLayerZhiDingFalse) {
        this.onLayerZhiDingFalse = onLayerZhiDingFalse;
    }

    public interface OnLayerZhiDingFalse {
        void zhidingFalse(int position, List<LayerItemData> layerDataList);
    }
}
