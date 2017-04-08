package mx.anahuac.telegram;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn1 ;
    TextView txtResponse ;
    TextView aviso ;
    EditText editText ;
    private Context context;
    public int telegram_id;
    private String nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this; //Creación de contexto de referencia
        txtResponse = (TextView) findViewById(R.id.txtResponse) ;
        aviso = (TextView) findViewById(R.id.aviso) ;
        editText = (EditText) findViewById(R.id.editText);
        doRequest();
        btn1 = (Button) findViewById(R.id.btn1);
//        bt2 = (Button) findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void doMessage() throws JSONException {
        RequestQueue queque = Volley.newRequestQueue(this);
        final String respuestaUsuario = editText.getText().toString();
        String url = "https://telegrame.azurewebsites.net/public/send";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        int duracion = Toast.LENGTH_LONG;
                        CharSequence text = "Se ha enviado mensaje de manera exitosa";
                        Toast toast = Toast.makeText(context, text, duracion);
                        toast.show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "Erroes");
                        try {
                            doMessage();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telegram_id", telegram_id+"");
                params.put("text", respuestaUsuario);
                return params;
            }
        };
        queque.add(postRequest);

    }
    public  void doRequest(){
        RequestQueue queque = Volley.newRequestQueue(this);
            String url = "https://telegrame.azurewebsites.net/public/getRegistroActual";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("LOOP TALKS", "YEah");
                        JSONArray registro = null;
                        try {
                            registro = new JSONArray(response);
                            telegram_id = Integer.parseInt(registro.getJSONObject(0).getString("telegram_id"));
                            nombre =  registro.getJSONObject(0).getString("nombre");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        aviso.setText("Respondele a: ");
                        txtResponse.setText(nombre);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOOP TALKS","That didn't work!");
                //txtResponse.setText("Error Http");
                doRequest();
            }
        });
        //DO REQUEST
        queque.add(stringRequest);
    }
}
