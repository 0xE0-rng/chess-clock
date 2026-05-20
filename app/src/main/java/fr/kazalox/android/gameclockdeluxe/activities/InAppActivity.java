package fr.kazalox.android.gameclockdeluxe.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import fr.kazalox.android.gameclockdeluxe.App;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog;
import fr.kazalox.android.gameclockdeluxe.fragments.InAppFragment;
import fr.kazalox.android.gameclockdeluxe.inapp.IabHelper;
import fr.kazalox.android.gameclockdeluxe.inapp.IabResult;
import fr.kazalox.android.gameclockdeluxe.inapp.Purchase;

/* JADX INFO: loaded from: classes.dex */
public class InAppActivity extends FragmentActivity implements InAppFragment.InAppFragmentListener, SimpleDialog.SimpleDialogListener, IabHelper.OnIabSetupFinishedListener, IabHelper.OnIabPurchaseFinishedListener {
    private static final int RC_REQUEST = 10001;
    public static final String TAG = "ChessClock";
    private IabHelper mHelper;
    private boolean mInAppIsAvailable;
    boolean mIsFullUpgrade = false;

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        setContentView(R.layout.activity_one_fragment);
        if (savedInstanceState == null) {
            boolean featureNotAvailable = getIntent().getBooleanExtra(C.ARG_FEATURE_NOT_AVAILABLE, false);
            String text = getString(R.string.in_app_desc);
            if (featureNotAvailable) {
                text = getString(R.string.in_app_desc_intro) + "\n\n" + text;
            }
            InAppFragment f = InAppFragment.newInstance(text);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.layout_container, f, InAppFragment.class.getSimpleName()).commit();
        }
        this.mHelper = new IabHelper(this, C.SETTINGS_ACTIVITY + C.LOG_PREFIX + C.TEST + C.TEST_2);
        this.mHelper.startSetup(this);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.inapp.IabHelper.OnIabSetupFinishedListener
    public void onIabSetupFinished(IabResult result) {
        Log.d("ChessClock", "Setup finished - result: " + result);
        this.mInAppIsAvailable = result.isSuccess();
        Log.d("ChessClock", "InAppActivity - onIabSetupFinished - mInAppIsAvailable " + this.mInAppIsAvailable);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.InAppFragment.InAppFragmentListener
    public void onInAppOk() {
        Log.d("ChessClock", "Upgrade button clicked; launching purchase flow for upgrade.");
        if (!this.mInAppIsAvailable) {
            SimpleDialog d = SimpleDialog.newInstance(getString(R.string.dialog_title_in_app_not_available), getString(R.string.dialog_message_in_app_not_available), getString(R.string.dialog_button_ok));
            d.show(getSupportFragmentManager(), (String) null);
        } else {
            this.mHelper.launchPurchaseFlow(this, C.SKU_FULL_UPGRADE, RC_REQUEST, this, "");
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.inapp.IabHelper.OnIabPurchaseFinishedListener
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        Log.d("ChessClock", "Purchase finished: " + result + ", purchase: " + purchase);
        if (this.mHelper != null) {
            if (result.isFailure()) {
                Log.d("ChessClock", "InAppActivity - onIabPurchaseFinished - result isFailure ");
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                Log.d("ChessClock", "Error purchasing. Authenticity verification failed.");
                return;
            }
            Log.d("ChessClock", "Purchase successful.");
            if (purchase.getSku().equals(C.SKU_FULL_UPGRADE)) {
                Log.d("ChessClock", "Purchase is premium upgrade. Congratulating user.");
                App.setFullUpgrade(true);
            }
            SimpleDialog d = SimpleDialog.newInstance(getString(R.string.dialog_title_purchase_succesfull), getString(R.string.dialog_message_purchase_succesfull), getString(R.string.dialog_button_ok));
            d.show(getSupportFragmentManager(), (String) null);
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        p.getDeveloperPayload();
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.SimpleDialogListener
    public void onButton1Click(DialogFragment dialog) {
        finish();
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.SimpleDialogListener
    public void onButton2Click(DialogFragment dialog) {
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.SimpleDialogListener
    public void onButton3Click(DialogFragment dialog) {
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.InAppFragment.InAppFragmentListener
    public void onInAppCancel() {
        finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        Log.d("ChessClock", "Destroying helper.");
        if (this.mHelper != null) {
            this.mHelper.dispose();
            this.mHelper = null;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        finish();
    }
}
