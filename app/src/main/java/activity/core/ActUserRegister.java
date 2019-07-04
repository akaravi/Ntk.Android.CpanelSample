package activity.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
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
import ntk.base.api.core.interfase.ICore;
import ntk.base.api.core.model.CoreUserRegisterRequest;
import ntk.base.api.core.model.CoreUserResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActUserRegister extends AppCompatActivity {

    @BindView(R.id.lblLayout)
    TextView lblLayout;
    @BindView(R.id.txtUsernameActRegister)
    EditText Username;
    @BindView(R.id.txtPasswordActRegister)
    EditText Password;
    @BindView(R.id.txtNameActRegister)
    EditText Name;
    @BindView(R.id.txtPhoneActRegister)
    EditText Phone;
    @BindView(R.id.txtRePasswordActRegister)
    EditText RePassword;
    @BindView(R.id.txtFullNameActRegister)
    EditText FullName;
    @BindView(R.id.txtLastNameActRegister)
    EditText LastName;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_core_user_register);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("CoreUserRegister");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CoreUserRegister");
    }

    @OnClick(R.id.api_test_submit_button)
    public void onSubmitClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        CoreUserRegisterRequest request = new CoreUserRegisterRequest();
        if (!Username.getText().toString().matches("")) {
            request.Username = Username.getText().toString();
        } else {
            Username.setError("Required Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!Password.getText().toString().matches("")) {
            request.Password = Password.getText().toString();
        } else {
            Password.setError("Required Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!Name.getText().toString().matches("")) {
            request.Name = Name.getText().toString();
        } else {
            Name.setError("Required Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!Phone.getText().toString().matches("")) {
            request.Phone = Phone.getText().toString();
        } else {
            Phone.setError("Required Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!RePassword.getText().toString().matches("")) {
            request.RePassword = RePassword.getText().toString();
        } else {
            RePassword.setError("Required Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!FullName.getText().toString().matches("")) {
            request.FullName = FullName.getText().toString();
        } else {
            FullName.setError("Required Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!LastName.getText().toString().matches("")) {
            request.LastName = LastName.getText().toString();
        } else {
            LastName.setError("Required Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        RetrofitManager manager = new RetrofitManager(ActUserRegister.this);
        ICore iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICore.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("PackageName", EasyPreference.with(this).getString("packageName",""));
        Observable<CoreUserResponse> call = iCore.setUserRegister(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoreUserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CoreUserResponse response) {
                        JsonDialog cdd = new JsonDialog(ActUserRegister.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActUserRegister.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
