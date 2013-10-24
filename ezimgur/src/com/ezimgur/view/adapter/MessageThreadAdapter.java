package com.ezimgur.view.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.datacontract.Message;
import com.ezimgur.view.builder.URLSpanConverter;
import com.ezimgur.view.builder.UiBuilder;
import com.ezimgur.view.utils.RichTextUtils;

import java.util.List;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 7:05 PM
 */
public class MessageThreadAdapter extends BaseAdapter {

    private List<Message> mMessages;

    public MessageThreadAdapter(List<Message> messages) {
        mMessages = messages;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Message getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMessages.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = UiBuilder.inflate(parent.getContext(), R.layout.view_message_thread, null);

            viewHolder = new ViewHolder();
            viewHolder.container = (RelativeLayout) convertView.findViewById(R.id.v_msg_thread_container);
            viewHolder.txtBody = (TextView) convertView.findViewById(R.id.v_msg_thread_body);
            viewHolder.txtFrom = (TextView) convertView.findViewById(R.id.v_msg_thread_from);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.v_msg_thread_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message message = mMessages.get(position);
        viewHolder.txtFrom.setText("from " +message.from);

        SpannableString spanBody = new SpannableString(message.body);
        viewHolder.txtBody.setText(spanBody);
        viewHolder.txtBody.setText(RichTextUtils.replaceAll((Spanned) viewHolder.txtBody.getText(), URLSpan.class, new URLSpanConverter()));
        viewHolder.txtDate.setText(message.timeStamp);

        if ((position % 2) > 0)
            viewHolder.container.setBackgroundResource(R.color.LightGrey);
        else
            viewHolder.container.setBackgroundResource(android.R.color.transparent);

        return convertView;
    }

    static class ViewHolder {
        RelativeLayout container;
        TextView txtFrom;
        TextView txtBody;
        TextView txtDate;
    }
}
