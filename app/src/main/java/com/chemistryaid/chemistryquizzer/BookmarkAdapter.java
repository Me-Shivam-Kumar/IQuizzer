package com.chemistryaid.chemistryquizzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.viewholder>{
    public BookmarkAdapter(List<QuestionModel> list) {
        this.list = list;
    }

    private List<QuestionModel> list;

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarkitem,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(list.get(position).getQuestion(),list.get(position).getCorrectAns(),position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        private ImageButton delete_btn;
       private TextView question,answer;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            delete_btn=itemView.findViewById(R.id.delete_btn);
            question=itemView.findViewById(R.id.Question);
            answer=itemView.findViewById(R.id.answer);
        }
        private void setData(String question, String answer, final int position){
            this.question.setText(question);
            this.answer.setText(answer);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }

}
