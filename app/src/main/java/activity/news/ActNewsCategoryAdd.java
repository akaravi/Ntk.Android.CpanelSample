package activity.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import ntk.base.api.news.interfase.INewsCategory;
import ntk.base.api.news.model.NewsCategoryAddRequest;
import ntk.base.api.news.model.NewsCategoryResponse;
import ntk.base.api.news.model.NewsGetAllRequest;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActNewsCategoryAdd extends AppCompatActivity {

    @BindView(R.id.TitleTxt)
    EditText TitleTxt;
    @BindView(R.id.Description)
    EditText Description;
    @BindView(R.id.RecordStatus)
    Spinner RecordStatus;
    @BindView(R.id.previewImageSrc)
    EditText previewImageSrc;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private List<String> recordStatusList = new ArrayList<String>();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private int recordStatusValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_news_category_add);
        ButterKnife.bind(this);
        initialize();
    }


    private void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("NewsCategory/Add/");
        recordStatusList.add("Available");
        recordStatusList.add("Deleted");
        recordStatusList.add("Pending");
        recordStatusList.add("Confirmed");
        recordStatusList.add("DeniedConfirmed");
        recordStatusList.add("AvailableArchive");
        RecordStatus.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recordStatusList));
        RecordStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recordStatusValue = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.api_test_submit_button)
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        NewsCategoryAddRequest request = new NewsCategoryAddRequest();
        request.Title = TitleTxt.getText().toString();
        request.RecordStatus = recordStatusValue;
        request.Title = Description.getText().toString();
        request.Title = previewImageSrc.getText().toString();

        RetrofitManager manager = new RetrofitManager(ActNewsCategoryAdd.this);
        INewsCategory iNews = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(INewsCategory.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("Authorization", EasyPreference.with(ActNewsCategoryAdd.this).getString("SiteCookie", ""));
        Observable<NewsCategoryResponse> call = iNews.Add(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NewsCategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(NewsCategoryResponse response) {
                        JsonDialog cdd = new JsonDialog(ActNewsCategoryAdd.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActNewsCategoryAdd.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
