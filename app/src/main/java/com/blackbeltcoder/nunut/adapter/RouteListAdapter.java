package com.blackbeltcoder.nunut.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.arlib.floatingsearchview.util.Util;
import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.component.CustomInfoDialog;
import com.blackbeltcoder.nunut.component.CustomTextView;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.model.RouteModel;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ainozenbook on 9/20/2016.
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> implements Filterable {

    private List<RouteModel> dataSet = new ArrayList<RouteModel>();
    private List<RouteModel> dataSetOriginal = new ArrayList<RouteModel>();
    private ItemFilter filter;
    private Context context;
    private final OnItemClickListener listener;

    private int mLastAnimatedItemPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(RouteModel item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvTitle1, tvTitle2, tvVoteLabel;
        public ImageView ivShareFacebook, ivShareTwitter, ivShareMail, ivShareWhatsapp;
        public ArcProgress apVote;
        public CustomInfoDialog dialogWarning;
        public Context c;

        public ViewHolder(View v, Context c) {
            super(v);
            this.c = c;
            tvTitle1 = (CustomTextView) v.findViewById(R.id.tvTitle1);
            tvTitle2 = (CustomTextView) v.findViewById(R.id.tvTitle2);
            apVote = (ArcProgress) v.findViewById(R.id.apVote);
            tvVoteLabel = (CustomTextView) v.findViewById(R.id.tvVoteLabel);
            ivShareFacebook = (ImageView) v.findViewById(R.id.ivShareFacebook);
            ivShareTwitter = (ImageView) v.findViewById(R.id.ivShareTwitter);
            ivShareMail = (ImageView) v.findViewById(R.id.ivShareMail);
            ivShareWhatsapp = (ImageView) v.findViewById(R.id.ivShareWhatsapp);

            dialogWarning = new CustomInfoDialog(c,
                    ConstantVariable.TITLE_WARNING,
                    c.getResources().getString(R.string.msg_install_first),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogWarning.dismiss();
                        }
                    });
        }

        public void bind(final RouteModel item, final OnItemClickListener listener) {
            tvTitle1.setText(item.nameOrigin);
            tvTitle2.setText(item.nameDestination);
            tvVoteLabel.setText(String.valueOf(item.vote) + " of " + String.valueOf(ConstantVariable.VOTE_MAX_DEFAULT) + " votes");

            int voteVal = (int) (item.vote * 100) / 500;
            apVote.setProgress(voteVal);

            if(voteVal >= 0 && voteVal < 50){
                apVote.setFinishedStrokeColor(c.getResources().getColor(R.color.colorProgressRed));
                apVote.setTextColor(c.getResources().getColor(R.color.colorPrimary));
            } else if(voteVal >= 50 && voteVal < 70){
                apVote.setFinishedStrokeColor(c.getResources().getColor(R.color.colorProgressYellow));
                apVote.setTextColor(c.getResources().getColor(R.color.colorPrimary));
            } else if(voteVal >= 70){
                apVote.setFinishedStrokeColor(c.getResources().getColor(R.color.colorProgressGreen));
                apVote.setTextColor(c.getResources().getColor(R.color.colorPrimary));
            }

            ivShareFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                    String application = "com.facebook.katana";

                    boolean installed = appInstalledOrNot(application);
                    if (installed) {
                        intent.setPackage(application);
                        c.startActivity(intent);
                    } else {
                        dialogWarning.show();
                    }
                }
            });

            ivShareTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                    String application = "com.twitter.android";

                    boolean installed = appInstalledOrNot(application);
                    if (installed) {
                        intent.setPackage(application);
                        c.startActivity(intent);
                    } else {
                        dialogWarning.show();
                    }
                }
            });

            ivShareMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                    String application = "com.google.android.gm";

                    boolean installed = appInstalledOrNot(application);
                    if (installed) {
                        intent.setPackage(application);
                        c.startActivity(intent);
                    } else {
                        dialogWarning.show();
                    }
                }
            });

            ivShareWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "NUNUT");

                    String application = "com.whatsapp";

                    boolean installed = appInstalledOrNot(application);
                    if (installed) {
                        intent.setPackage(application);
                        c.startActivity(intent);
                    } else {
                        dialogWarning.show();
                    }
                }
            });

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });*/
        }

        private boolean appInstalledOrNot(String uri) {
            PackageManager pm = c.getPackageManager();
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
    }

    public RouteListAdapter(List<RouteModel> dataSet, Context context, OnItemClickListener listener) {
        this.dataSet = dataSet;
        this.dataSetOriginal = dataSet;
        this.context = context;
        this.listener = listener;
        filter = new ItemFilter();
    }

    @Override
    public RouteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_list, parent, false);
        ViewHolder vh = new ViewHolder(v, context);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(dataSet.get(position), listener);

        if(mLastAnimatedItemPosition < position){
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public List<RouteModel> getAllItem() {
        return dataSet;
    }

    public List<RouteModel> getOriginalItem() {
        return dataSetOriginal;
    }

    public void swap(List<RouteModel> dataSet){
        this.dataSet = dataSet;

        if(dataSetOriginal.size() == 0)
            dataSetOriginal = dataSet;

        notifyDataSetChanged();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return filter;
    }

    private class ItemFilter extends Filter {

        private long sortValueGlobal;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //Log.d("visit adapter", "query : " + constraint.toString());
            FilterResults results = new FilterResults();

            List<RouteModel> nlist = new ArrayList<RouteModel>();
            if (constraint.length() == 0) {
                sortValueGlobal = 0l;
                nlist.addAll(dataSetOriginal);
            } else {
                String[] stringParam = constraint.toString().split("#");
                sortValueGlobal = Long.valueOf(stringParam[1]);
                String filterString = stringParam[0].toLowerCase();
                int count = dataSetOriginal.size();
                //Log.d("visit adapter", " query count : " + count);
                for (int i = 0; i < count; i++) {
                    if (dataSetOriginal.get(i).nameOrigin.toLowerCase().contains(filterString)) {
                        nlist.add(dataSetOriginal.get(i));
                    } else if (dataSetOriginal.get(i).nameDestination.toLowerCase().contains(filterString)) {
                        nlist.add(dataSetOriginal.get(i));
                    }
                }
            }
            //Log.d("visit adapter", " nlist count : " + nlist.size());
            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<RouteModel> pms = (List<RouteModel>) results.values;

            if(sortValueGlobal == 0l || sortValueGlobal == 1l) {
                Comparator<RouteModel> comp = new Comparator<RouteModel>() {
                    @Override
                    public int compare(RouteModel routeModel, RouteModel t1) {
                        return t1.nameOrigin.compareTo(routeModel.nameOrigin);
                    }
                };
                Collections.sort(pms, comp);

                if(sortValueGlobal == 0l)
                    Collections.reverse(pms);
            } else if(sortValueGlobal == 2l) {
                Comparator<RouteModel> comp = new Comparator<RouteModel>() {
                    @Override
                    public int compare(RouteModel routeModel, RouteModel t1) {
                        return t1.vote.compareTo(routeModel.vote);
                    }
                };
                Collections.sort(pms, comp);
                Collections.reverse(pms);
            } else if(sortValueGlobal == 3l) {
                Comparator<RouteModel> comp = new Comparator<RouteModel>() {
                    @Override
                    public int compare(RouteModel routeModel, RouteModel t1) {
                        return t1.vote.compareTo(routeModel.vote);
                    }
                };
                Collections.sort(pms, comp);
            }

            dataSet = pms;
            notifyDataSetChanged();
        }

    }
}
