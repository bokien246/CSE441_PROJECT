package com.ktpm1.restaurant.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.activities.ThankyouActivity;

public class WebFragment extends Fragment {
    private static final String URL_KEY = "url_key";
    private static final String VNP_RETURN_URL = "/vnpay-payment"; // Đường dẫn callback từ VNPay
    private WebView webView;
    private Toolbar toolbar;

    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        webView = view.findViewById(R.id.webview);
        toolbar = view.findViewById(R.id.toolbar_web);

        // Cấu hình WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Bật JavaScript nếu trang yêu cầu

        // Thiết lập WebViewClient để xử lý URL
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(VNP_RETURN_URL)) {
                    Uri uri = Uri.parse(url);
                    String transactionStatus = uri.getQueryParameter("vnp_TransactionStatus");
                    String amount = uri.getQueryParameter("vnp_Amount");

                    handlePaymentResult(transactionStatus, amount);
                    return true;
                }
                return false;
            }
        });

        String url = getArguments() != null ? getArguments().getString(URL_KEY) : null;
        if (url != null) {
            webView.loadUrl(url);
        }

        // Thiết lập Toolbar làm ActionBar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
                activity.getSupportActionBar().setTitle("Thanh toán đơn hàng");
            }
        }

        // Cho phép Fragment lắng nghe sự kiện MenuItem
        setHasOptionsMenu(true);

        // Xử lý sự kiện nút back trong Fragment
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack(); // Quay lại trang trước trong WebView
                } else {
                    requireActivity().onBackPressed(); // Quay lại Fragment trước đó
                }
            }
        });
    }

    // Xử lý kết quả thanh toán từ VNPay
    private void handlePaymentResult(String transactionStatus, String amount) {
        if ("00".equals(transactionStatus)) {
            // Hiển thị ThankYouFragment thay vì Dialog
            showThankYouFragment();
        } else {
            Toast.makeText(getContext(), "Thanh toán thất bại.", Toast.LENGTH_LONG).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    // Hiển thị Fragment ThankYou giống như modal
    private void showThankYouFragment() {
//        ThankYouFragment thankYouFragment = new ThankYouFragment();
//
//        // Hiển thị Fragment ở dạng fullscreen modal
//        requireActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)  // Hiệu ứng mờ
//                .add(android.R.id.content, thankYouFragment)
//                .addToBackStack(null)
//                .commit();

        Intent intent = new Intent(requireActivity(), ThankyouActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                requireActivity().onBackPressed(); // Quay lại Fragment trước đó
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            webView.destroy();
        }
    }
}