package activity.estate;

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
import ntk.base.api.estate.interfase.IEstate;
import ntk.base.api.estate.model.EstatePropertyAddRequest;
import ntk.base.api.estate.model.EstatePropertyAddResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActPropertyAdd extends AppCompatActivity {

    @BindView(R.id.lblLayout)
    TextView lblLayout;
    @BindView(R.id.txtLatitude)
    EditText Latitude;
    @BindView(R.id.txtLongitude)
    EditText Longitude;
    @BindView(R.id.txtEstateContractType)
    EditText EstateContractType;
    @BindView(R.id.txtEstatePropertyType)
    EditText EstatePropertyType;
    @BindView(R.id.txtTitle)
    EditText Title;
    @BindView(R.id.txtAddress)
    EditText Address;
    @BindView(R.id.txtDescription)
    EditText Description;
    @BindView(R.id.txtSalePrice)
    EditText SalePrice;
    @BindView(R.id.txtPresalePrice)
    EditText PresalePrice;
    @BindView(R.id.txtRentPrice)
    EditText RentPrice;
    @BindView(R.id.txtDepositPrice)
    EditText DepositPrice;
    @BindView(R.id.txtInputValue)
    EditText InputValue;
    @BindView(R.id.txtfileStream)
    EditText fileStream;
    @BindView(R.id.api_test_submit_button)
    Button apiTestSubmitButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_estate_property_add);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("EstatePropertyAdd");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("EstatePropertyAdd");
    }

    @OnClick(R.id.api_test_submit_button)
    public void onSubmitClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        EstatePropertyAddRequest request = new EstatePropertyAddRequest();
        if (!Latitude.getText().toString().matches("")) {
            if (Latitude.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                request.Latitude = Double.valueOf(Latitude.getText().toString());
            } else {
                progressBar.setVisibility(View.GONE);
                Latitude.setError("InValid Info !!");
                return;
            }
        }
        if (!Longitude.getText().toString().matches("")) {
            if (Longitude.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                request.Longitude = Double.valueOf(Longitude.getText().toString());
            } else {
                progressBar.setVisibility(View.GONE);
                Longitude.setError("InValid Info !!");
                return;
            }
        }
        if (!EstateContractType.getText().toString().matches("")) {
            if (EstateContractType.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                request.EstateContractType = Integer.valueOf(EstateContractType.getText().toString());
            } else {
                progressBar.setVisibility(View.GONE);
                EstateContractType.setError("InValid Info !!");
                return;
            }
        }
        if (!EstatePropertyType.getText().toString().matches("")) {
            if (EstatePropertyType.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                request.EstatePropertyType = Integer.valueOf(EstatePropertyType.getText().toString());
            } else {
                progressBar.setVisibility(View.GONE);
                EstatePropertyType.setError("InValid Info !!");
                return;
            }
        }
        if (!Title.getText().toString().matches("")) {
            request.Title = Title.getText().toString();
        }
        if (!Address.getText().toString().matches("")) {
            request.Address = Address.getText().toString();
        }
        if (!Description.getText().toString().matches("")) {
            request.Description = Description.getText().toString();
        }
        if (!SalePrice.getText().toString().matches("")) {
            if (SalePrice.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                request.SalePrice = Long.valueOf(SalePrice.getText().toString());
            } else {
                progressBar.setVisibility(View.GONE);
                SalePrice.setError("InValid Info !!");
                return;
            }
        }
        if (!PresalePrice.getText().toString().matches("")) {
            if (PresalePrice.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                request.PresalePrice = Long.valueOf(PresalePrice.getText().toString());
            } else {
                progressBar.setVisibility(View.GONE);
                PresalePrice.setError("InValid Info !!");
                return;
            }
        }
        if (!DepositPrice.getText().toString().matches("")) {
            if (EstateContractType.getInputType() != InputType.TYPE_CLASS_NUMBER) {
            request.DepositPrice = Long.valueOf(DepositPrice.getText().toString());
            } else {
                progressBar.setVisibility(View.GONE);
                EstateContractType.setError("InValid Info !!");
                return;
            }
        }
        if (!InputValue.getText().toString().matches("")) {
            request.InputValue = Long.valueOf(InputValue.getText().toString());
        }
        if (!fileStream.getText().toString().matches("")) {
            request.fileStream = Long.valueOf(fileStream.getText().toString());
        }
        RetrofitManager manager = new RetrofitManager(ActPropertyAdd.this);
        IEstate iEstate = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(IEstate.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("PackageName", EasyPreference.with(this).getString("packageName",""));

        Observable<EstatePropertyAddResponse> call = iEstate.SetPropertyAdd(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<EstatePropertyAddResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(EstatePropertyAddResponse response) {
                        JsonDialog cdd = new JsonDialog(ActPropertyAdd.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActPropertyAdd.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, ActEstate.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, ActEstate.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}