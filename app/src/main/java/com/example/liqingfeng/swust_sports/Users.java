package com.example.liqingfeng.swust_sports;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable{
    private int id;
    private String name;
    private int price;
    private String email;
    public Users()
    {
        super();
    }
    public Users(int id,String naem,int price,String email )
    {
        super();
        this.id=id;
        this.name=name;
        this.price=price;
        this.email=email;
    }
    public void setUser_ID(int ID)
    {
        this.id=ID;
    }
    public int getUser_ID()
    {
        return id;
    }
    public void setPrice(int dprice)
    {
        this.price=dprice;
    }
    public int getPrice()
    {
        return price;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }
    public String getEmail()
    {
        return email;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String toString()
    {
        return "Person[id="+id+",name="+name+",imagepath="+email+"]";
    }

    //序列化实体类
    public static final Parcelable.Creator<Users> CREATOR =new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel parcel) {
            Users users=new Users();
            users.id=parcel.readInt();
            users.name=parcel.readString();
            users.price=parcel.readInt();
            users.email=parcel.readString();
            return users;
        }

        @Override
        public Users[] newArray(int i) {
            return new Users[i];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(price);
        parcel.writeString(email);
    }
}
