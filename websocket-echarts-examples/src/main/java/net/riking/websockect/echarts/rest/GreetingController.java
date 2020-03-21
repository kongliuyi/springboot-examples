package net.riking.websockect.echarts.rest;

import com.github.abel533.echarts.*;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.series.Lines;
import com.github.abel533.echarts.series.Series;
import net.riking.websockect.echarts.util.EnhancedOption;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class GreetingController {


  @MessageMapping("/hello")
  @SendTo("/topic/echarts")
  @ResponseBody
  public String greeting(Message message)  {

    System.out.println(message);
    EnhancedOption option = new EnhancedOption();
    option.title().text("折线图堆叠");
    option.tooltip().setTrigger(Trigger.axis);
    option.legend().data("邮件营销","联盟广告","视频广告","直接访问","搜索引擎");
    option.grid().left("3%").right("4%").bottom("3%").containLabel(true);
    option.toolbox().feature(Tool.saveAsImage);


    CategoryAxis category = new CategoryAxis();
    category.data("周一","周二","周三","周四","周五","周六","周日").boundaryGap(false);
    option.xAxis(category);


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

    return  option.toString();
  }

}