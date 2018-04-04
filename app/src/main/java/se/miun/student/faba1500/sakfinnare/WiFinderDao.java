package se.miun.student.faba1500.sakfinnare;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WiFinderDao {
    @Query("SELECT * FROM wifinder")
    List<WiFinder> getAll();

    @Query("SELECT * FROM wifinder WHERE bssid = :bssid")
    List<WiFinder> get(String bssid);

    @Insert
    void insert(WiFinder wifinder);

    @Delete
    void delete(WiFinder wifinder);
}
