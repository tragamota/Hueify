package my.philipshueremote.Init.Viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import my.philipshueremote.DataCommunication.VolleyJsonSocket;

public class HueInitManualTokenViewModel extends AndroidViewModel {
    private VolleyJsonSocket volleySocket;

    public HueInitManualTokenViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
