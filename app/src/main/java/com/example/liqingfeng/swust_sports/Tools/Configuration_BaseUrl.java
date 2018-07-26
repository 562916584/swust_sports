package com.example.liqingfeng.swust_sports.Tools;

public class Configuration_BaseUrl  {
    public static String Base_Url="http://wangzhengyu.cn";

    //登陆接口
    public static String getLogin_interface()
    {
        return Base_Url+"/api/user/login.do";
    }
    //注册接口
    public static String getRegister_interface()
    {
        return Base_Url+"/sport/user/register.do";
    }
    //验证码接口
    public static String getVerify_interface()
    {
        return Base_Url+"/api/verify/getVerify.do";
    }
}
