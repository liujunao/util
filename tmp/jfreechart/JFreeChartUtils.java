package com.wonders.framework.util.jfreechart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTick;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.urls.CustomCategoryURLGenerator;
import org.jfree.chart.urls.CustomPieURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.wonders.framework.exception.ServiceException;
import com.wonders.framework.util.jfreechart.bo.BarChart;
import com.wonders.framework.util.jfreechart.bo.PieChart;
import com.wonders.framework.util.jfreechart.bo.XYLineChar;

/**
 * 
 * 生成图表的公用方法
 * 
 * @author Augus.Gao
 * 
 */
public class JFreeChartUtils {
	
	public static String PATH = "/jfreechart/images/DisplayChart?filename=";

	public String createXYLineBarChar(HttpSession session, String title,
			String xtitle, String ytitle1, String ytitle2, int width,
			int height, DefaultCategoryDataset dataset1,
			DefaultCategoryDataset dataset2) {
		String filename = "";
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 15)); // 设置标题字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 12));// 设置图例的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));// 设置轴向的字体
		ChartFactory.setChartTheme(standardChartTheme);// 应用主题样式

		JFreeChart chart = ChartFactory.createBarChart(title, // chart title
				xtitle, // x轴标题，domain axis label
				ytitle1, // y轴标题，range axis label
				dataset1, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				true, // tooltips?
				false // URLs?
				);
		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryDataset categorydataset = dataset2; // 设置第二个数据集
		plot.setDataset(1, categorydataset);
		plot.mapDatasetToRangeAxis(1, 1);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setOutlinePaint(Color.white); // 设置图片边框颜色，去掉边框

		// 柱体的样式设计
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, Color.orange);
		renderer.setDrawBarOutline(false);
		// 设置柱顶数据,API中居然没有StandardCategoryItemLabelGenerator这个类
		//renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		//renderer.setSeriesItemLabelsVisible(0, true);
		// 防止由于柱体太少而动态增加柱体宽度（JFreeChart默认是根据柱体多少而显示柱体宽度的）
		int k = dataset1.getColumnCount();
		if (k == 1) {
			plot.getDomainAxis().setLowerMargin(0.26);
			plot.getDomainAxis().setUpperMargin(0.66);
		} else if (k < 6) {
			double margin = (1.0 - k * 0.08) / 3;
			plot.getDomainAxis().setLowerMargin(margin);
			plot.getDomainAxis().setUpperMargin(margin);
			((BarRenderer) plot.getRenderer()).setItemMargin(margin);
		} else {
			((BarRenderer) plot.getRenderer()).setItemMargin(0.1);
		}

		/*------设置Y轴----*/
		double unit = 1d;// 刻度的长度
		// 右边Y轴，相关属性设置
		NumberAxis numberaxis1 = new NumberAxis(ytitle2);
		unit = Math.floor(10);// 刻度的长度
		// NumberTickUnit ntu= new NumberTickUnit(unit);
		// numberaxis1.setTickUnit(ntu);
		// numberaxis1.setRange(0,100);//刻度范围
		numberaxis1.setAutoRange(true);
		plot.setRangeAxis(1, numberaxis1);

		// 左边Y轴
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		numberaxis.setAutoTickUnitSelection(true);
		// numberaxis.setRange(0.0, 100.0);//刻度的范围
		numberaxis.setAutoRange(true);
		// ntu= new NumberTickUnit(unit);
		// numberaxis .setTickUnit(ntu);
		// ------设置柱状体与图片边框的上下间距---
		numberaxis.setUpperMargin(0.05);
		numberaxis.setLowerMargin(0.05);

		/*------设置X轴----*/
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		/*------设置X轴标题的倾斜程度----*/
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		/*------设置柱状体与图片边框的左右间距--*/
		// domainAxis.setLowerMargin(0.01);
		// domainAxis.setUpperMargin(0.01);

		// 设置折线图的样式
		LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		lineandshaperenderer
				.setBaseItemLabelFont(new Font("隶书", Font.BOLD, 10));

		plot.setRenderer(1, lineandshaperenderer);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		// 图例1声明及相关样式设置
		LegendTitle legendtitle = new LegendTitle(plot.getRenderer(0));
		// 图例2声明及相关样式设置
		LegendTitle legendtitle1 = new LegendTitle(plot.getRenderer(1));
		BlockContainer blockcontainer = new BlockContainer(
				new BorderArrangement());
		blockcontainer.add(legendtitle, RectangleEdge.LEFT);
		blockcontainer.add(legendtitle1, RectangleEdge.RIGHT);
		blockcontainer.add(new EmptyBlock(20D, 0.0D));
		CompositeTitle compositetitle = new CompositeTitle(blockcontainer);
		compositetitle.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(compositetitle);

		chart.setAntiAlias(false);
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}

		return filename;
	}
	
	
	/**
	 * 折线图()
	 * @param session
	 * @param title
	 * @param xtitle
	 * @param ytitle
	 * @param width
	 * @param height
	 * @param useMap
	 * @param dataset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String createLineChart(HttpSession session, String title,
			String xtitle, String ytitle, int width, int height, String useMap,
			DefaultCategoryDataset dataset) throws UnsupportedEncodingException {
		String filename = "";
		// 定义图表对象
		JFreeChart chart = ChartFactory.createLineChart(title, // 折线图名称
				xtitle, // 横坐标名称
				ytitle, // 纵坐标名称
				dataset, // 数据
				PlotOrientation.VERTICAL, // 水平显示图像
				true, // include legend
				true, // tooltips
				false // urls
				);
		chart.getTitle().setFont(new Font("黑体", Font.ITALIC, 20)); // 解决标题乱码
		chart.getLegend().setItemFont(
				new Font("宋体", Font.TYPE1_FONT, 16)); // 解决图例乱码
		
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setRangeGridlinesVisible(true); // 是否显示格子线
		plot.setBackgroundAlpha(0.3f); // 设置背景透明度
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		CategoryAxis categoryAxis = plot.getDomainAxis();// 取得横轴
		categoryAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置横轴显示标签的字体

		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		/*------设置X轴标题的倾斜程度----*/
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		
		categoryAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决横坐标乱码

		ValueAxis numberAxis = plot.getRangeAxis();// 取得纵轴

		numberAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置纵轴显示标签的字体

		numberAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决纵坐标乱码

		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer)plot.getRenderer();
		lineandshaperenderer.setBaseShapesVisible(true);
				        
				        
	    rangeAxis.setAutoRangeIncludesZero(true);
		rangeAxis.setUpperMargin(0.20);
		rangeAxis.setLabelAngle(Math.PI / 2.0);
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}

	/**
	 * 数据曲线图
	 * 
	 * @param session
	 * @param title
	 *            标题
	 * @param xtitle
	 *            横坐标显示标签
	 * @param ytitle
	 *            纵坐标显示标签
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param useMap
	 * @param pw
	 * @param xydataset
	 *            数据源
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("deprecation")
	public String createXYLineChart(HttpSession session, String title,
			String xtitle, String ytitle, int width, int height, String useMap,
			Map<String, Map<Double, Double>> dataMap, boolean islegend)
			throws UnsupportedEncodingException {

		XYSeriesCollection xyseriescollection = new XYSeriesCollection(); // 再用XYSeriesCollection添加入XYSeries
		/*
		 * if(dataMap==null||dataMap.size()==0){ return null; }
		 */
		for (String str : dataMap.keySet()) {
			XYSeries xyseries = new XYSeries(str); // 先产生XYSeries 对象
			Map<Double, Double> m1 = dataMap.get(str);
			for (Double k : m1.keySet()) {
				xyseries.add(k, m1.get(k));
			}
			xyseriescollection.addSeries(xyseries);
		}
		// 对象
		String filename = "";
		JFreeChart jfreechart = ChartFactory.createXYLineChart(title, // 图表标题名称
				xtitle, // 横坐标显示标签
				ytitle, // 纵坐标显示标签
				xyseriescollection, // 数据源
				PlotOrientation.VERTICAL, // PlotOrientation.VERTICAL-->垂直显示、PlotOrientation.HORIZONTAL-->水平显示
				islegend, // 是否显示图例（对于简单的柱状图必须是false）
				true, // 是否生产工具
				false // 是否生成URL链接
				);
		jfreechart.setTitle(new TextTitle(title,
				new Font("黑体", Font.ITALIC, 20)));// 重新设置图表标题，改变字体

		jfreechart.getTitle().setFont(new Font("黑体", Font.ITALIC, 20)); // 解决标题乱码

		if (islegend) {
			jfreechart.getLegend().setItemFont(
					new Font("宋体", Font.TYPE1_FONT, 16)); // 解决图例乱码
		}

		jfreechart.setBackgroundPaint(Color.white); // 设定背景色为白色

		XYPlot xyplot = (XYPlot) jfreechart.getPlot(); // 获得 plot：XYPlot！！

		xyplot.setBackgroundPaint(Color.lightGray); // 设定图表数据显示部分背景色

		xyplot.setDomainGridlinePaint(Color.white); // 横坐标网格线白色

		xyplot.setDomainGridlinesVisible(true); // 网格线:true-->可见,false-->不可见

		xyplot.setRangeGridlinePaint(Color.white); // 纵坐标网格线白色

		xyplot.setNoDataMessage("无 数 据!");

		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D)); // 设定坐标轴与图表数据显示部分距离

		ValueAxis categoryAxis = xyplot.getDomainAxis();// 取得横轴

		// categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//设置分类标签以45度角倾斜

		categoryAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置横轴显示标签的字体

		categoryAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决横坐标乱码

		NumberAxis numberAxis = (NumberAxis) xyplot.getRangeAxis();// 取得纵轴

		numberAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置纵轴显示标签的字体

		numberAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决纵坐标乱码

		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// 使纵坐标的最小单位格为整数

		numberAxis.setAutoRangeIncludesZero(true);

		// 获得 renderer 注意这里是XYLineAndShapeRenderer ！！
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
				.getRenderer();
		xylineandshaperenderer.setShapesVisible(true); // 数据点可见
		xylineandshaperenderer.setShapesFilled(true); // 数据点被填充即不是空心点

		// xylineandshaperenderer.setSeriesShapesVisible(0, false);
		// //第二个XYSeries数据点不可见
		xyplot.setRenderer(xylineandshaperenderer);

		xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F, 1, 1,
				1.0F, new float[] { 6F, 6F }, 0.0F)); // 定义series为”Second”的（即series2）点之间的连线

		// xylineandshaperenderer.setURLGenerator(new
		// StandardXYURLGenerator("jsp/test/showXYLine.action","seriesName","itemName"));
		xylineandshaperenderer.setBaseItemLabelsVisible(true);
		xylineandshaperenderer
				.setBasePositiveItemLabelPosition(new ItemLabelPosition(
						ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		xylineandshaperenderer
				.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xylineandshaperenderer.setBaseItemLabelPaint(new Color(102, 102, 102));// 显示折点数值字体的颜色

		// 设置底部标题字体
		xylineandshaperenderer.setLegendTextFont(0, new Font("宋体",
				Font.TYPE1_FONT, 16));

		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);

		try {
			filename = ServletUtilities.saveChartAsPNG(jfreechart, width,
					height, info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}

	/**
	 * 时间曲线图
	 * 
	 * @param session
	 * @param title
	 *            标题
	 * @param xtitle
	 *            横坐标显示标签
	 * @param ytitle
	 *            纵坐标显示标签
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param useMap
	 * @param pw
	 * @param xydataset
	 *            数据源
	 * @param format
	 *            :格式： year 年 ；month ：月 ； day ：日
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("deprecation")
	public String createXYLineTimeChart(HttpSession session, String title,
			String xtitle, String ytitle, int width, int height, String useMap,
			Map<String, Map<Date, Double>> dataMap, boolean islegend,
			String format) throws UnsupportedEncodingException {

		TimeSeriesCollection xyseriescollection = new TimeSeriesCollection(); // 再用XYSeriesCollection添加入XYSeries
		/*
		 * if(dataMap==null||dataMap.size()==0){ return null; }
		 */
		for (String str : dataMap.keySet()) {
			TimeSeries xyseries = new TimeSeries(str); // 先产生XYSeries 对象
			Map<Date, Double> m1 = dataMap.get(str);
			for (Date k : m1.keySet()) {
				if ("year".equals(format)) {
					xyseries.add(new Year(k), m1.get(k));// .add(new Year(k),
															// m1.get(k));
				} else if ("month".equals(format)) {
					xyseries.add(new Month(k), m1.get(k));// .add(new Year(k),
															// m1.get(k));
				} else {
					xyseries.add(new Day(k), m1.get(k));// .add(new Year(k),
														// m1.get(k));
				}
			}
			xyseriescollection.addSeries(xyseries);
		}
		// 对象
		String filename = "";
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title, // 图表标题名称
				xtitle, // 横坐标显示标签
				ytitle, // 纵坐标显示标签
				xyseriescollection, // 数据源
				islegend, // 是否显示图例（对于简单的柱状图必须是false）
				true, // 是否生产工具
				false // 是否生成URL链接
				);
		jfreechart.setTitle(new TextTitle(title,
				new Font("黑体", Font.ITALIC, 20)));// 重新设置图表标题，改变字体

		jfreechart.getTitle().setFont(new Font("黑体", Font.ITALIC, 20)); // 解决标题乱码

		if (islegend) {
			jfreechart.getLegend().setItemFont(
					new Font("宋体", Font.TYPE1_FONT, 16)); // 解决图例乱码
		}

		jfreechart.setBackgroundPaint(Color.white); // 设定背景色为白色

		XYPlot xyplot = (XYPlot) jfreechart.getPlot(); // 获得 plot：XYPlot！！

		xyplot.setBackgroundPaint(Color.lightGray); // 设定图表数据显示部分背景色

		xyplot.setDomainGridlinePaint(Color.white); // 横坐标网格线白色

		xyplot.setDomainGridlinesVisible(true); // 网格线:true-->可见,false-->不可见

		xyplot.setRangeGridlinePaint(Color.white); // 纵坐标网格线白色

		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D)); // 设定坐标轴与图表数据显示部分距离

		xyplot.setNoDataMessage("无 数 据!");
		// ValueAxis categoryAxis = xyplot.getDomainAxis();//取得横轴

		// categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//设置分类标签以45度角倾斜
		// categoryAxis.setTickUnit(new DateTickUnit(1, 1, new
		// SimpleDateFormat("yyyy")));
		// DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		DateAxis dateaxis = new DateAxis(xtitle) {
			@SuppressWarnings("unchecked")
			protected List<DateTick> refreshTicksHorizontal(Graphics2D g2,
					Rectangle2D dataArea, RectangleEdge edge) {
				List ticks = super.refreshTicksHorizontal(g2, dataArea, edge);
				List<DateTick> newTicks = new ArrayList<DateTick>();
				for (Iterator it = ticks.iterator(); it.hasNext();) {
					DateTick tick = (DateTick) it.next();
					newTicks.add(new DateTick(tick.getDate(), tick.getText(),
							TextAnchor.TOP_RIGHT, TextAnchor.TOP_RIGHT,
							-Math.PI / 3));
				}
				return newTicks;
			}
		};
		xyplot.setDomainAxis(dateaxis);
		if ("year".equals(format)) {
			dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
			for (String str : dataMap.keySet()) {
				if (dataMap.get(str).size() < 20) {
					dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR, 1));
				}
			}
		} else if ("month".equals(format)) {
			dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-yyyy"));
			for (String str : dataMap.keySet()) {
				if (dataMap.get(str).size() < 20) {
					dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
				}
			}
		} else {
			dateaxis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));
			for (String str : dataMap.keySet()) {
				if (dataMap.get(str).size() < 20) {
					dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
				}
			}
		}
		// / dateaxis.set
		dateaxis.setVerticalTickLabels(true);
		dateaxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置横轴显示标签的字体
		dateaxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决横坐标乱码

		NumberAxis numberAxis = (NumberAxis) xyplot.getRangeAxis();// 取得纵轴

		numberAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置纵轴显示标签的字体

		numberAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决纵坐标乱码

		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// 使纵坐标的最小单位格为整数

		numberAxis.setAutoRangeIncludesZero(true);

		// 获得 renderer 注意这里是XYLineAndShapeRenderer ！！
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
				.getRenderer();
		xylineandshaperenderer.setShapesVisible(true); // 数据点可见
		xylineandshaperenderer.setShapesFilled(true); // 数据点被填充即不是空心点

		// xylineandshaperenderer.setSeriesShapesVisible(0, false);
		// //第二个XYSeries数据点不可见
		xyplot.setRenderer(xylineandshaperenderer);

		xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F, 1, 1,
				1.0F, new float[] { 6F, 6F }, 0.0F)); // 定义series为”Second”的（即series2）点之间的连线

		xylineandshaperenderer.setBaseItemLabelsVisible(true);
		xylineandshaperenderer
				.setBasePositiveItemLabelPosition(new ItemLabelPosition(
						ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		xylineandshaperenderer
				.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xylineandshaperenderer.setBaseItemLabelPaint(new Color(102, 102, 102));// 显示折点数值字体的颜色

		// 设置底部标题字体
		xylineandshaperenderer.setLegendTextFont(0, new Font("宋体",
				Font.TYPE1_FONT, 16));

		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);

		try {
			filename = ServletUtilities.saveChartAsPNG(jfreechart, width,
					height, info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}

	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param xtitle
	 * @param ytitle
	 * @param width
	 * @param height
	 * @param session
	 * @param pw
	 * @param useMap
	 * @param is3D
	 * @param isone
	 *            是否为单柱子
	 * @return
	 */
	public String createBarChart(CategoryDataset dataset, String title,
			String xtitle, String ytitle, int width, int height,
			HttpSession session, String useMap, boolean is3D, boolean isone,
			boolean islegend) {
		String filename = "";
		JFreeChart chart = ChartFactory.createBarChart3D(title,// 图表标题
				xtitle,// 目录轴的显示标签(x轴)
				ytitle,// 数值轴的显示标签（y轴）
				dataset, // 数据
				PlotOrientation.VERTICAL,// 图表方向：水平、垂直
				islegend,// 是否显示图例(对于简单的柱状图必须是false)
				true,// 是否tooltip
				false// 是否生成URL链接
				);
		Font font = new Font("宋体", Font.BOLD, 16);
		TextTitle barTitle = new TextTitle(title, font);
		// 设置正上方的标题
		chart.setTitle(barTitle);
		// 图例显示位置 设置底部标题
		if (islegend) {
			chart.getLegend().setVisible(true);
			chart.getLegend().setPosition(RectangleEdge.BOTTOM);
			chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
		}
		// chart.getLegend().setItemLabelPadding(new RectangleInsets());
		CategoryPlot plot = chart.getCategoryPlot();// 获得图表区域对象

		// 设置图表的纵轴和横轴org.jfree.chart.axis.CategoryAxis
		CategoryAxis domainAxis = plot.getDomainAxis();// X轴
		// domainAxis.setLowerMargin(0.1);// 设置距离图片左端距离此时为10%
		// domainAxis.setUpperMargin(0.1);// 设置距离图片右端距离此时为百分之10
		// domainAxis.setCategoryLabelPositionOffset(10);// 图表横轴与标签的距离(10像素)
		// domainAxis.setCategoryMargin(0.3);// 横轴标签之间的距离20%
		// 设置X坐标上的文字
		domainAxis.setTickLabelFont(font);
		// 设置X轴标题
		domainAxis.setLabelFont(font);
		// domainAxis.setMaximumCategoryLabelLines(1);
		// domainAxis.setMaximumCategoryLabelWidthRatio(0);
		// 设定柱子的属性
		// 设置字体显示角度
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		NumberAxis vn = (NumberAxis) plot.getRangeAxis();
		vn.setAutoRangeIncludesZero(true);
		// vn.setTickUnit(new NumberTickUnit(1));
		vn.setNumberFormatOverride(new DecimalFormat("0"));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(0.1);// 设置最高的一个柱与图片顶端的距离(最高柱的10%)
		// 设置Y坐标上的文字
		rangeAxis.setTickLabelFont(font);
		// 设置Y轴标题
		rangeAxis.setLabelFont(font);

		// 设置Y轴刻度
		rangeAxis.setAutoRange(true);
		rangeAxis.setLowerBound(0);

		// 设置图表的颜色
		// BarRenderer3D renderer = new BarRenderer3D();
		BarRenderer renderer;
		if (isone) {
			if (is3D) {
				renderer = new CustomBarRenderer3D();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			} else {
				renderer = new CustomBarRenderer();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			}
		} else {
			if (is3D) {
				renderer = new BarRenderer3D();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			} else {
				renderer = new BarRenderer();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			}
		}
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setIncludeBaseInRange(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
				"{2}", NumberFormat.getNumberInstance()));
		/*
		 * renderer .setBaseItemLabelGenerator(new
		 * StandardCategoryItemLabelGenerator(
		 * "{0}={2}人",NumberFormat.getNumberInstance()));
		 */
		renderer.setBaseOutlinePaint(Color.red);
		/*
		 * renderer.setSeriesPaint(0, new Color(0, 255, 255));// 计划柱子的颜色为青色
		 * renderer.setSeriesOutlinePaint(0, Color.BLACK);// 边框为黑色
		 * renderer.setSeriesPaint(1, new Color(0, 255, 0));// 实报柱子的颜色为绿色
		 * renderer.setSeriesOutlinePaint(1, Color.red);// 边框为红色
		 */renderer.setItemMargin(0.1);// 组内柱子间隔为组宽的10%
		// java.lang.String labelFormat:"{0}={1}({2})":会显示成：apple=120(5%)
		// java.text.NumberFormat类有三个方法可以产生下列数据的标准格式化器：
		// 数字：NumberFormat.getNumberInstance();
		// 货币：NumberFormat.getCurrencyInstance();
		// 百分数：NumberFormat.getPercentInstance();
		// 设置柱状的宽度
		renderer.setMaximumBarWidth(0.1);
		renderer.setMinimumBarLength(0.1);
		// 设置底部
		renderer.setLegendItemLabelGenerator(new StandardCategorySeriesLabelGenerator(
				"{0}"));
		renderer.setBaseItemLabelsVisible(true);
		plot.setRenderer(renderer);// 使用我们设计的效果

		// 设置纵横坐标的显示位置
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);// 显示在下端(柱子竖直)或左侧(柱子水平)
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT); // 显示在下端(柱子水平)或左侧(柱子竖直)
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}

	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param xtitle
	 * @param ytitle
	 * @param width
	 * @param height
	 * @param session
	 * @param pw
	 * @param useMap
	 * @param is3D
	 * @param isone
	 *            是否为单柱子
	 * @return
	 */
	public String createBarChartRight(CategoryDataset dataset, String title,
			String xtitle, String ytitle, int width, int height,
			HttpSession session, String useMap, boolean is3D, boolean isone,
			boolean islegend) {
		String filename = "";
		JFreeChart chart = ChartFactory.createBarChart3D(title,// 图表标题
				xtitle,// 目录轴的显示标签(x轴)
				ytitle,// 数值轴的显示标签（y轴）
				dataset, // 数据
				PlotOrientation.VERTICAL,// 图表方向：水平、垂直
				islegend,// 是否显示图例(对于简单的柱状图必须是false)
				true,// 是否tooltip
				false// 是否生成URL链接
				);

		Font font = new Font("宋体", Font.BOLD, 16);
		// 设置正上方的标题
		chart.setTitle(new TextTitle(title, font));

		// 图例显示位置 设置底部标题
		if (islegend) {
			chart.getLegend().setVisible(true);
			chart.getLegend().setPosition(RectangleEdge.RIGHT);
			chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
		}
		CategoryPlot plot = chart.getCategoryPlot();// 获得图表区域对象

		// 设置图表的纵轴和横轴org.jfree.chart.axis.CategoryAxis
		CategoryAxis domainAxis = plot.getDomainAxis();// X轴
		// domainAxis.setLowerMargin(0.1);// 设置距离图片左端距离此时为10%
		// domainAxis.setUpperMargin(0.1);// 设置距离图片右端距离此时为百分之10
		// domainAxis.setCategoryLabelPositionOffset(10);// 图表横轴与标签的距离(10像素)
		// domainAxis.setCategoryMargin(0.2);// 横轴标签之间的距离20%
		// 设置X坐标上的文字
		domainAxis.setTickLabelFont(font);
		// 设置X轴标题
		domainAxis.setLabelFont(font);

		// domainAxis.setMaximumCategoryLabelLines(1);
		// domainAxis.setMaximumCategoryLabelWidthRatio(0);
		// 设定柱子的属性
		// 设置字体显示角度
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// 设置纵项数值

		NumberAxis vn = (NumberAxis) plot.getRangeAxis();
		vn.setAutoRangeIncludesZero(true);
		// vn.setTickUnit(new NumberTickUnit(1));
		vn.setNumberFormatOverride(new DecimalFormat("0"));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(0.1);// 设置最高的一个柱与图片顶端的距离(最高柱的10%)
		// 设置Y坐标上的文字
		rangeAxis.setTickLabelFont(font);
		// 设置Y轴标题
		rangeAxis.setLabelFont(font);

		// 设置Y轴刻度
		rangeAxis.setAutoRange(true);
		rangeAxis.setAutoRangeMinimumSize(1);
		rangeAxis.setLowerBound(0);

		// 设置图表的颜色
		// BarRenderer3D renderer = new BarRenderer3D();
		BarRenderer renderer;
		if (isone) {
			if (is3D) {
				renderer = new CustomBarRenderer3D();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			} else {
				renderer = new CustomBarRenderer();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			}
		} else {
			if (is3D) {
				renderer = new BarRenderer3D();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			} else {
				renderer = new BarRenderer();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			}
		}
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setIncludeBaseInRange(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
				"{2}", NumberFormat.getNumberInstance()));
		/*
		 * renderer .setBaseItemLabelGenerator(new
		 * StandardCategoryItemLabelGenerator(
		 * "{0}={2}人",NumberFormat.getNumberInstance()));
		 */
		renderer.setBaseOutlinePaint(Color.red);
		/*
		 * renderer.setSeriesPaint(0, new Color(0, 255, 255));// 计划柱子的颜色为青色
		 * renderer.setSeriesOutlinePaint(0, Color.BLACK);// 边框为黑色
		 * renderer.setSeriesPaint(1, new Color(0, 255, 0));// 实报柱子的颜色为绿色
		 * renderer.setSeriesOutlinePaint(1, Color.red);// 边框为红色
		 */renderer.setItemMargin(0.1);// 组内柱子间隔为组宽的10%
		// java.lang.String labelFormat:"{0}={1}({2})":会显示成：apple=120(5%)
		// java.text.NumberFormat类有三个方法可以产生下列数据的标准格式化器：
		// 数字：NumberFormat.getNumberInstance();
		// 货币：NumberFormat.getCurrencyInstance();
		// 百分数：NumberFormat.getPercentInstance();
		// 设置柱状的宽度
		renderer.setMaximumBarWidth(0.1);
		renderer.setMinimumBarLength(0.1);
		// 设置底部
		renderer.setLegendItemLabelGenerator(new StandardCategorySeriesLabelGenerator(
				"{0}"));
		renderer.setBaseItemLabelsVisible(true);
		plot.setRenderer(renderer);// 使用我们设计的效果

		// 设置纵横坐标的显示位置
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);// 显示在下端(柱子竖直)或左侧(柱子水平)
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT); // 显示在下端(柱子水平)或左侧(柱子竖直)
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}
	
	private double[][] convertDataset(CategoryDataset srcDataset){
		int columnCount = srcDataset.getColumnCount();
		double[][] data = new double[1][columnCount];
		for(int i=0;i<columnCount;i++){
			Number value = srcDataset.getValue(srcDataset.getRowKey(i), srcDataset.getColumnKey(i));
			if(value!=null){
				   data[0][i]=value.doubleValue();
			}
		}
	     return data;
	}
	
	

	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param width
	 * @param height
	 * @param session
	 * @param pw
	 * @param useMap
	 * @param is3D
	 * @return
	 */
	public String createPieChart(Dataset dataset, String title, int width,
			int height, HttpSession session, String useMap, boolean is3D) {
		String filename = "";
		PiePlot plot = null;
		if (is3D) {
			plot = new PiePlot3D((DefaultPieDataset) dataset);// 3D饼图
			/*
			 * plot.setURLGenerator(new
			 * StandardPieURLGenerator("jsp/test/detailPie3D.do", "fruit",
			 * "pieIdex"));
			 */// 设定热区超链接
		} else {
			plot = new PiePlot((DefaultPieDataset) dataset);
			/*
			 * plot.setURLGenerator(new StandardPieURLGenerator("detailPie.do",
			 * "fruit", "pieIdex"));
			 */// 设定热区超链接
		}
		plot.setPieIndex(1);
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色
		chart.setTitle(title);// 可选，设置图片标题

		// 指定饼图轮廓线的颜色
		// plot.setBaseSectionOutlinePaint(Color.red);
		// plot.setBaseSectionPaint(Color.BLACK);
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		plot.setNoDataMessage("无对应数据显示");

		// 图片中显示百分比:默认方式
		// plot.setLabelGenerator(new
		// StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
		/*
		 * // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		 * plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
		 * "{0}={1}({2})", NumberFormat.getNumberInstance(), new
		 * DecimalFormat("0.00%"))); // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2}
		 * 表示所占比例 plot.setLegendLabelGenerator(new
		 * StandardPieSectionLabelGenerator( "{0}={1}({2})"));
		 */

		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}:{1}/{2}", NumberFormat.getNumberInstance(),
				// "{1}/{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));

		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.8f);
		// 指定显示的饼图上圆形(true)还椭圆形(false)
		plot.setCircular(true);
		// 设置第一个 饼块section 的开始位置，默认是12点钟方向
		plot.setStartAngle(90);
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}
	
	

	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param width
	 * @param height
	 * @param session
	 * @param pw
	 * @param useMap
	 * @param is3D
	 * @return
	 */
	public String createPieChart(HttpSession session, PieChart pieChart) {

		DefaultPieDataset dataset = new DefaultPieDataset();
		if (!pieChart.getData().isEmpty()) {
			for (String key : pieChart.getData().keySet()) {
				dataset.setValue(
						key,
						Double.parseDouble(pieChart.getData().get(key)
								.toString()));
			}
		}

		String filename = "";
		PiePlot plot = null;
		if (pieChart.isIs3D()) {
			plot = new PiePlot3D((DefaultPieDataset) dataset);// 3D饼图
			/*
			 * plot.setURLGenerator(new
			 * StandardPieURLGenerator("jsp/test/detailPie3D.do", "fruit",
			 * "pieIdex"));
			 */// 设定热区超链接
		} else {
			plot = new PiePlot((DefaultPieDataset) dataset);
			/*
			 * plot.setURLGenerator(new StandardPieURLGenerator("detailPie.do",
			 * "fruit", "pieIdex"));
			 */// 设定热区超链接
		}
		plot.setPieIndex(1);
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色
		chart.setTitle(pieChart.getTitle());// 可选，设置图片标题

		// 指定饼图轮廓线的颜色
		// plot.setBaseSectionOutlinePaint(Color.red);
		// plot.setBaseSectionPaint(Color.BLACK);
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		plot.setNoDataMessage("无对应数据显示");

		// 图片中显示百分比:默认方式
		// plot.setLabelGenerator(new
		// StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
		/*
		 * // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		 * plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
		 * "{0}={1}({2})", NumberFormat.getNumberInstance(), new
		 * DecimalFormat("0.00%"))); // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2}
		 * 表示所占比例 plot.setLegendLabelGenerator(new
		 * StandardPieSectionLabelGenerator( "{0}={1}({2})"));
		 */

		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				// "{0}:{1}/{2}", NumberFormat.getNumberInstance(),
				"{1}/{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));

		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.8f);
		// 指定显示的饼图上圆形(true)还椭圆形(false)
		plot.setCircular(true);
		// 设置第一个 饼块section 的开始位置，默认是12点钟方向
		plot.setStartAngle(90);
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart,
					pieChart.getWidth(), pieChart.getHeight(), info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}
    
    /**
     * 
     * @param session
     * @param barChart
     * @return
     */
    public static String createBarChart(HttpServletRequest request,HttpServletResponse response,BarChart barChart, String useMap) {
        String filename = "";
        JFreeChart chart = ChartFactory.createBarChart3D(barChart.getTitle(),// 图表标题
        		barChart.getXtitle(),// 目录轴的显示标签(x轴)
        		barChart.getYtitle(),// 数值轴的显示标签（y轴）
        		barChart.getData(), //数据
                PlotOrientation.VERTICAL,// 图表方向：水平、垂直
                barChart.isLegend(),// 是否显示图例(对于简单的柱状图必须是false)
                true,// 是否tooltip
                false// 是否生成URL链接
                );
        Font font = new Font("宋体", Font.BOLD, 16);
        TextTitle barTitle = new TextTitle(barChart.getTitle(), font);
        // 设置正上方的标题
        chart.setTitle(barTitle);
        //图例显示位置 设置底部标题
        if(barChart.isLegend()){
        	chart.getLegend().setVisible(true);
        	chart.getLegend().setPosition(barChart.getLegendPosition());
        	chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
        }
        //chart.getLegend().setItemLabelPadding(new RectangleInsets());
        CategoryPlot plot = chart.getCategoryPlot();// 获得图表区域对象

        // 设置图表的纵轴和横轴org.jfree.chart.axis.CategoryAxis
        CategoryAxis domainAxis = plot.getDomainAxis();// X轴
        //domainAxis.setLowerMargin(0.1);// 设置距离图片左端距离此时为10%
        //domainAxis.setUpperMargin(0.1);// 设置距离图片右端距离此时为百分之10
        //domainAxis.setCategoryLabelPositionOffset(10);// 图表横轴与标签的距离(10像素)
        //domainAxis.setCategoryMargin(0.3);// 横轴标签之间的距离20%
        // 设置X坐标上的文字
        domainAxis.setTickLabelFont(font);
        // 设置X轴标题
        domainAxis.setLabelFont(font);
        // domainAxis.setMaximumCategoryLabelLines(1);
        // domainAxis.setMaximumCategoryLabelWidthRatio(0);
        // 设定柱子的属性
        //设置字体显示角度
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        NumberAxis vn = (NumberAxis) plot.getRangeAxis();   
        vn.setAutoRangeIncludesZero(true);
        //vn.setTickUnit(new NumberTickUnit(1));
        vn.setAutoRange(true);
        
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setUpperMargin(0.1);// 设置最高的一个柱与图片顶端的距离(最高柱的10%)
        // 设置Y坐标上的文字
        rangeAxis.setTickLabelFont(font);
        // 设置Y轴标题
        rangeAxis.setLabelFont(font);
        
        //设置Y轴刻度
        if(barChart.isIdInt()){
        	vn.setNumberFormatOverride(new DecimalFormat("0"));
        	rangeAxis.setAutoRange(true);
        	rangeAxis.setLowerBound(0);
        }

        // 设置图表的颜色
        // BarRenderer3D renderer = new BarRenderer3D();
        BarRenderer renderer;
        if(barChart.isOne()){
        	 if (barChart.isIs3D()) {
                 renderer = new CustomBarRenderer3D();
                 renderer
                         .setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
             } else {
                 renderer = new CustomBarRenderer();
                 renderer
                         .setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
             }
        }else{
        	if (barChart.isIs3D()) {
        		renderer = new BarRenderer3D();
        		renderer
        		.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        	} else {
        		renderer = new BarRenderer();
        		renderer
        		.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        	}
        }
        // 显示每个柱的数值，并修改该数值的字体属性
        renderer.setIncludeBaseInRange(true);
        renderer
        .setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                "{2}",NumberFormat.getNumberInstance()));
        /*renderer
                .setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
                        "{0}={2}人",NumberFormat.getNumberInstance()));*/
        renderer.setBaseOutlinePaint(Color.red);
      /*  renderer.setSeriesPaint(0, new Color(0, 255, 255));// 计划柱子的颜色为青色
        renderer.setSeriesOutlinePaint(0, Color.BLACK);// 边框为黑色
        renderer.setSeriesPaint(1, new Color(0, 255, 0));// 实报柱子的颜色为绿色
        renderer.setSeriesOutlinePaint(1, Color.red);// 边框为红色
*/        renderer.setItemMargin(0.1);// 组内柱子间隔为组宽的10%
        //java.lang.String labelFormat:"{0}={1}({2})":会显示成：apple=120(5%) 
        //java.text.NumberFormat类有三个方法可以产生下列数据的标准格式化器：
        //数字：NumberFormat.getNumberInstance();
        //货币：NumberFormat.getCurrencyInstance();
        //百分数：NumberFormat.getPercentInstance();
        //设置柱状的宽度
          renderer.setMaximumBarWidth(0.1);
          renderer.setMinimumBarLength(0.1);
        //设置底部
        renderer.setLegendItemLabelGenerator(new StandardCategorySeriesLabelGenerator("{0}"));
        renderer.setBaseItemLabelsVisible(true);
        plot.setRenderer(renderer);// 使用我们设计的效果
        
        
        // 设置纵横坐标的显示位置
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);// 显示在下端(柱子竖直)或左侧(柱子水平)
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT); // 显示在下端(柱子水平)或左侧(柱子竖直)
        StandardEntityCollection sec = new StandardEntityCollection();
        ChartRenderingInfo info = new ChartRenderingInfo(sec);
        try {
            filename = ServletUtilities.saveChartAsPNG(chart, barChart.getWidth(), barChart.getHeight(),
                    info, request.getSession());
            java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, filename,  info, false);
			pw.flush();
        } catch (IOException e) {
        	throw new ServiceException(e.getMessage());
        }
        return filename;
    }
    
	/**
	 * 时间曲线图
	 * @param session
	 * @param barXYLine
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    public String createXYLineTimeChart(HttpSession session, XYLineChar barXYLine) throws UnsupportedEncodingException {

		TimeSeriesCollection xyseriescollection = new TimeSeriesCollection(); // 再用XYSeriesCollection添加入XYSeries
		/*
		 * if(dataMap==null||dataMap.size()==0){ return null; }
		 */
		for (String str : barXYLine.getDataMap().keySet()) {
			TimeSeries xyseries = new TimeSeries(str); // 先产生XYSeries 对象
			Map<Object, Number> m1 = barXYLine.getDataMap().get(str);
			for (Object k : m1.keySet()) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd"); 
		        Date d;
				try {
					d = format.parse(k.toString());
					if ("year".equals(barXYLine.getFormat())) {
						xyseries.add(new Year(d), m1.get(k));// .add(new Year(k),
						// m1.get(k));
					} else if ("month".equals(barXYLine.getFormat())) {
						xyseries.add(new Month(d), m1.get(k));// .add(new Year(k),
						// m1.get(k));
					} else {
						xyseries.add(new Day(d), m1.get(k));// .add(new Year(k),
						// m1.get(k));
					}
				} catch (ParseException e) {
					throw new ServiceException(e.getMessage());
				}
			}
			xyseriescollection.addSeries(xyseries);
		}
		// 对象
		String filename = "";
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(barXYLine.getTitle(), // 图表标题名称
				barXYLine.getXtitle(), // 横坐标显示标签
				barXYLine.getYtitle(), // 纵坐标显示标签
				xyseriescollection, // 数据源
				barXYLine.isIslegend(), // 是否显示图例（对于简单的柱状图必须是false）
				true, // 是否生产工具
				false // 是否生成URL链接
				);
		jfreechart.setTitle(new TextTitle(barXYLine.getTitle(),
				new Font("黑体", Font.ITALIC, 20)));// 重新设置图表标题，改变字体

		jfreechart.getTitle().setFont(new Font("黑体", Font.ITALIC, 20)); // 解决标题乱码

		if (barXYLine.isIslegend()) {
			jfreechart.getLegend().setItemFont(
					new Font("宋体", Font.TYPE1_FONT, 16)); // 解决图例乱码
		}

		jfreechart.setBackgroundPaint(Color.white); // 设定背景色为白色

		XYPlot xyplot = (XYPlot) jfreechart.getPlot(); // 获得 plot：XYPlot！！

		xyplot.setBackgroundPaint(Color.lightGray); // 设定图表数据显示部分背景色

		xyplot.setDomainGridlinePaint(Color.white); // 横坐标网格线白色

		xyplot.setDomainGridlinesVisible(true); // 网格线:true-->可见,false-->不可见

		xyplot.setRangeGridlinePaint(Color.white); // 纵坐标网格线白色

		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D)); // 设定坐标轴与图表数据显示部分距离

		xyplot.setNoDataMessage("无 数 据!");
		// ValueAxis categoryAxis = xyplot.getDomainAxis();//取得横轴

		// categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//设置分类标签以45度角倾斜
		// categoryAxis.setTickUnit(new DateTickUnit(1, 1, new
		// SimpleDateFormat("yyyy")));
		// DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		DateAxis dateaxis = new DateAxis(barXYLine.getXtitle()) {
			@SuppressWarnings("unchecked")
			protected List<DateTick> refreshTicksHorizontal(Graphics2D g2,
					Rectangle2D dataArea, RectangleEdge edge) {
				List ticks = super.refreshTicksHorizontal(g2, dataArea, edge);
				List<DateTick> newTicks = new ArrayList<DateTick>();
				for (Iterator it = ticks.iterator(); it.hasNext();) {
					DateTick tick = (DateTick) it.next();
					newTicks.add(new DateTick(tick.getDate(), tick.getText(),
							TextAnchor.TOP_RIGHT, TextAnchor.TOP_RIGHT,
							-Math.PI / 3));
				}
				return newTicks;
			}
		};
		xyplot.setDomainAxis(dateaxis);
		if ("year".equals(barXYLine.getFormat())) {
			dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
			for (String str : barXYLine.getDataMap().keySet()) {
				if (barXYLine.getDataMap().get(str).size() < 20) {
					dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR, 1));
				}
			}
		} else if ("month".equals(barXYLine.getFormat())) {
			dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-yyyy"));
			for (String str : barXYLine.getDataMap().keySet()) {
				if (barXYLine.getDataMap().get(str).size() < 20) {
					dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
				}
			}
		} else {
			dateaxis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));
			for (String str : barXYLine.getDataMap().keySet()) {
				if (barXYLine.getDataMap().get(str).size() < 20) {
					dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
				}
			}
		}
		// / dateaxis.set
		dateaxis.setVerticalTickLabels(true);
		dateaxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置横轴显示标签的字体
		dateaxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决横坐标乱码

		NumberAxis numberAxis = (NumberAxis) xyplot.getRangeAxis();// 取得纵轴

		numberAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置纵轴显示标签的字体

		numberAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决纵坐标乱码

		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());// 使纵坐标的最小单位格为整数

		numberAxis.setAutoRangeIncludesZero(true);

		// 获得 renderer 注意这里是XYLineAndShapeRenderer ！！
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
				.getRenderer();
		xylineandshaperenderer.setShapesVisible(true); // 数据点可见
		xylineandshaperenderer.setShapesFilled(true); // 数据点被填充即不是空心点

		// xylineandshaperenderer.setSeriesShapesVisible(0, false);
		// //第二个XYSeries数据点不可见
		xyplot.setRenderer(xylineandshaperenderer);

		xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F, 1, 1,
				1.0F, new float[] { 6F, 6F }, 0.0F)); // 定义series为”Second”的（即series2）点之间的连线

		xylineandshaperenderer.setBaseItemLabelsVisible(true);
		xylineandshaperenderer
				.setBasePositiveItemLabelPosition(new ItemLabelPosition(
						ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		xylineandshaperenderer
				.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xylineandshaperenderer.setBaseItemLabelPaint(new Color(102, 102, 102));// 显示折点数值字体的颜色

		// 设置底部标题字体
		xylineandshaperenderer.setLegendTextFont(0, new Font("宋体",
				Font.TYPE1_FONT, 16));

		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);

		try {
			filename = ServletUtilities.saveChartAsPNG(jfreechart, barXYLine.getWidth(),
					barXYLine.getHeight(), info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}
    
    public String createDrillXYLineBarChar(HttpServletRequest request,HttpServletResponse response, String title,
			String xtitle, String ytitle1, String ytitle2, int width,
			int height, DefaultCategoryDataset dataset1,
			DefaultCategoryDataset dataset2) {
		String filename = "";
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 15)); // 设置标题字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 12));// 设置图例的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));// 设置轴向的字体
		ChartFactory.setChartTheme(standardChartTheme);// 应用主题样式

		JFreeChart chart = ChartFactory.createBarChart(title, // chart title
				xtitle, // x轴标题，domain axis label
				ytitle1, // y轴标题，range axis label
				dataset1, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				true, // tooltips?
				false // URLs?
				);
		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryDataset categorydataset = dataset2; // 设置第二个数据集
		plot.setDataset(1, categorydataset);
		plot.mapDatasetToRangeAxis(1, 1);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setOutlinePaint(Color.white); // 设置图片边框颜色，去掉边框

		// 柱体的样式设计
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, Color.orange);
		renderer.setDrawBarOutline(false);
		// 设置柱顶数据,API中居然没有StandardCategoryItemLabelGenerator这个类
		//renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		//renderer.setSeriesItemLabelsVisible(0, true);
		// 防止由于柱体太少而动态增加柱体宽度（JFreeChart默认是根据柱体多少而显示柱体宽度的）
		int k = dataset1.getColumnCount();
		if (k == 1) {
			plot.getDomainAxis().setLowerMargin(0.26);
			plot.getDomainAxis().setUpperMargin(0.66);
		} else if (k < 6) {
			double margin = (1.0 - k * 0.08) / 3;
			plot.getDomainAxis().setLowerMargin(margin);
			plot.getDomainAxis().setUpperMargin(margin);
			((BarRenderer) plot.getRenderer()).setItemMargin(margin);
		} else {
			((BarRenderer) plot.getRenderer()).setItemMargin(0.1);
		}

		/*------设置Y轴----*/
		double unit = 1d;// 刻度的长度
		// 右边Y轴，相关属性设置
		NumberAxis numberaxis1 = new NumberAxis(ytitle2);
		unit = Math.floor(10);// 刻度的长度
		// NumberTickUnit ntu= new NumberTickUnit(unit);
		// numberaxis1.setTickUnit(ntu);
		// numberaxis1.setRange(0,100);//刻度范围
		numberaxis1.setAutoRange(true);
		plot.setRangeAxis(1, numberaxis1);

		// 左边Y轴
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		numberaxis.setAutoTickUnitSelection(true);
		// numberaxis.setRange(0.0, 100.0);//刻度的范围
		numberaxis.setAutoRange(true);
		// ntu= new NumberTickUnit(unit);
		// numberaxis .setTickUnit(ntu);
		// ------设置柱状体与图片边框的上下间距---
		numberaxis.setUpperMargin(0.05);
		numberaxis.setLowerMargin(0.05);

		/*------设置X轴----*/
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		/*------设置X轴标题的倾斜程度----*/
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		/*------设置柱状体与图片边框的左右间距--*/
		// domainAxis.setLowerMargin(0.01);
		// domainAxis.setUpperMargin(0.01);

		// 设置折线图的样式
		LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		lineandshaperenderer
				.setBaseItemLabelFont(new Font("隶书", Font.BOLD, 10));
		CustomCategoryURLGenerator cGenerator=new CustomCategoryURLGenerator();
		String drillId = request.getParameter("drillId");
