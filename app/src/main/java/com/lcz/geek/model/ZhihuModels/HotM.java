package com.lcz.geek.model.ZhihuModels;

import android.util.Log;

import com.lcz.geek.Api.ZhihuApi.ZhihuServer;
import com.lcz.geek.Base.BaseMvpModel;
import com.lcz.geek.CallBackss.RibaoCallBack;
import com.lcz.geek.CallBackss.ZhihuCallback.HotCallBack;
import com.lcz.geek.bean.Zhihu.HotBean;
import com.lcz.geek.bean.Zhihu.RibaoBean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 李承泽 on 2019/4/11.
 */
public class HotM extends BaseMvpModel {
    private static final String TAG = "HotM";

    public void getData(final HotCallBack CallBack) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ZhihuServer.url)
                .build();

        ZhihuServer zhihuServer = retrofit.create(ZhihuServer.class);
        Observable<HotBean> hot = zhihuServer.getHot();

        hot.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(HotBean ribaoBean) {
                        CallBack.onSussion(ribaoBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        CallBack.onFali("请求不到网络数据");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
