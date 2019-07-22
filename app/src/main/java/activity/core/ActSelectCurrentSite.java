package activity.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
import ntk.base.api.core.interfase.ICoreUser;
import ntk.base.api.core.model.CoreUserResponse;
import ntk.base.api.core.model.CoreUserSelectCurrentSiteRequest;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActSelectCurrentSite extends AppCompatActivity {

    @BindView(R.id.txtIdActActSelectCurrentSite)
    EditText Id;
    @BindView(R.id.api_test_submit_button)
    Button apiTestSubmitButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_core_select_current_site);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Current Site");
    }

    @OnClick(R.id.api_test_submit_button)
    public void onSubmitClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        CoreUserSelectCurrentSiteRequest request = new CoreUserSelectCurrentSiteRequest();

        RetrofitManager manager = new RetrofitManager(ActSelectCurrentSite.this);
        ICoreUser iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICoreUser.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("Authorization",EasyPreference.with(ActSelectCurrentSite.this).getString("LoginCookie", ""));
        Observable<CoreUserResponse> call = iCore.SelectCurrentSite(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoreUserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CoreUserResponse response) {
                        JsonDialog cdd = new JsonDialog(ActSelectCurrentSite.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActSelectCurrentSite.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
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