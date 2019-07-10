package io.jawg.osmcontributor.ui.adapters.binding;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.jawg.osmcontributor.OsmTemplateApplication;
import io.jawg.osmcontributor.R;
import io.jawg.osmcontributor.ui.adapters.OpeningMonthAdapter;
import io.jawg.osmcontributor.ui.adapters.item.opening.OpeningMonth;
import io.jawg.osmcontributor.ui.adapters.item.opening.OpeningTime;
import io.jawg.osmcontributor.ui.adapters.item.shelter.TagItem;
import io.jawg.osmcontributor.ui.adapters.parser.OpeningTimeValueParser;
import io.jawg.osmcontributor.ui.adapters.parser.ParserManager;
import io.jawg.osmcontributor.ui.utils.views.DividerItemDecoration;
import io.jawg.osmcontributor.ui.utils.views.holders.TagItemOpeningTimeViewHolder;

public class OpeningHoursViewBinder extends CheckedTagViewBinder<TagItemOpeningTimeViewHolder, TagItem> {

    @Inject
    OpeningTimeValueParser openingTimeValueParser;

    public OpeningHoursViewBinder(FragmentActivity activity, TagItemChangeListener tagItemChangeListener) {
        super(activity, tagItemChangeListener);
        ((OsmTemplateApplication) activity.getApplication()).getOsmTemplateComponent().inject(this);
    }

    @Override
    public boolean supports(TagItem.Type type) {
        return TagItem.Type.OPENING_HOURS.equals(type) || TagItem.Type.TIME.equals(type);
    }

    @Override
    public void onBindViewHolder(TagItemOpeningTimeViewHolder holder, TagItem tagItem) {
        // Save holder
        this.content = holder.getContent();

        holder.getTextViewKey().setText(ParserManager.parseTagName(tagItem.getKey(), holder.getContent().getContext()));

        OpeningTime openingTime = null;
        try {
            openingTime = openingTimeValueParser.fromValue(tagItem.getValue());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            tagItem.setConform(false);
        }

        if (openingTime == null) {
            openingTime = new OpeningTime();
        }

        final OpeningMonthAdapter adapter = new OpeningMonthAdapter(openingTime, activity.get());
        adapter.setTime(tagItem.getValue());
        if (tagItem.getTagType() == TagItem.Type.TIME) {
            adapter.hideMonth(true);
        }
        holder.getOpeningTimeRecyclerView().setAdapter(adapter);
        holder.getOpeningTimeRecyclerView().setLayoutManager(new LinearLayoutManager(activity.get()));
        holder.getOpeningTimeRecyclerView().setHasFixedSize(false);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(300);
        holder.getOpeningTimeRecyclerView().setItemAnimator(itemAnimator);
        holder.getOpeningTimeRecyclerView().addItemDecoration(new DividerItemDecoration(activity.get()));

        final OpeningTime finalOpeningTime = openingTime;
        holder.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalOpeningTime.addOpeningMonth(new OpeningMonth());
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
            }
        });

        showInvalidityMessage(tagItem);
    }

    @Override
    public TagItemOpeningTimeViewHolder onCreateViewHolder(ViewGroup parent) {
        View poiTagOpeningHoursLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item_opening_time, parent, false);
        return new TagItemOpeningTimeViewHolder(poiTagOpeningHoursLayout);
    }
}
