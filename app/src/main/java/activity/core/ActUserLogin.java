package activity.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ntk.base.api.core.interfase.ICoreUser;
import ntk.base.api.core.model.CoreUserResponse;
import ntk.base.api.core.model.CoreUserloginRequest;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;

public class ActUserLogin extends AppCompatActivity {

    @BindView(R.id.txtUsernameActUserLogin)
    EditText Username;
    @BindView(R.id.txtPasswordActUserLogin)
    EditText Password;
    @BindView(R.id.spinnerLagActUserLogin)
    Spinner Lag;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private List<String> lagList = new ArrayList<String>();
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private int lagValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_core_user_login);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CoreUserLogin");
    }

    @OnClick(R.id.api_test_submit_button)
    public void onSubmitClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
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
        RetrofitManager manager = new RetrofitManager(ActUserLogin.this);
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
                        JsonDialog cdd = new JsonDialog(ActUserLogin.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActUserLogin.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
