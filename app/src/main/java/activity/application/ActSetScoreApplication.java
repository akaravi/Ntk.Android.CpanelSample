package activity.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import ntk.base.api.application.interfase.IApplication;
import ntk.base.api.application.model.ApplicationScoreRequest;
import ntk.base.api.application.model.ApplicationScoreResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActSetScoreApplication extends AppCompatActivity {

    @BindView(R.id.txtScoreComment)
    EditText txtScoreComment;
    @BindView(R.id.txtScorePercent)
    EditText txtScorePercent;
    @BindView(R.id.api_test_submit_button)
    Button apiTestSubmitButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.lblLayout)
    TextView lblLayout;
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_core_set_score_application);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("ApplicationScoreSubmit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("ApplicationScoreSubmit");
    }

    @OnClick(R.id.api_test_submit_button)
    public void onSubmitClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        ApplicationScoreRequest request = new ApplicationScoreRequest();
        if (!txtScoreComment.getText().toString().matches("")) {
            request.ScoreComment = txtScoreComment.getText().toString();
        } else {
            txtScoreComment.setError("Required !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!txtScorePercent.getText().toString().matches("")) {
            if (txtScorePercent.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                txtScorePercent.setError("inValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.ScorePercent = Integer.valueOf(txtScorePercent.getText().toString());
            }
        } else {
            txtScorePercent.setError("Required !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        RetrofitManager manager = new RetrofitManager(ActSetScoreApplication.this);
        IApplication iApplication = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(IApplication.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("PackageName", EasyPreference.with(this).getString("packageName",""));
        Observable<ApplicationScoreResponse> call = iApplication.SetScoreApplication(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ApplicationScoreResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ApplicationScoreResponse response) {
                        JsonDialog cdd = new JsonDialog(ActSetScoreApplication.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActSetScoreApplication.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, ActApplication.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, ActApplication.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
