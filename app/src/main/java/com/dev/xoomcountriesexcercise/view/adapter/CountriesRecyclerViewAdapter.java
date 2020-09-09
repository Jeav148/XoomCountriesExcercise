package com.dev.xoomcountriesexcercise.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.dev.xoomcountriesexcercise.R;
import com.dev.xoomcountriesexcercise.model.CountryDTO;
import com.dev.xoomcountriesexcercise.view.CountriesActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.RecyclerView.*;

public class CountriesRecyclerViewAdapter extends Adapter<CountriesRecyclerViewAdapter.CountryViewHolder> {

    private List<CountryDTO> mDataSet;
    private Context mContext;
    private final String mFlagType;

    private final String COUNTRIES_FLAG_BASE_URL = "https://www.countryflags.io/%s/%s/64.png";

    public CountriesRecyclerViewAdapter(Context context, CountriesActivity.FlagType flagType, List<CountryDTO> dataSet) {
        this.mContext = context;
        this.mFlagType = flagType.toString();
        this.mDataSet = dataSet;
    }

    public void addCountriesToDataSet(@NonNull List<CountryDTO> dataSet){
        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        final CountryDTO countryData = mDataSet.get(position);
        String countryFlagUrl = getFlagUrl(countryData.getCode());
        holder.countryName.setText(countryData.getName());
        Glide.with(mContext).load(countryFlagUrl).into(holder.countryFlag);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private String getFlagUrl(String countryCode){
        return String.format(COUNTRIES_FLAG_BASE_URL, countryCode, mFlagType);
    }

    static class CountryViewHolder extends ViewHolder{

        @BindView(R.id.country_flag) ImageView countryFlag;
        @BindView(R.id.country_name) TextView countryName;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
