package com.yxkj.controller.http;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * 接口API
 */
public interface RetrofitService {

    //
//    @GET("query")
//    Observable<BaseEntity<List<Data>>> getVideoUrl(@Query("type") String type, @Query("postid") String postid);
//
//    @FormUrlEncoded
//    @POST("query")
//    Observable<BaseEntity<List<Data>>> getQuery(@FieldMap Map<String, String> map);
//
//    @POST("rebate-interface/seller/getSellerCategory.jhtml")
//    Observable<BaseEntity<List<ShopCategery>>> getShopCategery(@Body Object body);
//
//    @POST("rebate-interface/endUser/rsa.jhtml")
//    Observable<BaseEntity<String>> getRsa();
//
//    @POST("rebate-interface/area/getHotCity.jhtml")
//    Observable<BaseEntity<List<HotCity>>> getHotCity(@Body RequestBody body);
    @GET("wapdl/hole/201512/03/SogouInput_android_v7.11_sweb.apk")
    Call<ResponseBody> downloadFileWithDynamicUrlAsync();

    @GET("wapdl/hole/201512/03/SogouInput_android_v7.11_sweb.apk")
    Observable<Response<ResponseBody>> download();

    @GET("tieba-smallvideo-transcode/14874724_0a9868df5f37a0502304285495794191_b12d2dd44787_3.mp4")
    Observable<Response<ResponseBody>> downloadVideo();
}
