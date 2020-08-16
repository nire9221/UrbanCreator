package com.erin.urbanCreator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class WritingMain extends AppCompatActivity {

    //initialize
    EditText editTextCon;
    Button buttonAdd;
    ListView listViewBoard;
    //a list to store all the User from firebase database
    List<Board> boardList;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// method for find ids of views
        findViews();

// to maintian click listner of views
        initListner();
    }

    private void findViews() {
//getRefrance for Board table
        databaseReference = FirebaseDatabase.getInstance().getReference("BoardList");

        editTextCon = (EditText) findViewById(R.id.updateText);
        listViewBoard = (ListView) findViewById(R.id.listViewBoard);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
//list for store objects of user
        boardList = new ArrayList<>();
    }

    private void initListner() {
//adding an onclicklistener to button
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//calling the method addItem()
                addItem();
            }
        });


// list item click listener
        listViewBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Board Board = boardList.get(i);
                CallUpdateAndDeleteDialog(Board.getBoardId(), Board.getBoardContent());
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//clearing the previous Board list
                boardList.clear();
//getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//getting Baord from firebase console
                    Board Board = postSnapshot.getValue(Board.class);
//adding Baord to the list
                    boardList.add(Board);
                }
//creating Boardlist adapter
                BoardList Adapter = new BoardList(WritingMain.this, boardList);
//attaching adapter to the listview
                listViewBoard.setAdapter(Adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void CallUpdateAndDeleteDialog(final String boardId, String boardContent) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
//Access Dialog views
        final EditText updateTextContent = (EditText) dialogView.findViewById(R.id.updateText);
        updateTextContent.setText(boardContent);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);
//username for set dialog title
        dialogBuilder.setTitle(boardContent);
        final AlertDialog b = dialogBuilder.create();
        b.show();

// Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateTextContent.getText().toString().trim();
//checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
//Method for update data
                    update(boardId, name);
                    b.dismiss();
                }
            }
        });

// Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//Method for delete data
                delete(boardId);
                b.dismiss();
            }
        });
    }

    private boolean update(String id, String boardContent) {
//getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("BoardsList").child(id);
        Board Board = new Board(id, boardContent);
//update User to firebase
        UpdateReference.setValue(Board);
        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean delete(String id) {
//getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("BoardsList").child(id);
//removing User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private void addItem() {

//getting the values to save
        String boardContent = editTextCon.getText().toString().trim();

//checking if the value is provided or not Here, you can Add More Validation as you required

        if (!TextUtils.isEmpty(boardContent)) {

//it will create a unique id and we will use it as the Primary Key for our User
            String id = databaseReference.push().getKey();
//creating an Board Object
            Board Board = new Board(id, boardContent);
//Saving the Board
            databaseReference.child(id).setValue(Board);

            editTextCon.setText("");
            Toast.makeText(this, "Added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter a text", Toast.LENGTH_LONG).show();
        }
    }
}