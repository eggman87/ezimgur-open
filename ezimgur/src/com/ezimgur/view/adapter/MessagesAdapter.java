package com.ezimgur.view.adapter;

import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.datacontract.Message;
import com.ezimgur.view.builder.UiBuilder;

import java.util.List;

/**
 *
 * User: matthewharris
 * Date: 5/25/13
 * Time: 11:20 AM
 */
public class MessagesAdapter extends BaseAdapter {

    private List<Message> mMessages;

    public MessagesAdapter(List<Message> messages){
        mMessages = messages;
    }

    public void updateDateSet(List<Message> messages){
        mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Message getItem(int pos) {
        return mMessages.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return mMessages.get(pos).id;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = UiBuilder.inflate(viewGroup.getContext(), R.layout.view_message_thread, null);

            viewHolder = new ViewHolder();
            viewHolder.txtBody = (TextView) convertView.findViewById(R.id.v_msg_thread_tv_body);
            viewHolder.txtFrom = (TextView) convertView.findViewById(R.id.v_msg_thread_tv_from);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.v_msg_thread_tv_date);
            viewHolder.threadContainer = (RelativeLayout) convertView.findViewById(R.id.v_msg_thread_rl_container);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message message = mMessages.get(pos);
        CharSequence time = DateUtils.getRelativeTimeSpanString(message.datetime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        viewHolder.txtDate.setText(time);
        viewHolder.txtBody.setText(message.body);
        viewHolder.txtFrom.setText(message.from);
        return convertView;
    }

    static class ViewHolder {
        TextView txtBody;
        TextView txtFrom;
        TextView txtDate;
        RelativeLayout threadContainer;
    }

}
