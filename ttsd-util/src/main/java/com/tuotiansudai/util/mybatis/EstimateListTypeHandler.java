package com.tuotiansudai.util.mybatis;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.enums.riskestimation.Estimate;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EstimateListTypeHandler extends BaseTypeHandler<List<Estimate>> {

    public EstimateListTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Estimate> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<Estimate> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToEstimate(s);
    }

    @Override
    public List<Estimate> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToEstimate(s);
    }

    @Override
    public List<Estimate> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToEstimate(s);
    }

    private List<Estimate> convertToEstimate(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        List<Estimate> sources = Lists.newArrayList();
        String[] sourceStr = s.split(",");
        for (String type : sourceStr) {
            sources.add(Enum.valueOf(Estimate.class, type));
        }
        return sources;
    }

}
