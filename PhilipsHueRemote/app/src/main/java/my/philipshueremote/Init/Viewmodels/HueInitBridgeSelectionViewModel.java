package my.philipshueremote.Init.Viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.Init.Models.BridgeInfo;

public class HueInitBridgeSelectionViewModel extends ViewModel {
    private MutableLiveData<BridgeInfo> selectedBridge;
    private List<BridgeInfo> bridges = new ArrayList<>();

    public List<BridgeInfo> getBridges() {
        return bridges;
    }

    public void setBridges(List<BridgeInfo> bridges) {
        if(bridges != null) {
            this.bridges.addAll(bridges);
        }
    }

    public LiveData<BridgeInfo> getSelectedBridge() {
        if(selectedBridge == null) {
            selectedBridge = new MutableLiveData<>();
        }
        return selectedBridge;
    }

    public void setSelectedBridge(BridgeInfo bridge) {
        selectedBridge.postValue(bridge);
    }
}
