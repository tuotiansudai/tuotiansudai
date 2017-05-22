package com.tuotiansudai.paywrapper.ghb.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DynamicTableMapper {

    String findTable(
            @Param("tableName") String tableName
    );

    int createTable(
            @Param("tableName") String tableName,
            @Param("foreignTableName") String foreignTableName,
            @Param("columns") List<String> columns,
            @Param("indexColumns") List<String> indexColumns,
            @Param("foreignKey") boolean foreignKey
    );

    int dropTable(
            @Param("tableName") String tableName
    );

    int insert(Map<String, Object> map);

    List<Map<String, Object>> findProcessingRequest(@Param("tableName") String tableName);

    int updateRequestStatus(
            @Param("tableName") String tableName,
            @Param("id") String id,
            @Param("status") String status
    );
}
