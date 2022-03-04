package com.sp.p2002640_cherish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StorageDataInput extends AppCompatActivity {
    private EditText foodname, foodprice, foodweight;
    private ImageView foodimage;
    private Button cancel, save;
    private Bitmap bitmap, resizedBitmap;
    private Drawable oldDrawable;
    private StorageHelper dbHelper;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;
    TextView foodexpiry;
    String btExpiryStr, btTodayStr;
    Button btToday, btExpiry, bTCalculate;
    DatePickerDialog.OnDateSetListener dateSetListener1,dateSetListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Objects.requireNonNull(getSupportActionBar()).hide();     // hide action bar

        setContentView(R.layout.activity_storage_data_input);
        foodname = (EditText) findViewById(R.id.food_name);
        foodname.setFilters(new InputFilter[]{getEditTextFilter()});

        foodexpiry = (TextView) findViewById(R.id.food_expiry);
        foodprice = (EditText) findViewById(R.id.food_price);
        foodprice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        foodprice.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void afterTextChanged(Editable arg0) {
                String str = foodprice.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 3, 2);

                if (!str2.equals(str)) {
                    foodprice.setText(str2);
                    foodprice.setSelection(str2.length());
                }
            }

            public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
                if(str.charAt(0) == '.') str = "0"+str;
                int max = str.length();

                String rFinal = "";
                boolean after = false;
                int i = 0, up = 0, decimal = 0; char t;
                while(i < max){
                    t = str.charAt(i);
                    if(t != '.' && after == false){
                        up++;
                        if(up > MAX_BEFORE_POINT) return rFinal;
                    }else if(t == '.'){
                        after = true;
                    }else{
                        decimal++;
                        if(decimal > MAX_DECIMAL)
                            return rFinal;
                    }
                    rFinal = rFinal + t;
                    i++;
                }return rFinal;
            }
        });
        foodweight = (EditText) findViewById(R.id.food_weight);
        foodweight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        foodweight.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void afterTextChanged(Editable arg0) {
                String str = foodweight.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 3, 2);

                if (!str2.equals(str)) {
                    foodweight.setText(str2);
                    foodweight.setSelection(str2.length());
                }
            }

            public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
                if(str.charAt(0) == '.') str = "0"+str;
                int max = str.length();

                String rFinal = "";
                boolean after = false;
                int i = 0, up = 0, decimal = 0; char t;
                while(i < max){
                    t = str.charAt(i);
                    if(t != '.' && after == false){
                        up++;
                        if(up > MAX_BEFORE_POINT) return rFinal;
                    }else if(t == '.'){
                        after = true;
                    }else{
                        decimal++;
                        if(decimal > MAX_DECIMAL)
                            return rFinal;
                    }
                    rFinal = rFinal + t;
                    i++;
                }return rFinal;
            }
        });
        foodimage = (ImageView) findViewById(R.id.image_view);
        oldDrawable = foodimage.getDrawable();
        save = (Button) findViewById(R.id.button_save);
        cancel = (Button) findViewById(R.id.button_cancel);
        btToday=findViewById(R.id.bt_today);
        btExpiry=findViewById(R.id.bt_expiry);
        bTCalculate=findViewById(R.id.bt_calculate);
        dbHelper = new StorageHelper(this);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Calendar calendar = Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        if (ContextCompat.checkSelfPermission(StorageDataInput.this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(StorageDataInput.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        btExpiry.setText(date);
        btToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        StorageDataInput.this
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , dateSetListener1, year, month, day

                );
                datePickerDialog.getWindow().setBackgroundDrawable(new
                        ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();


            }
        });

        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                btToday.setText(date);
                btTodayStr = btToday.getText().toString();
            }
        };

        btExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        StorageDataInput.this
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , dateSetListener2, year, month, day

                );
                datePickerDialog.getWindow().setBackgroundDrawable(new
                        ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                btExpiry.setText(date);
                btExpiryStr = btExpiry.getText().toString();
            }
        };

        bTCalculate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String sDate = btToday.getText().toString();
                String eDate = btExpiry.getText().toString();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yy");
                try {
                    Date date1 = simpleDateFormat1.parse(sDate);
                    Date date2 = simpleDateFormat1.parse(eDate);

                    long startDate = date1.getTime();
                    long endDate = date2.getTime();

                    if (startDate <= endDate) {
                        Period period = new Period(startDate, endDate, PeriodType.yearMonthDay());
                        int years = period.getYears();
                        int months = period.getMonths();
                        int days = period.getDays();

                        foodexpiry.setText(years + " Years| " + months + " Months | " + days + " Days");
                    } else {
                        Toast.makeText(getApplicationContext()
                                , "x."
                                , Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        foodimage.setOnClickListener(v -> {

            imagePickDialog();

        });

        cancel.setOnClickListener(v -> finish());

        save.setOnClickListener(onSave);

    }
    public static InputFilter getEditTextFilter() {
        return new InputFilter.LengthFilter(30) {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                Matcher ms = ps.matcher(String.valueOf(c));
                return ms.matches();
            }
        };
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void imagePickDialog() {

        //Additional Feature : Allow user to pick from either camera or gallery
        String[] options = {"Camera", "Gallery"};
        //                     0          1

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { //if which == 0, camera mode
                    if (!checkCameraPermission())
                        requestCameraPermission();
                    else
                        pickFromCamera();
                } else if (which == 1) { //if which == 1, gallery mode
                    if (!checkStoragePermission())
                        requestStoragePermission();
                    else
                        pickFromStorage();
                }
            }
        });
        builder.create().show();
    }

    private void pickFromCamera() {
        //get image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromStorage() {
        //get image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //check which code is being requested
        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted)
                        pickFromCamera();
                    else
                        Toast.makeText(this, "Camera permission required!", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted)
                        pickFromStorage();
                    else
                        Toast.makeText(this, "Storage permission required!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //added image crop library
        //Source : https://github.com/ArthurHub/Android-Image-Cropper

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE)
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            else if (requestCode == IMAGE_PICK_CAMERA_CODE)
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                Uri resultUri = result.getUri();
                imageUri = resultUri;
                foodimage.setImageURI(resultUri);
                // bitmap value to store in sql
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    resizedBitmap = getResizedBitmap(bitmap, 650, 650);
                    bitmap.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get data
            if (foodimage.getDrawable() == oldDrawable)
                Toast.makeText(v.getContext(), "You haven't chosen an image yet", Toast.LENGTH_LONG).show();
            else {

                String nameStr = foodname.getText().toString();
                String priceStr = foodprice.getText().toString();
                String weightStr = foodweight.getText().toString();
                String dateStr = btExpiry.getText().toString();

                if (nameStr.trim().length() == 0 || weightStr.trim().length() == 0 || priceStr.trim().length() == 0 || dateStr.trim().length() == 0)
                    Toast.makeText(v.getContext(), "Missing Inputs", Toast.LENGTH_LONG).show();
                else {
                    // convert bitmap to bytearray + vice versa

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] image = byteArrayOutputStream.toByteArray();

                    // send data to sql database
                    String timestamp = "" + System.currentTimeMillis();
                    long id = dbHelper.insertInfo(nameStr, dateStr, weightStr, priceStr, image,timestamp, timestamp);
                    Toast.makeText(StorageDataInput.this, "Added", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(v.getContext(), "Record added to id: " + id, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(StorageDataInput.this, StorageInfoDisplay.class));
                    finish();
                }
            }
        }
    };

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.about):
                setContentView(R.layout.activity_about1);
                break;
            case (R.id.contact):
                setContentView(R.layout.activity_contact1);
                TextView email = (TextView) findViewById(R.id.email);
                email.setText(Html.fromHtml("<a href=\"mailto:ZHIKAI.20@ichat.sp.edu.sg\">ZHIKAI.20@ichat.sp.edu.sg</a>"));
                email.setMovementMethod(LinkMovementMethod.getInstance());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(StorageDataInput.this, StorageInfoDisplay.class);
        startActivity(intent);
    }

    }




