package org.linlinjava.litemall.wx.service;

import org.linlinjava.litemall.wx.util.JwtHelper;

import java.util.Date;

/**
 * 维护用户token
 */
public class UserTokenManager {
	public static String generateToken(Integer id) {
        JwtHelper jwtHelper = new JwtHelper();
        return jwtHelper.createToken(id);
    }
    public static String generateToken(Integer id,Date expireDate) {
        JwtHelper jwtHelper = new JwtHelper();
        return jwtHelper.createToken(id,expireDate);
    }
    public static Integer getUserId(String token) {
    	JwtHelper jwtHelper = new JwtHelper();
    	Integer userId = jwtHelper.verifyTokenAndGetUserId(token);
    	if(userId == null || userId == 0){
    		return null;
    	}
        return userId;
    }

    public static void main(String args[]){
	    Integer userId = UserTokenManager.getUserId("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGlzIGlzIGxpdGVtYWxsIHRva2VuIiwiYXVkIjoiTUlOSUFQUCIsImlzcyI6IkxJVEVNQUxMIiwiZXhwIjoxNTk0Mjk0ODg5LCJ1c2VySWQiOjI2LCJpYXQiOjE1NjI2NzI0ODl9.MqDrsDaeJ9uleVraOiVz8N99CjD_fgcmUFgud8FIn1o");
	    System.out.println(userId);
    }
}
