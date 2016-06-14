package com.tuotiansudai.util.mybatis;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.InvestAchievement;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InvestAchievementListTypeHandler extends BaseTypeHandler<List<InvestAchievement>> {

    public InvestAchievementListTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<InvestAchievement> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<InvestAchievement> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToInvestAchievement(s);
    }

    @Override
    public List<InvestAchievement> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToInvestAchievement(s);
    }

    @Override
    public List<InvestAchievement> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToInvestAchievement(s);
    }

    private List<InvestAchievement> convertToInvestAchievement(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return Lists.newArrayList();
        }

        List<InvestAchievement> investAchievementList = Lists.newArrayList();
        String[] investAchievementStr = s.split(",");
        for (String type : investAchievementStr) {
            investAchievementList.add(InvestAchievement.valueOf(type));
        }
        return investAchievementList;
    }
}
