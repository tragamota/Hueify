package my.philipshueremote.MainUI.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.DataCommunication.Services.HueSyncService;
import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Database.Entities.Lamp;
import my.philipshueremote.Init.Models.BridgeInfo;

public class LampFragmentViewModel extends AndroidViewModel {
    private HueDatabase database;

    private LiveData<BridgeInfo> bridgeLiveInfo;
    private LiveData<List<Lamp>> liveLampList;

    public LampFragmentViewModel(@NonNull Application application) {
        super(application);
        database = HueDatabase.getInstance(application.getApplicationContext());
        bridgeLiveInfo = HueSyncService.getInstance(application).getSelectedBridge();
    }

    public LiveData<List<Lamp>> getLiveLamps() {
        if(liveLampList == null) {
            liveLampList = Transformations.switchMap(bridgeLiveInfo, input ->
                    database.lampDAO().getAllLamps(input.getBridgeID())
            );
        }
        return liveLampList;
    }

}
