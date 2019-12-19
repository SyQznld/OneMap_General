package com.appler.onemap_general.layer;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;

import com.appler.onemap_general.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 图层树 PopupWindow
 */
public class ShowLayerPop extends PopupWindow {
    private static final String TAG = "ShowPop";

    private WebView webView;
    private LayoutInflater inflater;
    private Context context;
    private View view;
    private PopupWindow popupwindow;

    private ExpandableListView elv_layer;
    private List<LayerTreeData> layerTreeDataList = new ArrayList<>();
    private SaveLayerCheckBean saveLayerCheckBean = new SaveLayerCheckBean();

    public ShowLayerPop(Context context, WebView webView, SaveLayerCheckBean saveLayerCheckBean) {
        this.context = context;
        this.webView = webView;
        this.saveLayerCheckBean = saveLayerCheckBean;

        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.layer_pop_layout, null);

        elv_layer = view.findViewById(R.id.elv_listview);


        initData(context, saveLayerCheckBean);
    }


    private void initData(Context context, SaveLayerCheckBean saveLayerCheckBean) {

//
//        //读取本地配置文件 图层树目录 二级列表显示
//        String layerConfigPath = CommonUtil.getStoragePath(context, true) + File.separator + "图层配置测试.txt";
//        List<String> rootList = new ArrayList<>();
//        List<LayerItemData> layerItemList = new ArrayList<>();
//        if (new File(layerConfigPath).exists()) {
//            //按行读取
//            try {
//                File file = new File(layerConfigPath);
//                InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
//                BufferedReader bf = new BufferedReader(inputReader);
//                // 按行读取字符串
//                String str;
//                String groupName = null;
//                while ((str = bf.readLine()) != null) {
//
//                    if (!"".equals(str.trim())) {
//                        if (!str.trim().contains(" ")) {
//                            rootList.add(str);
//                            groupName = str;
//                        } else if (str.trim().contains(" ")) {
//
//                            String[] s = CommonUtil.removeUnnecessarySpace(str).split(" ");
//                            String layerName, layerSDName, layerType;
//                            if (s.length > 2) {
//                                layerName = s[0].trim();
//                                layerSDName = s[1].trim();
//                                layerType = s[2].trim();
//                            } else {
//                                layerName = s[0].trim();
//                                layerSDName = s[1].trim();
//                                layerType = LayerUtils.LAYER_TYPE_WAP;
//                            }
//
//                            Map<String, String> layerItem = new HashMap<>();
//                            layerItem.put(layerSDName, layerName);
//
//                            LayerItemData itemData = new LayerItemData();
//                            itemData.setName(layerItem);
//                            itemData.setType(0);
//                            itemData.setLayerType(layerType);
//                            itemData.setGroupName(groupName);
//                            layerItemList.add(itemData);
//                        }
//                    }
//                }
//                bf.close();
//                inputReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//
//            for (int i = 0; i < rootList.size(); i++) {
//                String rootName = rootList.get(i);
//                List<LayerItemData> filterItemList = new ArrayList<>();
//                for (int j = 0; j < layerItemList.size(); j++) {
//                    if (layerItemList.get(j).getGroupName().equals(rootName)) {
//                        filterItemList.add(layerItemList.get(j));
//                    }
//                }
//                layerTreeDataList.add(new LayerTreeData(rootName, filterItemList));
//            }
//
//
//            setData(context, saveLayerCheckBean);
//
//        }else {
//            Toast.makeText(context, "请检查图层配置文件是否存在", Toast.LENGTH_SHORT).show();
//        }


        setData(context, saveLayerCheckBean);


    }

    private void setData(Context context, SaveLayerCheckBean saveLayerCheckBean) {
        LayerTreeData yx = new LayerTreeData("现状数据", LayerUtils.getXZLayers());
        LayerTreeData gh = new LayerTreeData("规划数据", LayerUtils.getGHLayers());
        LayerTreeData gt = new LayerTreeData("国土数据", LayerUtils.getGTLayers());
        layerTreeDataList.add(yx);
        layerTreeDataList.add(gh);
        layerTreeDataList.add(gt);
        final ExpandLayerTreeAdapter layerTreeAdapter = new ExpandLayerTreeAdapter(context, layerTreeDataList, webView, saveLayerCheckBean);
        elv_layer.setAdapter(layerTreeAdapter);
        //已经选中打开的图层显示打开状态
        int size = saveLayerCheckBean.layerCheckedMap.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Map<Integer, Boolean> map = saveLayerCheckBean.layerCheckedMap.get(i);
                for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
                    if (entry.getValue()) {
                        elv_layer.expandGroup(i);
                    }
                }
            }
        }


        layerTreeAdapter.setOnLayerZhiDingTrue(new ExpandLayerTreeAdapter.OnLayerZhiDingTrue() {
            @Override
            public void zhidingTrue(int childPosition, List<LayerTreeData> modelLayerDataLists, int groupPosition) {
                for (LayerTreeData modelLayerData : modelLayerDataLists) {
                    for (LayerItemData data : modelLayerData.getLayerItemData()) {
                        data.setType(0);
                    }
                }
                LayerItemData layerData1 = modelLayerDataLists.get(groupPosition).getLayerItemData().get(childPosition);
                layerData1.setType(1);
                layerTreeAdapter.notifyDataSetChanged();
            }
        });

        layerTreeAdapter.setOnLayerZhiDingFalse(new ExpandLayerTreeAdapter.OnLayerZhiDingFalse() {
            @Override
            public void zhidingFalse(int position, List<LayerItemData> layerDataList) {
                LayerItemData layerData1 = layerDataList.get(position);
                layerData1.setType(0);
                layerTreeAdapter.notifyDataSetChanged();
            }
        });
    }

    public void showPopupWindow(View parent) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        popupwindow = new PopupWindow(view, width / 3, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        popupwindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
        popupwindow.setTouchable(true);
        popupwindow.showAsDropDown(parent, 0, 20);
    }


}
