package com.blackbeltcoder.nunut.app;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.component.CustomInfoDialog;
import com.blackbeltcoder.nunut.component.CustomProgressDialog;
import com.blackbeltcoder.nunut.component.CustomTextView;
import com.blackbeltcoder.nunut.contract.RouteContract;
import com.blackbeltcoder.nunut.global.App;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.model.NuterModel;
import com.blackbeltcoder.nunut.model.RouteModel;
import com.blackbeltcoder.nunut.service.RestService;
import com.blackbeltcoder.nunut.util.ConverterUtil;
import com.blackbeltcoder.nunut.util.StringUtil;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainRegisterRouteCheckFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainRegisterRouteCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainRegisterRouteCheckFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout fragmentContainer;
    private CustomTextView tvTitle1, tvRoute, tvVoteLabel;
    private ArcProgress apVote;
    private EditText etHidden, etEmailValue, etPhoneValue;
    private Button btnPrev, btnNext;
    private CustomInfoDialog dialogWarning, dialogWarning2;
    private CustomProgressDialog dialogProgress;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NuterModel nm;
    private RouteModel rmGlobal;
    private App app;

    private OnFragmentInteractionListener mListener;

    public MainRegisterRouteCheckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainRegisterRouteCheckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainRegisterRouteCheckFragment newInstance(String param1, String param2) {
        MainRegisterRouteCheckFragment fragment = new MainRegisterRouteCheckFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_register_route_check, container, false);
        app = (App) getActivity().getApplicationContext();

        fragmentContainer = (LinearLayout) rootView.findViewById(R.id.fragmentContainer);

        tvTitle1 = (CustomTextView) rootView.findViewById(R.id.tvTitle1);
        tvRoute = (CustomTextView) rootView.findViewById(R.id.tvRoute);
        tvVoteLabel = (CustomTextView) rootView.findViewById(R.id.tvVoteLabel);
        apVote = (ArcProgress) rootView.findViewById(R.id.apVote);

        etHidden = (EditText) rootView.findViewById(R.id.etHidden);
        etEmailValue = (EditText) rootView.findViewById(R.id.etEmailValue);
        etPhoneValue = (EditText) rootView.findViewById(R.id.etPhoneValue);

        btnPrev = (Button) rootView.findViewById(R.id.btnPrev);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);

        etHidden.requestFocus();

        loadObject();

        dialogWarning = new CustomInfoDialog(getActivity(),
                ConstantVariable.TITLE_WARNING,
                getResources().getString(R.string.msg_mandatory),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogWarning.dismiss();
                    }
                });

        dialogWarning2 = new CustomInfoDialog(getActivity(),
                ConstantVariable.TITLE_WARNING,
                getResources().getString(R.string.msg_register_failed),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogWarning2.dismiss();
                    }
                });

        dialogProgress = new CustomProgressDialog(getActivity());

        return rootView;
    }

    private void loadObject() {
        nm = App.getNuter();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveObject()) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), RestService.class);
                    intent.putExtra(ConstantVariable.BROADCAST_KEY, ConstantVariable.BROADCAST_KEY_REGISTER);
                    intent.putExtra(ConstantVariable.BROADCAST_KEY_REGISTER_OBJ, nm);
                    intent.putExtra(ConstantVariable.BROADCAST_KEY_ROUTES_OBJ, rmGlobal);
                    getActivity().startService(intent);
                    dialogProgress.show();
                }
                //mListener.onFragmentInteraction(3);
            }
        });

        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        etPhoneValue.setText(tMgr.getLine1Number());

        etEmailValue.setText(getUsername());
    }

    public String getUsername() {
        List<String> possibleEmails = null;
        try {
            AccountManager manager = AccountManager.get(getActivity());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "test";
            }
            Account[] accounts = manager.getAccountsByType("com.google");
            possibleEmails = new LinkedList<>();

            for (Account account : accounts) {
                // TODO: Check possibleEmail against an email regex or treat
                // account.name as an email address only for certain account.type
                // values.
                possibleEmails.add(account.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (possibleEmails != null) {
                possibleEmails.clear();
            }
        }

        if (possibleEmails != null) {
            if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
                String email = possibleEmails.get(0);
                /*String[] parts = email.split("@");
                if (parts.length > 0 && parts[0] != null) {
                    return parts[0];

                } else {
                    return "test2";
                }*/

                return email;
            } else {
                return "test3";
            }
        } else {
            return "test4";
        }
    }

    private boolean saveObject(){
        String emailVal = etEmailValue.getText().toString();
        String phoneVal = etPhoneValue.getText().toString();

        if(StringUtil.isNotNull(emailVal) && StringUtil.isNotNull(phoneVal)){
            nm.email = emailVal;
            nm.phone = phoneVal;

            app.setNuter(nm);

            return true;
        } else {
            dialogWarning.show();

            return false;
        }
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter iFilter = new IntentFilter(ConstantVariable.BROADCAST_KEY_REGISTER_RESULT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, iFilter);
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String status = intent.getStringExtra(ConstantVariable.BROADCAST_STATUS);
            //Log.d("receiver", "Got message: " + status);

            if (status != null) {
                dialogProgress.dismiss();
                if (status.equals("Y")) {
                    NuterModel nm = (NuterModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_KEY_REGISTER_OBJ);
                    app.setNuter(nm);

                    RouteModel rm = (RouteModel) intent.getSerializableExtra(ConstantVariable.BROADCAST_KEY_ROUTES_OBJ);
                    if(rmGlobal.id != null){
                        rmGlobal.vote = rmGlobal.vote + 1;
                        getActivity().getContentResolver().update(
                                RouteContract.CONTENT_URI,
                                ConverterUtil.ObjecttoContentValue(rmGlobal),
                                RouteContract._ID + " = ? ",
                                new String[]{String.valueOf(rmGlobal._id)});
                    } else {
                        Uri insertedUri = getActivity().getContentResolver().insert(
                                RouteContract.CONTENT_URI,
                                ConverterUtil.ObjecttoContentValue(rm)
                        );
                        rmGlobal._id = ContentUris.parseId(insertedUri);
                    }

                    mListener.onFragmentInteraction(3);
                } else {
                    dialogWarning2.show();
                }
            }
        }
    };

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed(RouteModel rm) {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);

            rmGlobal = rm;

            if(rm.id != null){
                tvTitle1.setVisibility(View.GONE);
            }

            tvRoute.setText(rm.originArea + " - " + rm.destinationArea);
            tvVoteLabel.setText(rm.vote + " of 500 votes");

            int voteVal = (int) (rm.vote * 100) / 500;
            apVote.setProgress(voteVal);

            if(voteVal >= 0 && voteVal < 50){
                apVote.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.colorProgressRed));
                apVote.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
            } else if(voteVal >= 50 && voteVal < 70){
                apVote.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.colorProgressYellow));
                apVote.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
            } else if(voteVal >= 70){
                apVote.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.colorProgressGreen));
                apVote.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
            }
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
        void onFragmentInteraction(int pageMode);
    }
}
