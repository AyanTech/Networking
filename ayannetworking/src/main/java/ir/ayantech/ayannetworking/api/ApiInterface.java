package ir.ayantech.ayannetworking.api;

import java.util.Map;

import ir.ayantech.ayannetworking.ayanModel.AyanRequest;
import kotlin.Deprecated;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

@Deprecated(message = "Use v2/AyanApi instead.")
public interface ApiInterface {
    @POST
    Call<ResponseBody> callApi(@Url String url, @Body AyanRequest request, @HeaderMap Map<String, String> headers);
}
