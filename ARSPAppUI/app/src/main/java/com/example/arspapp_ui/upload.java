package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class upload extends AppCompatActivity {

    private static final String COGNITO_POOL_ID = "ap-northeast-2:ab1356c6-4163-4378-afc2-d7afb7c9f062";
    private static final String BUCKET_NAME = "asa-senier-project";

    private static final int PICK_VIDEO_REQUEST_CODE = 1;
    private static final String TAG = upload.class.getCanonicalName();

    EditText editTextUpload, editTextUploadId;
    VideoView videoViewUpload;
    Button buttonPickVideo;
    Button buttonUpload;
    Uri videoUri;
    ProgressDialog progressDialogUpload;
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        initUI();
        createTransferUtility();
    }

    private void initUI() {
        editTextUpload = findViewById(R.id.edit_upload);
        editTextUploadId = findViewById((R.id.upload_id));
        videoViewUpload = findViewById(R.id.video_upload);
        buttonPickVideo = findViewById(R.id.btn_pick);

        buttonPickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");


                startActivityForResult(intent, PICK_VIDEO_REQUEST_CODE);
            }
        });

        buttonUpload = findViewById(R.id.btn_upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent2 = getIntent();
                String upload_id = intent2.getStringExtra("upload_id");*/

                if (videoUri != null) {
                    if (!TextUtils.isEmpty(editTextUpload.getText().toString())){
                        String objectKey = editTextUpload.getText().toString();
                        String uploadId = editTextUploadId.getText().toString();
                        File file = null;

                        try {
                            file = createFileFromUri(videoUri, objectKey);
                            upload(file, objectKey);
                            uploadNewVideo(uploadId, objectKey);

                            progressDialogUpload = new ProgressDialog(upload.this);
                            progressDialogUpload.setMessage("Uploading file " + file.getName());
                            progressDialogUpload.setIndeterminate(true);
                            progressDialogUpload.setCancelable(false);
                            progressDialogUpload.show();

                        } catch (IOException e) {
                            Log.e(TAG, "onClick: ", e);
                            Toast.makeText(upload.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(upload.this, "Enter object key in EditText", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(upload.this, "Pick any video to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createTransferUtility() {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                COGNITO_POOL_ID,
                Regions.AP_NORTHEAST_2
        );
        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3Client, getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case PICK_VIDEO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    videoUri = uri;
                    videoViewUpload.setVideoURI(uri);
                    videoViewUpload.seekTo(0);
                    editTextUpload.setText(getFileNameFromUri(uri));
                    buttonUpload.setEnabled(true);
                }
        }
    }

    String getFileNameFromUri(Uri uri) {
        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = 0;
        if (returnCursor != null) {
            nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            returnCursor.close();
            return name;
        } else {
            return "";
        }
    }

    File createFileFromUri(Uri uri, String objectKey) throws IOException {
        InputStream is = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), objectKey);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[2046];
        int read = -1;
        while ((read = is.read(buf)) != -1) {
            fos.write(buf, 0, read);
        }
        fos.flush();
        fos.close();
        return file;
    }

    private void upload(File file, final String objectKey) {
        TransferObserver transferObserver = transferUtility.upload(
                BUCKET_NAME,
                objectKey,
                file
        );
        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d(TAG, "onStateChanged: " + state);
                if (TransferState.COMPLETED.equals(state)) {
                    editTextUpload.setText(objectKey);
                    progressDialogUpload.dismiss();
                    //Toast.makeText(upload.this, "영상이 업로드 되었습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {}

            @Override
            public void onError(int id, Exception ex) {
                progressDialogUpload.dismiss();
                Log.e(TAG, "onError: ", ex);
                Toast.makeText(upload.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadNewVideo(final String upload_id, final String upload_video) {

        final ProgressDialog progressDialog = new ProgressDialog(upload.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("동영상 업로드");
        progressDialog.show();

        String uRl = "http://13.124.25.195//loginregister/videoupload.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                if (response.equals("성공적으로 업로드했습니다")) {
                    progressDialog.dismiss();
                    Toast.makeText(upload.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(upload.this, post.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(upload.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(upload.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("upload_id", upload_id);
                param.put("upload_video", upload_video);

                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(upload.this).addToRequestQueue(request);
    }
}
