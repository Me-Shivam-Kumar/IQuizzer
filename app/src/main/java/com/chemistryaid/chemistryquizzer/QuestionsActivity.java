package com.chemistryaid.chemistryquizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    public List<String> result;
    public static final String FILE_NAME="QUIZZER";
    public static final String KEY_NAME="QUESTIONS";
    private int count=0;
    private TextView question,no_indicator;
    private LinearLayout options_container;
    private FloatingActionButton bookmarkBtn;
    private Button shareBtn,nextBtn;
    private int position=0;
    private int score=0;
    private String category;
    private int setNo;
    private Dialog loadingDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Sets");
    private  List<QuestionModel> list;
    private  List<QuestionModel> bookmarksList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private int matchedQuestionPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        final Calendar calendar=Calendar.getInstance();


        Toolbar toolbar2 =findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        question=findViewById(R.id.question);
        no_indicator=findViewById(R.id.no_indicator);
        bookmarkBtn=findViewById(R.id.bookmark_btn);
        options_container=findViewById(R.id.options_container);
        nextBtn=findViewById(R.id.next_btn);
        shareBtn=findViewById(R.id.share_btn);


        preferences=getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();
        getBookmarks();

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modelMatch()){
                    bookmarksList.remove(matchedQuestionPosition);
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));

                }else {
                    bookmarksList.add(list.get(position));
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));

                }
            }
        });

        category=getIntent().getStringExtra("category");
        setNo=getIntent().getIntExtra("setNo",1);

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        list=new ArrayList<>();
        loadingDialog.show();

        myRef.child(category).child("questions").orderByChild("setNo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(QuestionModel.class));
                }
                    if(list.size()>0){

                        for(int i=0;i<4;i++){
                            options_container.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    checkAnswer((Button) v);
                                }
                            });
                        }
                        playAnim(question,0,list.get(position).getQuestion());
                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nextBtn.setEnabled(true);
                                nextBtn.setAlpha(0.7f);
                                enableOption(true);
                                position++;
                                if(position==list.size()){

                                    Intent srcoreIntent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                                    srcoreIntent.putExtra("score", score);
                                    srcoreIntent.putExtra("total",list.size());
                                    startActivity(srcoreIntent);
                                    finish();
                                    return;
                                }
                                count=0;
                                playAnim(question,0,list.get(position).getQuestion());
                            }
                        });
                        shareBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String body=list.get(position).getQuestion()+ list.get(position).getOptionA()+ "\n"+list.get(position).getOptionB()+"\n"+ list.get(position).getOptionC()+"\n"+list.get(position).getOptionD();
                                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("plain/text");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"CHEMISTRY QUIZZER");
                                shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                                startActivity(Intent.createChooser(shareIntent,"Share via"));
                            }
                        });
                    }else{
                        finish();
                        Toast.makeText(QuestionsActivity.this,"No Questions",Toast.LENGTH_LONG).show();
                    }
                    loadingDialog.dismiss();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionsActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                finish();


            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAns())){
          selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
          score++;
        }else {
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption=(Button) options_container.findViewWithTag(list.get(position).getCorrectAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        }
    }



    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            options_container.getChildAt(i).setEnabled(enable);
            if (enable) {
                options_container.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));

            }
        }
    }



    private void playAnim(final View view, final int value,final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(400).setStartDelay(30)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
               if(value==0 && count<4){
                   String option="";
                   if(count==0){
                       option=list.get(position).getOptionA();

                   }else if (count==1){
                       option=list.get(position).getOptionB();

                   }else if (count==3){
                       option=list.get(position).getOptionD();

                   }
                   else if(count==2){
                       option=list.get(position).getOptionC();

                   }
                   playAnim(options_container.getChildAt(count),0,option);
                   count++;
               }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if(value==0){
                    try {((TextView)view).setText(data);
                        no_indicator.setText(position+1+"/"+list.size());
                        if(modelMatch()){
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));

                        }else {
                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));

                        }

                    }catch (ClassCastException ex){
                        ((Button)view).setText(data);

                    }
                    view.setTag(data);
                    playAnim(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    private void getBookmarks(){
        String json= preferences.getString(KEY_NAME,"");
        Type type=new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList=gson.fromJson(json,type);
        if (bookmarksList==null){
            bookmarksList=new ArrayList<>();
        }
    }
    private boolean modelMatch(){
        boolean matched=false;
        int i=0;
        for(QuestionModel model:bookmarksList){

            if(model.getQuestion().equals(list.get(position).getQuestion())&&
                    model.getCorrectAns().equals(list.get(position).getCorrectAns())&& model.getSetNo()==list.get(position).getSetNo()){
                matched=true;
                matchedQuestionPosition=i;
            }
            i++;
        }
        return matched;
    }
    private void storeBookmarks(){
        String json=gson.toJson(bookmarksList);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
}
