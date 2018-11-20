package my.philipshueremote.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import my.philipshueremote.Init.Models.BridgeInfo;

@Database(entities = {BridgeInfo.class}, version = 1)
public abstract class HueDatabase extends RoomDatabase {
    private static HueDatabase Instance;

    public abstract BridgeDAO bridgeDAO();

    public static synchronized HueDatabase getInstance(Context appContext) {
        if(Instance == null) {
            Instance = Room.databaseBuilder(appContext, HueDatabase.class, "HUE_DATABASE")
                    .build();
        }
        return Instance;
    }

    public void closeDatabase() {
        Instance.close();
        Instance = null;
    }
}
