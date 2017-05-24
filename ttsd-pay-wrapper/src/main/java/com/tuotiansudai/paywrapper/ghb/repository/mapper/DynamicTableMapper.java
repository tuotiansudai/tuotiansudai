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
            @Param("columns") List<String> columns,
            @Param("indexColumns") List<String> indexColumns
    );

    int dropTable(
            @Param("tableName") String tableName
    );

    int insert(
            @Param("tableName") String tableName,
            @Param("columns") List<String> columns,
            @Param("values") List<Object> values
    );

    List<Map<String, Object>> findProcessingRequest(@Param("tableName") String tableName);

    int updateRequestStatus(
            @Param("tableName") String tableName,
            @Param("id") String id,
            @Param("status") String status
    );
}
