package com.sp.p2002640_cherish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShoppingDataInput extends AppCompatActivity {
    private EditText foodname_shopping, quantity;
    private ImageView foodimage_shopping;
    private RadioGroup food_types;
    private Button cancel_shopping, save_shopping;
    private Bitmap bitmap, resizedBitmap_shopping;
    private Drawable oldDrawable_shopping;
    private ShoppingHelper dbHelper_shopping;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri_shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_data_input);
        foodname_shopping = (EditText) findViewById(R.id.food_name_shopping);
        foodname_shopping.setFilters(new InputFilter[]{getEditTextFilter()});
        quantity = (EditText) findViewById(R.id.food_quantity);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
       quantity.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (quantity.getText().toString().matches("^0") )
                {
                    quantity.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        food_types = (RadioGroup) findViewById(R.id.food_types);
        foodimage_shopping = (ImageView) findViewById(R.id.image_view_shopping);
        oldDrawable_shopping = foodimage_shopping.getDrawable();
        save_shopping = (Button) findViewById(R.id.button_save_shopping);
        cancel_shopping = (Button) findViewById(R.id.button_cancel_shopping);
        dbHelper_shopping = new ShoppingHelper(this);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        foodimage_shopping.setOnClickListener(v -> {

            imagePickDialog();

        });

        cancel_shopping.setOnClickListener(v -> finish());

        save_shopping.setOnClickListener(onSave);

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
        imageUri_shopping = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri_shopping);
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
                CropImage.activity(imageUri_shopping)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                Uri resultUri = result.getUri();
                imageUri_shopping = resultUri;
                foodimage_shopping.setImageURI(resultUri);
                // bitmap value to store in sql
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    resizedBitmap_shopping = getResizedBitmap(bitmap, 650, 650);
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
        dbHelper_shopping.close();
        super.onDestroy();
    }

    View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get data
            if (foodimage_shopping.getDrawable() == oldDrawable_shopping)
                Toast.makeText(v.getContext(), "You haven't chosen an image yet", Toast.LENGTH_LONG).show();
            else {

                String nameStr_shopping = foodname_shopping.getText().toString();
                String quantityStr = quantity.getText().toString();
                String food_typesStr = "";

                switch (food_types.getCheckedRadioButtonId()) {
                    case R.id.dairy:
                        food_typesStr = "Dairy";
                        break;
                    case R.id.snacks:
                        food_typesStr = "Snacks";
                        break;
                    case R.id.fruits_vegetables:
                        food_typesStr = "Fruits & Vegetables";
                        break;
                    case R.id.meat_seafood:
                        food_typesStr = "Meat & Seafood";
                        break;
                    case R.id.others:
                        food_typesStr = "Others";
                        break;
                }

                if (nameStr_shopping.trim().length() == 0 || quantityStr.trim().length() == 0 || food_typesStr.trim().length() == 0)
                    Toast.makeText(v.getContext(), "Missing Inputs", Toast.LENGTH_LONG).show();

                else {
                    // convert bitmap to bytearray + vice versa

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    resizedBitmap_shopping.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] image = byteArrayOutputStream.toByteArray();

                    // send data to sql database
                    String timestamp = "" + System.currentTimeMillis();
                    long id = dbHelper_shopping.insertInfo(nameStr_shopping, quantityStr, food_typesStr, image,timestamp, timestamp);
                    Toast.makeText(ShoppingDataInput.this, "Added", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(v.getContext(), "Record added to id: " + id, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ShoppingDataInput.this, ShoppingInfoDisplay.class));
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

    private class fun {
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
        Intent intent=new Intent(ShoppingDataInput.this, ShoppingInfoDisplay.class);
        startActivity(intent);
    }
}