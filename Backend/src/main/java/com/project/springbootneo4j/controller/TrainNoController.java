package com.project.springbootneo4j.controller;

import com.project.springbootneo4j.repository.TrainNoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kgqa")
public class TrainNoController {

    @Autowired
    private TrainNoRepository trainNoRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/findAllTrainNo")
    public List<Map<String, String>> getTrainNo() {
        return trainNoRepository.getTrainNo();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/findOneTrainNo")
    public List<Map<String, String>> getOneTrainNo(@RequestParam String trainNo) {
        return trainNoRepository.getOneTrainNo(trainNo);
    }

}
