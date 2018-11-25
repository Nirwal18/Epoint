package com.nirwal.epoint.adaptors;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nirwal.epoint.activities.ExamActivity;
import com.nirwal.epoint.activities.MainActivity;
import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.database.DatabaseHelper;
import com.nirwal.epoint.fragments.FavouritesFragment;
import com.nirwal.epoint.models.ParentChildListItem;

import java.util.ArrayList;

public class QuizListAdaptor extends RecyclerView.Adapter<QuizListAdaptor.QuizListHolder> {

    public final String Add_favourites = "Add favourites";
    public  final String Remove_favourites = "Remove favourites";

    private ArrayList<ParentChildListItem> _list;
    private ParentChildListItem _item;
    private Context _context;
    private String _parentName;

    public QuizListAdaptor(ArrayList<ParentChildListItem> list , Context context,String parentName) {
        this._list = list;
        this._context = context;
        this._parentName =parentName;
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
        this._item = _list.get(position);
        holder.mItem=this._item;
        holder.position = position;
        holder.title.setText(this._item.Title);
        holder.desc.setText(this._item.Description);
        holder.quizListLayout.setTag(this._item);
        holder.favImage.setVisibility(_item.Favourites == 0 ? View.GONE : View.VISIBLE);
        holder.favBtn.setTitle(_item.Favourites == 0 ? Add_favourites : Remove_favourites);
    }

    @Override
    public int getItemCount() {

        if(_list==null){
            return 0;
        }
        return _list.size();
    }

    public void removeItem(ParentChildListItem item){
        this._list.remove(item);
        //notifyDataSetChanged();
    }



    class QuizListHolder extends RecyclerView.ViewHolder{
        private PopupMenu popupMenu;
        int position;
        MenuItem favBtn;
        ParentChildListItem mItem;
        TextView title ,desc;
        RelativeLayout quizListLayout;
        ImageButton menuBtn;
        ImageView favImage;

        public QuizListHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quiz_list_title);
            desc =itemView.findViewById(R.id.quiz_list_desc);
            favImage = itemView.findViewById(R.id.quiz_fav_image);
            quizListLayout = itemView.findViewById(R.id.quiz_list_back);

            quizListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParentChildListItem item = (ParentChildListItem) v.getTag();
                    if(item.ChildType.equals("Question1")){

                        //firebase analytic
                        Bundle bundle = new Bundle();
                        bundle.putString("Title",item.Title);
                        bundle.putString("desc",item.Description);
                        bundle.putString("child_type",item.ChildType);
                        ((MyApp)((MainActivity)_context).getApplication())
                                .getFirebaseAnalytics()
                                .logEvent("quiz_open",bundle);


                        //another activity open cmd
                        Intent i = new Intent(_context, ExamActivity.class);
                        i.putExtra("ID",item.ChildId);
                        _context.startActivity(i);
                    }else if(item.ChildType.equals("ParentChildListItem")){
                       MainActivity activity = (MainActivity) _context;
                       activity.startQuizListFragment(item);
                    }

                }
            });

            menuBtn = itemView.findViewById(R.id.quiz_list_menu_btn);
            setUpPopupMenu();
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }


        void setUpPopupMenu(){

            popupMenu = new PopupMenu(_context,menuBtn);
            favBtn =popupMenu.getMenu().add("Add to favourites..");
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {



                  if(item.getTitle()== Add_favourites){
                        updateFavouritiesIntoDb(mItem.Id,1);
                      _list.get(position).Favourites=1;
                        Toast.makeText(_context,"Added to favourites",Toast.LENGTH_SHORT).show();
                  }else if(item.getTitle() == Remove_favourites){
                      updateFavouritiesIntoDb(mItem.Id,0);
                      _list.get(position).Favourites=0;
                      if(_parentName==FavouritesFragment.class.getName()){
                          popupMenu.getMenu().removeItem(0);
                      removeItem(mItem);
                      }
                      Toast.makeText(_context,"Removed from favourites",Toast.LENGTH_SHORT).show();
                  }
                  notifyDataSetChanged();
                  return true;
                }
            });

        }


        void updateFavouritiesIntoDb(String Id, int val){

            ContentValues cv=  new ContentValues();
            cv.put(DatabaseHelper.Table.col_9,val);
            ((MyApp)((Activity)_context).getApplication()).getSqlDb()
                    .getDb().update(DatabaseHelper.Table_Name2,cv,"Id=?",new String[]{Id});

        }

    }
}


