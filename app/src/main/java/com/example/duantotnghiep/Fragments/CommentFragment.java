package com.example.duantotnghiep.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Activities.UserInfoActivity;
import com.example.duantotnghiep.Adapter.CommentAdapter;
import com.example.duantotnghiep.Contract.CommentContact;
import com.example.duantotnghiep.Model.Comment;
import com.example.duantotnghiep.Presenter.CommentPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentFragment extends Fragment implements CommentContact.View {
    private CommentPresenter commentPresenter;
    private RecyclerView rcvComment;
    private Uri uri;
    private EditText edtComment;
    private ProductDetailActivity productDetailActivity;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private ImageView imgCommentPicture;
    private CircleImageView imgRemove;
    private CoordinatorLayout coordinatorLayout;
    private Bitmap bitmap;
    private SharedPreferences.Editor mEditor;
    private final ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            bitmap = (Bitmap) data.getExtras().get("data");
                            imgCommentPicture.setImageBitmap(bitmap);
                            if (coordinatorLayout.getVisibility() == View.GONE) {
                                coordinatorLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
    );
    private final ActivityResultLauncher<Intent> activityResultLauncherGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            uri = data.getData();
                            imgCommentPicture.setImageURI(uri);
                            if (coordinatorLayout.getVisibility() == View.GONE) {
                                coordinatorLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvComment = view.findViewById(R.id.rcvComment);
        imgCommentPicture = view.findViewById(R.id.imgCommentPicture);
        edtComment = view.findViewById(R.id.edtContent);
        coordinatorLayout = view.findViewById(R.id.layoutPictureComment);
        imgRemove = view.findViewById(R.id.imgRemovePicture);
        productDetailActivity = (ProductDetailActivity) getActivity();

        commentPresenter = new CommentPresenter(this);
        AppUtil.showDialog.show(getContext());
        SharedPreferences mSharePrefer = getContext().getSharedPreferences(String.valueOf(R.string.REMEMBER_LOGIN), 0);
        boolean isCameraPermissionGranted = mSharePrefer.getBoolean(String.valueOf(R.string.isCameraPermissionRequest), false);
        boolean isReadPermissionGranted = mSharePrefer.getBoolean(String.valueOf(R.string.isWritePermissionRequest), false);
        mEditor = mSharePrefer.edit();
        commentPresenter.onGetComment(productDetailActivity.getProductId());
        if (uri != null || bitmap != null) {
            imgCommentPicture.setVisibility(View.VISIBLE);
        }
        view.findViewById(R.id.imgSelectPicture).setOnClickListener(v -> {
            if (isCameraPermissionGranted && isCameraPermissionGranted) {
                openDialogPicture();
            }else {
                checkMyCameraPermission();
                checkReadPermission();
            }

        });
        view.findViewById(R.id.imgRemovePicture).setOnClickListener(v -> {
            uri = null;
            bitmap = null;
            coordinatorLayout.setVisibility(View.GONE);
        });
        view.findViewById(R.id.imgSendCmt).setOnClickListener(v -> {
            AppUtil.showDialog.show(getContext());
            if (bitmap != null || uri != null) {
                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
                    String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);
                    uploadtoFireBase(Uri.parse(path));
                } else {

                    uploadtoFireBase(uri);
                }
            } else {

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String time = format.format(date);
                Comment comment = new Comment(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()),
                        productDetailActivity.getProductId(), edtComment.getText().toString().trim(), time, "");
                commentPresenter.onAddComment(comment);

            }
        });
    }

    private void checkMyCameraPermission() {
        Dexter.withContext(getContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                mEditor.putBoolean(getString(R.string.isCameraPermissionRequest), true);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getContext(), "Please Granted Permission in your Setting", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void checkReadPermission() {
        Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                mEditor.putBoolean(getString(R.string.isWritePermissionRequest), true);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getContext(), "Please Granted Permission in your Setting", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void uploadtoFireBase(Uri uri) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri1 ->
                        {
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = new Date();
                            String time = format.format(date);
                            Comment comment = new Comment(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()),
                                    productDetailActivity.getProductId(), edtComment.getText().toString().trim(), time, uri1.toString());
                            commentPresenter.onAddComment(comment);
                        }
                )
                .addOnFailureListener(e -> {
                    AppUtil.showDialog.dismiss();
                    Toast.makeText(getContext(), e + "", Toast.LENGTH_SHORT).show();
                })).addOnFailureListener(e -> {
            Log.d("====>", e + "");

            Toast.makeText(getContext(), e + "", Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openDialogPicture() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chooose_picture);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        ImageView imgCamera = window.findViewById(R.id.imgOpenCamera);
        ImageView imgGallery = window.findViewById(R.id.imgOpenGallery);

        imgCamera.setOnClickListener(v1 -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncherCamera.launch(cameraIntent);
            dialog.dismiss();
        });
        imgGallery.setOnClickListener(v1 -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            activityResultLauncherGallery.launch(i);
            dialog.dismiss();

        });
        dialog.show();
    }


    @Override
    public void onGetCmtSuccess(List<Comment> userAddressList) {
        coordinatorLayout.setVisibility(View.GONE);
        bitmap = null;
        uri = null;
        edtComment.setText("");
        if (userAddressList == null) {
            commentPresenter.onGetComment(productDetailActivity.getProductId());
        }
        CommentAdapter adapter = new CommentAdapter(getContext(), userAddressList);
        rcvComment.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvComment.setAdapter(adapter);
        AppUtil.showDialog.dismiss();

    }

    @Override
    public void onGetCmtFailure(String msg) {
        Toast.makeText(productDetailActivity, msg, Toast.LENGTH_SHORT).show();
        AppUtil.showDialog.dismiss();

    }

    @Override
    public void onGetCmtResponseFail(Throwable t) {
        Toast.makeText(productDetailActivity, "Unknown Error . Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("Comment Fragment", t.getMessage());
        AppUtil.showDialog.dismiss();

    }


}