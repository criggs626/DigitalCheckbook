package labs.madscientist.accountbalance;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by francesco on 29/11/16.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    private TextView tv;
    private ImageView iv;
    private Context mContext;


    public FingerprintHandler(TextView tv, ImageView iv,Context mContext) {
        this.tv = tv;
        this.iv = iv;
        this.mContext=mContext;
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        tv.setText("Auth error");
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        tv.setText("Unlocked");
        iv.setImageResource(R.drawable.ic_lock_open_black_24dp);
        Intent myIntent = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(myIntent);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
    }

    public void doAuth(FingerprintManager manager, FingerprintManager.CryptoObject obj) {
        CancellationSignal signal = new CancellationSignal();
        try {
            manager.authenticate(obj, signal, 0, this, null);
        }
        catch(SecurityException sce) {}
    }
}
