package com.project.springbootneo4j.controller;

import com.project.springbootneo4j.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kgqa")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET, path = "/query")
    public Map<String, Object> query(@RequestParam String question) throws Exception {
//        return JSON.toJSONString(questionService.answer(question));
        return questionService.answer(question);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/query/msg")
    public String analysisMsg() {
        return questionService.analysisMsg();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/query/analysis/seg")
    public List<String> analysisSegment() {
        return questionService.analysisSegment();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/query/analysis/rec")
    public List<String> analysisEntityRecognition() {
        return questionService.analysisEntityRecognition();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/query/analysis/link")
    public List<String> analysisEntityLinking() {
        return questionService.analysisEntityLinking();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/query/analysis/type")
    public int analysisQuestionType() {
        return questionService.analysisQuestionType();
    }
}
