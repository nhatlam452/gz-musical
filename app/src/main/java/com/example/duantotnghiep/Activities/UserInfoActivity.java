package com.example.duantotnghiep.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Adapter.SpinnerAdapter;
import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity implements UserContract.View {
    private final static int MY_REQUEST_CODE = 1;
    private Button btnChangeInfo;
    private Uri uri;
    private String userId;
    private UserPresenter userPresenter;
    private final User user = LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo();
    private CircleImageView civAvt;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
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
                            btnChangeInfo.setVisibility(View.VISIBLE);
                        }
                    }

                }
            });

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
        userPresenter = new UserPresenter(this);
        String phoneNumber = user.getPhoneNumber();
        String email = user.getEmail();
        String salutation = user.getSalutation();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        userId = user.getUserId();
        Toast.makeText(this, "" + userId, Toast.LENGTH_SHORT).show();
        edtPhone.setText(phoneNumber);
        if (user.getAvt() != null) {
            Glide.with(this).load(user.getAvt()).into(civAvt);
        }
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

        edtSalutation.setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("Mr.");
            mList.add("Mrs.");
            mList.add("My Friend ");
            mList.add("Sir ");
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_spinner);
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
            RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);

            tvTitle.setText("YOUR SALUTATION");
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, mList, s -> {
                edtSalutation.setText(s);
                dialog.dismiss();
            });
            rcvSpinner.setLayoutManager(new LinearLayoutManager(this));
            rcvSpinner.setAdapter(spinnerAdapter);
            dialog.show();
        });

        civAvt.setOnClickListener(v -> {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        clickOpenGallery();
                        return;
                    }
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if (user.getAvt() == null) {
                            clickOpenGallery();
                        } else {
                            openAvtOptionDialog();
                        }
                    } else {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, MY_REQUEST_CODE);
                    }

                }

        );
        btnChangeInfo.setOnClickListener(v -> {
            AppUtil.showDialog.show(this);
            if (uri != null) {
                uploadtoFireBase(uri);
            }
            if (!edtEmail.getText().toString().equals(email)) {
//               userPresenter.onUpdateInfo("EMAIL",);
            }
            if (!edtSalutation.getText().toString().equals(salutation)) {
                userPresenter.onUpdateInfo("SALUTATION", edtSalutation.getText().toString(), userId);
            }
            if (!edtFirstName.getText().toString().equals(firstName)) {
                String name = edtFirstName.getText().toString();
                userPresenter.onUpdateInfo("FIRSTNAME", name, "6");

            }
            if (!edtLastName.getText().toString().equals(lastName)) {
                String name = edtLastName.getText().toString();

                userPresenter.onUpdateInfo("LASTNAME", name, userId);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clickOpenGallery();
                } else {
                    Toast.makeText(this, "Please access permission to open your galley", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
        tv2.setOnClickListener(v -> {
            Intent i = new Intent(this, ViewImageActivity.class);
            i.putExtra("ViewImage", user.getAvt());
            startActivity(i);
            avtDialog.dismiss();

        });
        tv1.setOnClickListener(v -> {
                    clickOpenGallery();
                    avtDialog.dismiss();
                }
        );
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

    private void uploadtoFireBase(Uri uri) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                userPresenter.onUpdateInfo("AVATAR", uri.toString(), userId);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AppUtil.showDialog.dismiss();
                                Toast.makeText(UserInfoActivity.this, e + "", Toast.LENGTH_SHORT).show();
                            }
                        })
                ;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("====>", e + "");

                Toast.makeText(UserInfoActivity.this, e + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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

    @Override
    public void onSuccess(User user) {
        AppUtil.showDialog.dismiss();
        Log.d("service", user.getPhoneNumber() + "asdfs");
        LocalStorage.getInstance(UserInfoActivity.this).getLocalStorageManager().setUserInfo(user);
        Toast.makeText(this, "Your Info have been updated", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }

    @Override
    public void onFail(String msg) {
        AppUtil.showDialog.dismiss();

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFail(Throwable t) {
        AppUtil.showDialog.dismiss();
        Log.d("User Info Actitvity", t.getMessage());

    }
}