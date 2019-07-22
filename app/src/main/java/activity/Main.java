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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.core.ActCore;
import activity.news.ActNews;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.ConfigRestHeader;
import config.ConfigStaticValue;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.base.api.core.interfase.ICoreSite;
import ntk.base.api.core.interfase.ICoreUser;
import ntk.base.api.core.model.CoreGetAllRequest;
import ntk.base.api.core.model.CoreSiteGetAllWithAliasRequest;
import ntk.base.api.core.model.CoreSiteResponse;
import ntk.base.api.core.model.CoreUserResponse;
import ntk.base.api.core.model.CoreUserSelectCurrentSiteRequest;
import ntk.base.api.core.model.CoreUserloginRequest;
import ntk.base.api.utill.RetrofitManager;
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

    @BindView(R.id.checkboxRememberMe)
    CheckBox rememberMe;

    @BindView(R.id.spinnerSelectSite)
    Spinner spinnerSelectSite;

    @BindView(R.id.txtSiteToken)
    TextView txtSiteToken;

    private String[] apiNames = new String[]{
            "Core",
            "News"};
    private List<String> lagList = new ArrayList<String>();
    private List<String> siteList = new ArrayList<String>();
    private List<Long> siteIdList = new ArrayList<Long>();
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private int lagValue = 0;
    private int siteValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        setLayoutLogin();
    }

    private void setLayoutLogin() {
        rememberMe.setChecked(true);
        if (EasyPreference.with(Main.this).getBoolean("RememberMe", true)) {
            Username.setText(EasyPreference.with(this).getString("Username", ""));
            Password.setText(EasyPreference.with(this).getString("Password", ""));
        }
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
        EasyPreference.with(Main.this).addBoolean("RememberMe", rememberMe.isChecked());
        getData();
    }

    private void getData() {
        CoreUserloginRequest request = new CoreUserloginRequest();
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
        ICoreUser iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICoreUser.class);
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
                            EasyPreference.with(Main.this).addString("Username", request.username);
                            EasyPreference.with(Main.this).addString("Password", request.pwd);
                            EasyPreference.with(Main.this).addString("LoginCookie", response.UserTicketToken);
                            layoutLogin.setVisibility(View.GONE);
                            getSite();
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

    private void getSite() {
        CoreSiteGetAllWithAliasRequest request = new CoreSiteGetAllWithAliasRequest();
        RetrofitManager manager = new RetrofitManager(Main.this);
        ICoreSite iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICoreSite.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("Authorization", EasyPreference.with(Main.this).getString("LoginCookie", ""));
        Observable<CoreSiteResponse> call = iCore.GetAllWithAlias(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoreSiteResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CoreSiteResponse response) {
                        for (int i = 0; i < response.ListItems.size(); i++) {
                            siteList.add(response.ListItems.get(i).Title);
                            siteIdList.add(response.ListItems.get(i).Id);
                        }
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void init() {
        EasyPreference.with(this).addString("url", "oco.ir/api/");
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

        token.setText(EasyPreference.with(Main.this).getString("LoginCookie", ""));

        spinnerSelectSite.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, siteList));
        spinnerSelectSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                siteValue = position;
                getSiteToken();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSiteToken() {
        CoreUserSelectCurrentSiteRequest request = new CoreUserSelectCurrentSiteRequest();
//        request.id = siteIdList.get(siteValue);
        RetrofitManager manager = new RetrofitManager(Main.this);
        ICoreUser iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICoreUser.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("Authorization", EasyPreference.with(Main.this).getString("LoginCookie", ""));
        Observable<CoreUserResponse> call = iCore.SelectCurrentSite(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoreUserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CoreUserResponse response) {
                        EasyPreference.with(Main.this).addString("SiteCookie", response.UserTicketToken);
                        txtSiteToken.setText(response.UserTicketToken);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
                            startActivity(new Intent(context, ActNews.class));
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
}