//		 String urlSrc = request.getParameter("url");
//		 int index = urlSrc.indexOf('?');
//		 String urlHead = urlSrc.substring(0, index+1);
//		 String s = urlSrc.substring(index+1);
//		 String urlBottom = s.replace('?','&');
//		 String url = urlHead+urlBottom;
//		String url = request.getParameter("url");
//		 List urls = new ArrayList();
//			for(int i = 0,count = dataset1.getColumnCount();i<count;i++){
//				urls.add("javascript:WCB.drill('"+drillId+"','"+dataset1.getColumnKey(i)+"','"+url+"')");
//			}
//	    cGenerator.addURLSeries(urls);
//		renderer.setSeriesItemURLGenerator(0, cGenerator);
		//设置钻取
				setBarDrill(request,dataset1,renderer);
		plot.setRenderer(1, lineandshaperenderer);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		// 图例1声明及相关样式设置
		LegendTitle legendtitle = new LegendTitle(plot.getRenderer(0));
		// 图例2声明及相关样式设置
		LegendTitle legendtitle1 = new LegendTitle(plot.getRenderer(1));
		BlockContainer blockcontainer = new BlockContainer(
				new BorderArrangement());
		blockcontainer.add(legendtitle, RectangleEdge.LEFT);
		blockcontainer.add(legendtitle1, RectangleEdge.RIGHT);
		blockcontainer.add(new EmptyBlock(20D, 0.0D));
		CompositeTitle compositetitle = new CompositeTitle(blockcontainer);
		compositetitle.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(compositetitle);

		chart.setAntiAlias(false);
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, filename,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		
		String file = request.getContextPath()
				+ PATH + filename;
		request.setAttribute("filename", filename);
		request.setAttribute("PNGURL", file);
		return filename;
	}
    
    public String createDrillXYLineBarChar_main(HttpServletRequest request,HttpServletResponse response, String title,
			String xtitle, String ytitle1, String ytitle2, int width,
			int height, DefaultCategoryDataset dataset1,
			DefaultCategoryDataset dataset2) {
		String filename = "";
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("黑体", Font.BOLD, 15)); // 设置标题字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 12));// 设置图例的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));// 设置轴向的字体
		ChartFactory.setChartTheme(standardChartTheme);// 应用主题样式

		JFreeChart chart = ChartFactory.createBarChart(title, // chart title
				xtitle, // x轴标题，domain axis label
				ytitle1, // y轴标题，range axis label
				dataset1, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				true, // tooltips?
				false // URLs?
				);
		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryDataset categorydataset = dataset2; // 设置第二个数据集
		plot.setDataset(1, categorydataset);
		plot.mapDatasetToRangeAxis(1, 1);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setOutlinePaint(Color.white); // 设置图片边框颜色，去掉边框

		// 柱体的样式设计
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, Color.orange);
		renderer.setDrawBarOutline(false);
		// 设置柱顶数据,API中居然没有StandardCategoryItemLabelGenerator这个类
		//renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		//renderer.setSeriesItemLabelsVisible(0, true);
		// 防止由于柱体太少而动态增加柱体宽度（JFreeChart默认是根据柱体多少而显示柱体宽度的）
		int k = dataset1.getColumnCount();
		if (k == 1) {
			plot.getDomainAxis().setLowerMargin(0.26);
			plot.getDomainAxis().setUpperMargin(0.66);
		} else if (k < 6) {
			double margin = (1.0 - k * 0.08) / 3;
			plot.getDomainAxis().setLowerMargin(margin);
			plot.getDomainAxis().setUpperMargin(margin);
			((BarRenderer) plot.getRenderer()).setItemMargin(margin);
		} else {
			((BarRenderer) plot.getRenderer()).setItemMargin(0.1);
		}

		/*------设置Y轴----*/
		double unit = 1d;// 刻度的长度
		// 右边Y轴，相关属性设置
		NumberAxis numberaxis1 = new NumberAxis(ytitle2);
		unit = Math.floor(10);// 刻度的长度
		// NumberTickUnit ntu= new NumberTickUnit(unit);
		// numberaxis1.setTickUnit(ntu);
		// numberaxis1.setRange(0,100);//刻度范围
		numberaxis1.setAutoRange(true);
		plot.setRangeAxis(1, numberaxis1);

		// 左边Y轴
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		numberaxis.setAutoTickUnitSelection(true);
		// numberaxis.setRange(0.0, 100.0);//刻度的范围
		numberaxis.setAutoRange(true);
		// ntu= new NumberTickUnit(unit);
		// numberaxis .setTickUnit(ntu);
		// ------设置柱状体与图片边框的上下间距---
		numberaxis.setUpperMargin(0.05);
		numberaxis.setLowerMargin(0.05);

		/*------设置X轴----*/
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
		/*------设置X轴标题的倾斜程度----*/
