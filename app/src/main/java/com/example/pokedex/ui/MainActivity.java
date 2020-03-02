package com.example.pokedex.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.aimservices.telabook.R;
import com.aimservices.telabook.models.agents.Agent;
import com.aimservices.telabook.models.company.Company;
import com.aimservices.telabook.ui.fragments.auth.AuthenticationViewModel;
import com.aimservices.telabook.ui.fragments.company_selector.CompanySelectionFragment;
import com.aimservices.telabook.ui.fragments.company_selector.CompanyViewModel;
import com.aimservices.telabook.ui.fragments.sms.AgentsFragment;
import com.aimservices.telabook.utils.Constants;
import com.aimservices.telabook.utils.PreferenceHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.InternalTokenResult;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements CompanySelectionFragment.OnCompanySelected, AgentsFragment.OnAgentInteractionListener {

    private NavController navController;
    private Company currenCompany;
    private CompanyViewModel companyViewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private CompositeDisposable companyDisposables = new CompositeDisposable();
    private AuthenticationViewModel authenticationViewModel;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposables.isDisposed()) {
            disposables.dispose();
            disposables.clear();
        }
        if (!companyDisposables.isDisposed()) {
            companyDisposables.dispose();
            companyDisposables.clear();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        navController = Navigation.findNavController(this, R.id.fragment);
        FirebaseAuth.getInstance().addAuthStateListener(this::firebaseAuthChanged);
        FirebaseAuth.getInstance().addIdTokenListener(this::authTokenChanged);


    }

    private void errorFetchingCompany(Throwable throwable) {
        Timber.e(throwable, "ERROR fetching company!");
    }

    private void firebaseAuthChanged(FirebaseAuth firebaseAuth) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            authenticationViewModel.authenticationState.setValue(AuthenticationViewModel.AuthenticationState.UNAUTHENTICATED);
        } else {
            authenticationViewModel.authenticationState.setValue(AuthenticationViewModel.AuthenticationState.AUTHENTICATED);

        }
        authenticationViewModel.authenticationState.observe(this, this::statusChanged);
    }

    private void statusChanged(AuthenticationViewModel.AuthenticationState authenticationState) {
        NavDestination currentFrag = navController.getCurrentDestination();
        switch (authenticationState) {
            case AUTHENTICATED:
                int currentCompanyId = (PreferenceHelper.getSharedPreferenceInt(Constants.CURRENT_COMPANY_ID, -1));
                if (currentCompanyId > 0) {
                    companyDisposables.add(companyViewModel.fetchCurrentCompanyData().subscribe(this::companyObtained, this::errorFetchingCompany));
                } else {
                    if (!companyDisposables.isDisposed()) {
                        companyDisposables.dispose();
                        companyDisposables.clear();
                    }
                    if (currentFrag != null) {
                        if (currentFrag.getId() != R.id.companySelector) {

                            navController.navigate(R.id.companySelector);
                        }
                    }
                }
                break;
            case UNAUTHENTICATED:
                if (!disposables.isDisposed()) {
                    disposables.dispose();
                    disposables.clear();
                }
                if (!companyDisposables.isDisposed()) {
                    companyDisposables.dispose();
                    companyDisposables.clear();
                }
                navController.navigate(R.id.loginFragment);
                break;


        }
    }


    private void authTokenChanged(InternalTokenResult internalTokenResult) {
        if (internalTokenResult.getToken() != null) {
            Timber.d("Token obtained %s", internalTokenResult.getToken());
            PreferenceHelper.setSharedPreferenceString(Constants.TOKEN_KEY, internalTokenResult.getToken());
            authenticationViewModel.authenticationState.postValue(AuthenticationViewModel.AuthenticationState.FETCHING_TELABOOK_DATA);
            authenticationViewModel.fetchUserFromTelabook();
        }

    }


    private void companyObtained(Company company) {
        NavDestination currentFrag = navController.getCurrentDestination();
        if (currentFrag != null) {
            if (currentFrag.getId() != R.id.mainFragment) {
                navController.navigate(R.id.mainFragment);
            }
        }

    }


    @Override
    public void onDefaultCompanySelected(Company item) {
        Timber.d("User has touched %s", item);
        currenCompany = item;
    }

    @Override
    public void onCompanySelectionDone() {
        Timber.d("Setting your current company... %s", currenCompany);
        PreferenceHelper.setSharedPreferenceInt(Constants.CURRENT_COMPANY_ID, currenCompany.getCompanyId());
        companyViewModel.setCurrentCompany(currenCompany);
        Timber.d("Company ID %d set successfully! -- Going to main fragment", currenCompany.getCompanyId());
        navController.navigate(R.id.mainFragment);

    }


    @Override
    public void agentTouched(Agent agent) {
        Timber.d("Showing agent %s chats!", agent.getPersonName());
    }
}