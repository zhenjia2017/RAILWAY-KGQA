package com.project.springbootneo4j.service.impl;

import com.hankcs.hanlp.seg.common.Term;
import com.project.springbootneo4j.core.CoreProcessor;
import com.project.springbootneo4j.model.Station;
import com.project.springbootneo4j.model.TrainInfo;
import com.project.springbootneo4j.model.TrainNo;
import com.project.springbootneo4j.model.TrainType;
import com.project.springbootneo4j.repository.QuestionRepository;
import com.project.springbootneo4j.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
public class QuestionServiceImpl implements QuestionService {
    // 分析结果消息
    private String analysisMsg = "";

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CoreProcessor coreProcessor;


    @Override
    public Map<String, Object> answer(String userQuestion) {

        List<Term> questionAnalysisResult = coreProcessor.analysis(userQuestion);

        int questionType = coreProcessor.getPytorchService(userQuestion);
//        int questionType = coreProcessor.questionClassify(userQuestion);
//        questionType = 4;
        System.out.printf("问题分类结果:%d%n", questionType);
        analysisMsg = coreProcessor.getAnalysisMsg() + String.format("问题分类结果:%d", questionType);

        List<String> answer = new ArrayList<>();
        String answerType = "";

        switch (questionType) {
            case 1:
                answer = getProvinceStation(questionAnalysisResult);
                answerType = "station";
                break;
            case 2:
                answer = getPassTrainNo(questionAnalysisResult);
                answerType = "train_no";
                break;
            case 3:
                answer = getFromTo(questionAnalysisResult);
                answerType = "train_no";
                break;
            case 4:
                answer = getStartStationTrainNo(questionAnalysisResult);
                answerType = "train_no";
                break;
            case 5:
                answer = getEndStationTrainNo(questionAnalysisResult);
                answerType = "train_no";
                break;
            case 11:
                answer = getFromToStartTime(questionAnalysisResult);
                answerType = "time";
                break;
            case 12:
                answer = getFromToEndTime(questionAnalysisResult);
                answerType = "time";
                break;
            case 18:
                answer = getPassStation(questionAnalysisResult);
                answerType = "station";
                break;
            case 19:
                answer = getEndStation(questionAnalysisResult);
                answerType = "station";
                break;
            case 20:
                answer = getStartStation(questionAnalysisResult);
                answerType = "station";
                break;
        }

        // 添加键值对
        Map<String, Object> result = new HashMap<>();
        result.put("answerType", answerType);
        result.put("answer", answer);

        return result;
    }

    @Override
    public String analysisMsg() {
        return analysisMsg;
    }

    @Override
    public List<String> analysisSegment() {
        List<Term> seg = coreProcessor.getSeg();
        List<String> result = new ArrayList<>();
        for (Term term : seg) {
            result.add(term.toString());
        }
        return result;
    }

    @Override
    public List<String> analysisEntityRecognition() {
        List<Term> entities = coreProcessor.getEntities();
        List<String> result = new ArrayList<>();
        for (Term term : entities) {
            result.add(term.toString());
        }
        return result;
    }

    @Override
    public List<String> analysisEntityLinking() {
        List<Term> neo4jEntities = coreProcessor.getNeo4jEntities();
        List<String> result = new ArrayList<>();
        for (Term term : neo4jEntities) {
            result.add(term.word);
        }
        return result;
    }

    @Override
    public int analysisQuestionType() {
        return coreProcessor.getQuestionType();
    }

    /**
     * 检查问题中是否有车次类型约束
     *
     * @param questionAnalysisResult 问题分析结果
     * @return true/false
     */
    private boolean checkTrainTypeConstrain(List<Term> questionAnalysisResult) {
        for (Term term : questionAnalysisResult) {
            if (Objects.equals(term.nature.toString(), TrainType.className))
                return true;
        }
        return false;
    }

