package activity.news;

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
import ntk.base.api.biography.interfase.IBiography;
import ntk.base.api.biography.model.BiographyCommentAddRequest;
import ntk.base.api.biography.model.BiographyCommentResponse;
import ntk.base.api.news.interfase.INews;
import ntk.base.api.news.model.NewsCommentAddRequest;
import ntk.base.api.news.model.NewsCommentResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActSetComment extends AppCompatActivity {

    @BindView(R.id.txtWriter)
    EditText txtWriter;
    @BindView(R.id.txtComment)
    EditText txtComment;
    @BindView(R.id.txtLinkParentId)
    EditText txtLinkParentId;
    @BindView(R.id.txtLinkContentId)
    EditText txtLinkContentId;
    @BindView(R.id.api_test_submit_button)
    Button apiTestSubmitButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.lblLayout)
    TextView lblLayout;
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_news_set_comment);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("NewsCommentAdd");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("NewsCommentAdd");
    }

    @OnClick(R.id.api_test_submit_button)
    public void onSubmitClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        NewsCommentAddRequest request = new NewsCommentAddRequest();
        if (!txtWriter.getText().toString().matches("")) {
            request.Writer = txtWriter.getText().toString();
        } else {
            txtWriter.setError("Required !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (!txtComment.getText().toString().matches("")) {
            request.Comment = txtWriter.getText().toString();
        } else {
            txtWriter.setError("Required !!");
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!txtLinkParentId.getText().toString().matches("")) {
            if (txtLinkParentId.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                txtLinkParentId.setError("InValid Info  !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.LinkParentId = Long.valueOf(txtLinkParentId.getText().toString());
            }
        }

        if (!txtLinkContentId.getText().toString().matches("")) {
            if (txtLinkContentId.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                txtLinkContentId.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.LinkContentId = Long.valueOf(txtLinkContentId.getText().toString());
            }
        } else {
            txtLinkContentId.setError("Required !!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        RetrofitManager manager = new RetrofitManager(ActSetComment.this);
        INews iNews = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(INews.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("PackageName", EasyPreference.with(this).getString("packageName",""));
        Observable<NewsCommentResponse> call = iNews.SetComment(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NewsCommentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NewsCommentResponse response) {
                        JsonDialog cdd = new JsonDialog(ActSetComment.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActSetComment.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
