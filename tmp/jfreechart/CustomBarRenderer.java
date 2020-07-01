package com.wonders.framework.util.jfreechart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.category.BarRenderer;

public class CustomBarRenderer extends BarRenderer{  
  
    /**  
     *   
     */  
    private static final long serialVersionUID = 784630226449158436L;  
    private Paint[] colors;  
    //初始化柱子颜色  
    private String[] colorValues = { "#AFD8F8", "#F6BD0F", "#8BBA00", "#FF8E46", "#008E8E", "#D64646" };  
  
    public CustomBarRenderer() {  
        colors = new Paint[colorValues.length];  
        for (int i = 0; i < colorValues.length; i++) {  
            colors[i] = Color.decode(colorValues[i]);  
        }  
    }  
  
    //每根柱子以初始化的颜色不断轮循  
    public Paint getItemPaint(int i, int j) {  
        return colors[j % colors.length];  
    }  
}  