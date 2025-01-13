package com.example.madproject;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.madproject.data.DAO.LocationDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.model.DbLocation;
import com.example.madproject.data.repository.LocationRepository;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class location extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_CODE = 102;
    private static final int LOCATION_SETTINGS_REQUEST_CODE = 103;

    private LocationRepository locationRepository;
    private List<LatLng> unsafeLocations; // List of unsafe locations
    private Location userCurrentLocation; // User's current location for distance calculation

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        locationRepository = new LocationRepository(requireContext());

        // Initialize Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Location Provider Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize buttons
        ImageButton button1 = view.findViewById(R.id.profile_button1);
        ImageButton button2 = view.findViewById(R.id.profile_button2);
        ImageButton button3 = view.findViewById(R.id.profile_button3);
        ImageButton button4 = view.findViewById(R.id.profile_button4);
        ImageButton button5 = view.findViewById(R.id.profile_button5);
        Button reportButton = view.findViewById(R.id.reportUnsafeLocation);

        // Fetch unsafe locations from database
        loadUnsafeLocations();

        // Set button click listeners
        button1.setOnClickListener(v -> showUnsafeLocationWithDistance(0));
        button2.setOnClickListener(v -> showUnsafeLocationWithDistance(1));
        button3.setOnClickListener(v -> showUnsafeLocationWithDistance(2));
        button4.setOnClickListener(v -> showUnsafeLocationWithDistance(3));
        button5.setOnClickListener(v -> showUnsafeLocationWithDistance(4));

        // Set report location button listener
        reportButton.setOnClickListener(v -> enableReportUnsafeLocation());

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        } else {
            checkLocationSettingsAndInitialize();
        }
    }

    private void checkLocationSettingsAndInitialize() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10 seconds
                .setFastestInterval(5000); // 5 seconds

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(locationSettingsResponse -> initializeLocationFunctionality())
                .addOnFailureListener(exception -> {
                    if (exception instanceof ResolvableApiException) {
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            resolvable.startResolutionForResult(requireActivity(), LOCATION_SETTINGS_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error enabling location services", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initializeLocationFunctionality() {
        if (mMap != null && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            userCurrentLocation = location; // Save user's current location
                            sortUnsafeLocationsByDistance();
                        } else {
                            startLocationUpdates();
                        }
                    });

            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000) // 10 seconds
                .setFastestInterval(5000); // 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
                    userCurrentLocation = location; // Save user's current location
                    sortUnsafeLocationsByDistance();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void loadUnsafeLocations() {
        unsafeLocations = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
//            AppDatabase database = AppDatabase.getInstance(requireContext());
//            LocationDAO locationDAO = database.locationDAO();

//            List<com.example.madproject.data.model.Location> locations = locationDAO.getAll();
            List<DbLocation> locations = locationRepository.getAllLocation();
            for (DbLocation loc : locations) {
                unsafeLocations.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
            }
        });
    }

    private void sortUnsafeLocationsByDistance() {
        if (userCurrentLocation == null || unsafeLocations == null) return;

        // Sort unsafe locations based on distance to user's current location
        Collections.sort(unsafeLocations, Comparator.comparingDouble(location -> {
            float[] results = new float[1];
            Location.distanceBetween(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude(),
                    location.latitude, location.longitude, results);
            return results[0];
        }));
    }

    private void showUnsafeLocationWithDistance(int index) {
        if (index >= unsafeLocations.size()) {
            Toast.makeText(requireContext(), "No location available.", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng location = unsafeLocations.get(index);
        if (userCurrentLocation != null) {
            float[] results = new float[1];
            Location.distanceBetween(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude(),
                    location.latitude, location.longitude, results);
            double distance = results[0] / 1000.0; // Convert to kilometers

            mMap.clear(); // Clear existing markers
            mMap.addMarker(new MarkerOptions().position(location).title("Unsafe Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

            Toast.makeText(requireContext(), "Distance: " + String.format("%.2f", distance) + " km", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Unable to calculate distance.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableReportUnsafeLocation() {
        Toast.makeText(requireContext(), "You can now report a location by long-pressing on the map.", Toast.LENGTH_LONG).show();

        mMap.setOnMapLongClickListener(latLng -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Report Unsafe Location")
                    .setMessage("Are you sure you want to report this location as unsafe?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Reported Unsafe Location"));

                        // Save to database
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(() -> {
//                            AppDatabase database = AppDatabase.getInstance(requireContext());
//                            LocationDAO locationDAO = database.locationDAO();
//
//                            String newId = locationDAO.getLastId();
//                            int id = (newId == null) ? 1 : Integer.parseInt(newId) + 1;
                            DbLocation newDbLocation = new DbLocation(
                                    locationRepository.createLocationId(), "Reported Location", latLng.longitude, latLng.latitude);
//                            locationDAO.insert(newDbLocation);
                            locationRepository.insertLocationInFirestore(newDbLocation);
                        });

                        Toast.makeText(requireContext(), "Unsafe location reported!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        Toast.makeText(requireContext(), "Action canceled", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}
