package com.db.member.mapper;

import com.db.member.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(@Param("mobile") String mobile,@Param("password")String password);

    /**根据openId查找用户信息*/
    User findByOpenId(@Param("qqOpenId")String qqOpenId);

    /**绑定授权账号*/
    int updateUserOpenId(@Param("qqOpenId")String qqOpenId,@Param("userId") Long userId);

    //@Insert("INSERT INTO `meite_user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, null, null, null, '1', null, null, null);")
    //int register(User userDo);

    //@Select("SELECT * FROM meite_user WHERE MOBILE=#{mobile};")
    User existMobile(@Param("mobile") String mobile);

//    @Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USERNAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID "
//            + "  FROM meite_user  WHERE MOBILE=#{0} and password=#{1};")
//    UserDo login(@Param("mobile") String mobile, @Param("password") String password);
}