//package asyns;
//
///**
// * Created by s on 01-03-2017.
// */
//import android.content.Context;
//
//import com.android.volley.Cache;
//import com.android.volley.Network;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.DiskBasedCache;
//import com.android.volley.toolbox.HurlStack;
//import com.android.volley.toolbox.NoCache;
//
//public class CustomVolleyRequestQueue {
//
//    private static CustomVolleyRequestQueue customRequestQueue;
//    private RequestQueue requestQueue;
//    Context context;
//
//    private CustomVolleyRequestQueue(Context context) {
//        this.context = context;
//        requestQueue = getRequestQueue();
//    }
//
//    public static CustomVolleyRequestQueue getInstance(Context context) {
//        if (customRequestQueue == null) {
//            customRequestQueue = new CustomVolleyRequestQueue(context);
//        }
//        return customRequestQueue;
//    }
//
//    public RequestQueue getRequestQueue() {
//        if (requestQueue == null) {
//            Cache cache = new DiskBasedCache(context.getCacheDir(), 40 * 1024 * 1024); //20MB cache Directory
//            Network network = new BasicNetwork(new HurlStack());
//
//            //case where we dont want any Cache for any request.
//
//            requestQueue = new RequestQueue(new NoCache(), network);
//
////            requestQueue = new RequestQueue(cache, network);
//            requestQueue.start();
//        }
//        return requestQueue;
//    }
//}
