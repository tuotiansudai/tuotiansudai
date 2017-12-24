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
            @Result(column = "updated_time", property = "updatedTime")
    })
    @Select("select * from user_point where login_name = #{loginName}")
    UserPointModel findByLoginName(
            @Param("loginName") String loginName);


    @ResultMap("userPointModelMap")
    @Select("select * from user_point where point > 0 order by point desc limit #{pageSize} offset #{pageIndex}")
    List<UserPointModel> list(
            @Param("pageIndex") int pageIndex,
            @Param("pageSize") int pageSize);


    @Select("select exists(select 1 from user_point where login_name = #{loginName})")
    boolean exists(
            @Param("loginName") String loginName);


    @Insert({
            " insert into user_point(login_name, point, updated_time)",
            " values (#{loginName}, #{point}, #{updatedTime})"
    })
    int create(@Param("loginName") String loginName,
               @Param("point") long point,
               @Param("updatedTime") Date updatedTime);

    @Update({
            " update user_point set",
            " point = point + #{point},",
            " updated_time = #{updatedTime}",
            " where login_name = #{loginName}"
    })
    int increase(@Param("loginName") String loginName,
                 @Param("point") long point,
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

    default void increaseOrCreate(String loginName, long point) {
        if (exists(loginName)) {
            increase(loginName, point, new Date());
        } else {
            create(loginName, point, new Date());
        }
    }
}
