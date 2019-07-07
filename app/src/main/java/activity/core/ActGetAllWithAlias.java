package activity.core;

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
import android.widget.TextView;
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
import ntk.base.api.core.interfase.ICore;
import ntk.base.api.core.model.CoreAllWithAliasResponse;
import ntk.base.api.core.model.CoreGetAllRequest;
import ntk.base.api.core.model.CoreUserLoginRequest;
import ntk.base.api.core.model.CoreUserResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActGetAllWithAlias extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.lblLayout)
    TextView lblLayout;
    @BindView(R.id.txtContentFullSearchActGetAllWithAlias)
    EditText txtContent;
    @BindView(R.id.SpinnerNeedToRunFakePaginationActGetAllWithAlias)
    Spinner NeedToRun;
    @BindView(R.id.api_test_submit_button)
    Button apiTestSubmitButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private int NeedToRunValue;
    private List<Boolean> spinnerValue = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_core_get_all_with_alias);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("GetAllWithAlias");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("GetAllWithAlias");
        spinnerValue.add(0, true);
        spinnerValue.add(1, false);
        NeedToRun.setAdapter(new ArrayAdapter<Boolean>(this, android.R.layout.simple_spinner_item, spinnerValue));
        NeedToRun.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.api_test_submit_button)
    public void onSubmitClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        CoreGetAllRequest request = new CoreGetAllRequest();
        if (!txtContent.getText().toString().matches("")) {
            request.ContentFullSearch = txtContent.getText().toString();
        }
        request.NeedToRunFakePagination = NeedToRunValue == 0;
        RetrofitManager manager = new RetrofitManager(ActGetAllWithAlias.this);
        ICore iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICore.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        Observable<CoreAllWithAliasResponse> call = iCore.GetAllWithAlias(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoreAllWithAliasResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CoreAllWithAliasResponse response) {
                        JsonDialog cdd = new JsonDialog(ActGetAllWithAlias.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActGetAllWithAlias.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        NeedToRunValue = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}