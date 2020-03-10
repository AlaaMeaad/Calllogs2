package alaaMeaad.calllogs.api;



import alaaMeaad.calllogs.CallLogs;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiServers {
    @POST("management_reports/calls/import")
    @FormUrlEncoded
    Call<CallLogs> callLogs(@Field("data") String data);
//    Call<CallLogs> callLogs(@Body RequestBody  data);



}


