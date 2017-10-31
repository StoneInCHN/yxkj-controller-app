package com.yxkj.controller.http;

import com.yxkj.controller.base.BaseEntity;
import com.yxkj.controller.beans.Category;
import com.yxkj.controller.beans.MachineInfoRequest;
import com.yxkj.controller.beans.SgByChannel;
import com.yxkj.controller.beans.VerifyStock;
import com.yxkj.controller.beans.VerifyStockBody;
import com.yxkj.controller.constant.Constant;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 接口API
 */
public interface RetrofitService {
    /*根据货道编号查询商品*/
    @POST(Constant.GETSGBYCHANNEL)
    Observable<BaseEntity<SgByChannel>> getSgByChannel(@Body Map<String, String> map);

    /*查询商品类别*/
    @POST(Constant.GETCATEGORY)
    Observable<BaseEntity<List<Category>>> getCategory();

    /*根据类别查询商品*/
    @POST(Constant.GETBYCATE)
    Observable<BaseEntity<List<SgByChannel>>> getByCate(@Body Map<String, String> map);

    /*验证商品库存数量*/
    @POST(Constant.VERIFYSTOCK)
    Observable<BaseEntity<List<VerifyStock>>> verifyStock(@Body VerifyStockBody body);

    @POST(Constant.UPATECMDSTATUS)
    Observable<ResponseBody> updateCmdStatus(@Query("commandId") Long commandId, @Query("isSuccess") Boolean isSuccess);

    @POST(Constant.UPDATESHIPMENTSTATUS)
    Observable<ResponseBody> updateShipmentStatus(@Query("orderItemId") Long orderId, @Query("shipmentStatus") String shipmentStatus);

    @POST(Constant.INITMACHINESTATUS)
    Observable<ResponseBody> initMachineStatus(@Body MachineInfoRequest request);

    @GET("wapdl/hole/201512/03/SogouInput_android_v7.11_sweb.apk")
    Call<ResponseBody> downloadFileWithDynamicUrlAsync();

    @GET("wapdl/hole/201512/03/SogouInput_android_v7.11_sweb.apk")
    Observable<Response<ResponseBody>> download();

    @GET(".mp4")
    Observable<Response<ResponseBody>> downloadVideo();
}
