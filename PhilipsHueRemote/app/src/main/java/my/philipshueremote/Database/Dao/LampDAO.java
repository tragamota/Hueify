package my.philipshueremote.Database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import my.philipshueremote.Database.Entities.Lamp;

@Dao
public interface LampDAO {
    @Query("SELECT * FROM Lamp WHERE bridgeID LIKE :bridgeID")
    LiveData<List<Lamp>> getAllLamps(String bridgeID);

    @Query("SELECT * FROM lamp WHERE bridgeID LIKE :bridgeID AND lampApiID LIKE :lampID")
    LiveData<Lamp> getLiveLamp(String bridgeID, short lampID);

    @Query("SELECT * FROM lamp WHERE bridgeID LIKE :bridgeID AND lampApiID LIKE :lampID")
    Lamp getLamp(String bridgeID, short lampID);

    @Query("SELECT COUNT(*) FROM lamp WHERE bridgeID LIKE :bridgeID AND lampApiID LIKE :lampID")
    int containsLamp(String bridgeID, short lampID);

    @Insert
    void insertLamp(Lamp lamp);

    @Insert
    void insertLamps(List<Lamp> lamps);

    @Update
    void updateLamp(Lamp lamp);

    @Update
    void updateLamps(List<Lamp> lamps);

    @Delete
    void deleteLamp(Lamp lamp);
}
