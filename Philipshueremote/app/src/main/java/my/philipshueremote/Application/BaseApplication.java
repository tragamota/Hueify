package my.philipshueremote.Application;

import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;

public class BaseApplication extends Application {
    private AppLifeCycleListener appChange;

    @Override
    public void onCreate() {
        super.onCreate();
        appChange = new AppLifeCycleListener(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appChange);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(appChange);
    }
}
