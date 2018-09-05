package com.nirwal.epoint.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nirwal.epoint.ExamActivity;
import com.nirwal.epoint.MainActivity;
import com.nirwal.epoint.R;
import com.nirwal.epoint.models.ParentChildListItem;

import java.util.ArrayList;

public class QuizListAdaptor extends RecyclerView.Adapter<QuizListAdaptor.QuizListHolder> {

    private ArrayList<ParentChildListItem> _list;
    private Context _context;
    public QuizListAdaptor(ArrayList<ParentChildListItem> list , Context context) {
        this._list = list;
        this._context = context;
    }


    @NonNull
    @Override
    public QuizListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_list,parent,false);


        return new QuizListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListHolder holder, int position) {
        holder.title.setText(_list.get(position).Title);
        holder.quizListLayout.setTag(_list.get(position));

    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    class QuizListHolder extends RecyclerView.ViewHolder{
        TextView title;
        CardView quizListLayout;
        public QuizListHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quiz_list_title);
            quizListLayout = itemView.findViewById(R.id.quiz_list_back);
            quizListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParentChildListItem item = (ParentChildListItem) v.getTag();
                    if(item.ChildType.equals("Question1")){
                        Intent i = new Intent(_context, ExamActivity.class);
                        i.putExtra("ID",item.ChildId);
                        _context.startActivity(i);
                    }else if(item.ChildType.equals("ParentChildListItem")){
                       MainActivity activity = (MainActivity) _context;
                       activity.startQuizListFragment(item);
                    }

                }
            });
        }

    }
}


