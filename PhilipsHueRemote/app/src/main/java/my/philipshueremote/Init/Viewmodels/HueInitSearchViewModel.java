package my.philipshueremote.Init.Viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import my.philipshueremote.Discovery.MultiCastDiscovery;
import my.philipshueremote.Init.Models.BridgeInfo;
import my.philipshueremote.Init.Models.SearchingStates;

public class HueInitSearchViewModel extends AndroidViewModel {
    private boolean initialSearch;
    private MultiCastDiscovery multiCastDiscovery;

    public HueInitSearchViewModel(@NonNull Application application) {
        super(application);
        initialSearch = false;
        multiCastDiscovery = MultiCastDiscovery.getInstance(application);
    }

    public LiveData<SearchingStates> getSearchState() {
        return multiCastDiscovery.getLiveSearchingState();
    }

    public List<BridgeInfo> getBridges() {
        return multiCastDiscovery.getBridges();
    }

    public boolean getInitialSearch() {
        return initialSearch;
    }

    public void setInitialSearch(boolean value) {
        initialSearch = value;
    }

    public void startSearching() {
        multiCastDiscovery.onStart();
    }

    public void stopSearching() {
        multiCastDiscovery.onStop();
    }

    public void resetSearchingState() {
        multiCastDiscovery.onReset();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopSearching();
    }
}