//		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
//				.createUpRotationLabelPositions(Math.PI / 6.0));
		/*------设置柱状体与图片边框的左右间距--*/
		// domainAxis.setLowerMargin(0.01);
		// domainAxis.setUpperMargin(0.01);

		// 设置折线图的样式
		LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		lineandshaperenderer
				.setBaseItemLabelFont(new Font("隶书", Font.BOLD, 10));
		CustomCategoryURLGenerator cGenerator=new CustomCategoryURLGenerator();
		String drillId = request.getParameter("drillId");
//		 String urlSrc = request.getParameter("url");
//		 int index = urlSrc.indexOf('?');
//		 String urlHead = urlSrc.substring(0, index+1);
//		 String s = urlSrc.substring(index+1);
//		 String urlBottom = s.replace('?','&');
//		 String url = urlHead+urlBottom;
//		String url = request.getParameter("url");
//		 List urls = new ArrayList();
//			for(int i = 0,count = dataset1.getColumnCount();i<count;i++){
//				urls.add("javascript:WCB.drill('"+drillId+"','"+dataset1.getColumnKey(i)+"','"+url+"')");
//			}
//	    cGenerator.addURLSeries(urls);
//		renderer.setSeriesItemURLGenerator(0, cGenerator);
		//设置钻取
				setBarDrill(request,dataset1,renderer);
		plot.setRenderer(1, lineandshaperenderer);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		// 图例1声明及相关样式设置
		LegendTitle legendtitle = new LegendTitle(plot.getRenderer(0));
		// 图例2声明及相关样式设置
		LegendTitle legendtitle1 = new LegendTitle(plot.getRenderer(1));
		BlockContainer blockcontainer = new BlockContainer(
				new BorderArrangement());
		blockcontainer.add(legendtitle, RectangleEdge.LEFT);
		blockcontainer.add(legendtitle1, RectangleEdge.RIGHT);
		blockcontainer.add(new EmptyBlock(20D, 0.0D));
		CompositeTitle compositetitle = new CompositeTitle(blockcontainer);
		compositetitle.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(compositetitle);

		chart.setAntiAlias(false);
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, filename,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		
		String file = request.getContextPath()
				+ PATH + filename;
		request.setAttribute("filename", filename);
		request.setAttribute("PNGURL", file);
		return filename;
	}

