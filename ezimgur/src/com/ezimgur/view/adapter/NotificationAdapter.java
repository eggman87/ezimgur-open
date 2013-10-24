package com.ezimgur.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.Message;
import com.ezimgur.datacontract.Notification;
import com.ezimgur.view.builder.UiBuilder;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/29/12
 * Time: 11:54 AM
 */
public class NotificationAdapter extends BaseAdapter {

    private List<Notification> mNotifications;

    public NotificationAdapter(List<Notification> notifications) {
        mNotifications = notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        mNotifications = notifications;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNotifications.size();
    }

    @Override
    public Notification getItem(int position) {
        return mNotifications.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
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

        Notification targetNote = mNotifications.get(position);
        if (targetNote.content instanceof Message) {
            Message targetMessage = (Message) targetNote.content;
            viewHolder.txtBody.setText(targetMessage.body);
            viewHolder.txtTitle.setText("from " +targetMessage.from);
            viewHolder.txtSubject.setText(targetMessage.subject);
            viewHolder.txtDate.setText(targetMessage.timeStamp);

        } else {
            Comment targetComment = (Comment) targetNote.content;
            viewHolder.txtBody.setText(targetComment.comment);
            viewHolder.txtTitle.setText("from "+targetComment.author);
            viewHolder.txtSubject.setText("reply to image comment " + targetComment.imageId);
            viewHolder.txtDate.setText("");

        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtBody;
        TextView txtSubject;
        TextView txtDate;
    }

}
