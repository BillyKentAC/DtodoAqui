package com.miedo.dtodoaqui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.EstablishmentCreateTO;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.model.CategoriesModel;
import com.miedo.dtodoaqui.model.LocationsModel;
import com.miedo.dtodoaqui.utils.CallbackUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEstablishmentViewModel extends ViewModel {

    private List<Integer> indices = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    CategoriesModel categoriesModel = new CategoriesModel();
    LocationsModel locationsModel = new LocationsModel();

    private final EstablishmentCreateTO establishment = new EstablishmentCreateTO();

    private List<Integer> indicesLocations = new ArrayList<>();
    private List<String> locationsNombres = new ArrayList<>();


    public enum RegisterState {
        FETCHING_FORM_DATA,
        ERROR_FETCHING,
        READY_TO_REGISTER,
        NEXT_STEP,
        NORMAL,
        TO_REGISTER,
        REGISTERING,
        ERROR_REGISTERING,
        REGISTER_SUCCESS
    }

    private final MutableLiveData<RegisterState> registerState = new MutableLiveData<>();

    public RegisterEstablishmentViewModel() {
        registerState.setValue(RegisterState.FETCHING_FORM_DATA);
        fetchCategories();
    }

    // ------------------------------------- Fetch data ----------------------------------------------
    public void fetchCategories() {
        categoriesModel.GetCategories(new CallbackUtils.SimpleCallback<Map<String, Integer>>() {
            @Override
            public void OnResult(Map<String, Integer> arg) {
                categories.clear();
                indices.clear();
                for (Map.Entry<String, Integer> entry : arg.entrySet()) {
                    categories.add(entry.getKey());
                    indices.add(entry.getValue());
                }
                if (categories.size() > 0) {
                    fetchLocations();
                } else {
                    registerState.setValue(RegisterState.ERROR_FETCHING);
                }
            }

            @Override
            public void OnFailure(String response) {
                registerState.setValue(RegisterState.ERROR_FETCHING);
            }
        });

    }

    public void fetchLocations() {
        locationsModel.getLocations(new LocationsModel.Callback<HashMap<String, Integer>>() {
            @Override
            public void onResult(HashMap<String, Integer> arg) {
                if (!arg.isEmpty()) {
                    indicesLocations.clear();
                    locationsNombres.clear();

                    for (Map.Entry<String, Integer> entry : arg.entrySet()) {
                        locationsNombres.add(entry.getKey());
                        indicesLocations.add(entry.getValue());
                    }


                    registerState.setValue(RegisterState.READY_TO_REGISTER);
                }
            }

            @Override
            public void onFailure() {
                registerState.setValue(RegisterState.ERROR_FETCHING);
            }
        });
    }

    // -----------------------------------------------------------------

    public void registerEstablishment() {

        registerState.setValue(RegisterState.TO_REGISTER);
        String requestBodyString = buildRequestBody(establishment);

        // Creamos el RequestBody para la peticion
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyString);

        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        Call<ResponseBody> call = api.registerEstablishment(requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 201) { // CREATED
                        registerState.setValue(RegisterState.REGISTER_SUCCESS);
                    }
                } else {
                    registerState.setValue(RegisterState.ERROR_REGISTERING);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                registerState.setValue(RegisterState.ERROR_REGISTERING);
            }
        });
    }

    public boolean validarPrimerPaso() {

        return !establishment.getName().isEmpty() && establishment.getCategoryId() != null
                && !establishment.getDescription().isEmpty();
    }

    public boolean validarSegundoPaso() {
        return establishment.getLatitude() != null &&
                establishment.getLongitude() != null &&
                establishment.getAddress() != null;
    }

    public boolean validarTercerPaso() {
        return establishment.getOpeningHours() != null &&
                establishment.getLocationId() != null &&
                establishment.getSlug() != null;
    }

    private String buildRequestBody(EstablishmentCreateTO establishment) {

        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject listing = new JSONObject();

        try {
            listing.put("name", establishment.getName());
            listing.put("address", establishment.getAddress());
            listing.put("category_id", establishment.getCategoryId());
            listing.put("location_id", establishment.getLocationId());
            listing.put("slug", establishment.getSlug());
            listing.put("description", establishment.getDescription());
            listing.put("latitude", establishment.getLatitude());
            listing.put("longitude", establishment.getLongitude());
            listing.put("opening_hours", establishment.getOpeningHours());
            listing.put("user_id", establishment.getUserId());

            body.put("listings", listing);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;


    }

    public MutableLiveData<RegisterState> getRegisterState() {
        return registerState;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public EstablishmentCreateTO getEstablishment() {
        return establishment;
    }

    public List<Integer> getIndicesLocations() {
        return indicesLocations;
    }

    public List<String> getLocationsNombres() {
        return locationsNombres;
    }
}
