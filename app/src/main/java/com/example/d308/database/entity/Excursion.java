package com.example.d308.database.entity;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "excursions", foreignKeys = @ForeignKey(entity = Vacation.class,
        parentColumns = "id",
        childColumns = "vacationId",
        onDelete = ForeignKey.RESTRICT))
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private int vacationId;
    private String date;

    public Excursion() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getVacationId() {
        return vacationId;
    }
    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }


    public Excursion(int id,String title, String date, int vacationId){
        this.id = id;
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
    }

}
