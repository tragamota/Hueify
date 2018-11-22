package my.philipshueremote.Application;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.widget.Toast;

import my.philipshueremote.DataCommunication.HueSyncService;

public class AppLifeCycleListener implements LifecycleObserver {
    private HueSyncService service;
    private Toast onEnter, onLeave;

    public AppLifeCycleListener(Context appContext) {
        service = HueSyncService.getInstance(appContext);
        onEnter = Toast.makeText(appContext, "Application back", Toast.LENGTH_LONG);
        onLeave = Toast.makeText(appContext, "Application gone", Toast.LENGTH_LONG);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        service.startService();
        onEnter.show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        service.stopService();
        onLeave.show();
    }
}
