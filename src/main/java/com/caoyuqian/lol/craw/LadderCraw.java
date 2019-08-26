package com.caoyuqian.lol.craw;

import com.caoyuqian.lol.entity.Ladder;
import com.caoyuqian.lol.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LadderCraw {

    /**
     * 爬取排行榜内容
     * @param url
     * @param page
     * @return
     * @throws IOException
     */
    public List<Ladder> ladderCraw(String url, int page) throws IOException {
        //排名
        List<String> rankingList = new ArrayList<>();
        //召唤师名称
        List<String> nameList = new ArrayList<>();
        //段位
        List<String> levelList = new ArrayList<>();
        //分数
        List<String> lpList = new ArrayList<>();
        //等级
        List<String> lvList = new ArrayList<>();
        //胜率
        List<String> winRatioList = new ArrayList<>();
        //数据封装
        List<Ladder> ladders = new ArrayList<>();
        Document document = HttpUtil.get(url + page);

        //爬取前5名信息
        if (page == 1) {
            Elements elements = document.getElementsByClass("ranking-highest__item");
            Elements rankings = elements.select("div.ranking-highest__rank");
            Elements names = elements.select("a.ranking-highest__name");
            Elements levels = elements.select("div.ranking-highest__tierrank span");
            Elements lps = elements.select("div.ranking-highest__tierrank b");
            Elements winRatios = elements.select("span.winratio__text");
            Elements lvs = elements.select("div.ranking-highest__level");

            rankingList = rankings.stream().map(Element::text).collect(Collectors.toList());
            nameList = names.stream().map(Element::text).collect(Collectors.toList());
            levelList = levels.stream().map(Element::text).collect(Collectors.toList());
            lpList = lps.stream().map(element -> {
                return StringUtils.substringBefore(element.text(), " LP").replaceAll(",", "");
            }).collect(Collectors.toList());
            winRatioList = winRatios.stream().map(Element::text).collect(Collectors.toList());
            lvList = lvs.stream().map(element -> {
                if (element.text().startsWith("Lv.")) {
                    return StringUtils.substringAfter(element.text(), "Lv.");
                }
                return element.text();
            }).collect(Collectors.toList());
            ladders = new ArrayList<>(rankingList.size());
        }

        //爬取第6名之后的信息

        //爬取排名信息
        Elements eles = document.getElementsByClass("ranking-table__row");

        Elements ranks = eles.select("td.ranking-table__cell--rank");

        List<String> rankss = ranks.stream().map(Element::text).collect(Collectors.toList());

        rankingList.addAll(rankss);

        //爬取召唤师名称
        Elements names1 = eles.select("td.ranking-table__cell--summoner span");
        List<String> namess = names1.stream().map(Element::text).collect(Collectors.toList());
        namess = namess.stream().map(name -> {
            name = name.replace("<span>", "");
            name = name.replace("</span>", "");
            return name;
        }).collect(Collectors.toList());
        nameList.addAll(namess);

        //爬取召唤师段位
        Elements dws = eles.select("td.ranking-table__cell--tier");
        List<String> dwss = dws.stream().map(Element::text).collect(Collectors.toList());
        levelList.addAll(dwss);

        //爬取召唤师分数
        Elements lpss = eles.select("td.ranking-table__cell--lp");
        List<String> lpsss = lpss.stream().map(element -> {
            return StringUtils.substringBefore(element.text(), " LP").replaceAll(",", "");
        }).collect(Collectors.toList());
        lpList.addAll(lpsss);

        //爬取召唤师等级
        Elements levs = eles.select("td.ranking-table__cell--level");
        List<String> levss = levs.stream().map(Element::text).collect(Collectors.toList());
        lvList.addAll(levss);

        //爬取召唤师胜率
        Elements winRates = eles.select("td.ranking-table__cell--winratio span");
        List<String> winRatess = winRates.stream().map(Element::text).collect(Collectors.toList());
        winRatioList.addAll(winRatess);

        //将数据转换为对象
        for (int i = 0; i < rankingList.size(); i++) {
            Ladder ladder = Ladder.builder().name(nameList.get(i))
                    .ranking(rankingList.get(i))
                    .level(levelList.get(i))
                    .lp(lpList.get(i))
                    .lv(lvList.get(i))
                    .winRatio(winRatioList.get(i))
                    .build();

            ladders.add(ladder);
        }
        log.info(ladders.toString());
        return ladders;
    }

}
