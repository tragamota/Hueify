package my.philipshueremote.Application;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import my.philipshueremote.DataCommunication.Services.HueSyncService;

public class AppLifeCycleListener implements LifecycleObserver {
    private HueSyncService service;

    public AppLifeCycleListener(Context appContext) {
        service = HueSyncService.getInstance(appContext);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        service.startService();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        service.stopService();
    }
}
