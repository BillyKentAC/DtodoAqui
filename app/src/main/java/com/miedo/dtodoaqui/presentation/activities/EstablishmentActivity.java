package com.miedo.dtodoaqui.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.viewmodels.EstablishmentViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EstablishmentActivity extends AppCompatActivity{

    private EstablishmentViewModel viewModel;

    private SupportMapFragment mapFragment;
    @BindView(R.id.establishmentCTL)
    CollapsingToolbarLayout establishmentToolbar;
    /*@BindView(R.id.establishmentCategoryTV)
    TextView establishmentCategory;*/
    /*@BindView(R.id.establishmentVerifiedTV)
    TextView establishmentVerified;*/
    @BindView(R.id.establishmentDecriptionTV)
    TextView establishmentDescription;
    @BindView(R.id.establishmentOpeningHoursTV)
    TextView establishmentOpeningHours;
    @BindView(R.id.establishmentPriceTV)
    TextView establishmentPrice;
    /*@BindView(R.id.establishmentAddressTV)
    TextView establishmentAddress;*/
    @BindView(R.id.establishmentBackgroundIV)
    ImageView establishmentBackground;
    @BindView(R.id.establishmentNameTV)
    TextView establishmentName;

    private GoogleMap establishmentMap = null;
    private Marker establishmentMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment);
        ButterKnife.bind(this);

        //Mapa del establecimiento
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //Viewmodel
        viewModel = ViewModelProviders.of(this).get(EstablishmentViewModel.class);
        viewModel.getEstablishment().observe(this, new Observer<EstablishmentTO>() {
            @Override
            public void onChanged(EstablishmentTO establishmentTO) {
                refreshEstablishment(establishmentTO);
            }
        });

        //Configurar
        int id = getIntent().getExtras().getInt("establishment_id");
        viewModel.GetEstablishment(id);

    }

    private void refreshEstablishment(EstablishmentTO establishment){
        if(establishmentMap == null){
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    establishmentMap = googleMap;
                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                    establishmentMarker = establishmentMap.addMarker(new MarkerOptions().position(establishment.getLatLng()).title(establishment.getName()));
                    establishmentMap.moveCamera(CameraUpdateFactory.newLatLng(establishment.getLatLng()));
                }
            });
        }else{
            establishmentMarker.setPosition(establishment.getLatLng());
            establishmentMap.moveCamera(CameraUpdateFactory.newLatLng(establishment.getLatLng()));
        }

        Picasso.get().load(establishment.getSlug()).into(establishmentBackground);

        establishmentName.setText(establishment.getName());
        //establishmentAddress.setText(establishment.getAddress());
        //establishmentCategory.setText(establishment.getCategory());
        establishmentDescription.setText(establishment.getDescription());
        establishmentOpeningHours.setText(establishment.getOpeningHours());
        establishmentPrice.setText(establishment.getPrice());
        establishmentName.setCompoundDrawablesWithIntrinsicBounds(0,0,establishment.isVerified()?R.drawable.ic_check_circle_black_24dp:R.drawable.ic_do_not_disturb_on_black_24dp,0);

        //establishmentVerified.setText(establishment.isVerified()?"Verificado":"No verificado");
    }
}
