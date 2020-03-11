package alaaMeaad.calllogs.remote;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface DataManager {


    void callLogs(RetrofitCallback callback, String data);


}
