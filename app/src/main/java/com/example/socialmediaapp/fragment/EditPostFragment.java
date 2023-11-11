package com.example.socialmediaapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.R;
import com.example.socialmediaapp.RealPathUtil;
import com.example.socialmediaapp.activity.MainActivity;
import com.example.socialmediaapp.adapter.ImagePagerAdapter;
import com.example.socialmediaapp.adapter.ImageSPagerAdapter;
import com.example.socialmediaapp.api.ApiService;
import com.example.socialmediaapp.dto.ConversationDTO;
import com.example.socialmediaapp.dto.ImgDTO;
import com.example.socialmediaapp.dto.NewFeedDTO;
import com.example.socialmediaapp.dto.PostImage;
import com.example.socialmediaapp.dto.ResponseDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostFragment extends Fragment {
    public static final String TAG = EditPostFragment.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    private static final int PERMISSION_REQUEST_CODE = 123;
    NewFeedDTO post;
    private MainActivity mMainActivity;
    private CircleImageView civUserAvatar;
    private TextView tvUserName;
    private EditText etCaption;
    private Button btEditPost;
    private ViewPager vpImgPost;
    private ImageView ivAddImg;
    private ImageView ivCloseEditPost;
    ImageSPagerAdapter adapter;
    private ProgressDialog progressDialog;
    List<ImgDTO> imageUris = new ArrayList<>();
    List<String> arrID =new ArrayList<>();
    public EditPostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        cnView(view);
        setEvent();
        return view;
    }
    private void cnView(View view) {
        mMainActivity=(MainActivity)getActivity();
        civUserAvatar = (CircleImageView) view.findViewById(R.id.civUserAvatar);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        etCaption = (EditText) view.findViewById(R.id.etCaption);
        vpImgPost = (ViewPager) view.findViewById(R.id.vpImgPost);
        ivAddImg=(ImageView) view.findViewById(R.id.ivAddImg);
        ivCloseEditPost = (ImageView) view.findViewById(R.id.ivCloseEditPost);
        btEditPost = (Button) view.findViewById(R.id.btEditPost);
    }

    private void setEvent() {
        progressDialog=new ProgressDialog(this.getContext());
        progressDialog.setMessage("Đang đăng ...");
        Bundle bundleReceive = getArguments();
        post = (NewFeedDTO) bundleReceive.get("editpost");
        Glide.with(getContext()).load(post.getUSER().getAVATAR()).into(civUserAvatar);
        tvUserName.setText(post.getUSER().getFULLNAME());
        etCaption.setText(post.getCAPTION());
        for(int i=0;i<post.getPOST_IMAGEs().size();i++){
            imageUris.add(new ImgDTO(post.getPOST_IMAGEs().get(i)));
        }
        ivAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
        adapter = new ImageSPagerAdapter(this.getContext(), imageUris);
        vpImgPost.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new ImageSPagerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                Log.d(TAG, "Position: "+position);
                showDeleteConfirmationDialog(position);
            }
        });
        btEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiEditPost();
            }
        });
        ivCloseEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void callApiEditPost() {
        progressDialog.show();
        String caption=etCaption.getText().toString().trim();
        RequestBody requestBodyCaption=RequestBody.create(MediaType.parse("multipart/form-data"),caption);
        RequestBody requestBodyArrDelete=RequestBody.create(MediaType.parse("multipart/form-data"),arrID.toString());
//        String strRealPath=RealPathUtil.getRealPath(this.getContext(),imageUris.get(0));
//        File file=new File(strRealPath);
//        RequestBody requestBodyFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
//        MultipartBody.Part multipartBodyFile=MultipartBody.Part.createFormData("file",file.getName(),requestBodyFile);

        List<MultipartBody.Part> multipartBodyFiles = new ArrayList<>();
        for (ImgDTO imageUri : imageUris) {
            if (imageUri.getImageUri() != null) {
                String strRealPath = RealPathUtil.getRealPath(this.getContext(), imageUri.getImageUri());
                File file = new File(strRealPath);
                RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part multipartBodyFile = MultipartBody.Part.createFormData("file", file.getName(), requestBodyFile);
                Log.d("Lik Img", ""+multipartBodyFile);
                multipartBodyFiles.add(multipartBodyFile);
            }

        }

        ApiService.apiService.postEdit(post.getID(),requestBodyCaption,requestBodyArrDelete,multipartBodyFiles, MainActivity.accessToken).enqueue(new Callback<ResponseDTO>() {
            @Override
            public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                progressDialog.dismiss();
                ResponseDTO message=response.body();
                Toast.makeText(getActivity(),"Chỉnh sửa thành công",Toast.LENGTH_LONG).show();
                ProfileFragment.loadPost(getContext());
                HomeFragment.loadPost(getContext());
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<ResponseDTO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Chỉnh sửa thất bại"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, MY_REQUEST_CODE);
        }
    }
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn xóa mục này không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (imageUris.get(position).getImageUrl() != null) {
                            arrID.add(imageUris.get(position).getImageUrl().getID());
                        }
                        imageUris.remove(position);
                        vpImgPost.setAdapter(adapter);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageUris.add(new ImgDTO(selectedImageUri));
            adapter.notifyDataSetChanged();
        }
    }

}