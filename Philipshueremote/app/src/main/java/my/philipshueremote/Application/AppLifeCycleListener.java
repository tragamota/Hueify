package my.philipshueremote.Application;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import my.philipshueremote.DataCommunication.Services.HueSyncService;
import my.philipshueremote.Discovery.MultiCastDiscovery;

public class AppLifeCycleListener implements LifecycleObserver {
    private HueSyncService service;
    private MultiCastDiscovery discovery;
    private SharedPreferences preferences;

    public AppLifeCycleListener(Context appContext) {
        service = HueSyncService.getInstance(appContext);
        discovery = MultiCastDiscovery.getInstance(appContext);
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        preferences.edit().putBoolean("INIT_DONE", true).apply();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        if(preferences.contains("INIT_DONE")) {
            service.startService();
            discovery.onStart();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        service.stopService();
        discovery.onStop();
    }
}
