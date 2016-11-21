package com.tuotiansudai.ask.repository.mybatis;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.Tag;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TagListTypeHandler extends BaseTypeHandler<List<Tag>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Tag> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<Tag> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToTags(s);
    }

    @Override
    public List<Tag> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToTags(s);
    }

    @Override
    public List<Tag> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToTags(s);
    }

    private List<Tag> convertToTags(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return Lists.newArrayList();
        }

        List<Tag> tags = Lists.newArrayList();
        String[] tagsStr = s.split(",");
        for (String type : tagsStr) {
            tags.add(Tag.valueOf(type));
        }
        return tags;
    }
}
