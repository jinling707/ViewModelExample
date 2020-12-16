---
title: SQLite
date: 2020-11-30
categories: Android
tags: 
   - Android檔案資料存取
---
### 簡介
基本MVVM架構(含ViewMode、DataBinding)

<!-- more -->

### 說明
MVVM是Model-View-ViewModel的簡稱，三者扮演的角色為：

Model：管理專案的資料來源，如API、資料庫和SharedPreference等，並回應來自ViewModel的資料請求。
View：可以是Activity、Fragment或custom view，用來顯示UI和監聽使用者動作。當使用者跟UI有互動時，會將指令傳給ViewModel，透過ViewModel來獲得所需的資料並顯示。
ViewModel：接收View的指令並對Model請求資料給View使用。

#### ViewModel中的3種資料儲存
1.SavedState:可在資料改變時，即時顯示在View上，並保護資料不會因為進入後台而消失。
2.LiveData:可在資料改變時，即時顯示在View上。
3.一般:直接在ViewModel中宣告即可使用

 <img src="/ViewModel生命週期.png" width="350px" /> 
 
基本上，ViewModel不會因為進入後台便被銷毀，但在程式擱置過久、空間不夠，或手機設定上不允許程式進入後台活動，ViewModel便會被銷毀。雖然隨著Activity的重建，ViewModel亦可以跟著重建，不過重建後資料都會消失。

### 範例

#### Module gradle 添加依賴
dependencies
``` java
//ViewModel
implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
//SavedState
implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0-alpha01'
```
defaultConfig
``` java
//DataBinding
dataBinding{ enabled true}
```

#### MyViewModel.java
``` java

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

```

#### MainActivity.java
``` java
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.example.viewmodelrestore.databinding.ActivityMainBinding;

//使用DataBinding 需要再Gradle(app) android defaultConfig 裡添加 dataBinding{ enabled true}
//並到Activity按小燈泡 將其轉換為BundingStyle
public class MainActivity extends AppCompatActivity {
    MyViewModel myViewModel;
    ActivityMainBinding binding;

    //SavedState的KEY(字串)
    public final static String SavedState_Number = "my_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);這行就不用了

        //ViewModel
        myViewModel =  new ViewModelProvider(this,new SavedStateViewModelFactory(getApplication(), this))
                .get(MyViewModel.class);

//        儲存ViewModel資料不背後台殺掉的舊方法
//        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
//        if(savedInstanceState != null ) //儲存ViewModel方法一  舊方法 第一次開啟時會等於空
//        {
//            myViewModel.getNumber().setValue(savedInstanceState.getInt(KEY_NUMBER));
//        }

        //DataBinding
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);
        /**呼叫|Activity元件*/
        //binding.tv

    }

// 儲存ViewModel資料不背後台殺掉的舊方法
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState)//儲存ViewModel方法一  舊方法: 後臺如果沒有辦法保存ViewModel時 用這保存
//    {
//        super.onSaveInstanceState(outState);
//
//        outState.putInt(KEY_NUMBER,myViewModel.getNumber().getValue());
//    }
}
```
#### activity_main.xml
``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="data"
            type="com.example.viewmodelrestore.MyViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".MainActivity">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#c5d3a7"
            android:orientation="vertical">

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:gravity="center"
                android:text="SavedStateData"
                android:textSize="30dp" />

            <TextView
                android:id="@+id/tv"
                android:layout_width="200dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:background="@drawable/textview_bg"
                android:gravity="center"
                android:text="@{String.valueOf(data.savedState_Number)}"
                android:textSize="30dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.498"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.272" />

            <Button
                android:id="@+id/button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:onClick="@{()->data.addSavedStateNumber()}"
                android:text="+1"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.498"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.368" />
            />
        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#d3a7bd"
            android:orientation="vertical">

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:gravity="center"
                android:text="LiveData"
                android:textSize="30dp" />

            <TextView
                android:id="@+id/tv2"
                android:layout_width="200dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:background="@drawable/textview_bg"
                android:gravity="center"
                android:text="@{String.valueOf(data.getLiveData_Number())}"
                android:textSize="30dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.498"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.272" />

            <Button
                android:id="@+id/button2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:onClick="@{()->data.addLiveDataNumber()}"
                android:text="+1"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.498"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.368" />
            />
        </LinearLayout>

    </LinearLayout>
</layout>
```

##### textview_bg.xml  (res/drawable)
``` xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#FFFFFF"/>
    <corners android:radius="10dp"/>
    <stroke android:width="3dp" android:color="#728ea3"/>
</shape>
```

### 參考資料
https://www.youtube.com/watch?v=uonkx0G2lng&list=PLPh5-KovAYtFyX5elSTT9wEMt0HxMhR7L&index=14


