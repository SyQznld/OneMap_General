var fx_pointArray = [];//存放坐标点
var fx_markerArray = [];//存放坐标点marker
var fx_polylineArray = [];//存放每条线
var fx_polygon = null;//存放画的面
var fx_polygonMarker = null;
var curr_zx_polygons=[];//存放当前加载的数据

function fx_measureEnd() {
    map.off("click",fx_mapClickPoint);
    map.off("click",fx_Draw_backout);
    map.off("dragend", mapDragEnd);
    
     var accordinates = "";
     if(fx_pointArray.length >2){
     for (var i = 0; i < fx_pointArray.length; i++) {
         accordinates += fx_pointArray[i].lng + " " + fx_pointArray[i].lat + ", ";
     }
     if (accordinates != "") {
         accordinates += fx_pointArray[0].lng + " " + fx_pointArray[0].lat;
         accordinates = "POLYGON ((" + accordinates + "))";
     }
        android.getArea(accordinates);
    }else{
        android.getArea("");
    }
}

function caiji_measureEnd() {
    map.off("click",fx_mapClickPoint);
    map.off("click",fx_Draw_backout);
    map.off("dragend", mapDragEnd);

     var accordinates = "";
     if(fx_pointArray.length >2){
     for (var i = 0; i < fx_pointArray.length; i++) {
         accordinates += fx_pointArray[i].lng + " " + fx_pointArray[i].lat + ", ";
     }
     if (accordinates != "") {
         accordinates += fx_pointArray[0].lng + " " + fx_pointArray[0].lat;
         accordinates = "POLYGON ((" + accordinates + "))";
     }
        android.getCaijiArea(accordinates);
    }else{
        android.getCaijiArea("");
    }
}

function fx_measureBack() {
    fx_measureClear();

    map.off("click",fx_mapClickPoint);
    map.off("click",mapClick);
    map.off("dragend", mapDragEnd);
    map.on("click",mapClick);
    map.on("dragend", mapDragEnd);
}

function fx_measure_area() {
    fx_measureClear();
    $(".measure-accordinate").show();
    map.off("click", fx_mapClickPoint);
    map.on("click", fx_mapClickPoint);
    map.off("click",mapClick);
    map.off("dragend", mapDragEnd);
}
function fx_measureClear() {
    map.off("click",mapClick);
    map.off("dragend", mapDragEnd);

//    fx_measureEnd();
    fx_pointArray = [];
    for (var i = 0; i < fx_polylineArray.length; i++) {
        if (map.hasLayer(fx_polylineArray[i])) {
            map.removeLayer(fx_polylineArray[i]);
        }
    }
    fx_polylineArray = [];
    for (var i = 0; i < fx_markerArray.length; i++) {
        if (map.hasLayer(fx_markerArray[i])) {
            map.removeLayer(fx_markerArray[i]);
        }
    }
    fx_markerArray = [];
    if (fx_polygon != null && map.hasLayer(fx_polygon)) {
        map.removeLayer(fx_polygon);
        fx_polygon = null;
    }
    if (fx_polygonMarker != null && map.hasLayer(fx_polygonMarker)) {
        map.removeLayer(fx_polygonMarker);
        fx_polygonMarker = null;
    }
}


