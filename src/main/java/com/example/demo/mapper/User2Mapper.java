package com.example.demo.mapper;

import com.example.demo.entity.User2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface User2Mapper {

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User2 selectUserByOpenid(String openid);

    @Insert("INSERT INTO user (openid, session_key, nickname, avatar_url, gender, city, province, country) VALUES (#{openid}, #{sessionKey}, #{nickname}, #{avatarUrl}, #{gender}, #{city}, #{province}, #{country})")
    void insertUser(User2 user);

    @Update("UPDATE user SET session_key = #{sessionKey}, nickname = #{nickname}, avatar_url = #{avatarUrl}, gender = #{gender}, city = #{city}, province = #{province}, country = #{country} WHERE openid = #{openid}")
    void updateUser(User2 user);
}
