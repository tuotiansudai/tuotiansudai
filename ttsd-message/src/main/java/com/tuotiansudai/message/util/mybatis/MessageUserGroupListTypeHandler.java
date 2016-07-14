package com.tuotiansudai.message.util.mybatis;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageUserGroupListTypeHandler extends BaseTypeHandler<List<MessageUserGroup>> {

    public MessageUserGroupListTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<MessageUserGroup> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<MessageUserGroup> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToMessageUserGroup(s);
    }

    @Override
    public List<MessageUserGroup> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToMessageUserGroup(s);
    }

    @Override
    public List<MessageUserGroup> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToMessageUserGroup(s);
    }

    private List<MessageUserGroup> convertToMessageUserGroup(String s) {
        List<MessageUserGroup> messageUserGroupList = Lists.newArrayList();
        if (Strings.isNullOrEmpty(s)) {
            return messageUserGroupList;
        }

        String[] messageUserGroupStr = s.split(",");
        for (String messageUserGroup : messageUserGroupStr) {
            messageUserGroupList.add(Enum.valueOf(MessageUserGroup.class, messageUserGroup));
        }
        return messageUserGroupList;
    }
}
