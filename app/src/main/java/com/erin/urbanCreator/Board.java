package com.erin.urbanCreator;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.IgnoreExtraProperties;

//RealtimeDBList
@IgnoreExtraProperties
public class Board {

    private String boardId;
    private String boardContent;

    public Board() {

    }

    public Board(String boardId,String boardContent)
    {
        this.boardId = boardId;
        this.boardContent = boardContent;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }
}