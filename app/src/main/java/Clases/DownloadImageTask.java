package Clases;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public   class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String NameImage;
   // String name2;
    private boolean ContinuarDescargando = true;
    URLServices ur = new URLServices();

    Context co;

   private WeakReference<ImageView> imageViewReference;

    public DownloadImageTask(ImageView bmImage, Context cont,String nameimage ) {
        try{

             co = cont;
             // Log.d("DownloadImageTask",bmImage.toString() + " Dibujado " + bmImage.getDrawable());
            if(bmImage.getDrawable() == null || true) {
                this.bmImage = bmImage;
                imageViewReference = new WeakReference<ImageView>(bmImage);
                this.NameImage = nameimage;

                if(nameimage.equals(""))
                {
                    ContinuarDescargando = true;
                }else
                {
                    loadImageFromStorage(this.NameImage);
                }


                Log.i("ImagenDescargadada",bmImage.toString());
            }else
            {
              //  Log.i("ImagenDescargada",bmImage.toString());
            }
        }catch (Exception ex) {

        }

    }

    protected Bitmap doInBackground(String... urls) {

        Bitmap mIcon11 = null;

        if(ContinuarDescargando==true) {
            String urldisplay = urls[0];

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        try{

              if(ContinuarDescargando == true) {

                  if (imageViewReference != null && result != null) {
                      bmImage = imageViewReference.get();
                      if (bmImage != null) {
                          // imageView.setImageBitmap(result);
                          bmImage.setImageBitmap(result);
                          saveToInternalSorage(result, this.NameImage);
                      }
                  }

              }else
              {

              }

       } catch (Exception e) {
            Log.e("Error ", " Error AL onpostExecute Fijar Imane : " + e.toString());
        }

    }

    private String saveToInternalSorage(Bitmap bitmapImage,String NameImagen) throws IOException {
        ContextWrapper cw = new ContextWrapper( co.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(ur.PATHImages, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,NameImagen);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String nameimage)
    {
       try {
           ContextWrapper cw = new ContextWrapper( co.getApplicationContext());

           File directory = cw.getDir(ur.PATHImages, Context.MODE_PRIVATE);

           File f = new File(directory, nameimage);

           if(f.exists()){
               Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
               // ImageView img=(ImageView)findViewById(R.id.imgPicker);
               bmImage.setImageBitmap(b);

               ContinuarDescargando = false;

             //  Log.i("ImagenExistente","La imagen " + nameimage + " Existe");

           }else
           {
            //   Log.i("ImagenExistente","La imagen " + nameimage + " NOOOO Existe");
               ContinuarDescargando = true;
           }


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }



}