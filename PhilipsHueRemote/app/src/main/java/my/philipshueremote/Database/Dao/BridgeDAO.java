package my.philipshueremote.Database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import my.philipshueremote.Init.Models.BridgeInfo;

@Dao
public interface BridgeDAO {
    @Query("SELECT * FROM bridgeinfo")
    List<BridgeInfo> getAllBridgeInformation();

    @Query("SELECT * FROM BridgeInfo WHERE bridgeID LIKE :bridgeID")
    BridgeInfo getBridgeBasedOnID(String bridgeID);

    @Query("SELECT COUNT(*) FROM BridgeInfo")
    int sizeOfBridges();

    @Insert
    void insertBridgeInformation(BridgeInfo bridge);

    @Delete
    void removeBridgeInformation(BridgeInfo bridge);
}
