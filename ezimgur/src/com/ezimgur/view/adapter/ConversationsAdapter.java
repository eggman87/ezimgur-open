package com.ezimgur.view.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.datacontract.Conversation;
import com.ezimgur.view.builder.UiBuilder;

import java.util.List;

/**
 * Created by mharris on 1/16/15.
 *
 */
public class ConversationsAdapter extends BaseAdapter {

    private List<Conversation> conversations;

    public ConversationsAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Conversation getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return conversations.get(position).id;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        Context context = parent.getContext();
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = UiBuilder.inflate(context, R.layout.view_conversation, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.v_conversation_tv_title);
            holder.txtDate = (TextView) view.findViewById(R.id.v_conversation_tv_date);
            holder.txtSubject = (TextView) view.findViewById(R.id.v_conversation_tv_subject);
            holder.txtBody = (TextView) view.findViewById(R.id.v_conversation_tv_body);

            view.setTag(holder);
        }

        Conversation conversation = conversations.get(position);

        holder.txtTitle.setText("convo with " + conversation.withAccountUsername);
        CharSequence time = DateUtils.getRelativeTimeSpanString(conversation.datetime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.txtDate.setText(time);
        holder.txtBody.setText(conversation.lastMessagePreview);
        holder.txtSubject.setText(conversation.messageCount + " messages");

        return view;
    }

    public void updateDataSet(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtDate;
        TextView txtSubject;
        TextView txtBody;

    }
}