package com.project.springbootneo4j.repository;

import com.project.springbootneo4j.model.TrainNo;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TrainNoRepository extends Neo4jRepository<TrainNo, Long> {

    @Query("MATCH \n" +
            "(node1:TrainNode)<-[:`站点信息`]-(no:TrainNo{name:\"K1130\"})-[:`站点信息`]->(node2:TrainNode),\n" +
            "(s1:Station)<-[:`途径`]-(node1)-[:`站点顺序`]->(seq1:TrainInfo{name:\"1\"}),\n" +
            "(s2:Station)<-[:`途径`]-(node2)-[:`站点性质`]->(it2:TrainInfo{name:\"1\"}),\n" +
            "(node1)-[:`到达时间`]->(ta1:TrainInfo),\n" +
            "(node1)-[:`发车时间`]->(td1:TrainInfo),\n" +
            "(node1)-[:`行驶时长`]->(tt1:TrainInfo),\n" +
            "(node1)-[:`行驶距离`]->(dt1:TrainInfo),\n" +
            "(node1)-[:`站点性质`]->(it1:TrainInfo),\n" +
            "(node2)-[:`到达时间`]->(ta2:TrainInfo),\n" +
            "(node2)-[:`发车时间`]->(td2:TrainInfo),\n" +
            "(node2)-[:`行驶时长`]->(tt2:TrainInfo),\n" +
            "(node2)-[:`行驶距离`]->(dt2:TrainInfo),\n" +
            "(node2)-[:`站点顺序`]->(seq2:TrainInfo)\n" +
            "RETURN no.name AS trainNo, s1.name AS stationFrom, ta1.name AS timeArriveFrom, td1.name AS timeDepartureFrom, tt1.name AS timeTravelFrom, dt1.name AS distanceTravelFrom, seq1.name AS seqFrom,  it1.name AS  isTerminalFrom,  s2.name AS stationTo, ta2.name AS timeArriveTo, td2.name AS timeDepartureTo, tt2.name AS timeTravelTo, dt2.name AS distanceTravelTo, seq2.name AS seqTo, it2.name AS isTerminalTo\n")
    List<Map<String, String>> getTrainNo();

    @Query("MATCH \n" +
            "(node1:TrainNode)<-[:`站点信息`]-(no:TrainNo{name:{trainNo}})-[:`站点信息`]->(node2:TrainNode),\n" +
            "(s1:Station)<-[:`途径`]-(node1)-[:`站点顺序`]->(seq1:TrainInfo{name:\"1\"}),\n" +
            "(s2:Station)<-[:`途径`]-(node2)-[:`站点性质`]->(it2:TrainInfo{name:\"1\"}),\n" +
            "(node1)-[:`到达时间`]->(ta1:TrainInfo),\n" +
            "(node1)-[:`发车时间`]->(td1:TrainInfo),\n" +
            "(node1)-[:`行驶时长`]->(tt1:TrainInfo),\n" +
            "(node1)-[:`行驶距离`]->(dt1:TrainInfo),\n" +
            "(node1)-[:`站点性质`]->(it1:TrainInfo),\n" +
            "(node2)-[:`到达时间`]->(ta2:TrainInfo),\n" +
            "(node2)-[:`发车时间`]->(td2:TrainInfo),\n" +
            "(node2)-[:`行驶时长`]->(tt2:TrainInfo),\n" +
            "(node2)-[:`行驶距离`]->(dt2:TrainInfo),\n" +
            "(node2)-[:`站点顺序`]->(seq2:TrainInfo)\n" +
            "RETURN no.name AS trainNo, s1.name AS stationFrom, ta1.name AS timeArriveFrom, td1.name AS timeDepartureFrom, tt1.name AS timeTravelFrom, dt1.name AS distanceTravelFrom, seq1.name AS seqFrom,  it1.name AS  isTerminalFrom,  s2.name AS stationTo, ta2.name AS timeArriveTo, td2.name AS timeDepartureTo, tt2.name AS timeTravelTo, dt2.name AS distanceTravelTo, seq2.name AS seqTo, it2.name AS isTerminalTo\n")
    List<Map<String, String>> getOneTrainNo(@Param("trainNo") String trainNo);

    @Query("MATCH \n" +
            "(node1:TrainNode)<-[:`站点信息`]-(no:TrainNo{name:{trainNo}})-[:`站点信息`]->(node2:TrainNode),\n" +
            "(s1:Station)<-[:`途径`]-(node1)-[:`站点顺序`]->(seq1:TrainInfo{name:\"1\"}),\n" +
            "(s2:Station)<-[:`途径`]-(node2)-[:`站点性质`]->(it2:TrainInfo{name:\"1\"}),\n" +
            "(node1)-[:`到达时间`]->(ta1:TrainInfo),\n" +
            "(node1)-[:`发车时间`]->(td1:TrainInfo),\n" +
            "(node1)-[:`行驶时长`]->(tt1:TrainInfo),\n" +
            "(node1)-[:`行驶距离`]->(dt1:TrainInfo),\n" +
            "(node1)-[:`站点性质`]->(it1:TrainInfo),\n" +
            "(node2)-[:`到达时间`]->(ta2:TrainInfo),\n" +
            "(node2)-[:`发车时间`]->(td2:TrainInfo),\n" +
            "(node2)-[:`行驶时长`]->(tt2:TrainInfo),\n" +
            "(node2)-[:`行驶距离`]->(dt2:TrainInfo),\n" +
            "(node2)-[:`站点顺序`]->(seq2:TrainInfo)\n" +
            "RETURN no.name AS trainNo, s1.name AS stationFrom, ta1.name AS timeArriveFrom, td1.name AS timeDepartureFrom, tt1.name AS timeTravelFrom, dt1.name AS distanceTravelFrom, seq1.name AS seqFrom,  it1.name AS  isTerminalFrom,  s2.name AS stationTo, ta2.name AS timeArriveTo, td2.name AS timeDepartureTo, tt2.name AS timeTravelTo, dt2.name AS distanceTravelTo, seq2.name AS seqTo, it2.name AS isTerminalTo\n")
    List<Map<String, String>> getOneTrainNo2(@Param("trainNo") String trainNo);

}
