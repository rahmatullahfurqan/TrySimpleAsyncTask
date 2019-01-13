package belajar.furqan.com.simpleasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements MyAsyncCallback{
    final static String INPUT_STRING = "Halo Ini Demo AsyncTask!!";
    TextView tvStatus,tvDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tv_status);//inisialisasi textview
        tvDesc = findViewById(R.id.tv_desc);//inisialisasi textview
        DemoAsync demoAsync = new DemoAsync(this);//inisialisasi DemoAsync class
        demoAsync.execute(INPUT_STRING);//set string into DemoAsync class
    }

    //implement dari myAsyncCallback
    @Override
    public void onPreExecute() {
        tvStatus.setText("PreExcute");//set status
        tvDesc.setText(INPUT_STRING);//set desc
    }
    //implement dari myAsyncCallback
    @Override
    public void onPostExecute(String result) {
        tvStatus.setText("PostExcute");//set status
        if (result != null) {
            tvDesc.setText(result);//set desc
        }
    }

    //private static class DemoAsync
    private static class DemoAsync extends AsyncTask<String, Void, String> {
        static final String LOG_ASYNC = "DemoAsync";
        WeakReference<MyAsyncCallback> myListener;
//WeakReference berfungsi untuk menghubungkan sebuah kelas dengan kelas lain
// dengan bantuan sebuah kelas interface sebagai callback. Perhatikan kode berikut :
        DemoAsync(MyAsyncCallback myListener) {
            this.myListener = new WeakReference<>(myListener);//insert this.myListener
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(LOG_ASYNC, "status : onPreExecute");//log for status onPreExcute
            MyAsyncCallback myListener = this.myListener.get();//create MyAsyncCallback and insert from this.myListener.get()
            if (myListener != null) {//check condition
                myListener.onPreExecute();//call onPreExcute
            }
        }
        @Override
        protected String doInBackground(String... params) {//get string on params
            Log.d(LOG_ASYNC, "status : doInBackground"); // log for status doInbackground
            String output = null;

            try {
                String input = params[0];
                output = input + " Selamat Belajar!!";
                Thread.sleep(5000);//sleep to 5 seconds
            } catch (Exception e) {
                Log.d(LOG_ASYNC, e.getMessage());
            }

            return output;
        }
        @Override
        protected void onPostExecute(String result) {//get data from doInBackground on result
            super.onPostExecute(result);
            Log.d(LOG_ASYNC, "status : onPostExecute");//log for status onPostExcute
            MyAsyncCallback myListener = this.myListener.get();
            if (myListener != null) {
                myListener.onPostExecute(result);//call onPostExcute on MyAsyncCallback
            }
        }
    }
}
