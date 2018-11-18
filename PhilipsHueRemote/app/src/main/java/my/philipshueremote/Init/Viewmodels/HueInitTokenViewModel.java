package my.philipshueremote.Init.Viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.android.volley.toolbox.JsonRequest;

import my.philipshueremote.DataCommunication.VolleyJsonSocket;

public class HueInitTokenViewModel extends AndroidViewModel {
    private VolleyJsonSocket volleySocket;

    private MutableLiveData<String> accessToken;
    private CountDownTimer buttonPressCounter;
    private JsonRequest onGoingUserRequest;

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

    public void startAccessToken() {
        retrieveAccessToken();
        buttonPressCounter = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                volleySocket.cancel(onGoingUserRequest);
            }
        };
    }

    public void stopAccessToken() {

    }



    public void retrieveAccessToken() {
        onGoingUserRequest =
        volleySocket.addRequestToQueue()


    }
}
