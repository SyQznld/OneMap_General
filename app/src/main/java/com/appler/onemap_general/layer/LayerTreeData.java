package com.appler.onemap_general.layer;

import java.util.List;


/**
 * 二级列表 实体类 填充图层树
 * */
public class LayerTreeData {

    private String groupName;
    private List<LayerItemData> layerItemData;

    public LayerTreeData() {
    }

    public LayerTreeData(String groupName, List<LayerItemData> layerItemData) {
        this.groupName = groupName;
        this.layerItemData = layerItemData;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<LayerItemData> getLayerItemData() {
        return layerItemData;
    }

    public void setLayerItemData(List<LayerItemData> layerItemData) {
        this.layerItemData = layerItemData;
    }

    @Override
    public String toString() {
        return "ModelLayerData{" +
                "groupName='" + groupName + '\'' +
                ", layerData=" + layerItemData +
                '}';
    }
}
