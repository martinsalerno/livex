package com.example.martinsalerno.wikitest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.wikitude.architect.ArchitectJavaScriptInterfaceListener;
import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private ArchitectView architectView;
    private final String TAG = "MAIN_ACTIVITY";
    private boolean mustExit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.architectView = (ArchitectView)this.findViewById(R.id.architectView);
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey("TsTbN1sQuxf7XTEJWq+KxgA4pdjpgd3Dgi2i9mZqzOFYn/AazPuogfgDRz6w+X9LaPAzMErVGBhH2fDUbobGH8Tf/pGGFuE8eEsUcAnXhTY2fB0OOWWA8WfgExrKeMwVSZYcqkyrdR/JfxS/T6ddwZ02gzCgxWOt1b+KWbpNoIFTYWx0ZWRfX9F26p8J9zHwTK5iZCOTM6gAGHwIeJwKOffVR5b8V3y+EuieFBogW5fwNl9EDAz6aVI+YkGfcuI5Vnr0ZQWXlsblMVRrOsyx9n7mf+XMHPkk6dHgNhdZPK+4+G7sTd/UMVNl0U3hZGejzUGXTtzG44vHa5VOjT3v/Crmwn5StAEsIRpvuFfHu7ugtyTKBLu053NcBTZVh4SvCPR/kpqkf1xQUX9ePVI5at8y3jN3RBVAKPTq2UIgxhMmmqHty+zZjT7BPqSHMd3NSUFO6SDdA5ddSyZUQkoJsnLeBw5lktOd0WnwJaGKXM1CFvuBXqzw4njK8LjiEul6EFw1MVY+0Hfc2OOuCDeGp3e5NR7mccu+SJhfEDwepOhRdXd7q98B0VoCYnxu4ob52wA1X15LGAWjMCwLcipQ1TPcoY5wktC+3vrGYjv5lb4lEZv2Fl7oaODUVlK3AkqCs/wXPHg7mWy3FTDq+2VqKf2GIWXfu1y8qwkUq7Mb3wwXQYR5WQpz67fzrVttUqG0MHimgIeweaAx058UXaf3iWUhblF8lfMir8mgum+9E3SpYkiBDhEljWs5efrckeXz3vA6P6C/nManXib6BMaZ/w==");
        this.architectView.onCreate(config);
        goToSleep();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        architectView.onPostCreate();
        try {
            this.architectView.load( "file:///android_asset/scan/index.html" );

        } catch(Exception e) {
            Log.d(TAG, e.toString());
        }

        JSONObject poi = new JSONObject();
        try {
            poi.put("id", 3);
            poi.put("longitude", -58.4222305);
            poi.put("latitude", -34.5989465);
            poi.put("description", "Renault servicios");
            poi.put("name", "Renault");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.architectView.callJavascript("World.loadPois(" + poi.toString() + ");");

    }

    @Override
    protected void onResume() {
        super.onResume();
        architectView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        architectView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        architectView.onPause();

    }

    public void onJSONObjectReceived(JSONObject jsonObject) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void goToSleep(){
        architectView.addArchitectJavaScriptInterfaceListener(new ArchitectJavaScriptInterfaceListener() {
            @Override
            public void onJSONObjectReceived(JSONObject jsonObject) {
                Log.d("ENTRE A JSON BOJECT", jsonObject.toString());
                try {
                    switch (jsonObject.getString("name")) {
                        case "entradaScanned":
                            Log.d("ENTRADA ESCANEADA", "ENTRADA");
                            new RequestHandler().addEvent(getBaseContext(), jsonObject.getString("id"));
                            break;
                        case "imagenDetectada":
                            mustExit = false;
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onJSONObjectReceived: ", e);
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d("SCANEO", Boolean.toString(mustExit));
                if (mustExit){
                    Toast.makeText(getBaseContext(), "No se detecto ninguna imagen", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, 1000 * 60);
    }
}