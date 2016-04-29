package com.tuotiansudai.util.mybatis;

import com.google.common.base.Joiner;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class StringListHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToPushObject(s);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToPushObject(s);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToPushObject(s);
    }

    private List<String> convertToPushObject(String s) {
        if (s == null) {
            return null;
        }

       return Arrays.asList(s.split(","));
    }
}
