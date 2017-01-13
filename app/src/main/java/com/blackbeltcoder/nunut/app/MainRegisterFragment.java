package com.blackbeltcoder.nunut.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.component.CustomFormDialog;
import com.blackbeltcoder.nunut.component.CustomInfoDialog;
import com.blackbeltcoder.nunut.component.CustomProgressDialog;
import com.blackbeltcoder.nunut.component.CustomTextView;
import com.blackbeltcoder.nunut.global.App;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.model.NuterModel;
import com.blackbeltcoder.nunut.model.RouteModel;
import com.blackbeltcoder.nunut.util.StringUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainRegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainRegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout fragmentContainer;
    private EditText etHidden;
    private Switch switchIsDriver;
    private CustomTextView tvAccountExist, etHomeAddress, etOfficeAddress;
    private AutoCompleteTextView etHomePostal, etOfficePostal;
    private Button btnNext;
    private CustomInfoDialog dialogInfo, dialogWarning;
    private CustomProgressDialog dialogProgress;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDb;
    private DatabaseReference areaCodeRef;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> areaCodes;
    private ArrayAdapter<String> adapterAreaCode;
    private NuterModel nm;
    private RouteModel rmGlobal;
    private App app;

    private OnFragmentInteractionListener mListener;

    public MainRegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainRegisterFragment newInstance(String param1, String param2) {
        MainRegisterFragment fragment = new MainRegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_register, container, false);

        app = (App) getActivity().getApplicationContext();

        /*
        * FIREBASE
        * */
        firebaseDb = FirebaseDatabase.getInstance().getReference();

        areaCodes = new ArrayList<String>();
        areaCodeRef = firebaseDb.child(ConstantVariable.FIREBASEDB_AREA_CODE);
        Query qAreaCodeRef = areaCodeRef.orderByChild("areaName");
        qAreaCodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    HashMap<String, Object> obj = (HashMap<String, Object>) postSnapshot.getValue();
                    areaCodes.add("(" + obj.get("postalCode").toString() + ") " + obj.get("areaName").toString());

                    //Log.d("fb","String get: " + obj.get("areaName"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*areaCodeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Added",dataSnapshot.getValue(ListItemModel.class).toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("Changed",dataSnapshot.getValue(ListItemModel.class).toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Removed",dataSnapshot.getValue(ListItemModel.class).toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("Moved",dataSnapshot.getValue(ListItemModel.class).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Cancelled",databaseError.toString());
            }
        });*/

        fragmentContainer = (LinearLayout) rootView.findViewById(R.id.fragmentContainer);
        etHidden = (EditText) rootView.findViewById(R.id.etHidden);
        tvAccountExist = (CustomTextView) rootView.findViewById(R.id.tvAccountExist);

        etHomeAddress = (CustomTextView) rootView.findViewById(R.id.etHomeAddress);
        etHomePostal = (AutoCompleteTextView) rootView.findViewById(R.id.etHomePostal);
        etOfficeAddress = (CustomTextView) rootView.findViewById(R.id.etOfficeAddress);
        etOfficePostal = (AutoCompleteTextView) rootView.findViewById(R.id.etOfficePostal);
        switchIsDriver = (Switch) rootView.findViewById(R.id.switchIsDriver);

        btnNext = (Button) rootView.findViewById(R.id.btnNext);

        dialogInfo = new CustomInfoDialog(getActivity(),
                ConstantVariable.TITLE_INFO,
                getResources().getString(R.string.msg_login_failed),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInfo.dismiss();
                    }
                });

        dialogWarning = new CustomInfoDialog(getActivity(),
                ConstantVariable.TITLE_WARNING,
                getResources().getString(R.string.msg_mandatory),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogWarning.dismiss();
                    }
                });

        dialogProgress = new CustomProgressDialog(getActivity());

        loadObject();

        return rootView;
    }

    private void loadObject(){
        nm = App.getNuter();

        if(nm != null){
            etHomeAddress.setText(nm.homeAddress);
            etHomePostal.setText("(" + nm.homePostal + ") " + nm.homeArea);
            etOfficeAddress.setText(nm.officeAddress);
            etOfficePostal.setText("(" + nm.officePostal + ") " + nm.officeArea);

            if(nm.isDriver.equals("Y"))
                switchIsDriver.setChecked(true);
            else
                switchIsDriver.setChecked(false);
        } else {
            nm = new NuterModel();
        }

        etHomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getAddressFromMaps("HOME");
            }
        });

        etOfficeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getAddressFromMaps("OFFICE");
            }
        });

        tvAccountExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hashtable<String, String> param = new Hashtable<String, String>();
                new CustomFormDialog(getActivity(), param
                        , new CustomFormDialog.OnResultFormDialogListener() {

                    @Override
                    public void OnResultFormDialog(String email, String password) {
                        // TODO Auto-generated method stub
                        dialogProgress.show();

                        /*
                        * REST
                        * */
                        /*
                        NuterModel nmObj = new NuterModel();
                        nmObj.email = email;
                        nmObj.phone = phone;

                        Intent intent = new Intent(getActivity().getApplicationContext(), RestService.class);
                        intent.putExtra(ConstantVariable.BROADCAST_KEY, ConstantVariable.BROADCAST_KEY_LOGIN);
                        intent.putExtra(ConstantVariable.BROADCAST_OBJ, nmObj);
                        getActivity().startService(intent);
                        */

                        /*
                        * FIREBASE
                        * */
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(dialogProgress.isShowing())
                                            dialogProgress.dismiss();

                                        if (!task.isSuccessful()) {
                                            dialogInfo.show();
                                        } else {
                                            mListener.onFragmentInteraction(3);
                                        }
                                    }
                                });

                        /*firebaseAuth.createUserWithEmailAndPassword(email, "Ainozen0210")
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        dialogProgress.dismiss();
                                        if (!task.isSuccessful()) {
                                            dialogWarning.show();
                                        } else {
                                            dialogInfo.show();
                                        }
                                    }
                                });*/

                    }
                }).show();
            }
        });

        adapterAreaCode = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, areaCodes);
        etHomePostal.setAdapter(adapterAreaCode);
        etOfficePostal.setAdapter(adapterAreaCode);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveObject()){
                    rmGlobal = new RouteModel();
                    rmGlobal.postalOrigin = nm.homePostal;
                    rmGlobal.postalDestination = nm.officePostal;
                    rmGlobal.nameOrigin = nm.homeArea;
                    rmGlobal.nameDestination = nm.officeArea;
                    rmGlobal.vote = 1l;

                    /*
                    * REST
                    * */
                    /*Intent intent = new Intent(getActivity().getApplicationContext(), RestService.class);
                    intent.putExtra(ConstantVariable.BROADCAST_KEY, ConstantVariable.BROADCAST_KEY_CHECK);
                    intent.putExtra(ConstantVariable.BROADCAST_OBJ, rmGlobal);

                    getActivity().startService(intent);
                    dialogProgress.show();*/

                    /*
                    * FIREBASE
                    * */


                    mListener.onFragmentInteraction(rmGlobal);
                }
            }
        });

        etHidden.requestFocus();
    }

    private boolean saveObject(){
        String homeAddress = etHomeAddress.getText().toString();
        String[] homePostal = etHomePostal.getText().toString().replace("(", "").replace(")", "#").split("#");
        String officeAddress = etOfficeAddress.getText().toString();
        String[] officePostal = etOfficePostal.getText().toString().replace("(", "").replace(")", "#").split("#");

        if(StringUtil.isNotNull(homeAddress) && StringUtil.isNotNull(homePostal[0])
                && StringUtil.isNotNull(officeAddress) && StringUtil.isNotNull(officePostal[0])){
            nm.homeAddress = homeAddress;
            nm.homePostal = homePostal[0];
            nm.homeArea = homePostal[1];
            nm.officeAddress = officeAddress;
            nm.officePostal = officePostal[0];
            nm.officeArea = officePostal[1];
            nm.serverKey = "";
            nm.balance = 0l;
            nm.referralCode = "";

            if(switchIsDriver.isChecked())
                nm.isDriver = "Y";
            else
                nm.isDriver = "N";

            app.setNuter(nm);

            return true;
        } else {
            dialogWarning.show();

            return false;
        }
    }

    public void refreshField(String fieldName, String fieldValue) {
        if (fragmentContainer != null) {
            if(fieldName.equals("HOME")){
                etHomeAddress.setText(fieldValue);
            } else if(fieldName.equals("OFFICE")){
                etOfficeAddress.setText(fieldValue);
            }

            etHidden.requestFocus();
        }
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiverRoute);
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter iFilter = new IntentFilter(ConstantVariable.BROADCAST_KEY_LOGIN_RESULT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, iFilter);
        IntentFilter iFilterRoute = new IntentFilter(ConstantVariable.BROADCAST_KEY_CHECK_RESULT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiverRoute, iFilterRoute);
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String status = intent.getStringExtra(ConstantVariable.BROADCAST_STATUS);

            if (status != null) {
                dialogProgress.dismiss();
                if (status.equals("Y")) {
                    NuterModel nm = (NuterModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_KEY_LOGIN_OBJ);
                    app.setNuter(nm);
                } else {
                    dialogInfo.show();
                }
            }
        }
    };

    private BroadcastReceiver mMessageReceiverRoute = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String status = intent.getStringExtra(ConstantVariable.BROADCAST_STATUS);

            if (status != null) {
                dialogProgress.dismiss();
                if (status.equals("Y")) {
                    RouteModel rm = (RouteModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_KEY_CHECK_OBJ);
                    if(rm.id == null)
                        mListener.onFragmentInteraction(rmGlobal);
                    else
                        mListener.onFragmentInteraction(rm);
                } else {
                    mListener.onFragmentInteraction(rmGlobal);
                }
            }
        }
    };

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
            etHidden.requestFocus();
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(RouteModel rm);
        void getAddressFromMaps(String fieldName);
        void onFragmentInteraction(int pageMode);
    }
}
