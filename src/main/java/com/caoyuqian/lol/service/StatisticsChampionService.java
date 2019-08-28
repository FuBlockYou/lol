package com.caoyuqian.lol.service;

import com.caoyuqian.lol.model.StatisticsChampion;
import com.caoyuqian.lol.repository.StatisticsChampionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author qian
 * @version V1.0
 * @Title: StatisticsChampionService
 * @Package: com.caoyuqian.lol.service
 * @Description: TOTO
 * @date 2019-08-27 17:41
 **/
@Slf4j
@Service
public class StatisticsChampionService {
    @Autowired
    private StatisticsChampionRepository statisticsChampionRepository;

    public Flux<StatisticsChampion> saveAll(List<StatisticsChampion> statisticsChampions){
        return statisticsChampionRepository
                .saveAll(statisticsChampions);
    }
}