    /**
     * 1 {province}有什么车站？
     */
    private List<String> getProvinceStation(List<Term> questionAnalysisResult) {
        String province = "";
        if (!questionAnalysisResult.isEmpty()) {
            province = questionAnalysisResult.get(0).word;
        }
        List<Station> stations = questionRepository.getProvinceStation(province);
        List<String> answer = new ArrayList<>();
        for (Station station : stations) {
            answer.add(station.getName());
        }
        return answer;
    }

    /**
     * 2 有什么车途经{station}？
     */
    private List<String> getPassTrainNo(List<Term> questionAnalysisResult) {
        String station = "";
        String train_type = "";
        List<TrainNo> trainNos;
        for (Term term : questionAnalysisResult) {
            switch (term.nature.toString()) {
                case "station":
                    station = term.word;
                    break;
                case "train_type":
                    train_type = term.word;
                    break;
            }
        }
        if (checkTrainTypeConstrain(questionAnalysisResult)) {
            trainNos = questionRepository.getPassTrainNoWithConstrain(station, train_type);
        } else {
            trainNos = questionRepository.getPassTrainNo(station);
        }

        List<String> answer = new ArrayList<>();
        for (TrainNo trainNo : trainNos) {
            answer.add(trainNo.getName());
        }
        return answer;
    }

    /**
     * 3 {station}到{station}有什么{train_type}？
     */
    private List<String> getFromTo(List<Term> questionAnalysisResult) {
        String station_from = "";
        String station_to = "";
        String train_type = "";
        List<TrainNo> trainNos = new ArrayList<>();
        for (Term term : questionAnalysisResult) {
            switch (term.nature.toString()) {
                case "station":
                    if (Objects.equals(station_from, ""))
                        station_from = term.word;
                    else
                        station_to = term.word;
                    break;
                case "train_type":
                    train_type = term.word;
                    break;
            }
        }
        if (checkTrainTypeConstrain(questionAnalysisResult)) {
            trainNos = questionRepository.getFromToWithConstrain(station_from, station_to, train_type);
        } else {
            trainNos = questionRepository.getFromTo(station_from, station_to);
        }

        List<String> answer = new ArrayList<>();
        for (TrainNo trainNo : trainNos) {
            answer.add(trainNo.getName());
        }
        return answer;
    }

    /**
     * 4 {station}始发的{train_type}有哪些？
     */
    private List<String> getStartStationTrainNo(List<Term> questionAnalysisResult) {
        String station = "";
        String train_type = "";
        List<TrainNo> trainNos = new ArrayList<>();
        for (Term term : questionAnalysisResult) {
            switch (term.nature.toString()) {
                case "station":
                    station = term.word;
                    break;
                case "train_type":
                    train_type = term.word;
                    break;
            }
        }
        if (checkTrainTypeConstrain(questionAnalysisResult))
            trainNos = questionRepository.getStartStationTrainNoWithConstrain(station, train_type);
        else
            trainNos = questionRepository.getStartStationTrainNo(station);

        List<String> answer = new ArrayList<>();
        for (TrainNo trainNo : trainNos) {
            answer.add(trainNo.getName());
        }
        return answer;
    }

    /**
     * 5 {station}是终点站的{train_type}有哪些？
     */
    private List<String> getEndStationTrainNo(List<Term> questionAnalysisResult) {
        String station = "";
        String train_type = "";
        List<TrainNo> trainNos = new ArrayList<>();
        for (Term term : questionAnalysisResult) {
            switch (term.nature.toString()) {
                case "station":
                    station = term.word;
                    break;
                case "train_type":
                    train_type = term.word;
                    break;
            }
        }
        if (checkTrainTypeConstrain(questionAnalysisResult))
            trainNos = questionRepository.getEndStationTrainNoWithConstrain(station, train_type);
        else
            trainNos = questionRepository.getEndStationTrainNo(station);

        List<String> answer = new ArrayList<>();
        for (TrainNo trainNo : trainNos) {
            answer.add(trainNo.getName());
        }
        return answer;
    }

