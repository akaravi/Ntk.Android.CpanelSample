package activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.application.ActApplication;
import activity.article.ActArticle;
import activity.biography.ActBiography;
import activity.blog.ActBlog;
import activity.chart.ActChart;
import activity.core.ActCore;
import activity.core.ActUserLogin;
import activity.estate.ActEstate;
import activity.file.ActFile;
import activity.imageGallery.ActImageGallery;
import activity.member.ActMember;
import activity.movieGallery.ActMovieGallery;
import activity.musicGallery.ActMusicGallery;
import activity.news.ActNews;
import activity.pooling.ActPooling;
import activity.product.ActProduct;
import activity.service.ActService;
import activity.shop.ActShop;
import activity.ticketing.ActTicket;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.ConfigRestHeader;
import config.ConfigStaticValue;
import dialog.JsonDialog;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.base.api.core.interfase.ICore;
import ntk.base.api.core.model.CoreMain;
import ntk.base.api.core.model.CoreUserLoginRequest;
import ntk.base.api.core.model.CoreUserResponse;
import ntk.base.api.core.model.MainCoreResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.ActMain;
import ntk.base.app.BuildConfig;
import ntk.base.app.R;
import utill.AppUtill;
import utill.EasyPreference;
import utill.FontManager;

public class Main extends AppCompatActivity {


    @BindView(R.id.main_recycler_view)
    RecyclerView mainRecyclerView;

    @BindView(R.id.txtUrl)
    EditText url;

    @BindView(R.id.txtToken)
    TextView token;

    @BindView(R.id.txtUsername)
    EditText Username;

    @BindView(R.id.txtPassword)
    EditText Password;

