package com.tuotiansudai.message.util.mybatis;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGroupListTypeHandler extends BaseTypeHandler<List<UserGroup>> {

    public UserGroupListTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<UserGroup> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<UserGroup> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToUserGroup(s);
    }

    @Override
    public List<UserGroup> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToUserGroup(s);
    }

    @Override
    public List<UserGroup> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToUserGroup(s);
    }

    private List<UserGroup> convertToUserGroup(String s) {
        List<UserGroup> userGroupList = Lists.newArrayList();
        if (Strings.isNullOrEmpty(s)) {
            return userGroupList;
        }

        String[] userGroupStr = s.split(",");
        for (String userGroup : userGroupStr) {
            userGroupList.add(Enum.valueOf(UserGroup.class, userGroup));
        }
        return userGroupList;
    }
}
