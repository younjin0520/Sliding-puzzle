package edu.skku.map.pa1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class subActivity extends AppCompatActivity {
    GridView gridView;
    static public ArrayList<Bitmap> imgArr = new ArrayList<Bitmap>();
    static public ArrayList<Bitmap> imgcmp = new ArrayList<Bitmap>();
    static public int type=3, blank=8;

    public void cut(int n){//이미지 자르기
        Bitmap bit_img = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        Bitmap bm;
        imgArr.clear();
        imgcmp.clear();
        for(int i=0; i<n;i++){
            for(int j=0;j<n;j++) {
                if(i==n-1&&j==n-1){
                    imgArr.add(null);
                    imgcmp.add(null);
                    break;
                }
                bm = Bitmap.createBitmap(bit_img, bit_img.getWidth()*j/n, bit_img.getWidth()*i/n, bit_img.getWidth() / n, bit_img.getHeight() / n);
                imgArr.add(bm);
                imgcmp.add(bm);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        gridView = (GridView)findViewById(R.id.gridView1);
        Button button1 = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button_shuffle);

        cut(3);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(possible(blank, position)==1){
                    //자리바꾸기
                    imgArr.set(blank,imgArr.get(position));
                    imgArr.set(position,null);
                    blank=position;
                    gridView.setAdapter(new ImageAdapter(subActivity.this));
                    if(imgcmp.equals(imgArr)){
                        Toast.makeText(getApplicationContext(),"FINISH!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cut(3);
                type =3; blank=8;//초기화
                gridView.setNumColumns(3);
                gridView.setAdapter(new ImageAdapter(subActivity.this));
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cut(4);
                type =4; blank=15;//초기화
                gridView.setNumColumns(4);
                gridView.setAdapter(new ImageAdapter(subActivity.this));
            }
        });
        //shuffle
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.shuffle(imgArr);
                blank = findBlank(type);
                gridView.setAdapter(new ImageAdapter(subActivity.this));
            }
        });
    }
    public int possible(int blank, int position){
        if(position==blank-1||position==blank+1||position==blank-type||position==blank+type){
            if(position==blank-1&&(blank%type)==0){
                return 0;
            }else if((blank+1)%type==0&&position==blank+1){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 0;
        }
    }

    public int findBlank(int type){
        for(int i=0;i<type*type;i++) {
            if (subActivity.imgArr.get(i) == null) {
                return i;
            }
        }
        return 0;
    }
}
class ImageAdapter extends BaseAdapter {

    private Context context;
    public ImageAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount(){
        return subActivity.imgArr.size();
    }

    @Override
    public Object getItem(int position) {
        return subActivity.imgArr.get(position);//아이템 호출 시 사용
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null){
            imageView = new ImageView(context);
            if(subActivity.type==3) {
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            }else{
                imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else{
            imageView=(ImageView)convertView;
        }
        imageView.setImageBitmap(subActivity.imgArr.get(position));

        return imageView;
    }
}