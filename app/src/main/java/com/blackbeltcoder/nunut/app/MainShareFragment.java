package com.blackbeltcoder.nunut.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.component.CustomInfoDialog;
import com.blackbeltcoder.nunut.global.ConstantVariable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainShareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainShareFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout fragmentContainer;
    private ImageView ivShareFacebook, ivShareTwitter, ivShareMail, ivShareWhatsapp;
    private CustomInfoDialog dialogWarning;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainShareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainShareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainShareFragment newInstance(String param1, String param2) {
        MainShareFragment fragment = new MainShareFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main_share, container, false);

        fragmentContainer = (LinearLayout) rootView.findViewById(R.id.fragmentContainer);

        ivShareFacebook = (ImageView) rootView.findViewById(R.id.ivShareFacebook);
        ivShareTwitter = (ImageView) rootView.findViewById(R.id.ivShareTwitter);
        ivShareMail = (ImageView) rootView.findViewById(R.id.ivShareMail);
        ivShareWhatsapp = (ImageView) rootView.findViewById(R.id.ivShareWhatsapp);

        loadObject();

        dialogWarning = new CustomInfoDialog(getActivity(),
                ConstantVariable.TITLE_WARNING,
                getResources().getString(R.string.msg_install_first),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogWarning.dismiss();
                    }
                });

        return rootView;
    }

    private void loadObject(){
        ivShareFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                String application = "com.facebook.katana";

                boolean installed = appInstalledOrNot(application);
                if (installed) {
                    intent.setPackage(application);
                    startActivity(intent);
                } else {
                    dialogWarning.show();
                }
            }
        });

        ivShareTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                String application = "com.twitter.android";

                boolean installed = appInstalledOrNot(application);
                if (installed) {
                    intent.setPackage(application);
                    startActivity(intent);
                } else {
                    dialogWarning.show();
                }
            }
        });

        ivShareMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                String application = "com.google.android.gm";

                boolean installed = appInstalledOrNot(application);
                if (installed) {
                    intent.setPackage(application);
                    startActivity(intent);
                } else {
                    dialogWarning.show();
                }
            }
        });

        ivShareWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                String application = "com.whatsapp";

                boolean installed = appInstalledOrNot(application);
                if (installed) {
                    intent.setPackage(application);
                    startActivity(intent);
                } else {
                    dialogWarning.show();
                }
            }
        });
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
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
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
        void onFragmentInteraction(Uri uri);
    }
}
