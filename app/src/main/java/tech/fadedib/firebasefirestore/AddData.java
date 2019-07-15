package tech.fadedib.firebasefirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddData extends AppCompatActivity {

    EditText titleET, descET;
    Button btnSaveData, btnClearData;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    CollectionReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        Toolbar toolbar = findViewById(R.id.toolbar_add_data);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Add Data");
        toolbar.setTitleTextColor(getColor(R.color.colorAccent));

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection(mAuth.getCurrentUser().getUid());

        titleET = findViewById(R.id.dataTitle);
        descET = findViewById(R.id.dataDescription);
        btnSaveData = findViewById(R.id.btnSaveData);
        btnClearData = findViewById(R.id.btnClear);
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });
        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleET.getText().toString().trim();
                String desc = descET.getText().toString().trim();
                if (title.isEmpty()){
                    Toast.makeText(AddData.this, "Please enter title.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (desc.isEmpty()){
                    Toast.makeText(AddData.this, "Please enter Description.", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData(title, desc);

            }
        });
    }

    private void clearData() {
        titleET.setText("");
        descET.setText("");
        titleET.requestFocus();
    }

    private void saveData(String title, String desc) {
        Map<String, String> datamap = new HashMap<>();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        datamap.put("title", title);
        datamap.put("description", desc);
        reference.add(datamap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(AddData.this, "Saved data successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddData.this, "Failed to save data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
