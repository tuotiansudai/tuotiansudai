package com.tuotiansudai.paywrapper.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
