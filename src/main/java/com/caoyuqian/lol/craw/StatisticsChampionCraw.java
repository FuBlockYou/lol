package com.caoyuqian.lol.craw;

import com.caoyuqian.lol.model.StatisticsChampion;
import com.caoyuqian.lol.service.StatisticsChampionService;
import com.caoyuqian.lol.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qian
 * @version V1.0
 * @Title: StatisticsChampion
 * @Package: com.caoyuqian.lol.craw
 * @Description: 英雄统计
 * @date 2019-08-27 09:59
 **/
@Component
public class StatisticsChampionCraw {

    private final static Logger log = LoggerFactory.getLogger(StatisticsChampionCraw.class);
    public List<StatisticsChampion> get() throws IOException {
        String url = "https://www.op.gg/statistics/ajax2/champion/";
        //String url = "https://www.op.gg/statistics/champion/";
        Document document;
        List<StatisticsChampion> statisticsChampions = new ArrayList<>();
        document = HttpUtil.get(url);
        Elements elements = document.select("table.StatisticsChampionTable tbody.Content");

        Elements tr = elements.select("tr.Row");
//        championNameList = championNames.stream().map()
       // log.info(tr.get(0).html());
        tr.forEach(element -> {
            Elements td = element.select("td.Cell");
            if (td.hasText()){
                StatisticsChampion statisticsChampion = StatisticsChampion.builder()
                        .rank(Integer.parseInt(StringUtils.strip(td.get(0).text())))
                        .championName(StringUtils.strip(td.select("td.Cell.ChampionName a").text()))
                        .lp(StringUtils.strip(td.get(3).getElementsByClass("Value").text()))
                        .games(Integer.parseInt(StringUtils.strip(td.get(4).text().replaceAll(",",""))))
                        .kda(Double.parseDouble(StringUtils.strip(td.select("td.Cell.KDARatio").attr("data-value"))))
                        .cs(Double.parseDouble(StringUtils.strip(td.select("span.Value.Green").text())))
                        .gold(Integer.parseInt(StringUtils.strip(td.select("span.Value.Orange").text()).replaceAll(",","")))
                        .build();
                statisticsChampions.add(statisticsChampion);
            }
        });
        log.info(statisticsChampions.toString());
        log.info(String.valueOf(statisticsChampions.size()));
        return statisticsChampions;
    }

}
