package com.example.duantotnghiep.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.ChatAdapter;
import com.example.duantotnghiep.Model.ChatMsg;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class InboxFragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView rcvInbox;
    private EditText edtInbox;
    private List<ChatMsg> msgList;
    private ChatAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initControl(view);
        listenerMess();
    }

    private void initControl(View view) {
        view.findViewById(R.id.imgSendChat).setOnClickListener(v -> {
            sendMessToFire();
        });
    }

    private void sendMessToFire() {
        String mess = edtInbox.getText().toString().trim();
        if (TextUtils.isEmpty(mess)) {

        } else {
            HashMap<String, Object> msg = new HashMap<>();
            msg.put(getString(R.string.send_id), LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId());
            msg.put(getString(R.string.receive_id), "ID_RECEIVE");
            msg.put(getString(R.string.mess), mess);
            msg.put(getString(R.string.datetime), new Date());
            db.collection(getString(R.string.path_chat)).add(msg).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    edtInbox.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("thisINbox", e.getMessage());
                }
            });
        }
    }

    private void listenerMess() {

        db.collection(getString(R.string.path_chat))
                .whereEqualTo(getString(R.string.send_id), LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId())
                .whereEqualTo(getString(R.string.receive_id), "ID_RECEIVE")
                .addSnapshotListener(eventListener);

        db.collection(getString(R.string.path_chat))
                .whereEqualTo(getString(R.string.send_id), "ID_RECEIVE")
                .whereEqualTo(getString(R.string.receive_id), LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) ->
    {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String id_send = documentChange.getDocument().getString("id_send");
                    String id_receive = documentChange.getDocument().getString("id_receive");
                    String mess = documentChange.getDocument().getString("message");
                    String datetime = formatDate(documentChange.getDocument().getDate("datetime"));
                    ChatMsg chatMsg = new ChatMsg(id_send, id_receive, mess, datetime);
                    msgList.add(chatMsg);
                }
            }
            Collections.sort(msgList, (obj1, obj2) ->
                    obj1.getDatetime().compareTo(obj2.getDatetime()));
            if (msgList.size() == 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeChanged(msgList.size(), msgList.size());
                rcvInbox.smoothScrollToPosition(msgList.size() - 1);
            }
        }
    }
    );

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy, hh:mm", Locale.getDefault()).format(date);
    }

    private void initUI(View view) {
        msgList = new ArrayList<>();
        rcvInbox = view.findViewById(R.id.rcvInbox);
        edtInbox = view.findViewById(R.id.edtInbox);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvInbox.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(getContext(), msgList, LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId());
        rcvInbox.setAdapter(adapter);

    }
}