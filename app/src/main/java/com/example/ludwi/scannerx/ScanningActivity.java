package com.example.ludwi.scannerx;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import org.json.*;

import java.io.IOException;

public class ScanningActivity extends AppCompatActivity implements View.OnClickListener
{


    SurfaceView scannerView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnFindProduct;
    String intentData = "";

    String ean;

    JSONObject object;

    //rückgaben der EAN abfrage
    String bezeichnung;
    String hersteller;
    String preis;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        scannerView = findViewById(R.id.scannerView);
        btnFindProduct = findViewById(R.id.btnFindProduct);
        btnFindProduct.setOnClickListener(this);
    }


    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnFindProduct)
        {
            //führt EAN abfrage aus
            dataRequest(ean);

            //weiterführende funktionen


        }
    }

    private void initialiseDetectorsAndSources(){

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        //CameraSource manages the camera in conjunction with an underlying detector.
        //Here SurfaceView is the underlying detector.
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        //SurfaceHolder.Callback is used to receive information about changes that occur in the surface
        scannerView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try{
                    if (ActivityCompat.checkSelfPermission(ScanningActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        //open camera and start sending preview frames
                        cameraSource.start(scannerView.getHolder());
                    }else{
                        ActivityCompat.requestPermissions(ScanningActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override   //changes of size or format
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        //Processor: The interface contains the callback to the method receiveDetections() which receives the QR Code from the camera preview and adds them in the SparseArray.
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "Barcode Scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0){
                    //The value of the QR Code is displayed in the TextView using a Runnable since the Barcodes are detected in a background thread.
                    txtBarcodeValue.post(new Runnable() {
                                             @Override
                                             public void run() {

                                                     intentData = barcodes.valueAt(0).displayValue;
                                                     txtBarcodeValue.setText(intentData);
                                                     ean = intentData;

                                             }
                                         }
                    );
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initialiseDetectorsAndSources();
    }

    //methode zur EAN abfrage
    public String dataRequest(String ean)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.upcitemdb.com/prod/trial/lookup?upc="+ean;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                object = response;
                // display response
                Log.d("Scannen", response.toString());
                //success

                readJSON();

                addProduct();

            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("Scannen", "requesterror");

                        Toast.makeText(getApplicationContext(), "request failed", Toast.LENGTH_SHORT).show();

                        //error

                        startActivity(new Intent(ScanningActivity.this , MainActivity.class));
                    }
                });

        queue.add(request);

        return null;

    }

    public SurfaceView getScannerView() {
        return scannerView;
    }

    public void addProduct()
    {
       try
       {
           //Datenbank erstellen

        AppDatabase database = Room.databaseBuilder(ScanningActivity.this, AppDatabase.class, "product_db")
                .allowMainThreadQueries().build();

        Product product = new Product();

        product.setBezeichnung(this.bezeichnung);
        product.setHersteller(this.hersteller);
        product.setPreis(this.preis + "€");

        database.getProductDao().insertProduct(product);

        startActivity(new Intent(ScanningActivity.this , MainActivity.class));

           Log.d("Scannen", "addsucess");
       }
       catch(Exception e)
       {
           Log.d("Scannen", "addfehler");
       }

    }

    public void readJSON()
    {
        try
        {
            //response ist das JSON File das von der datenbank zurückkommt
            JSONObject object = this.object;
            JSONArray itemlist = object.getJSONArray("items");


            for (int i = 0; i < itemlist.length(); i++ )
            {
                bezeichnung = itemlist.getJSONObject(i).getString("title");

                Log.d("Scannen", bezeichnung);

                hersteller = itemlist.getJSONObject(i).getString("brand");

                Log.d("Scannen", hersteller);


                JSONArray offers = new JSONArray(itemlist.getJSONObject(i).getString("offers"));

                for (int z = 0; z < offers.length(); z++ )
                {
                    Integer preis_temp = offers.getJSONObject(i).getInt("price");

                    preis = preis_temp.toString();

                    Log.d("Scannen", preis);

                }

            }

            Log.d("Scannen", "readsucess");

        }

        catch(JSONException jsonexception)
        {

            //exception handling

            Log.d("Scannen", "readfehler");

            Toast.makeText(getApplicationContext(), "reading failded", Toast.LENGTH_SHORT).show();

        }
    }


}

