package com.example.viewmodelrestore;


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