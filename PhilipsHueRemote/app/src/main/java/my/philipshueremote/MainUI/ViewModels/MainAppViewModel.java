package my.philipshueremote.MainUI.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.DataCommunication.Services.HueSyncService;
import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Discovery.MultiCastDiscovery;
import my.philipshueremote.Init.Models.BridgeInfo;
import my.philipshueremote.Init.Models.SearchingStates;

public class MainAppViewModel extends AndroidViewModel {
    public final int FIRST_VIEWPAGER_FRAGMENT = 0;

    private HueSyncService hueService;
    private MultiCastDiscovery hueDiscoverer;

    private MutableLiveData<BridgeInfo> selectedBridge;
    private LiveData<SearchingStates> discoveryState;

    public MainAppViewModel(@NonNull Application application) {
        super(application);

        hueService = HueSyncService.getInstance(application);
        hueDiscoverer = MultiCastDiscovery.getInstance(application);
    }

    public LiveData<BridgeInfo> getSelectedBridge() {
        if(selectedBridge == null) {
            selectedBridge = new MutableLiveData<>();
        }
        return selectedBridge;
    }

    public LiveData<SearchingStates> getDiscoveryState() {
        if(discoveryState == null) {
            discoveryState = hueDiscoverer.getLiveSearchingState();
        }
        return discoveryState;
    }

    public void setSelectedBridge(BridgeInfo selectedBridge) {
        this.selectedBridge.setValue(selectedBridge);
    }

    public List<BridgeInfo> getListOfPossibleBridges() {
        HueDatabase tempDatabase = HueDatabase.getInstance(getApplication().getApplicationContext());
        List<BridgeInfo> selectableBridges = new ArrayList<>();
        List<BridgeInfo> discoveredBridges = hueDiscoverer.getBridges();

        for(BridgeInfo discoveredBridge : discoveredBridges) {
            BridgeInfo bridgeInstance = tempDatabase.bridgeDAO()
                    .getBridgeBasedOnID(discoveredBridge.getBridgeID());
            if(bridgeInstance != null) {
                selectableBridges.add(bridgeInstance);
            }
        }

        if(selectableBridges.isEmpty()) {
            selectableBridges.addAll(tempDatabase.bridgeDAO().getAllBridgeInformation());
        }

        return selectableBridges;
    }
}
