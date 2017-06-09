package pnuproject.coffeefinder;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-06-02.
 */

public class SearchResult extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);

        Intent intent = getIntent();
        int e,s,a,j,n,p,b,t =0;

        // 인텐트로 부터 체크된 브랜드 정보 가져오기
        e = intent.getIntExtra("ediya", 0);
        s = intent.getIntExtra("starbucks", 0);
        a = intent.getIntExtra("angel", 0);
        j = intent.getIntExtra("jasmin", 0);
        n = intent.getIntExtra("north", 0);
        p = intent.getIntExtra("pascucci", 0);
        b = intent.getIntExtra("bene", 0);
        t = intent.getIntExtra("twosome", 0);








    }
}



