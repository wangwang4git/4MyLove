package com.bbs.whu.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.BulletinReplyActivity;
import com.bbs.whu.model.BulletinBean;
import com.bbs.whu.utils.MyRegexParseUtils;

/**
 * ���������б�������������
 * ע�⣬�б�����������ʾ��ʽ���ֱ��ǡ��������ߡ��͡����ۡ�
 * 
 * @author double
 * 
 */

public class BulletinAdapter extends MyBaseAdapter {

	// ���岻ͬItem��ͼ�ı�־
	public static final int AUTHOR_ITEM = 0;
	public static final int COMMENT_ITEM = 1;

	public BulletinAdapter(Context context, ArrayList<BulletinBean> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return AUTHOR_ITEM;
		else
			return COMMENT_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		// ����Item��ͼ
		return 2;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// ����
		String title = ((BulletinBean) mItems.get(position)).getTitle();
		// ����
		String author = ((BulletinBean) mItems.get(position)).getAuthor();
		// ʱ��
		String datetime = ((BulletinBean) mItems.get(position)).getTime();

		// ����
		String text = ((BulletinBean) mItems.get(position)).getText();
		// ���ı���ʾ
		String newText = MyRegexParseUtils.getRichTextString(text);
		CharSequence textCharSequence = Html.fromHtml(newText,
				new ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						Drawable drawable = context.getResources().getDrawable(
								getResourceId(source));
						// ��仰��д����ȻͼƬ������ ������ʾ�����Ϊ0.
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
								drawable.getIntrinsicHeight());
						return drawable;
					}
				}, null);

		// ��Դ
		String source = ((BulletinBean) mItems.get(position)).getFrom();
		// �ظ�
		String reply = ((BulletinBean) mItems.get(position)).getReply();

		if (getItemViewType(position) == AUTHOR_ITEM) {
			// ��ȡ���������ߡ����ֵĿؼ�
			BulletinAuthorViewHolder holder = new BulletinAuthorViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bulletin_author_item, null);
				holder.holderBulletinAuthorTitle = (TextView) convertView
						.findViewById(R.id.bulletin_author_title);
				holder.holderBulletinAuthorAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_author_author);
				holder.holderBulletinAuthorDateTime = (TextView) convertView
						.findViewById(R.id.bulletin_author_datetime);
				holder.holderBulletinAuthorText = (TextView) convertView
						.findViewById(R.id.bulletin_author_text);
				holder.holderBulletinAuthorSource = (TextView) convertView
						.findViewById(R.id.bulletin_author_source);
				// ���ÿؼ�����convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinAuthorViewHolder) convertView.getTag();
			}
			// ���ؼ�
			holder.holderBulletinAuthorTitle.setText(title);
			holder.holderBulletinAuthorAuthor.setText(author);
			holder.holderBulletinAuthorDateTime.setText(datetime);

			// holder.holderBulletinAuthorText.setText(text);
			holder.holderBulletinAuthorText.setText(textCharSequence);
			// ����������ú�ؼӣ���Ȼû���κ�Ч��
			holder.holderBulletinAuthorText
					.setMovementMethod(LinkMovementMethod.getInstance());

			holder.holderBulletinAuthorSource.setText(source);

		} else if (getItemViewType(position) == COMMENT_ITEM) {
			// ��ȡ���ظ������ֵĿؼ�
			BulletinCommentViewHolder holder = new BulletinCommentViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bulletin_comment_item, null);
				holder.holderBulletinCommentAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_comment_author);
				holder.holderBulletinCommentContent = (TextView) convertView
						.findViewById(R.id.bulletin_comment_content);
				holder.holderBulletinCommentReplyLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.bulletin_comment_reply_linearLayout);
				holder.holderBulletinCommentReply = (TextView) convertView
						.findViewById(R.id.bulletin_comment_reply);
				holder.holderBulletinCommentSource = (TextView) convertView
						.findViewById(R.id.bulletin_comment_source);
				// ���ÿؼ�����convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinCommentViewHolder) convertView.getTag();
			}
			// ���ؼ�
			holder.holderBulletinCommentAuthor.setText(author);
			// if (reply.equals("null"))
			// holder.holderCommentReplyLinearLayout.setVisibility(View.GONE);
			// else
			holder.holderBulletinCommentReply.setText(reply);

			// holder.holderBulletinCommentContent.setText(text);
			holder.holderBulletinCommentContent.setText(textCharSequence);
			// ����������ú�ؼӣ���Ȼû���κ�Ч��
			holder.holderBulletinCommentContent
					.setMovementMethod(LinkMovementMethod.getInstance());

			holder.holderBulletinCommentSource.setText(source);
		}
		// ���ó�������
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final String[] choice = { "�ظ���¥", "�ظ�¥��" };
				// �����ظ��Ի���
				new AlertDialog.Builder(context)
						.setTitle("�б��")
						.setItems(choice,
								new DialogInterface.OnClickListener() {
									// ��Ӧ�б�ĵ���¼�
									public void onClick(DialogInterface dialog,
											int which) {
										// ����
										processLongClick(which, position);
									}
								}).show();
				return false;
			}
		});
		return convertView;
	}

	/**
	 * ����������Ӧ�¼�
	 * 
	 * @param which
	 *            �Ի����а��µ�ѡ�����
	 * @param position
	 *            ������item���б��е����
	 */

	private void processLongClick(int which, int position) {
		switch (which) {
		case 0:
			goToBulletinReply(position);
			break;
		case 1:
			// �ظ�¥�����ظ���һ��item
			goToBulletinReply(0);
			break;
		}
	}

	/**
	 * ��ת�����۽���
	 * 
	 * @param position
	 *            �������������б��е����
	 */
	private void goToBulletinReply(int position) {
		String itemBoard = ((BulletinBean) mItems.get(position)).getBoard();
		String itemId = ((BulletinBean) mItems.get(position)).getId();
		String itemTitle = ((BulletinBean) mItems.get(position)).getTitle();
		String itemAuthor = ((BulletinBean) mItems.get(position)).getAuthor();
		String itemBody = ((BulletinBean) mItems.get(position)).getText();
		String itemSign = ((BulletinBean) mItems.get(position)).getSign();

		// ��ת�����ӻظ�����
		Intent intent = new Intent(context, BulletinReplyActivity.class);

		// ��Ӳ���
		intent.putExtra("head", "���ӻظ�");
		intent.putExtra("board", itemBoard);
		intent.putExtra("id", itemId);
		intent.putExtra("title", itemTitle);
		intent.putExtra("author", itemAuthor);
		intent.putExtra("content", itemBody);
		intent.putExtra("signature", itemSign);

		// ����Activity��������һ��intend����
		context.startActivity(intent);
	}

	/**
	 * �����޷�ֱ��ʹ���ļ���������res/drawable�е�ͼ����Դ�� �������÷��似�� ��R.drawable����ͨ��ͼ����Դ�ļ�����
	 * ȥ�����Ӧ��ͼ����ԴID��ʵ�ִ������£�
	 */
	public static int getResourceId(String name) {
		int id = 0;
		try {
			Field field = R.drawable.class.getField(name);
			id = Integer.parseInt(field.get(null).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}
