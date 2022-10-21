package com.example.duantotnghiep.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;




import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {
    private Button btnChangeInfo;
    private Uri uri;
    private CircleImageView civAvt;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            uri = data.getData();
                            civAvt.setImageURI(uri);
                        }
                    }

                }
            });

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        civAvt = findViewById(R.id.imgChangeAvt);
        EditText edtPhone = findViewById(R.id.edtPhoneNumberInfo);
        EditText edtEmail = findViewById(R.id.edtEmailInfo);
        EditText edtSalutation = findViewById(R.id.edtSalutationInfo);
        EditText edtFirstName = findViewById(R.id.edtFirstNameInfo);
        EditText edtLastName = findViewById(R.id.edtLastNameInfo);
        EditText edtBirthday = findViewById(R.id.edtDOBInfo);
        ImageView imgBackInfo = findViewById(R.id.imgBackInfo);
        btnChangeInfo = findViewById(R.id.btnUpdateUser);
        User user = AppUtil.getUserInfo(this);
        String phoneNumber = user.getPhoneNumber();
        String email = user.getEmail();
        String salutation = user.getSalutation();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        edtPhone.setText(phoneNumber);
        if (email != null) {
            edtEmail.setText(email);
        }
        edtSalutation.setText(salutation);
        edtFirstName.setText(firstName);
        edtLastName.setText(lastName);
        edtBirthday.setText(user.getDob());
        edtPhone.setOnClickListener(v -> showToast("Your Phone Number"));
        edtBirthday.setOnClickListener(v -> showToast("Your Date Of Birth"));
        imgBackInfo.setOnClickListener(v -> onBackPressed());
        onTextChange(edtEmail, email);
        onTextChange(edtFirstName, firstName);
        onTextChange(edtLastName, lastName);
        onTextChange(edtSalutation, salutation);

        civAvt.setOnClickListener(v -> {
            if (!checkCameraPermission()){
                requestCameraPermission();
            }else {
                if (user.getAvt() == null) {
                    clickOpenGallery();
                } else {
                    openAvtOptionDialog();
                }
            }

        });


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission(){
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(){
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }
    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }
    private boolean checkCameraPermission(){
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }
    private void openAvtOptionDialog() {
        Dialog avtDialog = new Dialog(this);
        avtDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        avtDialog.setContentView(R.layout.contact_dialog);
        Window window = avtDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        TextView tv1 = window.findViewById(R.id.tvHotline);
        TextView tv2 = window.findViewById(R.id.tvEmailUS);
        TextView tvTitle = window.findViewById(R.id.tvTitle);
        CircleImageView civ1 = window.findViewById(R.id.imgHotline);
        CircleImageView civ2 = window.findViewById(R.id.imgEmail);

        civ1.setImageResource(R.drawable.ic_baseline_image_search_24);
        civ2.setImageResource(R.drawable.ic_baseline_person_pin_24);
        tv1.setText("Change your Avatar");
        tvTitle.setText("Choose your Options");
        tv2.setText("View your Avatar");
        tv1.setOnClickListener(v -> clickOpenGallery());
        avtDialog.show();
    }

    private void onTextChange(EditText edt, String oldInfo) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edt.getText().toString().equals(oldInfo)) {
                    btnChangeInfo.setVisibility(View.GONE);
                } else {
                    btnChangeInfo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg + " can not be changed", Toast.LENGTH_SHORT).show();
    }

    private void clickOpenGallery() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        activityResultLauncher.launch(i);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }
}