package AlaaMeaad.calllogs.api;



import AlaaMeaad.calllogs.CallLogs;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServers {
    @POST("management_reports/calls/import")
    @FormUrlEncoded
    Call<CallLogs> callLogs(@Field("data") String data);
//    Call<CallLogs> callLogs(@Body RequestBody  data);



}


