package com.example.arspapp_ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class post extends Fragment {
    private View view;
    private VideoView self_video_1;

    private TransferUtility transferUtility;

    private static final String COGNITO_POOL_ID = "ap-northeast-2:ab1356c6-4163-4378-afc2-d7afb7c9f062";
    private static final String BUCKET_NAME = "asa-senier-project";
    private static final String stringObjKeyName = null;

    private static final String TAG = post.class.getCanonicalName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_post, container, false);

        self_video_1 = view.findViewById(R.id.self_video1);

        createTransferUtility();

        pullmyvideo();

        return view;
    }

    private void createTransferUtility() {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                requireContext(),
                COGNITO_POOL_ID,
                Regions.AP_NORTHEAST_2
        );
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3Client, requireContext());
    }

    private void pullmyvideo() {
        SharedPreferences settings = requireContext().getSharedPreferences("UserInfo",0);
        final String user_id = settings.getString("ID","");
        final com.android.volley.Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    download(response);

                }
            }
        };
        String uRl = "http://13.124.25.195//phpFiles/pullmyvideo.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, response, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_id", user_id);

                return param;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(getContext()).addToRequestQueue(request);
    }

    private void download(String objectKey) {
        final File fileDownload = new File(requireContext().getCacheDir(), objectKey);

        TransferObserver transferObserver = transferUtility.download(
                BUCKET_NAME,
                objectKey,
                fileDownload
        );
        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED.equals(state)) {
                    String videopath = fileDownload.toString();
                    MediaController mc = new MediaController(getContext());

                    self_video_1.setMediaController(mc);
                    self_video_1.setVideoURI(Uri.parse(videopath));

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {}

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(getContext(), "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}