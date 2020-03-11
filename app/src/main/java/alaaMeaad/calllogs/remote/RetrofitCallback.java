package alaaMeaad.calllogs.remote;

import retrofit2.Response;

public interface RetrofitCallback {
    void onSuccess(Object response);
    void onError(Throwable throwable);
    void onErrorCode(Response<Object> response);
}
