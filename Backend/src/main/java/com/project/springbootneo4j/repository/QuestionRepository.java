package com.project.springbootneo4j.repository;

import com.project.springbootneo4j.model.Province;
import com.project.springbootneo4j.model.Station;
import com.project.springbootneo4j.model.TrainInfo;
import com.project.springbootneo4j.model.TrainNo;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends Neo4jRepository<Object, Long> {

    /**
     * 1 {province}有什么车站？
     *
     * @param province 省份
     * @return 车站
     */
    @Query("MATCH (s:Station)-[:`所属省份`]->(p:Province{name:{province}}) RETURN s AS station")
    List<Station> getProvinceStation(@Param("province") String province);

    /**
     * 2 有什么车途经{station}？
     *
     * @param station    车站
     * @param train_type 车次类型
     * @return 车次号
     */
    @Query("MATCH (no:TrainNo)-[:`站点信息`]->(:TrainNode)-[:`途径`]->(s:Station{name:{station}}) WITH no MATCH (no)-[:`实例关系`]->(:TrainType{name:{train_type}}) RETURN no AS train_no")
    List<TrainNo> getPassTrainNoWithConstrain(@Param("station") String station, @Param("train_type") String train_type);

    @Query("MATCH (no:TrainNo)-[:`站点信息`]->(:TrainNode)-[:`途径`]->(s:Station{name:{station}}) RETURN no AS train_no")
    List<TrainNo> getPassTrainNo(@Param("station") String station);

    /**
     * 3 从{station}到{station}有什么车？
     *
     * @param station_from 开出车站
     * @param station_to   到达车站
     * @param train_type   车次类型
     * @return 车次号
     */
    @Query("MATCH (:Station{name:{station_from}})<-[:`途径`]-(id1:TrainNode)<-[:`站点信息`]-(no:TrainNo)-[:`站点信息`]->(id2:TrainNode)-[:`途径`]->(:Station{name:{station_to}}) WITH id1,no,id2 MATCH (id1)-[:`站点顺序`]->(seq1:TrainInfo),(id2)-[:`站点顺序`]->(seq2:TrainInfo), (no)-[:`实例关系`]->(:TrainType{name:{train_type}}) WHERE toInt(seq1.name) < toInt(seq2.name) RETURN no AS train_no")
    List<TrainNo> getFromToWithConstrain(@Param("station_from") String station_from, @Param("station_to") String station_to, @Param("train_type") String train_type);

    @Query("MATCH (:Station{name:{station_from}})<-[:`途径`]-(id1:TrainNode)<-[:`站点信息`]-(no:TrainNo)-[:`站点信息`]->(id2:TrainNode)-[:`途径`]->(:Station{name:{station_to}}) WITH id1,no,id2 MATCH (id1)-[:`站点顺序`]->(seq1:TrainInfo),(id2)-[:`站点顺序`]->(seq2:TrainInfo) WHERE toInt(seq1.name) < toInt(seq2.name) RETURN no As train_no")
    List<TrainNo> getFromTo(@Param("station_from") String station_from, @Param("station_to") String station_to);

    /**
     * 4 {station}始发的{train_type}有哪些？
     *
     * @param station    始发站
     * @param train_type 车次类型
     * @return 车次号
     */
    @Query("MATCH (:Station{name:{station}})<-[:`途径`]-(id:TrainNode)-[:`站点性质`]->(:TrainInfo{name:'0'}) WHERE id.name ends with '-1' WITH id MATCH (id)<-[:`站点信息`]-(no:TrainNo)-[:`实例关系`]->(:TrainType{name:{train_type}}) RETURN no AS train_no")
    List<TrainNo> getStartStationTrainNoWithConstrain(@Param("station") String station, @Param("train_type") String train_type);

    @Query("MATCH (:Station{name:{station}})<-[:`途径`]-(id:TrainNode)-[:`站点性质`]->(:TrainInfo{name:'0'}) WHERE id.name ends with '-1' WITH id MATCH (id)<-[:`站点信息`]-(no:TrainNo) RETURN no AS train_no")
    List<TrainNo> getStartStationTrainNo(@Param("station") String station);


    /**
     * 5 {station}是哪些{train_type}的终点站?
     *
     * @param station    终点站
     * @param train_type 车次类型
     * @return 车次号
     */
    @Query("MATCH (:Station{name:{station}})<-[:`途径`]-(id:TrainNode)-[:`站点性质`]->(:TrainInfo{name:'1'}) WITH id MATCH (id)<-[:`站点信息`]-(no:TrainNo)-[:`实例关系`]->(:TrainType{name:{train_type}}) RETURN no AS train_no")
    List<TrainNo> getEndStationTrainNoWithConstrain(@Param("station") String station, @Param("train_type") String train_type);

    @Query("MATCH (:Station{name:{station}})<-[:`途径`]-(id:TrainNode)-[:`站点性质`]->(:TrainInfo{name:'1'}) WITH id MATCH (id)<-[:`站点信息`]-(no:TrainNo) RETURN no AS train_no")
    List<TrainNo> getEndStationTrainNo(@Param("station") String station);

    /**
     * 11 从{station}到{station}有几点出发的{train_type}?
     *
     * @param station_from 开出车站
     * @param station_to   到达车站
     * @param train_type   车次类型
     * @return 时间
     */
    @Query("MATCH (:Station{name:{station_from}})<-[:`途径`]-(id1:TrainNode)<-[:`站点信息`]-(no:TrainNo)-[:`站点信息`]->(id2:TrainNode)-[:`途径`]->(:Station{name:{station_to}}) WITH id1,no,id2 MATCH (id1)-[:`发车时间`]->(t:TrainInfo),(id1)-[:`站点顺序`]->(seq1:TrainInfo),(id2)-[:`站点顺序`]->(seq2:TrainInfo),(no)-[:`实例关系`]->(:TrainType{name:{train_type}}) WHERE toInt(seq1.name) < toInt(seq2.name) RETURN t AS time")
    List<TrainInfo> getFromToStartTimeWithConstrain(@Param("station_from") String station_from, @Param("station_to") String station_to, @Param("train_type") String train_type);

    @Query("MATCH (:Station{name:{station_from}})<-[:`途径`]-(id1:TrainNode)<-[:`站点信息`]-(no:TrainNo)-[:`站点信息`]->(id2:TrainNode)-[:`途径`]->(:Station{name:{station_to}}) WITH id1,no,id2 MATCH (id1)-[:`发车时间`]->(t:TrainInfo),(id1)-[:`站点顺序`]->(seq1:TrainInfo),(id2)-[:`站点顺序`]->(seq2:TrainInfo) WHERE toInt(seq1.name) < toInt(seq2.name) RETURN t AS time")
    List<TrainInfo> getFromToStartTime(@Param("station_from") String station_from, @Param("station_to") String station_to);


    /**
     * 12 从{station}到{station}有几点到达的{train_type}?
     *
     * @param station_from 开出车站
     * @param station_to   到达车站
     * @param train_type   车次类型
     * @return 时间
     */
    @Query("MATCH (:Station{name:{station_from}})<-[:`途径`]-(id1:TrainNode)<-[:`站点信息`]-(no:TrainNo)-[:`站点信息`]->(id2:TrainNode)-[:`途径`]->(:Station{name:{station_to}}) WITH id1,no,id2 MATCH (id2)-[:`到达时间`]->(t:TrainInfo),(id1)-[:`站点顺序`]->(seq1:TrainInfo),(id2)-[:`站点顺序`]->(seq2:TrainInfo),(no)-[:`实例关系`]->(:TrainType{name:{train_type}}) WHERE toInt(seq1.name) < toInt(seq2.name) RETURN t AS time")
    List<TrainInfo> getFromToEndTimeWithConstrain(@Param("station_from") String station_from, @Param("station_to") String station_to, @Param("train_type") String train_type);

    @Query("MATCH (:Station{name:{station_from}})<-[:`途径`]-(id1:TrainNode)<-[:`站点信息`]-(no:TrainNo)-[:`站点信息`]->(id2:TrainNode)-[:`途径`]->(:Station{name:{station_to}}) WITH id1,no,id2 MATCH (id2)-[:`到达时间`]->(t:TrainInfo),(id1)-[:`站点顺序`]->(seq1:TrainInfo),(id2)-[:`站点顺序`]->(seq2:TrainInfo) WHERE toInt(seq1.name) < toInt(seq2.name) RETURN t AS time")
    List<TrainInfo> getFromToEndTime(@Param("station_from") String station_from, @Param("station_to") String station_to);


    /**
     * 18 {train_no}途径哪些站？
     *
     * @param train_no 车次
     * @return 车站
     */
    @Query("MATCH (s:Station)<-[:`途径`]-(:TrainNode)<-[:`站点信息`]-(:TrainNo{name:{train_no}}) RETURN s AS station")
    List<Station> getPassStation(@Param("train_no") String train_no);


    /**
     * 19 {train_no}车次终点在哪儿?
     *
     * @param train_no 车次
     * @return 车站
     */
    @Query("MATCH (s:Station)<-[:`途径`]-(id:TrainNode)<-[:`站点信息`]-(:TrainNo{name:{train_no}}) WHERE id.name ends with '-1' RETURN s AS station")
    List<Station> getEndStation(@Param("train_no") String train_no);


    /**
     * 20 {train_no}车次起点在哪儿?
     *
     * @param train_no 车次
     * @return 车站
     */
    @Query("MATCH (:TrainInfo{name:'1'})<-[:`站点性质`]-(id:TrainNode)<-[:`站点信息`]-(:TrainNo{name:{train_no}}) WITH id MATCH (id)-[:`途径`]->(s:Station) RETURN s AS station")
    List<Station> getStartStation(@Param("train_no") String train_no);

}
