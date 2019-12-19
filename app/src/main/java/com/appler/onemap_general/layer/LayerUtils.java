package com.appler.onemap_general.layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LayerUtils {
    private static final String TAG = "LayerUtils";
    public static final String LAYER_TYPE_SHP = "shp";
    public static final String LAYER_TYPE_WAP = "wap";



    public static List<LayerItemData> getXZLayers() {
        List<LayerItemData> datas = new ArrayList<>();
        Map<String, String> zsyx = new HashMap<String, String>();
        zsyx.put("yuCheng_zsyx", "正射影像");
        LayerItemData a1 = new LayerItemData();
        a1.setName(zsyx);
        a1.setType(0);
        a1.setLayerType(LAYER_TYPE_WAP);

        Map<String, String> dxt = new HashMap<String, String>();
        dxt.put("yuCheng_dxt", "地形图");
        LayerItemData a2 = new LayerItemData();
        a2.setName(dxt);
        a2.setType(0);
        a2.setLayerType(LAYER_TYPE_WAP);

        datas.add(a1);
        datas.add(a2);
        return datas;
    }



    public static List<LayerItemData> getGHLayers() {
        List<LayerItemData> datas = new ArrayList<>();
        Map<String, String> ztgh = new HashMap<String, String>();
        ztgh.put("yuCheng_ztgh", "总体规划");
        Map<String, String> lmlw = new HashMap<String, String>();
        lmlw.put("yuCheng_lmlw", "路名路网");

        LayerItemData a1 = new LayerItemData();
        a1.setName(ztgh);
        a1.setType(0);
        a1.setLayerType(LayerUtils.LAYER_TYPE_WAP);

        LayerItemData a2 = new LayerItemData();
        a2.setName(lmlw);
        a2.setType(0);
        a2.setLayerType(LAYER_TYPE_WAP);

        datas.add(a1);
        datas.add(a2);
        return datas;
    }




    public static List<LayerItemData> getGTLayers() {
        List<LayerItemData> datas = new ArrayList<>();
        Map<String, String> tdlyxz = new HashMap<String, String>();
        tdlyxz.put("yuCheng_tdlyxz", "土地利用现状");
        LayerItemData a1 = new LayerItemData();
        a1.setName(tdlyxz);
        a1.setType(0);
        a1.setLayerType(LAYER_TYPE_WAP);
        datas.add(a1);
        return datas;
    }






}
