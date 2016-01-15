package com.tuotiansudai.util.mybatis;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.ProductType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductTypeListTypeHandler extends BaseTypeHandler<List<ProductType>> {

    public ProductTypeListTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<ProductType> parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Joiner.on(",").join(parameter));
        } else {
            ps.setObject(i, Joiner.on(",").join(parameter), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public List<ProductType> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return this.convertToProductType(s);
    }

    @Override
    public List<ProductType> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return this.convertToProductType(s);
    }

    @Override
    public List<ProductType> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return this.convertToProductType(s);
    }

    private List<ProductType> convertToProductType(String s) {
        if (s == null) {
            return null;
        }

        return Lists.transform(Lists.newArrayList(s.split(",")), new Function<String, ProductType>() {
            @Override
            public ProductType apply(String value) {
                return Enum.valueOf(ProductType.class, value);
            }
        });
    }
}
