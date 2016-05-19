package dps924.adpulse.api;

import java.util.concurrent.Callable;

public final class ApiManager {

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    static String baseUrl = "http://adpulse.ca/api/";
    public static void getApiLocations(final Callable<Void> callback) {

    }

    public static void getApiCreatives(final Callable<Void> callback) {


    }
}


