package com.elkriefy.android.apps.torchi;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;



public class MainActivity extends AppCompatActivity {

    boolean torchMode;//holds the mode
    CameraManager manager;
    torchiCallback mTorchiCallback;//just an example for the callback on torch mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        torchMode = false;
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mTorchiCallback = new torchiCallback();

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTorchMode();

            }
        });

    }

    private void toggleTorchMode() {
        try {
            String[] cameraIdList = manager.getCameraIdList();
            CameraCharacteristics cameraCharacteristics = manager.getCameraCharacteristics(cameraIdList[0]);
            if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                torchMode = !torchMode;
                manager.setTorchMode(cameraIdList[0], torchMode);
            } else {
                CameraCharacteristics cameraCharacteristics1 = manager.getCameraCharacteristics(cameraIdList[1]);
                if (cameraCharacteristics1.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                    torchMode = !torchMode;
                    manager.setTorchMode(cameraIdList[1], torchMode);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if ((manager!=null)&& (mTorchiCallback!=null)) {
            manager.unregisterTorchCallback(mTorchiCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((manager!=null)&& (mTorchiCallback!=null)){
            manager.registerTorchCallback(mTorchiCallback,null);
        }
    }

    class torchiCallback extends CameraManager.TorchCallback{
        @Override
        public void onTorchModeUnavailable(String cameraId) {
            super.onTorchModeUnavailable(cameraId);
            Log.e("onTorchModeUnavailable", "CameraID:" + cameraId);
        }

        @Override
        public void onTorchModeChanged(String cameraId, boolean enabled) {
            super.onTorchModeChanged(cameraId, enabled);
            Log.e("onTorchModeChanged", "CameraID:"+cameraId+" TorchMode : "+enabled);
        }
    }

}
