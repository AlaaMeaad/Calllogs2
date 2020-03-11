package alaaMeaad.calllogs.remote;


import java.util.HashMap;

import alaaMeaad.calllogs.CallLogs;
import alaaMeaad.calllogs.api.ApiServers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManagerImpl implements DataManager {

    ApiServers apiService;

    public DataManagerImpl() {
        apiService = ApiClient.getClient().create(ApiServers.class);
    }


    @Override
    public void callLogs(final RetrofitCallback callback, String data) {
        Call<CallLogs> call = apiService.callLogs(data);
        call.enqueue(new Callback<CallLogs>() {
            @Override
            public void onResponse(Call<CallLogs> call, Response<CallLogs> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CallLogs> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
