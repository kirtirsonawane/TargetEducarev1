package com.targeteducare.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.targeteducare.Classes.Item;
import com.targeteducare.Classes.Question;
import com.targeteducare.R;

import java.util.ArrayList;

public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;

    //listeners
    private final ItemClickListener mItemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.layout_section;
    private static final int VIEW_TYPE_ITEM = R.layout.layout_item; //TODO : change this
    boolean isanswersheet = false;

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                          final GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener,
                                          SectionStateChangeListener sectionStateChangeListener, boolean isanswersheet) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;
        this.isanswersheet = isanswersheet;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof Section;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM:
                final Question item = (Question) mDataArrayList.get(position);
                holder.itemTextView.setText("" + item.getSrno());
                if (item.isIswrongAnswer())
                    holder.itemTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedtxtvisited));
                else if (item.isIsreview())
                    holder.itemTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedtxtreview));
                else if (item.isIsanswered())
                    holder.itemTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedtxtanswer));
                else if (item.isIswrongAnswer())
                    holder.itemTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedtxtvisited));
                else
                    holder.itemTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundedtxt1));
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(item);
                    }
                });
                break;
            case VIEW_TYPE_SECTION:
                final Section section = (Section) mDataArrayList.get(position);
                Item sectionitem = section.getItem();
                String s="";
                if (isanswersheet)
                 s = "Obtained Marks : (" + sectionitem.getObtainedmarks() + ")  Negative Marks : (" + sectionitem.getNegativemarks() + ")" + "  Total Question : (" + sectionitem.getQdata().size() + ")  Attempted Questions : (" + (sectionitem.getQdata().size() - sectionitem.getTotalnotanswered()) + ")  Not Attempted Questions : (" + sectionitem.getTotalnotanswered() + ")  Right Questions : (" + sectionitem.getTotalright() + ")  Wrong Questions : (" + sectionitem.getTotalwrong() + ")";
                else
                    s =  "Total Question :" + sectionitem.getQdata().size() + "  Attempted Questions : (" + (sectionitem.getQdata().size() - sectionitem.getTotalnotanswered()) + ")  Not Attempted Questions : (" + sectionitem.getTotalnotanswered() + ")";

                holder.sectionTextView.setText(section.getItem().getName() + " " + s);
                holder.sectionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(section);
                    }
                });

                holder.sectionToggleButton.setChecked(section.isExpanded);

                holder.sectionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.sectionToggleButton.setChecked(!holder.sectionToggleButton.isChecked());
                        mSectionStateChangeListener.onSectionStateChanged(section, holder.sectionToggleButton.isChecked());
                    }
                });

                holder.sectionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSectionStateChangeListener.onSectionStateChanged(section, isChecked);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        //common
        View view;
        int viewType;
        //for section
        TextView sectionTextView;
        ToggleButton sectionToggleButton;

        //for item
        TextView itemTextView;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                itemTextView = (TextView) view.findViewById(R.id.text_item);
            } else {
                sectionTextView = (TextView) view.findViewById(R.id.text_section);
                sectionToggleButton = (ToggleButton) view.findViewById(R.id.toggle_button_section);
            }
        }
    }
}