function fx_getAccordinate() {
        var mapContainer = $("#map");
        var obj = { latlng: map.containerPointToLatLng([mapContainer.width() / 2, mapContainer.height() / 2]) };
        fx_mapClickPoint(obj);
}
function fx_mapClickPoint(e) {
        fx_pointArray.push(e.latlng);
        var newAddMarker = new AIMap.Marker(e.latlng, { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_marker" + (fx_pointArray.length == 1 ? "1" : "") + ".png'/></div>" }) });
        if (fx_pointArray.length > 1) {

            newAddMarker = new AIMap.Marker(e.latlng, { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_marker" + (fx_pointArray.length == 1 ? "1" : "") + ".png'/></div>" }) });
        }
        if (fx_pointArray.length == 2) {
            var newAddPolyline = new AIMap.Polyline([e.latlng, fx_pointArray[fx_pointArray.length - 2]], { color: 'red' });
            newAddPolyline.addTo(map);
            fx_polylineArray.push(newAddPolyline);
        }
        if (fx_pointArray.length > 2) {
            //先把线清空
            for (var i = 0; i < fx_polylineArray.length; i++) {
                if (map.hasLayer(fx_polylineArray[i])) {
                    map.removeLayer(fx_polylineArray[i]);
                }
            }
            fx_polylineArray = [];

            //画面
            if (fx_polygon != null && map.hasLayer(fx_polygon)) {
                map.removeLayer(fx_polygon);
                fx_polygon = null;
            }
            fx_polygon = new AIMap.Polygon(fx_pointArray, { color: 'red', fill:false});
            fx_polygon.addTo(map);
        }
        newAddMarker.addTo(map);
        fx_markerArray.push(newAddMarker);

        if (fx_pointArray.length > 2) {
            //重绘第一个点，注明最后一段距离长度
            if (fx_markerArray[0] != null && map.hasLayer(fx_markerArray[0])) {
                map.removeLayer(fx_markerArray[0]);

                newAddMarker = new AIMap.Marker(fx_pointArray[0], { icon: new AIMap.DivIcon({ html: "<div class='measure_marker'><img src='images/measure_marker1.png'/></div>" }) })
                fx_markerArray[0] = newAddMarker;
                map.addLayer(newAddMarker);
            }
        }

}

//画面 撤销一步
function fx_Draw_backout() {
    //移除最后一次添加的点
    if (fx_markerArray.length > 0) {
        map.removeLayer(fx_markerArray.pop());
    }
    //重新绘制多边形
    if(fx_pointArray.length == 1){
        fx_pointArray = [];
    }
    if (fx_pointArray.length == 2) {
        if (fx_polylineArray.length > 0) {
            if (fx_polylineArray[0] != null && map.hasLayer(fx_polylineArray[0])) {
                map.removeLayer(fx_polylineArray[0]);
                fx_polylineArray=[];
            }
        }
        fx_pointArray.pop();
    }
    if (fx_pointArray.length ==3) {
       fx_pointArray.pop();
       if (map.hasLayer(fx_polygon)) {
           map.removeLayer(fx_polygon);
       }
       var newAddPolyline = new AIMap.Polyline([fx_pointArray[0], fx_pointArray[1]], { color: 'red' });
           newAddPolyline.addTo(map);
           fx_polylineArray.push(newAddPolyline);
    }
    if(fx_pointArray.length >3){
            fx_pointArray.pop();
            var _pointArray = fx_pointArray;
            if (map.hasLayer(fx_polygon)) {
                map.removeLayer(fx_polygon);
            }
            fx_polygon = new AIMap.Polygon(_pointArray, { color: 'red', opacity: 0.5 });
            fx_polygon.addTo(map);
    }
}





//显示所有图斑信息  智能分析
var allQueryResultPolygons  = [];
function loadQueryResultData(data1) {
    clearQueryPolygons();
    var data = eval("(" + data1 + ")");
    if (data != null && data.Rows != null && data.Rows.length > 0) {
        var feature_length = data.Rows.length;
        var suijise = "blue";
        var cur_function_polys = [];
        var cur_function_polys_labels = [];
        for (var i = 0; i < feature_length; i++) {
            var _ll = [];
            var _geom = data.Rows[i].ogr_geometry;
            console.log(_geom);
            if (_geom.indexOf("MULTIPOLYGON ") != -1) {
                        _geom = _geom.replace("MULTIPOLYGON (((", "");
                        _geom = _geom.replace(")))", "");
                        _t = _geom.split(")), ((");
                        for (var m = 0; m < _t.length; m++) {
                            if (_t[m].indexOf("), (") != -1) {
                                var _ll1 = [];
                                var _t1 = _t[m].split("), (");
                                for (var p = 0; p < _t1.length; p++) {
                                    var _ll2 = [];
                                    var _t2 = _t1[p].split(", ");
                                    for (var n = 0; n < _t2.length; n++) {
                                        var _t3 = _t2[n].split(" ");
                                        _ll2.push([_t3[1], _t3[0]]);
                                    }
                                    _ll1.push(_ll2);
                                }
                                _ll.push(_ll1);
                            }
                            else {
                                var _ll1 = [];
                                var _t1 = _t[m].split(", ");
                                for (var n = 0; n < _t1.length; n++) {
                                    var _t2 = _t1[n].split(" ");
                                    _ll1.push([_t2[1], _t2[0]]);
                                }
                                _ll.push(_ll1);
                            }
                        }
                    } else {
                        _geom = _geom.replace("POLYGON ((", "");
                        _geom = _geom.replace("))", "");
                        _t = _geom.split("), (");
                        for (var ii = 0; ii < _t.length; ii++) {
                            var _ll1 = [];
                            var _t1 = _t[ii].split(", ");
                            for (var ij = 0; ij < _t1.length; ij++) {
                                var _t2 = _t1[ij].split(" ");
                                _ll1.push([_t2[1], _t2[0]]);
                            }
                            _ll.push(_ll1);
                        }
                    }

            var pg = new AIMap.Polygon(_ll, { weight: 2, fill: false, color: suijise, clickable: false });
            map.addLayer(pg);
            allQueryResultPolygons.push(pg);
        }
    }
}

function clearQueryPolygons() {
    if (allQueryResultPolygons.length > 0) {
        for (var i = 0; i < allQueryResultPolygons.length; i++) {
            if (allQueryResultPolygons[i] && map.hasLayer(allQueryResultPolygons[i])) {
                map.removeLayer(allQueryResultPolygons[i]);
            }
        }
        allQueryResultPolygons = [];
    }
}



















//显示单个图斑
var dingweiPolygon;
function showFW(accordinate){
  fx_clearLocationPolygon();
if (accordinate != "" && accordinate.length > 0) {
    var _ll = [];
    var _geom = accordinate;
    var _t;
    if (_geom.indexOf("MULTIPOLYGON ") != -1) {

        _geom = _geom.replace("MULTIPOLYGON (((", "");
        _geom = _geom.replace(")))", "");
        _t = _geom.split(")), ((");
    } else {
        _geom = _geom.replace("POLYGON ((", "");
        _geom = _geom.replace("))", "");
        _t = _geom.split("), (");
    }
    for (var ii = 0; ii < _t.length; ii++) {
        var _ll1 = [];
        var _t1 = _t[ii].split(", ");
        for (var ij = 0; ij < _t1.length; ij++) {
            var _t2 = _t1[ij].split(" ");
            _ll1.push([_t2[1], _t2[0]]);
        }
        _ll.push(_ll1);
    }
    if (_t.length == 1) {
        _ll = _ll[0];
    }

     dingweiPolygon = new AIMap.Polygon(_ll, { color: 'red', weight: 4, opacity: 1, fill: false, clickable: false });
     map.addLayer(dingweiPolygon);
     map.setView(dingweiPolygon.getBounds().getCenter(), map.getBoundsZoom(dingweiPolygon.getBounds()));
//    map.setView(dingweiPolygon.getBounds().getCenter(), 3);
}
}

function fx_clearLocationPolygon(){
 if (map.hasLayer(dingweiPolygon)) {
      map.removeLayer(dingweiPolygon);
    }
}







//显示在线图层图斑
var zx_dingweiPolygon;
function zx_showTuBan(accordinate){
//  var cur_function_polys = [];
//  zx_clearLocationPolygon();
if (accordinate != "" && accordinate.length > 0) {
    var _ll = [];
    var _geom = accordinate;
    var _t;
    if (_geom.indexOf("MULTIPOLYGON ") != -1) {
        _geom = _geom.replace("MULTIPOLYGON (((", "");
        _geom = _geom.replace(")))", "");
        _t = _geom.split(")), ((");
    } else {
        _geom = _geom.replace("POLYGON ((", "");
        _geom = _geom.replace("))", "");
        _t = _geom.split("), (");
    }
    for (var ii = 0; ii < _t.length; ii++) {
        var _ll1 = [];
        var _t1 = _t[ii].split(", ");
        for (var ij = 0; ij < _t1.length; ij++) {
            var _t2 = _t1[ij].split(" ");
            _ll1.push([_t2[1], _t2[0]]);
        }
        _ll.push(_ll1);
    }
    if (_t.length == 1) {
        _ll = _ll[0];
    }

     zx_dingweiPolygon = new AIMap.Polygon(_ll, { weight: 4, fillOpacity: 0.2,fillColor: "yellow", color: "blue", clickable: true });
     map.addLayer(zx_dingweiPolygon);
     map.setView(zx_dingweiPolygon.getBounds().getCenter(), map.getBoundsZoom(zx_dingweiPolygon.getBounds()));

 }
}




function zx_clearLocationPolygon(){
 if (map.hasLayer(zx_dingweiPolygon)) {
      map.removeLayer(zx_dingweiPolygon);
    }
}







//显示所有图斑信息  在线图层
function zx_loadQueryResultData(data1,name) {
console.log("======load_name======"+name);
//    zx_clearQueryPolygons();
    var data = eval("(" + data1 + ")");
var zx_allQueryResultPolygons  = [];
    var suijise = "blue";
    var cur_function_polys = [];
    var cur_function_polys_labels = [];
    if (data != null && data.length != null && data.length > 0) {
        var data_length=data.length;
        for(var j=0;j<data_length;j++){
            //二级结构，嵌套数组
          var shp_length=data[j].shp.length;
          for(var m=0;m<shp_length;m++ ){
          var _ll = [];
          var ogr_fid = data[j].shp[m].ogr_fid;
          var _geom = data[j].shp[m].ogr_geometry;
          var zxtc_type = data[j].type;
//          var _geom = data[j].ogr_geometry;
//          var ogr_fid = data[j].ogr_fid;
//          var zxtc_type = zxtc_type1;
          var _t;
          if (_geom.indexOf("MULTIPOLYGON ") != -1) {
              _geom = _geom.replace("MULTIPOLYGON (((", "");
              _geom = _geom.replace(")))", "");
              _t = _geom.split(")), ((");
          } else {
              _geom = _geom.replace("POLYGON ((", "");
              _geom = _geom.replace("))", "");
              _t = _geom.split("), (");
          }
          for (var ii = 0; ii < _t.length; ii++) {
              var _ll1 = [];
              var _t1 = _t[ii].split(", ");
              for (var ij = 0; ij < _t1.length; ij++) {
                  var _t2 = _t1[ij].split(" ");
                  _ll1.push([_t2[1], _t2[0]]);
              }
              _ll.push(_ll1);
          }
          if (_t.length == 1) {
              _ll = _ll[0];
          }
          var pg = new AIMap.Polygon(_ll, {
          zxtc_type:zxtc_type,
          ogr_geometry:_geom,
          ogr_fid:ogr_fid,
          weight: 2, fillOpacity: 0.1,fillColor: "yellow", color: "blue", clickable: true });
          pg.on('click', pgClick);
          map.addLayer(pg);
          zx_allQueryResultPolygons.push(pg);
          }
               var _layer = { layer: zx_allQueryResultPolygons, name: name };
               curr_zx_polygons.push(_layer);
        }
    }
}

function zx_clearQueryPolygons(name) {
console.log("=====name====="+name);
    if (curr_zx_polygons.length > 0) {
        for (var i = 0; i < curr_zx_polygons.length; i++) {
            if (curr_zx_polygons[i].name==name) {
                for(var j=0;j<curr_zx_polygons[i].layer.length;j++){
                    map.removeLayer(curr_zx_polygons[i].layer[j]);
                }
                curr_zx_polygons.splice(i,1);
            }
        }
        //zx_allQueryResultPolygons = [];
    }
}




//function zx_clearQueryPolygons() {
//    if (zx_allQueryResultPolygons.length > 0) {
//        for (var i = 0; i < zx_allQueryResultPolygons.length; i++) {
//            if (zx_allQueryResultPolygons[i] && map.hasLayer(zx_allQueryResultPolygons[i])) {
//                map.removeLayer(zx_allQueryResultPolygons[i]);
//            }
//        }
//        zx_allQueryResultPolygons = [];
//    }
//}






//在线图层 图斑点击事件
function pgClick(e) {
    if (e != "") {
        var ogr_fid = e.target.options.ogr_fid;
        var ogr_geometry = e.target.options.ogr_geometry;
        var zxtc_type = e.target.options.zxtc_type;
        locationPolygon(ogr_fid, ogr_geometry,zxtc_type);
    }
}

 var selectedPolygon = null;
function locationPolygon(ogr_fid, ogr_geometry,zxtc_type){
         zxtb_removeLocation();

        var _geom = ogr_geometry;
        var _ll = [];
        var _t;
        if (_geom.indexOf("MULTIPOLYGON ") != -1) {
            _geom = _geom.replace("MULTIPOLYGON (((", "");
            _geom = _geom.replace(")))", "");
            _t = _geom.split(")), ((");
        } else {
            _geom = _geom.replace("POLYGON ((", "");
            _geom = _geom.replace("))", "");
            _t = _geom.split("), (");
        }
        for (var ii = 0; ii < _t.length; ii++) {
            var _ll1 = [];
            var _t1 = _t[ii].split(", ");
            for (var ij = 0; ij < _t1.length; ij++) {
                var _t2 = _t1[ij].split(" ");
                _ll1.push([_t2[1], _t2[0]]);
            }
            _ll.push(_ll1);
        }
        if (_t.length == 1) {
            _ll = _ll[0];
        }

        selectedPolygon = new AIMap.Polygon(_ll, {weight: 3, fillOpacity: 0.1, fillColor: "#40E0D0", color: "yellow", clickable: true });
        selectedPolygon.on('click', pgClick);
        map.addLayer(selectedPolygon);
//          map.setView(selectedPolygon.getBounds().getCenter(),map.getMaxZoom());
        map.setView(selectedPolygon.getBounds().getCenter(),3);
        android.checkTbDetail(ogr_fid, ogr_geometry,zxtc_type);
    }



 function zxtb_removeLocation() {
     if (selectedPolygon && map.hasLayer(selectedPolygon)) {
         map.removeLayer(selectedPolygon);
     }
 }

 //显示图斑中每张图片角度
 //../upload/tb_xianzhuang/2019-03-28-27/70aff57682d845128264c62deb9c7897&-45.58&34.870915,113.636969.jpg
 var marker = null;
  function showTb_picAngle(photo_path,photo_xy,photo_angle) {
         console.log("=====showTb_picAngle  photo_xy=====" +photo_xy);
          if (marker && map.hasLayer(marker)) {
                  map.removeLayer(marker);
              }
         //图片拍摄角度
         var _jiaodu = photo_angle;
         _jiaodu = parseFloat(_jiaodu);
         var fangwei = "";
         if (_jiaodu >= -15 && _jiaodu < 15) {
             fangwei = "正北方向";
         }
         else if (_jiaodu >= 15 && _jiaodu < 75) {
             fangwei = "东北方向";
         }
         else if (_jiaodu >= 75 && _jiaodu <= 105) {
             fangwei = "正东方向";
         }
         else if (_jiaodu >= 105 && _jiaodu < 165) {
             fangwei = "东南方向";
         }
         else if ((_jiaodu >= 165 && _jiaodu <= 180) || (_jiaodu) >= -180 && _jiaodu < -165) {
             fangwei = "正南方向";
         }
         else if (_jiaodu >= -165 && _jiaodu < -100) {
             fangwei = "西南方向";
         }
         else if (_jiaodu >= -165 && _jiaodu < -65) {
             fangwei = "正西方向";
         }
         else if (_jiaodu >= -65 && _jiaodu < -15) {
             fangwei = "西北方向";
         }

         var pname = photo_path.split("/")[3];
         var picAndmarkerID = pname.split("&")[0];
         $("#imgs").append('<div class="col-md-2 photoWidth">' +
             '<div class="thumbnail"><img src="../' + photo_path + '" alt="' + pname.substr(0, pname.length - 4) + '"><h5 style="text-align: center;" id=' + picAndmarkerID + '>' + fangwei + '</h5></div></div>');

         //图片位置图标
         var _p = photo_xy.split(",");

 //     //[488556.87688078044, 3765844.6931750574]    3860332.410864, 466807.526287
           var _pReal = transformPoint(parseFloat(_p[0]), parseFloat(_p[1]));
           console.log("=======_pReal======="+_pReal)
           var point = AIMap.latLng(_pReal[1], _pReal[0]);
           marker = AIMap.marker(point, {
                     icon: AIMap.divIcon({
                    html:"<div id='" + picAndmarkerID + "MapArrow'><img src='images/arrow01.png' width=30 height=50 /></div>",
                 })
             });
         map.addLayer(marker);
       $("#" + picAndmarkerID + "MapArrow").rotate({ center: ["10px", "100%"], angle: _jiaodu });

  }


function transformPoint(y, x) {
         return proj4('PROJCS["China_2000_3_Degree_GK_CM_114E",GEOGCS["GCS_china_2000",DATUM["D_china_2000",SPHEROID["china_2000",6378137.0,298.257222101]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]],PROJECTION["Transverse_Mercator"],PARAMETER["false_easting",500000.0],PARAMETER["false_northing",0.0],PARAMETER["central_meridian",114.0],PARAMETER["scale_factor",1.0],PARAMETER["latitude_of_origin",0.0],UNIT["Meter",1.0]]', [x, y]);
}


// function transformPoint(y,x) {
// //proj4(mapCoordinateSystem).forward([lng, lat])
//    return proj4('PROJCS["CGCS2000_3_Degree_GK_CM_114E",GEOGCS["GCS_China_Geodetic_Coordinate_System_2000",DATUM["D_China_2000",SPHEROID["CGCS2000",6378137.0,298.257222101]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]],PROJECTION["Gauss_Kruger"],PARAMETER["False_Easting",500000.0],PARAMETER["False_Northing",0.0],PARAMETER["Central_Meridian",114.0],PARAMETER["Scale_Factor",1.0],PARAMETER["Latitude_Of_Origin",0.0],UNIT["Meter",1.0],AUTHORITY["EPSG",4547]]').forward([x,y]);
//    //return proj4('PROJCS["China_2000_3_Degree_GK_CM_114E",GEOGCS["GCS_china_2000",DATUM["D_china_2000",SPHEROID["china_2000",6378137.0,298.257222101]],PRIMEM["Greenwich",0.0],UNIT["Degree",0.0174532925199433]],PROJECTION["Transverse_Mercator"],PARAMETER["false_easting",500000.0],PARAMETER["false_northing",0.0],PARAMETER["central_meridian",114.0],PARAMETER["scale_factor",1.0],PARAMETER["latitude_of_origin",0.0],UNIT["Meter",1.0]]', [x, y]);
// }

