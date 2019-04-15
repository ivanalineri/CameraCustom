package com.camera.custom;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.camera.custom.dao.DaoSession;
import com.camera.custom.dao.FotoDao;
import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.markushi.ui.CircleButton;

public class CameraActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private CircleButton photoButton;
    private CircleButton gallery;
    private CircleButton changeCamera;
    private CircleButton flash_off;
    private CircleButton flash_on;
    private CircleButton close;
    private TextView usarFotos;
    private ImageView countPhoto;
    private TextView myImageViewText;
    private int cont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraKitView = findViewById(R.id.camera);
        photoButton = findViewById(R.id.photoButton);
        gallery = findViewById(R.id.gallery);
        changeCamera = findViewById(R.id.changeCamera);
        flash_off = findViewById(R.id.flash);
        flash_on = findViewById(R.id.flash_on);
        close = findViewById(R.id.close);
        usarFotos = findViewById(R.id.usarFotos);
        countPhoto = findViewById(R.id.countPhoto);
        myImageViewText = findViewById(R.id.myImageViewText);


        DaoSession daoSession = ((CustomApplication) getApplication()).getDaoSession();
        final FotoDao fotoDao = daoSession.getFotoDao();


        View.OnClickListener photoOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                        cont = cont + 1;
                        myImageViewText.setText(String.valueOf(cont));
                        countPhoto.setVisibility(View.VISIBLE);
                        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        switch( audio.getRingerMode() ){
                            case AudioManager.RINGER_MODE_NORMAL:
                                MediaActionSound sound = new MediaActionSound();
                                sound.play(MediaActionSound.SHUTTER_CLICK);
                                break;
                            case AudioManager.RINGER_MODE_SILENT:
                                break;
                            case AudioManager.RINGER_MODE_VIBRATE:
                                break;
                        }

                        Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                        String date = format.format(new Date());
                        File imagesFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "TesteVidiPhoto");
                        if (!imagesFolder.exists()) {
                            imagesFolder.mkdirs();
                        }
                        File file = new File(imagesFolder, "TesteFoto" + date + ".jpg");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file.getPath());
                            outputStream.write(photo);
                            outputStream.close();
                            Foto foto = new Foto();
                            foto.setCaminho(file.getPath());
                            foto.setPesquisa(1);
                            fotoDao.insert(foto);
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                            Log.e("CKDemo", "Exception in photo callback");
                        }
                    }
                });
            }
        };
        photoButton.setOnClickListener(photoOnClickListener);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraActivity.this, GaleriaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        flash_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraKitView.setFlash(CameraKit.FLASH_ON);
                flash_on.setVisibility(View.VISIBLE);
                flash_off.setVisibility(View.GONE);

            }
        });

        flash_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraKitView.setFlash(CameraKit.FLASH_OFF);
                flash_off.setVisibility(View.VISIBLE);
                flash_on.setVisibility(View.GONE);

            }
        });

        changeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraKitView.getFacing() == CameraKit.FACING_BACK){
                    cameraKitView.setFacing(CameraKit.FACING_FRONT);
                } else{
                    cameraKitView.setFacing(CameraKit.FACING_BACK);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
