package com.example.viewmodelrestore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel
{
    /**SavedState*/
    //  使用SavedState需添加  implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0'
    private SavedStateHandle handle;
    //ViewModel的建構子需傳送SavedStateHandle
    public MyViewModel(SavedStateHandle handle)
    {
        this.handle = handle;
    }

    //取得SavedStateNumber
    public MutableLiveData<Integer> getSavedState_Number()
    {
        if(!handle.contains(MainActivity.SavedState_Number))
        {
            //初始化
            handle.set(MainActivity.SavedState_Number,0);
        }
        return  handle.getLiveData(MainActivity.SavedState_Number);
    }


    /**LiveData*/
    private MutableLiveData<Integer> LiveData_Number;
    //取得LiveData
    public MutableLiveData<Integer> getLiveData_Number()
    {
        if(LiveData_Number == null)
        {
            LiveData_Number = new MutableLiveData<>();
            LiveData_Number.setValue(0);
        }
        return LiveData_Number;
    }

    /**一般資料*/
    private List<String> List0 = new ArrayList<String>();


    //操作SavedState
    public void addSavedStateNumber()
    {
        getSavedState_Number().setValue(getSavedState_Number().getValue()+1);
    }

    //操作LiveData
    public void addLiveDataNumber()
    {
        getLiveData_Number().setValue(getLiveData_Number().getValue()+1);
    }


}
