package com.example.liqingfeng.swust_sports;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class Json_analyze {
    public Json_analyze(){

    }
    //Gson进行解析person
    public static<T> T getperson(String jsonString,Class<T> cls)
    {
        T t=null;
        try
        {
            Gson gson=new Gson();
            t=gson.fromJson(jsonString,cls);
        }catch(Exception e)
        {
            Log.e("TAG",e.toString());
        }
        return t;
    }
    //使用Gson进行解析List<Person>
    public static <T> List<T> getPersons1(String jsonString, Class<T> cls)
    {
        List<T> list=new ArrayList<T>();
        JsonArray array=new JsonParser().parse(jsonString).getAsJsonArray();
        try
        {
            Gson gson = new Gson();
            for(final JsonElement elem:array)
            {
                list.add(gson.fromJson(elem,cls));
            }
        }catch (Exception e)
        {
            Log.e("TAG",e.toString());
        }
        return list;
    }

    //使用Gson，Json解析数组  传入json字符串
    public static List<Users> getPersons2(String jsonString)
    {
        JsonParser parser=new JsonParser();
        //将json的String转换为JsonArray对象
        JsonArray jsonArray=parser.parse(jsonString).getAsJsonArray();
        Gson gson=new Gson();
        ArrayList<Users> userList=new ArrayList<>();
        //for循环解析JSON
        for(JsonElement user:jsonArray)
        {
            Users users1=gson.fromJson(user,Users.class);
            userList.add(users1);
        }
        return userList;
    }
}
