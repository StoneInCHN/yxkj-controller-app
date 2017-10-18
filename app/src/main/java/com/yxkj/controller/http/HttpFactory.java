package com.yxkj.controller.http;

import com.yxkj.controller.base.BaseObserver;
import com.yxkj.controller.beans.ByCate;
import com.yxkj.controller.beans.Category;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.beans.VerifyStock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 请求类
 */

public class HttpFactory {
    /**
     * 根据货道编号查询商品
     *
     * @param cImei 中控唯一标识imei编号
     * @param cSn   货道(商品)编号
     */
    public static void getSgByChannel(String cImei, String cSn, BaseObserver<SgByChannel> baseObsver) {
        Map<String, String> map = new HashMap<>();
        map.put("cImei", cImei);
        map.put("cSn", cSn);
        autoSubscribe(RetrofitFactory.getInstance().getSgByChannel(map), baseObsver);
    }

    /**
     * 查询商品类别
     */
    public static void getCategory(BaseObserver<List<Category>> observer) {
        autoSubscribe(RetrofitFactory.getInstance().getCategory(), observer);
    }

    /**
     * 根据类别查询商品
     *
     * @param cImei      中控唯一标识imei编号
     * @param cateId     商品类别ID
     * @param pageSize
     * @param pageNumber
     */
    public static void getByCate(String cImei, String cateId, String pageSize,
                                 String pageNumber,boolean isAll, BaseObserver<List<ByCate>> observer) {
        Map<String, String> map = new HashMap<>();
        map.put("cImei", cImei);
        if (!isAll)
            map.put("cateId", 1+"");
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        autoSubscribe(RetrofitFactory.getInstance().getByCate(map), observer);
    }

    /**
     * 验证商品库存数量
     *
     * @param gList 商品数组 格式[货道cId-商品数量]
     */
    public static void verifyStock(String gList, BaseObserver<List<VerifyStock>> observer) {
        Map<String, String> map = new HashMap<>();
        map.put("gList", gList);
        autoSubscribe(RetrofitFactory.getInstance().verifyStock(map), observer);
    }

    /**
     * @param
     */
    public static void updateCmdStatus(Long comomandId, Boolean isSuccess) {
        RetrofitFactory.getInstance().updateCmdStatus(comomandId, isSuccess).subscribeOn(Schedulers.io()).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(@NonNull ResponseBody responseBody) throws Exception {
                System.out.println(responseBody);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
    }

    /**
     * 订阅(带compse)
     */
    private static <T> void autoCompseSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribe(observer);
    }

    /**
     * 订阅(带compse)
     */
    private static <T> void autoSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


}
