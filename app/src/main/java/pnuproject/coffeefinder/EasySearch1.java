package pnuproject.coffeefinder;

/**
 * Created by Administrator on 2017-05-16.
 */

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.support.v7.app.AppCompatActivity;
        import android.widget.Toast;
        import android.widget.ToggleButton;

/**
 * Created by songmho on 2015-01-18.
 */

public class EasySearch1 extends AppCompatActivity {
    int e,s,a,j,n,p,b,t =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easysearch);




        final ToggleButton north = (ToggleButton)findViewById(R.id.toggleButton22);
        north.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(north.isChecked()){
                    n= 1 ;
                }
                else {
                    n=0;
                }
            }
        });

        final ToggleButton jasmin = (ToggleButton)findViewById(R.id.toggleButton19);
        jasmin.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(jasmin.isChecked()){
                    j= 1 ;
                }
                else {
                    j= 0;
                }
            }
        });

        final ToggleButton ediya = (ToggleButton)findViewById(R.id.toggleButton20);
        ediya.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(ediya.isChecked()){
                    e= 1 ;
                }
                else {
                    e= 0;
                }
            }
        });

        final ToggleButton starbucks = (ToggleButton)findViewById(R.id.toggleButton28);
        starbucks.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(starbucks.isChecked()){
                    s= 1 ;
                }
                else {
                    s= 0;
                }
            }
        });

        final ToggleButton pascucci = (ToggleButton)findViewById(R.id.toggleButton17);
        pascucci.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(pascucci.isChecked()){
                    p= 1 ;
                }
                else {
                    p= 0;
                }
            }
        });

        final ToggleButton angel = (ToggleButton)findViewById(R.id.toggleButton14);
        angel.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(angel.isChecked()){
                    a= 1 ;
                }
                else {
                    a= 0;
                }
            }
        });

        final ToggleButton twosome = (ToggleButton)findViewById(R.id.toggleButton27);
        twosome.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(twosome.isChecked()){


                    t= 1 ;

                }
                else {
                    t= 0;
                }
            }
        });

        final ToggleButton bene = (ToggleButton)findViewById(R.id.toggleButton24);
        bene.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                if(bene.isChecked()){
                    b= 1 ;
                }
                else {
                    b= 0;
                }
            }
        });



        Button resultButton =(Button)findViewById(R.id.button2);
        resultButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EasySearch1.this,SearchResult.class);
                intent.putExtra("ediya", e);
                intent.putExtra("starbucks", s);
                intent.putExtra("angel", a);
                intent.putExtra("jasmin", j);
                intent.putExtra("north", n);
                intent.putExtra("pascucci", p);
                intent.putExtra("bene", b);
                intent.putExtra("twosome", t);


                startActivity(intent);
            }
        });


    }
}