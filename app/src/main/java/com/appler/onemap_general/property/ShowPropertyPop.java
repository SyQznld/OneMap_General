package com.appler.onemap_general.property;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.appler.onemap_general.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * 点查 弹框显示
 */
public class ShowPropertyPop extends PopupWindow {
    private View view;
    private Context context1;
    private PopupWindow popupWindow;
    private Button btn_guanbi;
    private ListView listView;
    private ListView list_info;

    private List<String> TitleDatas;
    private List<PropertyData> datasInfo;
    private PropertyListAdapter adapter;
    private WebView webView;


    public ShowPropertyPop(Context context, WebView webView) {
        super(context);
        this.context1 = context;
        this.webView = webView;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.pop_info, null);
    }

    public void params(List<String> group_list, String area, String info) {
        this.TitleDatas = group_list;
        datasInfo = new ArrayList<>();
        try {
            JSONObject js = new JSONObject(info);
            if (area.equals("批次数据")) {
                String substring = info.substring(1, info.length() - 1);
                if (substring.contains(",")){
                    String[] split = substring.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String s = split[i];
                        if (s.contains(":")){
                            String[] split1 = s.split(":");
                            if (split1.length > 1){
                                String key = split1[0].replace("\"","");
                                addParams(key, js);
                            }
                        }
                    }
                }else {
                    if (substring.contains(":")){
                        String[] split1 = substring.split(":");
                        if (split1.length > 1){
                            String key = split1[0];
                            addParams(key, js);
                        }
                    }
                }
//                {"Layer":"已批准","报批批次":"许昌市2017年第十一批城市建设用地","面积（㎡）":"74441.458","面积（亩）":"111.66","ID":"293","批次内编号":"5","项目名称":"72-2/72-3科技孵化器","备注":"已批准"}
                //"Layer": "已建", "报批批次": "许昌市2010年第一批城市违法用地补办手续", "面积（㎡）": 17491.873, "面积（亩）": 26.24, "ID": "1", "批次内编号": "8", "项目名称": "潘窑加工厂", "备注": "其他"
//                addParams("Layer", js);
//                addParams("报批批次", js);
//                addParams("面积（㎡）", js);
//                addParams("面积（亩）", js);
//                addParams("ID", js);
//                addParams("批次内编号", js);
//                addParams("项目名称", js);
//                addParams("备注", js);

            }
            if (area.equals("总体规划")) {
                addParams("用地性质", js);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 填充属性弹框 key-value键值对，与点查文件中对应匹配
     * */
    @SuppressLint("DefaultLocale")
    private void addParams(String parseKey, JSONObject js) throws JSONException {
        Log.i(TAG, "addParams parseKey: " + parseKey);
        final Map<String, String> map = ParamMapUtils.propertiseMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String map_key = entry.getKey();
            String name = entry.getValue();
            if (parseKey.equals(map_key)) {
                PropertyData params = new PropertyData();
                params.setName(name);
                String param = js.getString(parseKey);
                if (null == param || "null".equals(param)) {
                    param = "";
                }
                if (parseKey.contains("MJ") || parseKey.contains("面积")) {
                    Double mj = Double.valueOf(param);    //取两位小数位
                    param = String.format("%.2f", mj) + "平方米";
                }
                if (parseKey.contains("亩") || parseKey.contains("MS") || parseKey.contains("M")) {
                    Double mj = Double.valueOf(param);
                    param = String.format("%.2f", mj) + "亩";
                }
                params.setParam(param);
                datasInfo.add(params);
            }
        }

    }


    public void showpupwindow(View parent) {

        if (popupWindow == null) {
            listView = view.findViewById(R.id.list_tile);
            listView.setAdapter(new MylistviewAdapter(TitleDatas, context1));
            list_info = view.findViewById(R.id.list_info);
            adapter = new PropertyListAdapter(context1, datasInfo);
            list_info.setAdapter(adapter);


            btn_guanbi = view.findViewById(R.id.btn_guanbi);
            WindowManager manager = (WindowManager) context1
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = manager.getDefaultDisplay().getWidth();
            int hight = manager.getDefaultDisplay().getHeight();
            int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
            Log.i(TAG, "showpupwindow: " + wrapContent);
            if (wrapContent > (hight / 2)) {
                popupWindow = new PopupWindow(view, width * 2 / 3,
                        hight / 2, true);
            } else {
                popupWindow = new PopupWindow(view, width * 2 / 3,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true);
            }

            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            btn_guanbi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webView.loadUrl("javascript:removeLocation()");
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAsDropDown(parent, -200, 20);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    Intent intent = new Intent("2222");
                    intent.putExtra("contextName", TitleDatas.get(position));
                    context1.sendBroadcast(intent);
                }
            });

        } else {
            listView = view.findViewById(R.id.list_tile);
            list_info = view.findViewById(R.id.list_info);
            listView.setAdapter(new MylistviewAdapter(TitleDatas, context1));
            list_info.setAdapter(new PropertyListAdapter(context1, datasInfo));
            btn_guanbi = view.findViewById(R.id.btn_guanbi);
            btn_guanbi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webView.loadUrl("javascript:removeLocation()");
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAsDropDown(parent, -200, 20);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    Intent intent = new Intent("2222");
                    intent.putExtra("contextName", TitleDatas.get(position));
                    context1.sendBroadcast(intent);
                }
            });

        }

    }

    private class MylistviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<String> datas;


        public MylistviewAdapter(List<String> datas, Context context) {
            this.datas = datas;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_title, null);
                holder.tv_title =  convertView.findViewById(R.id.list_tilename);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(datas.get(position));
            return convertView;
        }

    }

    class ViewHolder {
        TextView tv_title;
    }

}
