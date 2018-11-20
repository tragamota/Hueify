package my.philipshueremote.MainUI.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import my.philipshueremote.DataCommunication.HueSyncService;

public class MainAppViewModel extends AndroidViewModel {
    private HueSyncService hueService;


    public MainAppViewModel(@NonNull Application application) {
        super(application);
        hueService = new HueSyncService(application);
    }

    public void startHueBackgroundService() {
        hueService.startService();
    }

    public void forceHueRefresh() {

    }

    public void stopHueBackgroundService() {
        hueService.stopService();
    }
}
