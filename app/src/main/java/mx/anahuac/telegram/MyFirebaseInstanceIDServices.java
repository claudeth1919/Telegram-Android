package mx.anahuac.telegram;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDServices extends FirebaseInstanceIdService{
    private String token;
    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("LOOP Talks", "Regresed Token" + refreshedToken);
//        sendRegistrationToServer(refreshedToken);
        token = refreshedToken;
    }

    public String sendRegistrationToServer() {
        onTokenRefresh();
        return token;
    }
}
