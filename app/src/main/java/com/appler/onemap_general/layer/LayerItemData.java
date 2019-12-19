package com.appler.onemap_general.layer;

import java.util.Map;

/**
 * 图层类型 名称 置顶属性 实体类
 * */
public class LayerItemData {

    private int type;
    private String layerType;
    private Map<String, String> name;
    private String groupName;


    public LayerItemData() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "LayerItemData{" +
                "type=" + type +
                ", layerType='" + layerType + '\'' +
                ", name=" + name +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
