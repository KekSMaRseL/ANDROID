package algonquin.cst2335.android.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public MutableLiveData<Boolean> isCheckboxChecked = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSwitchChecked = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRadioChecked = new MutableLiveData<>();
    public MutableLiveData<String> theText = new MutableLiveData<>();
}


