package my.philipshueremote.MainUI.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
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

    private MediatorLiveData<SearchingStates> discoveryState;
    private boolean isConnectionStatusHidden;

    public MainAppViewModel(@NonNull Application application) {
        super(application);

        hueService = HueSyncService.getInstance(application);
        hueDiscoverer = MultiCastDiscovery.getInstance(application);
        hueDiscoverer.onStart();
    }

    public LiveData<SearchingStates> getDiscoveryState() {
        if (discoveryState == null) {
            discoveryState = new MediatorLiveData<>();
            discoveryState.addSource(hueDiscoverer.getLiveSearchingState(), searchingStates -> {
                if (searchingStates == SearchingStates.FOUND) {
                    HueDatabase database = HueDatabase.getInstance(getApplication());
                    database.performBackgroundQuery(() -> {
                        List<BridgeInfo> validBridges = new ArrayList<>();

                        BridgeInfo bridgeToAdd;
                        for (BridgeInfo bridge : hueDiscoverer.getBridges()) {
                            bridgeToAdd = database.bridgeDAO().getBridgeBasedOnID(bridge.getBridgeID());
                            if(bridgeToAdd != null) {
                                validBridges.add(bridgeToAdd);
                            }
                        }

                        if (!validBridges.isEmpty()) {
                            discoveryState.postValue(SearchingStates.FOUND);
                            setSelectedBridge(validBridges.get(0));
                        } else {
                            discoveryState.postValue(SearchingStates.WAITING);
                        }
                    });
                } else {
                    discoveryState.postValue(searchingStates);
                }
            });
        }
        return discoveryState;
    }

    public boolean isConnectionStateHidden() {
        return isConnectionStatusHidden;
    }

    public void hideConnectionState() {
        isConnectionStatusHidden = true;
    }

    public void setSelectedBridge(BridgeInfo selectedBridge) {
        hueService.setSelectedBridge(selectedBridge);
        hueService.startService();
    }
}
