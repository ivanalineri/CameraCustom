package com.camera.custom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.camera.custom.dao.DaoSession;
import com.camera.custom.dao.FotoDao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GaleriaActivity extends AppCompatActivity {

    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        imageView = findViewById(R.id.imagem);

        DaoSession daoSession = ((CustomApplication) getApplication()).getDaoSession();
        final FotoDao fotoDao = daoSession.getFotoDao();

        ArrayList<Foto> fotos = (ArrayList<Foto>) fotoDao.loadAll();

        for (Foto foto : fotos) {
            File fotoAnterior = new File(foto.getCaminho());
            int w = imageView.getWidth();
            int h = imageView.getHeight();
            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(fotoAnterior), w, h, false);
            Bitmap fotoRotacionada = rotacionarFoto(fotoAnterior, bitmap);
            imageView.setImageBitmap(fotoRotacionada);
        }
    }

    public static Bitmap rotacionarFoto(File file, Bitmap bitmap) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
