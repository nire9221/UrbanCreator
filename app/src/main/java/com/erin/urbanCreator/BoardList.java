package com.erin.urbanCreator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class BoardList extends ArrayAdapter<Board> {

    private Activity context;
    //list of boards
    List<Board> BoardList;

    public BoardList(Activity context, List<Board> BoardList) {
        super(context, R.layout.boardlist, BoardList);
        this.context = context;
        this.BoardList = BoardList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.boardlist, null, true);
//initialize
        TextView textViewName = (TextView) listViewItem.findViewById
                (R.id.textViewName);

//getting user at position
        Board Board = BoardList.get(position);
        textViewName.setText(Board.getBoardContent());
//set user mobilenumber
        return listViewItem;
    }
}
