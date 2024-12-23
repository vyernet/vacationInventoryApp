package com.example.d308.database.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String hotel;
    private String startDate;
    private String endDate;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setHotel(String hotel){
        this.hotel = hotel;
    }
    public String getHotel(){
        return hotel;
    }
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    public String getStartDate(){
        return startDate;
    }
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    public String getEndDate(){
        return endDate;
    }

    public Vacation(int id,String title, String hotel, String startDate, String endDate){
        this.id = id;
        this.title = title;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
