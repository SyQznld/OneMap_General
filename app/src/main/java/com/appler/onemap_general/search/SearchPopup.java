package com.appler.onemap_general.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.appler.onemap_general.CommonUtil;
import com.appler.onemap_general.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SearchPopup extends PopupWindow {
    private static final String TAG = "SearchPopup";
    private PopupWindow popupWindow;
    private WebView mWebView;
    private Activity context;
    private LayoutInflater inflater;
    private View view;

    private POI data;

    private EditText et_search_input;
    private TextView tv_search_close;
    private ListView list_search;

    public SearchPopup(Activity context, WebView mWebView) {
        this.context = context;
        this.mWebView = mWebView;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.popup_search, null);

        initView();
        String json = CommonUtil.getJson("DMDZ.json", context);
        Log.i(TAG, "SearchPopup: "+json);

        Gson gson = new Gson();
        data = gson.fromJson(json, new TypeToken<POI>() {
        }.getType());
        Log.i(TAG, "SearchPopup: " + data);

    }

    public void showSearchPopup(View parent) {
        et_search_input.setText("");
        final SearchAdapter adapter = new SearchAdapter(context, data.getFeatures());
        list_search.setAdapter(adapter);

        et_search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter.setOnItemClick(new SearchAdapter.OnItemClick() {
            @Override
            public void toItemClick(String geometry, int item) {
                String substring = geometry.substring(1, geometry.length() - 1);
                Log.i(TAG, "toItemClick: " + substring);

//                ShowPop.cbDmdz.setChecked(true);
//                ShowPop.rbDmdz.setVisibility(View.VISIBLE);
//                mWebView.loadUrl("javascript:layer('cg_dmdz','true')");
                String[] split = substring.split(",");
                mWebView.loadUrl("javascript:zoomToLatlng('" + split[1] + "','" + split[0] + "')");
                popupWindow.dismiss();
            }
        });

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int heigh = manager.getDefaultDisplay().getHeight();

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, heigh / 2, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

    }

    private void initView() {
        et_search_input = (EditText) view.findViewById(R.id.et_search_input);
        tv_search_close = (TextView) view.findViewById(R.id.tv_search_close);
        list_search = (ListView) view.findViewById(R.id.list_search);

        tv_search_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }


}
