package com.project.springbootneo4j.repository;

import com.project.springbootneo4j.model.Station;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends Neo4jRepository<Station, Long> {

    @Query("MATCH p=(:Station)-[:`途径`]->(:TrainNode)-[:`站点信息`]->(:TrainNo{name:'K1130'})<-[:`站点信息`]-(:TrainNode)<-[:`途径`]-(:Station) RETURN p")
    List<Station> getStation(@Param("name") String name);


}
