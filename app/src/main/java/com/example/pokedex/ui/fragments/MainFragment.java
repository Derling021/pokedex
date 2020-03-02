package com.aimservices.telabook.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aimservices.telabook.R;
import com.aimservices.telabook.models.messages.UnreadMessagesViewModel;
import com.aimservices.telabook.utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import timber.log.Timber;

public class MainFragment extends Fragment {


    private BottomNavigationView navView;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATIONS_TOPIC).addOnCompleteListener(this::subscribedToFCM).addOnFailureListener(this::errorSubscribing);
        MaterialToolbar myToolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(myToolbar);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                });


        navView = v.findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.agentsFragment, R.id.callsFragment, R.id.moreFragment)
                .build();
        NavController navController = Navigation.findNavController(v.findViewById(R.id.nav_host_fragment));
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) requireActivity(), navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        UnreadMessagesViewModel unreadViewModel = new ViewModelProvider(requireActivity()).get(UnreadMessagesViewModel.class);
        unreadViewModel.getMessageCount().observe(getViewLifecycleOwner(), this::messageCountUpdated);

        return v;
    }

    private void errorSubscribing(Exception e) {
        Timber.e(e, "Error subscribing to firebase");
    }

    private void subscribedToFCM(Task<Void> voidTask) {
        Timber.d("User is now subscribed to %s", Constants.NOTIFICATIONS_TOPIC);
    }

    private void messageCountUpdated(Integer integer) {
        Timber.d("Message count updated %d", integer);
        if (integer >0){
            navView.getOrCreateBadge(R.id.agentsFragment).setBackgroundColor(requireActivity().getColor(R.color.colorError));
            navView.getOrCreateBadge(R.id.agentsFragment).setNumber(integer);
        } else{
            navView.removeBadge(R.id.agentsFragment);
        }

    }


}
