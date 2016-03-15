package com.tuotiansudai.util.mybatis;


import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.jpush.repository.model.PushUserType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PushUserTypeHandler extends BaseTypeHandler<List<PushUserType>>{

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<PushUserType> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<PushUserType> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToPushUserType(s);
    }

    @Override
    public List<PushUserType> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToPushUserType(s);
    }

    @Override
    public List<PushUserType> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToPushUserType(s);
    }

    private List<PushUserType> convertToPushUserType(String s) {
        if (s == null) {
            return null;
        }
        return Lists.transform(Lists.newArrayList(s.split(",")), new Function<String, PushUserType>() {
            @Override
            public PushUserType apply(String input) {
                return Enum.valueOf(PushUserType.class, input);
            }
        });
    }

}
