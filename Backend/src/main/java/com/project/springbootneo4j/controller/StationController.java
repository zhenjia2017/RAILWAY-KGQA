package com.project.springbootneo4j.controller;

import com.project.springbootneo4j.repository.StationRepository;
import com.project.springbootneo4j.model.Station;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/kgqa")
public class StationController {

    @Resource
    private StationRepository stationRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/findAllStation")
    public List<Station> getStation(@RequestParam String station) {
        return stationRepository.getStation(station);
    }


}
