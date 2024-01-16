package com.project.springbootneo4j;

import com.project.springbootneo4j.repository.StationRepository;
import com.project.springbootneo4j.model.Station;
import com.project.springbootneo4j.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
class SpringBootNeo4jApplicationTests {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    QuestionService questionService;

    @Test
    public void query() {
//        return JSON.toJSONString(questionService.answer(question));
        System.out.println(questionService.answer("哪些车站位于吉林"));
    }



}
