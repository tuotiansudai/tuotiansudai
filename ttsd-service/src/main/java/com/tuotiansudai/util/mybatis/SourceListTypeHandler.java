package com.tuotiansudai.util.mybatis;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SourceListTypeHandler extends BaseTypeHandler<List<Source>> {

    public SourceListTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Source> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<Source> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToSource(s);
    }

    @Override
    public List<Source> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToSource(s);
    }

    @Override
    public List<Source> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToSource(s);
    }

    private List<Source> convertToSource(String s) {
        if (s == null) {
            return null;
        }
        List<Source> sources = Lists.newArrayList();
        String[] sourceStr = s.split(",");
        for (String type : sourceStr) {
            sources.add(Enum.valueOf(Source.class, type));
        }
        return sources;
    }

}
