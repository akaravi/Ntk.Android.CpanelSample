package activity.pooling;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.article.ActArticle;
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
import ntk.base.api.pooling.interfase.IPooling;
import ntk.base.api.pooling.model.PoolingSubmitRequest;
import ntk.base.api.pooling.model.PoolingSubmitResponse;
import ntk.base.api.pooling.model.PoolingVote;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActSetSubmitPooling extends AppCompatActivity {

    @BindView(R.id.lblLayout)
    TextView lblLayout;
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    @BindView(R.id.ContentId)
    EditText ContentId;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.OptionId)
    EditText OptionId;
    @BindView(R.id.OptionScore)
    EditText OptionScore;
    @BindView(R.id.voteAdd)
    Button voteAdd;
    private List<PoolingVote> votes = new ArrayList<>();
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pooling_set_submit_pooling);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("PoolingVoteSubmit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("PoolingVoteSubmit");
    }

    @OnClick(R.id.api_test_submit_button)
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    @OnClick(R.id.voteAdd)
    public void onClickVoteAdd(View v) {
        PoolingVote poolingVote = new PoolingVote();
        if (!OptionId.getText().toString().matches("")) {
            if (OptionId.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                OptionId.setError("inValid Info !!");
                return;
            } else {
                poolingVote.OptionId = Long.valueOf(OptionId.getText().toString());
                OptionId.setText("");
            }
        } else {
            OptionId.setError("Required !!");
            return;
        }
        if (!OptionScore.getText().toString().matches("")) {
            if (OptionScore.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                OptionScore.setError("inValid Info !!");
                return;
            } else {
                poolingVote.OptionScore = Integer.valueOf(OptionScore.getText().toString());
                OptionScore.setText("");
            }
        } else {
            OptionScore.setError("Required !!");
            return;
        }
        if (poolingVote.OptionId != null && poolingVote.OptionScore != -1) {
            votes.add(poolingVote);
        }
    }

    private void getData() {
        PoolingSubmitRequest request = new PoolingSubmitRequest();
        if (!ContentId.getText().toString().matches("")) {
            if (ContentId.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                ContentId.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.ContentId = Long.valueOf(ContentId.getText().toString());
            }
        } else {
            ContentId.setError("InValid Info !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!votes.isEmpty()) {
            request.votes = votes;
        } else {
            OptionId.setError("Required !!");
            OptionScore.setError("Required !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        RetrofitManager manager = new RetrofitManager(ActSetSubmitPooling.this);
        IPooling iPooling = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(IPooling.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("PackageName", EasyPreference.with(this).getString("packageName",""));
        Observable<PoolingSubmitResponse> call = iPooling.SetSubmitPooling(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<PoolingSubmitResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(PoolingSubmitResponse response) {
                        JsonDialog cdd = new JsonDialog(ActSetSubmitPooling.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage().toString());
                        Toast.makeText(ActSetSubmitPooling.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        votes.clear();
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

