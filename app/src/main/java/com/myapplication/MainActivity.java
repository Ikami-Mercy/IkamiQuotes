package com.myapplication;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();
    private RequestQueue mRequestQueue;
    private TextView acme_response;
    private long initialTime;
    private long totalTime = 10000;
    private FloatingActionButton refreshBtn;
    private Handler h = new Handler();
    private Runnable runnable;
    private ProgressDialog pDialog;
    private ImageView back;
    private HashMap<String,String> postMap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        acme_response = findViewById(R.id.acme_response);
        refreshBtn = findViewById(R.id.refreshBtn);
        back = findViewById(R.id.back);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        setProgressDialog();
        initialTime = System.currentTimeMillis();

        Log.e(TAG, "INITIAL TIME: " + initialTime);

        retryAction();

 /*       runnable = new Runnable() {
            @Override
            public void run() {
                long endTime = System.currentTimeMillis();

                Log.e(TAG, "END TIME: " + endTime);

                if ((endTime - initialTime) > totalTime) {
                    h.removeCallbacksAndMessages(runnable);
                    dismissProgessdialog();
                    Log.e(TAG, "Time elapsed!!");
                    mRequestQueue.cancelAll(TAG);
                    acme_response.setText("A penny saved, is a penny earned :-)");
                    runnable = null;

                } else {
                    getAcmeResponse();
                }


                h.postDelayed(runnable, 5000);


            }
        };

        h.post(runnable);*/

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //h.post(runnable);
                recreate();
               // retryAction();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void getAcmeResponse() {
        JSONObject responseJson = new JSONObject();
        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.resUrl, responseJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "String Response : " + response.toString());
                        try {
                            JSONObject json = response;
                            int statusCode = 200;
                            if (statusCode == HttpURLConnection.HTTP_OK) {
                                h.removeCallbacks(runnable);
                                dismissProgessdialog();
                                String resMessage = json.getString("fortune");
                                acme_response.setText(resMessage);
                                String resId = json.getString("id");
                                postMap.put("id",resId);

                                postAcmeResponse();

                            } else {

                                String errorMsg = json.getString("message");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.i("TAG", "Error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setTag(TAG);
      /* RetryPolicy retryPolicy=new DefaultRetryPolicy(timeout, retries, multiplier);
        retryPolicy.getCurrentRetryCount();
        Log.e(TAG, "retries count is:" +  retryPolicy.getCurrentRetryCount());
        jsonObjectRequest.setRetryPolicy(retryPolicy);*/
        mRequestQueue.add(jsonObjectRequest);
    }

    public void postAcmeResponse() {
        JSONObject responseJson = new JSONObject();
        showProgressDialog();
        String id = postMap.get("id");
        String timeElapsed = "A penny saved, is a penny earned :-)";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.resUrl + id, responseJson,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "String Response : " + response.toString());
                        try {
                            JSONObject json = response;
                            int statusCode = 200;
                            
                                String errorMsg = json.getString("message");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            
                        } catch (Exception e) {
                            Log.i("TAG", "Error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setTag(TAG);

    }
    private void setProgressDialog() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMax(5);
        pDialog.setMessage("Fetching response.....");
        pDialog.setCancelable(false);
    }

    private void showProgressDialog() {
        pDialog.show();
    }

    private void dismissProgessdialog() {
        pDialog.dismiss();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void retryAction() {
        runnable = new Runnable() {
            @Override
            public void run() {
                long endTime = System.currentTimeMillis();

                Log.e(TAG, "END TIME: " + endTime);

                if ((endTime - initialTime) > totalTime) {
                    h.removeCallbacksAndMessages(runnable);
                    dismissProgessdialog();
                    Log.e(TAG, "Time elapsed!!");
                    mRequestQueue.cancelAll(TAG);
                    acme_response.setText("A penny saved, is a penny earned :-)");
                    runnable = null;

                } else {
                    getAcmeResponse();
                }


                h.postDelayed(runnable, 5000);


            }
        };

        h.post(runnable);
    }
}
