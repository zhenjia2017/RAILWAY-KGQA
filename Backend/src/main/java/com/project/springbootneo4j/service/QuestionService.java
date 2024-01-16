package com.project.springbootneo4j.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface QuestionService {

    Map<String, Object> answer(String userQuestion);

    String analysisMsg();

    List<String> analysisSegment();

    List<String> analysisEntityRecognition();

    List<String> analysisEntityLinking();

    int analysisQuestionType();


}
