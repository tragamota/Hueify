package my.philipshueremote.MainUI.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Database.Entities.Lamp;

public class LampFragmentViewModel extends AndroidViewModel {
    private HueDatabase database;
    private List<Lamp> lamps;

    private LiveData<String> bridgeID;
    private MediatorLiveData<List<Lamp>> lampChanges;

    public LampFragmentViewModel(@NonNull Application application) {
        super(application);
        database = HueDatabase.getInstance(application.getApplicationContext());
    }



    public LiveData<List<Lamp>> getLampStateChanger() {
        if(lamps == null) {
            lampChanges = new MediatorLiveData<>();
            //lampChanges.addSource(database.lampDAO().);
        }
        return lampChanges;
    }
}
