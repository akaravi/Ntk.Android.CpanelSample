package activity.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import ntk.base.api.core.model.CoreAllMenuResponse;
import ntk.base.api.core.model.CoreGetAllRequest;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActGetAllMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.lblLayout)
    TextView lblLayout;
    @BindView(R.id.txtContentFullSearchActGetAllMenu)
    EditText txtContent;
    @BindView(R.id.SpinnerNeedToRunFakePaginationActGetAllMenu)
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
        setContentView(R.layout.act_core_get_all_menu);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("GetAllMenu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("GetAllMenu");
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
        RetrofitManager manager = new RetrofitManager(ActGetAllMenu.this);
        ICore iCore = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ICore.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("Set-Cookie",EasyPreference.with(ActGetAllMenu.this).getString("Cookie", ""));
        Observable<CoreAllMenuResponse> call = iCore.GetAllMainMenu(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CoreAllMenuResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CoreAllMenuResponse response) {
                        JsonDialog cdd = new JsonDialog(ActGetAllMenu.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActGetAllMenu.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
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