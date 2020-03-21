package net.riking.echarts;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Lines;
import com.github.abel533.echarts.series.Series;
import net.riking.websockect.echarts.util.EnhancedOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EchartsTest {
    public static void main(String[] args) {
        EnhancedOption option = new EnhancedOption();
        option.title().text("折线图堆叠");
        option.tooltip().setTrigger(Trigger.axis);
        option.legend().data("邮件营销","联盟广告","视频广告","直接访问","搜索引擎");
        option.grid().left("3%").right("4%").bottom("3%").containLabel(true);
        option.toolbox().feature(Tool.saveAsImage);

        //横轴为值轴
        CategoryAxis category = new CategoryAxis();
        category.data("周一","周二","周三","周四","周五","周六","周日").boundaryGap(false);
        option.xAxis(category);

        //创建类目轴
        option.yAxis(new ValueAxis());

        List<Series> linesList= new ArrayList<>();
        linesList.add(new Lines().data(120, 132, 101, 134, 90, 230, 210)
                .name("邮件营销").stack("总量").type(SeriesType.line));
        linesList.add(new Lines().data(220, 182, 191, 234, 290, 330, 310)
                .name("联盟广告").stack("总量").type(SeriesType.line));
        linesList.add(new Lines().data(150, 232, 201, 154, 190, 330, 410)
                .name("视频广告").stack("总量").type(SeriesType.line));
        linesList.add(new Lines().data(320, 332, 301, 334, 390, 330, 320)
                .name("直接访问").stack("总量").type(SeriesType.line));
        linesList.add(new Lines().data(820, 932, 901, 934, 200, 100, 1)
                .name("搜索引擎").stack("总量").type(SeriesType.line));
        option.setSeries(linesList);
  /*      option.exportToHtml("line5.html");
        option.view();*/
        option.print();
        System.out.println(option.toString());
    }

    @Test
    public void test() {
        //地址:http://echarts.baidu.com/doc/example/line5.html
        EnhancedOption option = new EnhancedOption();
        option.legend("高度(km)与气温(°C)变化关系");

        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar), Tool.restore, Tool.saveAsImage);

        option.calculable(true);
        option.tooltip().trigger(Trigger.axis).formatter("Temperature : <br/>{b}km : {c}°C");

        ValueAxis valueAxis = new ValueAxis();
        valueAxis.axisLabel().formatter("{value} °C");
        option.xAxis(valueAxis);

        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.axisLine().onZero(false);
        categoryAxis.axisLabel().formatter("{value} km");
        categoryAxis.boundaryGap(false);
        categoryAxis.data(0, 10, 20, 30, 40, 50, 60, 70, 80);
        option.yAxis(categoryAxis);

        Line line = new Line();
        line.smooth(true).name("高度(km)与气温(°C)变化关系").data(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
        option.series(line);
        option.exportToHtml("line5.html");
        option.view();
    }
}
