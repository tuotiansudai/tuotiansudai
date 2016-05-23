package com.tuotiansudai.message.util.mybatis;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.message.repository.model.MessageChannel;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageChannelListTypeHandler extends BaseTypeHandler<List<MessageChannel>> {

    public MessageChannelListTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<MessageChannel> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<MessageChannel> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToChannel(s);
    }

    @Override
    public List<MessageChannel> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToChannel(s);
    }

    @Override
    public List<MessageChannel> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToChannel(s);
    }

    private List<MessageChannel> convertToChannel(String s) {
        List<MessageChannel> channelList = Lists.newArrayList();
        if (Strings.isNullOrEmpty(s)) {
            return channelList;
        }

        String[] channelStr = s.split(",");
        for (String channel : channelStr) {
            channelList.add(Enum.valueOf(MessageChannel.class, channel));
        }
        return channelList;
    }
}
