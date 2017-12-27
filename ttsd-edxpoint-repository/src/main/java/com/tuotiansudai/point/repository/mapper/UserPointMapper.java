package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.UserPointModel;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserPointMapper {

    @Results(id = "userPointModelMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "login_name", property = "loginName"),
            @Result(column = "point", property = "point"),
            @Result(column = "sudai_point", property = "sudaiPoint"),
            @Result(column = "channel_point", property = "channelPoint"),
            @Result(column = "channel", property = "channel"),
            @Result(column = "updated_time", property = "updatedTime")
    })
    @Select("select * from user_point where login_name = #{loginName}")
    UserPointModel findByLoginName(
            @Param("loginName") String loginName);


    @ResultMap("userPointModelMap")
    @Select({
            "<script>",
            " select * from user_point",
            " <where>",
            " <if test=\"channel != null and channel != ''\"> and channel = #{channel} </if>",
            " <if test='minPoint != null'> and point &gt;= #{minPoint} </if>",
            " <if test='maxPoint != null'> and point &lt;= #{maxPoint} </if>",
            " <if test='minSudaiPoint != null'> and sudai_point &gt;= #{minSudaiPoint} </if>",
            " <if test='maxSudaiPoint != null'> and sudai_point &lt;= #{maxSudaiPoint} </if>",
            " <if test='minChannelPoint != null'> and channel_point &gt;= #{minChannelPoint} </if>",
            " <if test='maxChannelPoint != null'> and channel_point &lt;= #{maxChannelPoint} </if>",
            " </where>",
            " order by point desc limit #{rowLimit} offset #{rowIndex}",
            "</script>"
    })
    List<UserPointModel> list(
            @Param("channel") String channel,
            @Param("minPoint") Integer minPoint,
            @Param("maxPoint") Integer maxPoint,
            @Param("minSudaiPoint") Integer minSudaiPoint,
            @Param("maxSudaiPoint") Integer maxSudaiPoint,
            @Param("minChannelPoint") Integer minChannelPoint,
            @Param("maxChannelPoint") Integer maxChannelPoint,
            @Param("rowIndex") int rowIndex,
            @Param("rowLimit") int rowLimit);


    @Select({
            "<script>",
            " select count(*) from user_point",
            " <where>",
            " <if test=\"channel != null and channel != ''\"> and channel = #{channel} </if>",
            " <if test='minPoint != null'> and point &gt;= #{minPoint} </if>",
            " <if test='maxPoint != null'> and point &lt;= #{maxPoint} </if>",
            " <if test='minSudaiPoint != null'> and sudai_point &gt;= #{minSudaiPoint} </if>",
            " <if test='maxSudaiPoint != null'> and sudai_point &lt;= #{maxSudaiPoint} </if>",
            " <if test='minChannelPoint != null'> and channel_point &gt;= #{minChannelPoint} </if>",
            " <if test='maxChannelPoint != null'> and channel_point &lt;= #{maxChannelPoint} </if>",
            " </where>",
            "</script>"
    })
    long count(
            @Param("channel") String channel,
            @Param("minPoint") Integer minPoint,
            @Param("maxPoint") Integer maxPoint,
            @Param("minSudaiPoint") Integer minSudaiPoint,
            @Param("maxSudaiPoint") Integer maxSudaiPoint,
            @Param("minChannelPoint") Integer minChannelPoint,
            @Param("maxChannelPoint") Integer maxChannelPoint);

    @Select("select exists(select 1 from user_point where login_name = #{loginName})")
    boolean exists(
            @Param("loginName") String loginName);


    @Insert({
            " insert into user_point(login_name, point, sudai_point, channel_point, channel, updated_time)",
            " values (#{loginName}, #{point}, #{sudaiPoint}, #{channelPoint}, #{channel}, #{updatedTime})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int create(UserPointModel model);

    @Update({
            " update user_point set",
            " channel = #{channel},",
            " updated_time = #{updatedTime}",
            " where login_name = #{loginName}"
    })
    int updateChannel(
            @Param("loginName") String loginName,
            @Param("channel") String channel,
            @Param("updatedTime") Date updatedTime);

    @Update({
            " update user_point set",
            " point = point + #{sudaiPoint},",
            " sudai_point = sudai_point + #{sudaiPoint},",
            " updated_time = #{updatedTime}",
            " where login_name = #{loginName}"
    })
    int increaseSudaiPoint(
            @Param("loginName") String loginName,
            @Param("sudaiPoint") long sudaiPoint,
            @Param("updatedTime") Date updatedTime);

    @Update({
            " update user_point set",
            " point = point + #{channelPoint},",
            " channel_point = channel_point + #{channelPoint},",
            " updated_time = #{updatedTime}",
            " where login_name = #{loginName}"
    })
    int increaseChannelPoint(
            @Param("loginName") String loginName,
            @Param("channelPoint") long channelPoint,
            @Param("updatedTime") Date updatedTime);

    @Update({
            " update user_point set",
            " point = point + #{sudaiPoint} + #{channelPoint},",
            " sudai_point = sudai_point + #{sudaiPoint},",
            " channel_point = channel_point + #{channelPoint},",
            " updated_time = #{updatedTime}",
            " where login_name = #{loginName}"
    })
    int increasePoint(
            @Param("loginName") String loginName,
            @Param("sudaiPoint") long sudaiPoint,
            @Param("channelPoint") long channelPoint,
            @Param("updatedTime") Date updatedTime);


    default long getPointByLoginName(String loginName) {
        return getPointByLoginName(loginName, null);
    }

    default long getPointByLoginName(String loginName, Long defaultValue) {
        UserPointModel userPointModel = findByLoginName(loginName);
        if (userPointModel == null) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw new IllegalArgumentException(String.format("cannot find user point record with login_name = '%s'", loginName));
        } else {
            return userPointModel.getPoint();
        }
    }
}
