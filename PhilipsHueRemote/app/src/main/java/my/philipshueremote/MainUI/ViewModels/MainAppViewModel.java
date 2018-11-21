package my.philipshueremote.MainUI.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.DataCommunication.HueSyncService;
import my.philipshueremote.Discovery.MultiCastDiscovery;
import my.philipshueremote.Init.Models.BridgeInfo;
import my.philipshueremote.Init.Models.SearchingStates;

public class MainAppViewModel extends AndroidViewModel {
    private HueSyncService hueService;
    private MultiCastDiscovery hueDiscoverer;

    private MutableLiveData<BridgeInfo> selectedBridge;
    private MutableLiveData<SearchingStates> discoveryState;

    public MainAppViewModel(@NonNull Application application) {
        super(application);



        hueService = new HueSyncService(application);
        hueDiscoverer = new MultiCastDiscovery(application, discoveryState);
    }

    public void startHueBackgroundService() {
        hueService.startService();
    }

    public void forceHueRefresh() {

    }

    public void stopHueBackgroundService() {
        hueService.stopService();
    }

    public LiveData<BridgeInfo> getSelectedBridge() {
        
    }
}
