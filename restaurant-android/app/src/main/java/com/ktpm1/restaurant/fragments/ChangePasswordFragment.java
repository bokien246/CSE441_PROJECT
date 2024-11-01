package com.ktpm1.restaurant.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.apis.AuthApi;
import com.ktpm1.restaurant.configs.ApiClient;
import com.ktpm1.restaurant.dtos.requests.ChangePasswordRequest;
import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
import com.ktpm1.restaurant.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private EditText editTextCurrentPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button buttonChangePassword;
    private TextView textViewMessage;
    private Button buttonBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_changepassword, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextCurrentPassword = view.findViewById(R.id.txt_OldPassword);
        editTextNewPassword = view.findViewById(R.id.txt_NewPassword);
        editTextConfirmPassword = view.findViewById(R.id.txt_ConfirmPassword);
        buttonChangePassword = view.findViewById(R.id.btn_Save);
        textViewMessage = view.findViewById(R.id.textViewMessage);
        buttonBack = view.findViewById(R.id.btn_Back);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        buttonBack.setOnClickListener(view1 -> {
            ProfileFragment profileFragment = new ProfileFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, profileFragment) // Change 'fragment_container' to your actual container ID
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void changePassword() {
        String currentPassword = editTextCurrentPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            textViewMessage.setText("Các ô không được để trống.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            textViewMessage.setText("Mật khẩu nhập lại không trùng, vui lòng nhập lại.");
            return;
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);
        Call<ResponseMessage> call = authApi.changePassword("Bearer " + token,
                new ChangePasswordRequest(currentPassword, newPassword, confirmPassword));
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    // Password successfully updated
                    ResponseMessage responseMessage = response.body();
                    if (responseMessage.getStatus() == 200) {
                        textViewMessage.setText("Đổi mật khẩu thành công.");
                        clearFields();
                    } else {
                        textViewMessage.setText(responseMessage.getMessage());
                    }

                    buttonBack.setOnClickListener(view -> {
                        ProfileFragment profileFragment = new ProfileFragment();
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, profileFragment)
                                .addToBackStack(null)
                                .commit();
                    });
                } else {
                    textViewMessage.setText("Không đổi được mật khẩu.");
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable throwable) {
                textViewMessage.setText("Error: " + throwable.getMessage());
            }
        });
    }

    private void clearFields() {
        editTextCurrentPassword.setText("");
        editTextNewPassword.setText("");
        editTextConfirmPassword.setText("");
    }
}
