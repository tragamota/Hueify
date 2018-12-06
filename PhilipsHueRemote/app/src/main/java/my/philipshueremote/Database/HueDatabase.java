package my.philipshueremote.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import my.philipshueremote.Database.Dao.BridgeDAO;
import my.philipshueremote.Database.Dao.GroupDAO;
import my.philipshueremote.Database.Dao.LampDAO;
import my.philipshueremote.Database.Dao.SceneDAO;
import my.philipshueremote.Database.Entities.Lamp;
import my.philipshueremote.Init.Models.BridgeInfo;

@Database(entities = {BridgeInfo.class, Lamp.class}, version = 1)
public abstract class HueDatabase extends RoomDatabase {
    private static HueDatabase Instance;
    private ThreadPoolExecutor queryPoolExecutor;

    public abstract BridgeDAO bridgeDAO();
    public abstract LampDAO lampDAO();
    public abstract GroupDAO groupDAO();
    public abstract SceneDAO sceneDAO();

    public static synchronized HueDatabase getInstance(Context appContext) {
        if(Instance == null) {
            Instance = Room.databaseBuilder(appContext, HueDatabase.class, "HUE_DATABASE")
                    .build();
            Instance.queryPoolExecutor = new ThreadPoolExecutor(4, 4,
               30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        }
        return Instance;
    }

    public Future performBackgroundQuery(Runnable runnable) {
        return queryPoolExecutor.submit(runnable);
    }

    public void closeDatabase() {
        Instance.close();
        Instance = null;
    }
}
