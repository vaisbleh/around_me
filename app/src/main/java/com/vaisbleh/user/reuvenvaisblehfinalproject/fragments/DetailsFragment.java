package com.vaisbleh.user.reuvenvaisblehfinalproject.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private TextView detailsNameText, detailsAddressText, detailsPhoneText;
    private Place currentPlace;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        imageView = v.findViewById(R.id.imageView);
        detailsNameText = v.findViewById(R.id.detailsNameText);
        detailsAddressText = v.findViewById(R.id.detailsAddressText);
        detailsPhoneText = v.findViewById(R.id.detailsPhoneText);

        v.findViewById(R.id.btnWaze).setOnClickListener(this);
        v.findViewById(R.id.btnCall).setOnClickListener(this);

        if(savedInstanceState != null){
            currentPlace = savedInstanceState.getParcelable("place");
            if(currentPlace != null) {
                setPlace(currentPlace);
            }
        }




        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnCall: //call to place
                try {
                    Intent intentTel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + detailsPhoneText.getText().toString()));
                    startActivity(intentTel);
                }
                catch (ActivityNotFoundException ex){
                    Toast.makeText(getContext(), "it is not possible to call from your device", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnWaze:// navigate to the place
                try
                {
                    // Launch Waze to look for address:

                    String uri = "waze://?ll=" + currentPlace.getLat() + ", " +  currentPlace.getLon() + "&navigate=yes";
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( uri ) );
                    startActivity( intent );
                }
                catch ( ActivityNotFoundException ex  )
                {
                    // If Waze is not installed
                    Toast.makeText(getContext(), "have no Waze on your device", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void setPlace(Place place){
        currentPlace = place;
        detailsNameText.setText(place.getName().toString());
        detailsAddressText.setText(place.getAddress().toString());
        detailsPhoneText.setText(place.getPhoneNum().toString());

        String picReference = place.getPicReference().toString();
        Picasso.with(getContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + picReference + "&key=AIzaSyBFUdC0MhBIl2wKkQ0O4DPSf4cgQehJ2p8")
                .error(R.drawable.no_image_available)
                .into(imageView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("place", currentPlace);
    }
}