    @BindView(R.id.spinnerLag)
    Spinner Lag;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.layoutLogin)
    RelativeLayout layoutLogin;

    private String[] apiNames = new String[]{
            "Core",
            "Article",
            "News",
            "Pooling",
            "Ticketing",
            "Biography",
            "Application",
            "Estate",
            "File",
            "Blog",
            "Image Gallery ",
            "Move Gallery",
            "Music Gallery",
            "Product",
            "Service",
            "Shop",
            "Chart",
            "Member",
            "Quote",
            "Link Management",
            "Reservation",
            "Bank payment",
            "Job",
            "Advertisement",
            "Vehicle",
            "Object"};
    private List<String> lagList = new ArrayList<String>();
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private int lagValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        setLayoutLogin();
    }

    private void setLayoutLogin() {
        lagList.add(0, "فارسی");
        lagList.add(1, "English");
        lagList.add(2, "German");
        lagList.add(3, "عربی");
        Lag.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lagList));
        Lag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lagValue = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick() {
        btnLogin.setVisibility(View.INVISIBLE);
        getData();
    }

    private void getData() {
        CoreUserLoginRequest request = new CoreUserLoginRequest();
        if (!Username.getText().toString().matches("")) {
            request.username = Username.getText().toString();
        }
        if (!Password.getText().toString().matches("")) {
            request.pwd = Password.getText().toString();
        }
        switch (lagValue) {
            case 0:
                request.lang = "fa_IR";
                break;
            case 1:
                request.lang = "en_US";
                break;
            case 2:
                request.lang = "el_GR";
                break;
            case 3:
                request.lang = "ar_AE";
                break;
        }
        RetrofitManager manager = new RetrofitManager(Main.this);
        ICore iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICore.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        Observable<CoreUserResponse> call = iCore.UserLogin(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoreUserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CoreUserResponse response) {
                        if (response.IsSuccess) {
                            EasyPreference.with(Main.this).addString("Cookie", response.UserTicketToken);
                            layoutLogin.setVisibility(View.GONE);
                            init();
                        } else {
                            btnLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(Main.this, response.ErrorMessage, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        btnLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void init() {
        EasyPreference.with(this).addString("url", "oco.ir/api/app");
        EasyPreference.with(this).addString("packageName", "ntk.cms.android.academy.app10");
        mainRecyclerView.setLayoutManager(new GridLayoutManager(Main.this, 2));
        mainRecyclerView.setAdapter(new MainRecyclerViewAdapter(this, apiNames));
        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EasyPreference.with(Main.this).addString("url", s.toString());
            }
        });

        token.setText(EasyPreference.with(Main.this).getString("Cookie", ""));
    }

    public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder> {
        private Context context;
        private String[] list;

        MainRecyclerViewAdapter(Context context, String[] list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, int i) {
            mainViewHolder.button.setText(list[i]);
            mainViewHolder.button.setId(i);
            mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case 0:
                            startActivity(new Intent(context, ActCore.class));
                            break;
                        case 1:
                            startActivity(new Intent(context, ActArticle.class));
                            break;
                        case 2:
                            startActivity(new Intent(context, ActNews.class));
                            break;
                        case 3:
                            startActivity(new Intent(context, ActPooling.class));
                            break;
                        case 4:
                            startActivity(new Intent(context, ActTicket.class));
                            break;
                        case 5:
                            startActivity(new Intent(context, ActBiography.class));
                            break;
                        case 6:
                            startActivity(new Intent(context, ActApplication.class));
                            break;
                        case 7:
                            startActivity(new Intent(context, ActEstate.class));
                            break;
                        case 8:
                            startActivity(new Intent(context, ActFile.class));
                            break;
                        case 9:
                            startActivity(new Intent(context, ActBlog.class));
                            break;
                        case 10:
                            startActivity(new Intent(context, ActImageGallery.class));
                            break;
                        case 11:
                            startActivity(new Intent(context, ActMovieGallery.class));
                            break;
                        case 12:
                            startActivity(new Intent(context, ActMusicGallery.class));
                            break;
                        case 13:
                            startActivity(new Intent(context, ActProduct.class));
                            break;
                        case 14:
                            startActivity(new Intent(context, ActService.class));
                            break;
                        case 15:
                            startActivity(new Intent(context, ActShop.class));
                            break;
                        case 16:
                            startActivity(new Intent(context, ActChart.class));
                            break;
                        case 17:
                            startActivity(new Intent(context, ActMember.class));
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        class MainViewHolder extends RecyclerView.ViewHolder {
            private Button button;

            MainViewHolder(@NonNull View itemView) {
                super(itemView);
                button = itemView.findViewById(R.id.button_item);
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (EasyPreference.with(this).getString("configapp", "").isEmpty()) {
            HandelData();
        }
    }

    private void HandelData() {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager manager = new RetrofitManager(this);
            ICore iCore = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(ICore.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
            Observable<MainCoreResponse> observable = iCore.GetResponseMain(headers);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MainCoreResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MainCoreResponse mainCoreResponse) {
                            EasyPreference.with(Main.this).addString("configapp", new Gson().toJson(mainCoreResponse.Item));
                            CheckUpdate();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            CheckUpdate();
        }
    }

    private void CheckUpdate() {
        String st = EasyPreference.with(this).getString("configapp", "");
        CoreMain mcr = new Gson().fromJson(st, CoreMain.class);
        if (mcr.AppVersion > BuildConfig.VERSION_CODE && BuildConfig.APPLICATION_ID.indexOf(".APPNTK") < 0) {
            if (mcr.AppForceUpdate) {
                UpdateFore();
            } else {
                Update();
            }
        }
    }

    private void Update() {
        String st = EasyPreference.with(this).getString("configapp", "");
        CoreMain mcr = new Gson().fromJson(st, CoreMain.class);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_permission);
        ((TextView) dialog.findViewById(R.id.lbl1PernissionDialog)).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        ((TextView) dialog.findViewById(R.id.lbl1PernissionDialog)).setText("توجه");
        ((TextView) dialog.findViewById(R.id.lbl2PernissionDialog)).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        ((TextView) dialog.findViewById(R.id.lbl2PernissionDialog)).setText("نسخه جدید اپلیکیشن اومده دوست داری آبدیت بشه؟؟");
        Button Ok = (Button) dialog.findViewById(R.id.btnOkPermissionDialog);
        Ok.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Ok.setOnClickListener(view1 -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mcr.AppUrl));
            startActivity(i);
            dialog.dismiss();
        });
        Button Cancel = (Button) dialog.findViewById(R.id.btnCancelPermissionDialog);
        Cancel.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Cancel.setOnClickListener(view12 -> dialog.dismiss());
        dialog.show();
    }

    private void UpdateFore() {
        String st = EasyPreference.with(this).getString("configapp", "");
        CoreMain mcr = new Gson().fromJson(st, CoreMain.class);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_update);
        ((TextView) dialog.findViewById(R.id.lbl1PernissionDialogUpdate)).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        ((TextView) dialog.findViewById(R.id.lbl1PernissionDialogUpdate)).setText("توجه");
        ((TextView) dialog.findViewById(R.id.lbl2PernissionDialogUpdate)).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        ((TextView) dialog.findViewById(R.id.lbl2PernissionDialogUpdate)).setText("نسخه جدید اپلیکیشن اومده حتما باید آبدیت بشه");
        Button Ok = (Button) dialog.findViewById(R.id.btnOkPermissionDialogUpdate);
        Ok.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Ok.setOnClickListener(view1 -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mcr.AppUrl));
            startActivity(i);
            dialog.dismiss();
        });
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    finish();
            }
            return true;
        });
        dialog.show();
    }
}