    /**
     * 11 从{station}到{station}的{train_type}有几点发车的？
     */
    private List<String> getFromToStartTime(List<Term> questionAnalysisResult) {
        String station_from = "";
        String station_to = "";
        String train_type = "";
        for (Term term : questionAnalysisResult) {
            switch (term.nature.toString()) {
                case "station":
                    if (Objects.equals(station_from, ""))
                        station_from = term.word;
                    else
                        station_to = term.word;
                    break;
                case "train_type":
                    train_type = term.word;
                    break;
            }
        }

        List<TrainInfo> trainInfos = new ArrayList<>();
        if (checkTrainTypeConstrain(questionAnalysisResult)) {
            trainInfos = questionRepository.getFromToStartTimeWithConstrain(station_from, station_to, train_type);
        } else {
            trainInfos = questionRepository.getFromToStartTime(station_from, station_to);
        }

        List<String> answer = new ArrayList<>();
        for (TrainInfo trainInfo : trainInfos) {
            answer.add(trainInfo.getName());
        }

        return answer;
    }

    /**
     * 12 从{station}到{station}的{train_type}有几点到达的？
     */
    private List<String> getFromToEndTime(List<Term> questionAnalysisResult) {
        String station_from = "";
        String station_to = "";
        String train_type = "";
        for (Term term : questionAnalysisResult) {
            switch (term.nature.toString()) {
                case "station":
                    if (Objects.equals(station_from, ""))
                        station_from = term.word;
                    else
                        station_to = term.word;
                    break;
                case "train_type":
                    train_type = term.word;
                    break;
            }
        }

        List<TrainInfo> trainInfos = new ArrayList<>();
        if (checkTrainTypeConstrain(questionAnalysisResult)) {
            trainInfos = questionRepository.getFromToEndTimeWithConstrain(station_from, station_to, train_type);
        } else {
            trainInfos = questionRepository.getFromToEndTime(station_from, station_to);
        }

        List<String> answer = new ArrayList<>();
        for (TrainInfo trainInfo : trainInfos) {
            answer.add(trainInfo.getName());
        }
        return answer;
    }

    /**
     * 18 {train_no}途径哪些站？
     */
    private List<String> getPassStation(List<Term> questionAnalysisResult) {
        String train_no = "";
        List<Station> stations = new ArrayList<>();
        for (Term term : questionAnalysisResult) {
            if ("train_no".equals(term.nature.toString())) {
                train_no = term.word;
            }
        }

        stations = questionRepository.getPassStation(train_no);
        List<String> answer = new ArrayList<>();
        for (Station station : stations) {
            answer.add(station.getName());
        }
        return answer;
    }

    /**
     * 19 {train_no}车次终点在哪儿?
     */
    private List<String> getEndStation(List<Term> questionAnalysisResult) {
        String train_no = "";
        List<Station> stations = new ArrayList<>();
        for (Term term : questionAnalysisResult) {
            if ("train_no".equals(term.nature.toString())) {
                train_no = term.word;
            }
        }

        stations = questionRepository.getEndStation(train_no);
        List<String> answer = new ArrayList<>();
        for (Station station : stations) {
            answer.add(station.getName());
        }
        return answer;
    }

    /**
     * 20 {train_no}车次起点在哪儿?
     */
    private List<String> getStartStation(List<Term> questionAnalysisResult) {
        String train_no = "";
        List<Station> stations = new ArrayList<>();
        for (Term term : questionAnalysisResult) {
            if ("train_no".equals(term.nature.toString())) {
                train_no = term.word;
            }
        }

        stations = questionRepository.getStartStation(train_no);
        List<String> answer = new ArrayList<>();
        for (Station station : stations) {
            answer.add(station.getName());
        }
        return answer;
    }


    public static void main(String[] args) {
        new QuestionServiceImpl().answer("哪些车站位于吉林省");
    }
}