/**
	 * 创建有钻取功能的柱状图
	 * @param dataset
	 * @param title
	 * @param xtitle
	 * @param ytitle
	 * @param width
	 * @param height
	 * @param request
	 * @param useMap
	 * @param is3D
	 * @param isone
	 * @param islegend
	 * @param url
	 * @return
	 */
	public String createDrillBarChart(HttpServletRequest request,HttpServletResponse response,DefaultCategoryDataset dataset, String title,
			String xtitle, String ytitle, int width, int height,
			 String useMap, boolean is3D, boolean isone,
			boolean islegend) {
		JFreeChart chart = ChartFactory.createBarChart3D(title,// 图表标题
				xtitle,// 目录轴的显示标签(x轴)
				ytitle,// 数值轴的显示标签（y轴）
				dataset, // 数据
				PlotOrientation.VERTICAL,// 图表方向：水平、垂直
				islegend,// 是否显示图例(对于简单的柱状图必须是false)
				true,// 是否tooltip
				false// 是否生成URL链接
				);

		Font font = new Font("宋体", Font.BOLD, 16);
		// 设置正上方的标题
		chart.setTitle(new TextTitle(title, font));
		// 图例显示位置 设置底部标题
		if (islegend) {
			chart.getLegend().setVisible(true);
			chart.getLegend().setPosition(RectangleEdge.RIGHT);
			chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
		}
		CategoryPlot plot = chart.getCategoryPlot();// 获得图表区域对象

		// 设置图表的纵轴和横轴org.jfree.chart.axis.CategoryAxis
		CategoryAxis domainAxis = plot.getDomainAxis();// X轴
		// domainAxis.setLowerMargin(0.1);// 设置距离图片左端距离此时为10%
		// domainAxis.setUpperMargin(0.1);// 设置距离图片右端距离此时为百分之10
		// domainAxis.setCategoryLabelPositionOffset(10);// 图表横轴与标签的距离(10像素)
		// domainAxis.setCategoryMargin(0.2);// 横轴标签之间的距离20%
		// 设置X坐标上的文字
		domainAxis.setTickLabelFont(font);
		// 设置X轴标题
		domainAxis.setLabelFont(font);

		// domainAxis.setMaximumCategoryLabelLines(1);
		// domainAxis.setMaximumCategoryLabelWidthRatio(0);
		// 设定柱子的属性
		// 设置字体显示角度
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// 设置纵项数值

		NumberAxis vn = (NumberAxis) plot.getRangeAxis();
		vn.setAutoRangeIncludesZero(true);
		// vn.setTickUnit(new NumberTickUnit(1));
		vn.setNumberFormatOverride(new DecimalFormat("0"));

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(0.1);// 设置最高的一个柱与图片顶端的距离(最高柱的10%)
		// 设置Y坐标上的文字
		rangeAxis.setTickLabelFont(font);
		// 设置Y轴标题
		rangeAxis.setLabelFont(font);

		// 设置Y轴刻度
		rangeAxis.setAutoRange(true);
		rangeAxis.setAutoRangeMinimumSize(1);
		rangeAxis.setLowerBound(0);

		// 设置图表的颜色
		// BarRenderer3D renderer = new BarRenderer3D();
		BarRenderer renderer;
		if (isone) {
			if (is3D) {
				renderer = new CustomBarRenderer3D();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			} else {
				renderer = new CustomBarRenderer();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			}
		} else {
			if (is3D) {
				renderer = new BarRenderer3D();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			} else {
				renderer = new BarRenderer();
				renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
			}
		}
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setIncludeBaseInRange(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
				"{2}", NumberFormat.getNumberInstance()));
		/*
		 * renderer .setBaseItemLabelGenerator(new
		 * StandardCategoryItemLabelGenerator(
		 * "{0}={2}人",NumberFormat.getNumberInstance()));
		 */
		renderer.setBaseOutlinePaint(Color.red);
		/*
		 * renderer.setSeriesPaint(0, new Color(0, 255, 255));// 计划柱子的颜色为青色
		 * renderer.setSeriesOutlinePaint(0, Color.BLACK);// 边框为黑色
		 * renderer.setSeriesPaint(1, new Color(0, 255, 0));// 实报柱子的颜色为绿色
		 * renderer.setSeriesOutlinePaint(1, Color.red);// 边框为红色
		 */renderer.setItemMargin(0.1);// 组内柱子间隔为组宽的10%
		// java.lang.String labelFormat:"{0}={1}({2})":会显示成：apple=120(5%)
		// java.text.NumberFormat类有三个方法可以产生下列数据的标准格式化器：
		// 数字：NumberFormat.getNumberInstance();
		// 货币：NumberFormat.getCurrencyInstance();
		// 百分数：NumberFormat.getPercentInstance();
		// 设置柱状的宽度
		renderer.setMaximumBarWidth(0.1);
		renderer.setMinimumBarLength(0.1);
		// 设置底部
		renderer.setLegendItemLabelGenerator(new StandardCategorySeriesLabelGenerator(
				"{0}"));
		renderer.setBaseItemLabelsVisible(true);
		//设置钻取
		setBarDrill(request,dataset,renderer);
		plot.setRenderer(renderer);// 使用我们设计的效果

		// 设置纵横坐标的显示位置
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);// 显示在下端(柱子竖直)或左侧(柱子水平)
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT); // 显示在下端(柱子水平)或左侧(柱子竖直)
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		String filename="";
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info,request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, filename,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		
		
		String file = request.getContextPath()
				+ PATH + filename;
		request.setAttribute("filename", filename);
		request.setAttribute("PNGURL", file);
		return filename;
	}

	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param width
	 * @param height
	 * @param session
	 * @param useMap
	 * @param is3D
	 * @return
	 */
	public String createDrillPieChart(HttpServletRequest request,HttpServletResponse response,DefaultPieDataset dataset, String title, int width,
			int height, String useMap, boolean is3D) {
		String filename = "";
		PiePlot plot = null;
		if (is3D) {
			plot = new PiePlot3D((DefaultPieDataset) dataset);// 3D饼图
		} else {
			plot = new PiePlot((DefaultPieDataset) dataset);
		}
//		plot.setPieIndex(1);
		
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色
		chart.setTitle(title);// 可选，设置图片标题
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		plot.setNoDataMessage("无对应数据显示");

		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}:{1}/{2}", NumberFormat.getNumberInstance(),
				// "{1}/{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));

		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.8f);
		// 指定显示的饼图上圆形(true)还椭圆形(false)
		plot.setCircular(true);
		// 设置第一个 饼块section 的开始位置，默认是12点钟方向
		plot.setStartAngle(90);
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		//设置饼图钻取
		setPieDrill(request,dataset,plot);
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, useMap,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		    
			String file = request.getContextPath()
					+PATH + filename;
			request.setAttribute("filename", filename);
			request.setAttribute("PNGURL", file);
			return filename;
	}
	
	public String createDrillPieChart_bottom(HttpServletRequest request,HttpServletResponse response,DefaultPieDataset dataset, String title, int width,
			int height, String useMap, boolean is3D) {
		String filename = "";
		PiePlot plot = null;
		if (is3D) {
			plot = new PiePlot3D((DefaultPieDataset) dataset);// 3D饼图
		} else {
			plot = new PiePlot((DefaultPieDataset) dataset);
		}
//		plot.setPieIndex(1);
		
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色
		chart.setTitle(title);// 可选，设置图片标题
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		plot.setNoDataMessage("无对应数据显示");

		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		/*plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}:{1}/{2}", NumberFormat.getNumberInstance(),
				// "{1}/{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));*/
		plot.setLabelGenerator(null);
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));

		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.8f);
		// 指定显示的饼图上圆形(true)还椭圆形(false)
		plot.setCircular(true);
		// 设置第一个 饼块section 的开始位置，默认是12点钟方向
		plot.setStartAngle(90);
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		//设置饼图钻取
		setPieDrill(request,dataset,plot);
		chart.getLegend().setPosition(RectangleEdge.BOTTOM);
		chart.getLegend().setVisible(true);//不显示图例
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, useMap,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		    
			String file = request.getContextPath()
					+ PATH + filename;
			request.setAttribute("filename", filename);
			request.setAttribute("PNGURL", file);
			return filename;
	}
	
	
	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param width
	 * @param height
	 * @param session
	 * @param useMap
	 * @param is3D
	 * @return
	 */
	public String createDrillPieChart_parkMian(HttpServletRequest request,HttpServletResponse response,DefaultPieDataset dataset, String title, int width,
			int height, String useMap, boolean is3D) {
		String filename = "";
		PiePlot plot = null;
		if (is3D) {
			plot = new PiePlot3D((DefaultPieDataset) dataset);// 3D饼图
		} else {
			plot = new PiePlot((DefaultPieDataset) dataset);
		}
//		plot.setPieIndex(1);
		
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色
		chart.setTitle(title);// 可选，设置图片标题
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		plot.setNoDataMessage("无对应数据显示");

		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		/*plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}:{1}/{2}", NumberFormat.getNumberInstance(),
				// "{1}/{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));*/
		plot.setLabelGenerator(null);
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));

		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.8f);
		// 指定显示的饼图上圆形(true)还椭圆形(false)
		plot.setCircular(true);
		// 设置第一个 饼块section 的开始位置，默认是12点钟方向
		plot.setStartAngle(90);
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		//设置饼图钻取
		setPieDrill(request,dataset,plot);
		chart.getLegend().setPosition(RectangleEdge.LEFT);
		chart.getLegend().setVisible(true);//不显示图例
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, useMap,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		    
			String file = request.getContextPath()
					+PATH + filename;
			request.setAttribute("filename", filename);
			request.setAttribute("PNGURL", file);
			return filename;
	}
	
	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param width
	 * @param height
	 * @param session
	 * @param useMap
	 * @param is3D
	 * @return
	 */
	public String createDrillPieChart_park(HttpServletRequest request,HttpServletResponse response,DefaultPieDataset dataset, String title, int width,
			int height, String useMap, boolean is3D) {
		String filename = "";
		PiePlot plot = null;
		if (is3D) {
			plot = new PiePlot3D((DefaultPieDataset) dataset);// 3D饼图
		} else {
			plot = new PiePlot((DefaultPieDataset) dataset);
		}
//		plot.setPieIndex(1);
		
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色
		chart.setTitle(title);// 可选，设置图片标题
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		plot.setNoDataMessage("无对应数据显示");

		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		/*plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}:{1}/{2}", NumberFormat.getNumberInstance(),
				// "{1}/{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));*/
		plot.setLabelGenerator(null);
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));

		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.8f);
		// 指定显示的饼图上圆形(true)还椭圆形(false)
		plot.setCircular(true);
		// 设置第一个 饼块section 的开始位置，默认是12点钟方向
		plot.setStartAngle(90);
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		//设置饼图钻取
		setPieDrill(request,dataset,plot);
		chart.getLegend().setPosition(RectangleEdge.RIGHT);
		chart.getLegend().setVisible(true);//不显示图例
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, useMap,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		    
			String file = request.getContextPath()
					+ PATH + filename;
			request.setAttribute("filename", filename);
			request.setAttribute("PNGURL", file);
			return filename;
	}
	
	/**
	 * 
	 * @param dataset
	 * @param title
	 * @param width
	 * @param height
	 * @param session
	 * @param useMap
	 * @param is3D
	 * @return
	 */
	public String createDrillManyPieChart(HttpServletRequest request,HttpServletResponse response,DefaultPieDataset dataset, String title, int width,
			int height, String useMap, boolean is3D) {
		String filename = "";
		PiePlot plot = null;
		if (is3D) {
			plot = new PiePlot3D((DefaultPieDataset) dataset);// 3D饼图
		} else {
			plot = new PiePlot((DefaultPieDataset) dataset);
		}
//		plot.setPieIndex(1);
		
		JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		chart.setBackgroundPaint(java.awt.Color.white);// 可选，设置图片背景色
		chart.setTitle(title);// 可选，设置图片标题
		plot.setToolTipGenerator(new StandardPieToolTipGenerator());
		plot.setNoDataMessage("无对应数据显示");

		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}:{1}/{2}", NumberFormat.getNumberInstance(),
				// "{1}/{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));

		// 设置背景色为白色
		chart.setBackgroundPaint(Color.white);
		// 指定图片的透明度(0.0-1.0)
		plot.setForegroundAlpha(0.8f);
		// 指定显示的饼图上圆形(true)还椭圆形(false)
		plot.setCircular(true);
		// 设置第一个 饼块section 的开始位置，默认是12点钟方向
		plot.setStartAngle(90);
		plot.setLabelFont(new Font("宋体", Font.BOLD, 12));
		//设置饼图钻取
		setPieDrill(request,dataset,plot);
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, request.getSession());
			java.io.PrintWriter pw= response.getWriter();
			ChartUtilities.writeImageMap(pw, useMap,  info, false);
			pw.flush();
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		    
			/*String file = request.getContextPath()
					+ PATH + filename;
			request.setAttribute("filename", filename);
			request.setAttribute("PNGURL", file);*/
			return filename;
	}
	
	//设置柱状图钻取
	private void setBarDrill(HttpServletRequest request,DefaultCategoryDataset dataset,BarRenderer renderer){
		CustomCategoryURLGenerator cGenerator=new CustomCategoryURLGenerator();
		 String sDrill = request.getParameter("drill");
		 if(null == sDrill){
			 return;
		 }
		for(int k = 0;k<dataset.getRowCount();k++){
			List urls = new ArrayList();
			for(int i = 0,count = dataset.getColumnCount();i<count;i++){
				 JSONObject obj = JSONObject.fromObject(sDrill);
				 obj.put("itemType", dataset.getRowKey(k));
				 obj.put("drillKey", dataset.getColumnKey(i));
				 String jsParam = "";
				 for(Object elem : obj.keySet()){
					 jsParam += elem+":"+obj.get(elem)+"," ;
				 }
				 jsParam=jsParam.substring(0, jsParam.length()-1);
				 urls.add("javascript:WCB.drill('"+jsParam+"')");			
			}
			cGenerator.addURLSeries(urls);
		}
		renderer.setItemURLGenerator(cGenerator);
	}
	//设置柱状图钻取
		private void setPieDrill(HttpServletRequest request,DefaultPieDataset dataset,PiePlot plot){
			 Map urlMap=new HashMap();
			 int count = dataset.getKeys().size();
			 String sDrill = request.getParameter("drill");
			 if(null == sDrill){
				 return ;
			 }
			 for(int i=0;i<count;i++){
				 JSONObject obj = JSONObject.fromObject(sDrill);
				 obj.put("drillKey", dataset.getKey(i));
				 String jsParam = "";
				 for(Object elem : obj.keySet()){
					 jsParam += elem+":"+obj.get(elem)+"," ;
				 }
				 jsParam=jsParam.substring(0, jsParam.length()-1);
				String js = "javascript:WCB.drill('"+jsParam+"')";
				urlMap.put(dataset.getKey(i), js); 
			 }		    		
			CustomPieURLGenerator cpu = new CustomPieURLGenerator(); 
			cpu.addURLs(urlMap); 
			plot.setURLGenerator(cpu);//设定链接
		}
    
    /**
	 * 折线图()
	 * @param session
	 * @param title
	 * @param xtitle
	 * @param ytitle
	 * @param width
	 * @param height
	 * @param useMap
	 * @param dataset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String createLineChart(HttpSession session,XYLineChar barXYLine) throws UnsupportedEncodingException {
		String filename = "";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<String, Map<Object, Number>> dataMap = barXYLine.getDataMap();
		for(String str:dataMap.keySet()){
			Map<Object, Number> map = dataMap.get(str);
			for(Object o:map.keySet()){
				dataset.addValue(map.get(o), str, o.toString());
			}
		}
		// 定义图表对象
		JFreeChart chart = ChartFactory.createLineChart(barXYLine.getTitle(), // 折线图名称
				barXYLine.getXtitle(), // 横坐标名称
				barXYLine.getYtitle(), // 纵坐标名称
				dataset , // 数据
				PlotOrientation.VERTICAL, // 水平显示图像
				barXYLine.isIslegend(), // include legend
				true, // tooltips
				false // urls
				);
		chart.getTitle().setFont(new Font("黑体", Font.ITALIC, 20)); // 解决标题乱码
		if(barXYLine.isIslegend()){
			chart.getLegend().setItemFont(
					new Font("宋体", Font.TYPE1_FONT, 16)); // 解决图例乱码
		}
		
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setRangeGridlinesVisible(true); // 是否显示格子线
		plot.setBackgroundAlpha(0.3f); // 设置背景透明度
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		CategoryAxis categoryAxis = plot.getDomainAxis();// 取得横轴
		categoryAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置横轴显示标签的字体

		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		/*------设置X轴标题的倾斜程度----*/
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		
		categoryAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决横坐标乱码

		ValueAxis numberAxis = plot.getRangeAxis();// 取得纵轴

		numberAxis.setLabelFont(new Font("宋体", Font.TYPE1_FONT, 16));// 设置纵轴显示标签的字体

		numberAxis.setTickLabelFont(new Font("宋体", Font.TYPE1_FONT, 16)); // 解决纵坐标乱码

		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer)plot.getRenderer();
		lineandshaperenderer.setBaseShapesVisible(true);
				        
				        
	    rangeAxis.setAutoRangeIncludesZero(true);
		rangeAxis.setUpperMargin(0.20);
		rangeAxis.setLabelAngle(Math.PI / 2.0);
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		
		try {
			filename = ServletUtilities.saveChartAsPNG(chart, barXYLine.getWidth(), barXYLine.getHeight(),
					info, session);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return filename;
	}
}
