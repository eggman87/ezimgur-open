package com.ezimgur.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            convertView = UiBuilder.inflate(viewGroup.getContext(), R.layout.view_notification, null);

            viewHolder = new ViewHolder();
            viewHolder.txtBody = (TextView) convertView.findViewById(R.id.v_not_txt_body);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.v_not_txt_title);
            viewHolder.txtSubject = (TextView) convertView.findViewById(R.id.v_not_txt_subject);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.v_not_txt_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message message = mMessages.get(pos);
        viewHolder.txtBody.setText(message.body);
        viewHolder.txtTitle.setText("from " +message.from);
        viewHolder.txtSubject.setText(message.subject);
        viewHolder.txtDate.setText(message.timeStamp);

        return convertView;
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtBody;
        TextView txtSubject;
        TextView txtDate;
    }

}
