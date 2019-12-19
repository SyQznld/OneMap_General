package com.appler.onemap_general.property;

import java.util.HashMap;
import java.util.Map;

/**
 * 将点查属性中常见的key value 键值对
 * */
public class ParamMapUtils {

    public static final Map<String, String> propertiseMap() {
        Map<String, String> map = new HashMap<>();

        map.put("Id", "Id");
        map.put("ID", "ID");
        map.put("名称", "名称");
        map.put("年份", "年份");

        map.put("BSM", "标识码");
        map.put("YSDM", "要素代码");
        map.put("QSXZ", "权属性质");
        map.put("TBBH", "图斑编号");
        map.put("JBNTTBBH", "基本农田图斑编号");
        map.put("DLBM", "地类编码");
        map.put("DLDM", "地类代码");
        map.put("DLMC", "地类名称");
        map.put("XZDWDM", "行政单位代码");
        map.put("XZDWMC", "行政单位名称");
        map.put("XZQMC", "行政区名称");
        map.put("XZQDM", "行政区代码");
        map.put("QSDWDM", "权属单位代码");
        map.put("QSDWMC", "权属单位名称");
        map.put("ZLDWDM", "坐落单位代码");
        map.put("ZLDWMC", "坐落单位名称");
        map.put("TDYTQLXDM", "土地用途区类型代码");
        map.put("TDYTQBH", "土地用途区编号");
        map.put("GZQLXDM", "管制区类型代码");


        map.put("XMBH", "项目编号");
        map.put("YSBH", "要素编号");
        map.put("XMMC", "项目名称");
        map.put("XMLX", "项目类型");
        map.put("Layer", "图层");
        map.put("报批批次", "报批批次");
        map.put("批次内编号", "批次内编号");
        map.put("项目名称", "项目名称");
        map.put("备注", "备注");
        map.put("bz", "备注");


        map.put("用地类型", "用地类型");
        map.put("地块编号", "地块编号");
        map.put("土地坐落", "土地坐落");
        map.put("所属单位", "所属单位");
        map.put("土地用途", "土地用途");
        map.put("征收状态", "征收状态");
        map.put("地块代码", "地块代码");

        map.put("批复日期", "批复日期");
        map.put("批文", "批文");
        map.put("YSWH", "验收文号");
        map.put("YSRQ", "验收日期");

        map.put("TBMJ", "图斑面积");
        map.put("面积", "面积");
        map.put("XZDWMJ", "线状地物面积");
        map.put("TBDLMJ", "图斑地类面积");
        map.put("JBNTMJ", "基本农田面积");
        map.put("TDYTQMJ", "土地用途区面积");
        map.put("GZQMJ", "管制区面积");
        map.put("TBJMJ", "图斑净面积");
        map.put("MJ", "面积");
        map.put("亩数", "亩数");
        map.put("MS", "亩数");
        map.put("面积亩", "亩数");

        return map;
    }
}
