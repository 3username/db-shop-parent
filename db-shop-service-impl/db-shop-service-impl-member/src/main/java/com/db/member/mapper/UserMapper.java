package com.db.member.mapper;

import com.db.member.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //@Insert("INSERT INTO `meite_user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, null, null, null, '1', null, null, null);")
    //int register(User userDo);

    //@Select("SELECT * FROM meite_user WHERE MOBILE=#{mobile};")
    User existMobile(@Param("mobile") String mobile);
}