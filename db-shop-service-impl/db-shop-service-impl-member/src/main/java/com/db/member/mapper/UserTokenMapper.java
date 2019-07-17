package com.db.member.mapper;

import com.db.member.model.UserToken;
import org.apache.ibatis.annotations.Param;

public interface UserTokenMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserToken record);

    int insertSelective(UserToken record);

    UserToken selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserToken record);

    int updateByPrimaryKey(UserToken record);

    /**
     * 查出用户已登录的信息
     * 根据userid+loginType +is_availability=0 进行查询
     * @param userId
     * @param loginType
     * @return
     */
    UserToken selectByUserIdAndLoginType(@Param("userId") Long userId, @Param("loginType") String loginType);

    //@Update(" update meite_user_token set is_availability  ='1', update_time=now() where token=#{token}")

    /**
     * 根据userId+loginType token的状态修改为不可用
     * @param token
     * @return
     */
    int updateTokenAvailability(@Param("token") String token);

    /**
     * token记录表中插入一条记录
     *
     * @param userToken
     * @return
     */
    //@Insert("INSERT INTO `meite_user_token` VALUES (null, #{token},#{loginType}, #{deviceInfor}, 0, #{userId} ,now(),null ); ")
    int insertUserToken(UserToken userToken);

    //@Select("SELECT id as id ,token as token ,login_type as LoginType, device_infor as deviceInfor ,is_availability as isAvailability,user_id as userId"
    //        + "" + ""
    //       + " , create_time as createTime,update_time as updateTime   FROM meite_user_token WHERE user_id=#{userId} AND login_type=#{loginType} and is_availability ='0'; ")

}