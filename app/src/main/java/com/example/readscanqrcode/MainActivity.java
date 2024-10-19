package com.example.readscanqrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    private Button btnGenerar, btnScannear, btnLimpiar, btnFoto;
    private ImageView imgCodigoqr;
    private EditText txtcodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGenerar = findViewById(R.id.btGenerar);
        btnScannear = findViewById(R.id.btScanear);
        btnLimpiar = findViewById(R.id.btLimpiar);
        btnFoto = findViewById(R.id.btFoto);
        imgCodigoqr = findViewById(R.id.imvCodigoQR);
        imgCodigoqr.setImageResource(com.google.android.material.R.drawable.abc_ic_search_api_material);
        txtcodigo = findViewById(R.id.edtCodigo);

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (txtcodigo.getText().toString().isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setIcon(R.drawable.logo)
                                .setTitle("Advertencia")
                                .setMessage("Debe capturar el codigo a generar")
                                .setCancelable(false);

                        builder.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               // Toast.makeText(MainActivity.this,"Gracias por entender", Toast.LENGTH_SHORT).show();
                               // dialogInterface.cancel();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else {
                        BarcodeEncoder codigo_qr = new BarcodeEncoder();
                        Bitmap bitmap = codigo_qr.encodeBitmap(txtcodigo.getText().toString(), BarcodeFormat.QR_CODE, 800, 800);
                        imgCodigoqr.setImageBitmap(bitmap);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        btnScannear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Lectura de codigo");
                    integrator.setCameraId(0);

                    integrator.setOrientationLocked(false);
                    integrator.setCaptureActivity(CapturaVertical.class);

                    integrator.setBeepEnabled(true);
                    integrator.setBarcodeImageEnabled(true);
                    integrator.initiateScan();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtcodigo.setText("");
                imgCodigoqr.setImageResource(com.google.android.material.R.drawable.abc_ic_search_api_material);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            }else {
                txtcodigo.setText(result.getContents());
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}