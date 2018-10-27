package my.philipshueremote.Init.Viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import my.philipshueremote.DataCommunication.VolleyJsonSocket;

public class HueInitTokenViewModel extends AndroidViewModel {
    private VolleyJsonSocket volleySocket;

    private MutableLiveData<String> accessToken;

    public HueInitTokenViewModel(@NonNull Application application) {
        super(application);
        volleySocket = VolleyJsonSocket.getInstance(application);
    }

    public LiveData<String> getAccessToken() {
        if(accessToken == null) {
            accessToken = new MutableLiveData<>();
            accessToken.postValue(null);
        }
        return accessToken;
    }

    public void retrieveAccessToken() {

    }
}
