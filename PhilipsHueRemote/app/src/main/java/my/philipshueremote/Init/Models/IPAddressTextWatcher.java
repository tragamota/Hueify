package my.philipshueremote.Init.Models;

import android.arch.lifecycle.MutableLiveData;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.util.regex.Pattern;

public class IPAddressTextWatcher implements TextWatcher {
    private final Pattern ipRegexPattern =
            Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    private MutableLiveData<Boolean> isValidIp;

    public IPAddressTextWatcher(MutableLiveData<Boolean> isValidIp) {
        this.isValidIp = isValidIp;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(ipRegexPattern.matcher(editable).matches()) {
            isValidIp.postValue(true);
        }
        else {
            isValidIp.postValue(false);
        }
    }
}
