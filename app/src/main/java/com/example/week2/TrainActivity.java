package com.example.week2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.TimeZoneFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class TrainActivity extends Fragment {
    private ArrayList<Skill> addrList = new ArrayList<Skill>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Context context;
    ImageView my_poke;
    ImageView train_back;
    private TrainAdapter mAdapter;
    private ContentResolver contentResolver;
    Skill skill1 = new Skill("몸통박치기",1.0,10,10);
    Skill skill2 = new Skill("씨뿌리기",5.0,10,15);
    Skill skill3 = new Skill("덩굴채찍",5.0,1,20);
    Skill skill4 = new Skill("독가루",7.0,1,25);
    Skill skill5 = new Skill("잎날가르기",11.0,1,40);
    Skill skill6 = new Skill("수면가루",4.0,1,20);
    Skill skill7 = new Skill("솔라빔",7.0,1,100);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.train_activity, container, false);
        return root;
    }
    private void updateData(){
        addrList.clear();
//        skill1.setCool(5.0f);
//        skill1.setLevel(1);
//        skill1.setPower(10);
//        skill1.setName("몸통박치기");
        addrList.add(skill1);
        addrList.add(skill2);
        addrList.add(skill3);
        addrList.add(skill4);
        addrList.add(skill5);
        addrList.add(skill6);
        addrList.add(skill7);
        mAdapter.notifyDataSetChanged();
//        String [] arrProjection = {
//                ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME,
//                ContactsContract.Contacts.PHOTO_ID
//        };
//        String[] arrPhoneProjection = {
//                ContactsContract.CommonDataKinds.Phone.NUMBER
//        };
//        Cursor clsCursor=contentResolver.query(
//                ContactsContract.Contacts.CONTENT_URI,
//                arrProjection,
//                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1" ,
//                null,
//                "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");
//        //if (clsCursor.moveToFirst()) {
//        clsCursor.moveToNext();
//        do{
//            String contactId = clsCursor.getString(0);
//            Cursor phoneCursor = contentResolver.query(
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    arrPhoneProjection,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
//                    null, null
//            );
//            phoneCursor.moveToNext();
//            String realnumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            String phone = realnumber.replace("-","");
//            phoneCursor.close();
//            PhoneBook contactInfo = new PhoneBook(
//                    Integer.toString(i),
//                    clsCursor.getString(clsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
//                    phone,
//                    clsCursor.getLong(clsCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID)),
//                    clsCursor.getLong(clsCursor.getColumnIndex(ContactsContract.Contacts._ID))
//            );
//            Log.i("name",clsCursor.getString(clsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
//            i++;
//            addrList.add(contactInfo);
//            mAdapter.notifyDataSetChanged();
//        }while (clsCursor.moveToNext());
//        //}
//        clsCursor.close();
        /* Notify to the adapter */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        mAdapter = new TrainAdapter(context, addrList);
        contentResolver = getActivity().getContentResolver();
        final Animation skillupanim = AnimationUtils.loadAnimation(context,R.anim.skill_level_up);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = view.findViewById(R.id.recyclerview_list);
        my_poke = view.findViewById(R.id.train_poke);
        train_back = view.findViewById(R.id.train_back);
        my_poke.setImageResource(R.drawable.pokemon1);
        train_back.setImageResource(R.drawable.home);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        updateData();
        mAdapter.setOnItemCLickListener(new TrainAdapter.OnItemClickListener() {
            @Override
        public void onUpClick(View v, int position){
            Skill s = mAdapter.getItem(position);
            int temp = s.getLevel();
            temp++;
            //코인 양 체크

            //코인 감소
                // 요구레벨 체크
            //맥스레벨 체크
                Log.i("getlevel before",""+s.getLevel());
            s.setLevel(temp);
                Log.i("getlevel after",""+s.getLevel());
            //powerup
            //exp++
             updateData();
             my_poke.startAnimation(skillupanim);
             mAdapter.notifyDataSetChanged();
        }
        @Override
        public void onItemClick(View v, int position, View itemView){
            modal(v,position);
        }
    });
    }
    public void modal(final View view, int position) {
        String msg;
        Skill s = mAdapter.getItem(position);
        msg = s.getName();
//        if (pay) {
//            msg = "주문을 접수 하시겠습니까?";
//        } else {
//            msg = "본 주문은 무통장 입금 주문입니다.\n입금 확인이 되었다면 확인 버튼을 눌러주세요.";
//        }

        new AlertDialog.Builder(view.getContext())
                .setTitle("스킬 정보")
                .setMessage(msg)

                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "확인", Toast.LENGTH_SHORT).show();
                        //((Activity) view.getContext()).finish();
                    }
                }).show();
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(view.getContext(), "코인을 결제하시겠습니까?", Toast.LENGTH_SHORT).show();
//                        //((Activity) view.getContext()).finish();
//                    }
//                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                Toast.makeText(view.getContext(), "코인이 부족합니다.", Toast.LENGTH_SHORT).show();
//
//            }
    }
}
