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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.BulletinReplyActivity;
import com.bbs.whu.activity.PersonActivity;
import com.bbs.whu.activity.TopicActivity;
import com.bbs.whu.model.BulletinBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyRegexParseUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 帖子详情列表数据适配器，
 * 注意，列表中有两种显示形式，分别是“帖子作者”和“评论”
 * 
 * @author double
 * 
 */

public class BulletinAdapter extends MyBaseAdapter {

	// 图片异步下载下载器
	private ImageLoader imageLoader;
	// 图片异步下载缓存设置变量
	private DisplayImageOptions options;

	/*帖子版块信息，TopTen跳转使用*/
	private String board;// 帖子英文名
	private String boardName;// 帖子中文名

	// 定义不同Item视图的标志
	public static final int AUTHOR_ITEM = 0;
	public static final int COMMENT_ITEM = 1;

	public BulletinAdapter(Context context, ArrayList<BulletinBean> items,
			int rLayoutList, ImageLoader imageLoader,
			DisplayImageOptions options) {
		super(context, items, rLayoutList);
		this.imageLoader = imageLoader;
		this.options = options;
	}

	public BulletinAdapter(Context context, ArrayList<BulletinBean> items,
			int rLayoutList, ImageLoader imageLoader,
			DisplayImageOptions options, String board, String boardName) {
		super(context, items, rLayoutList);
		this.imageLoader = imageLoader;
		this.options = options;
		this.board = board;
		this.boardName = boardName;
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
		// 两种Item视图
		return 2;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 标题
		String title = ((BulletinBean) mItems.get(position)).getTitle();
		// 作者
		String author = ((BulletinBean) mItems.get(position)).getAuthor();
		// 作者头像
		String userfaceImg = ((BulletinBean) mItems.get(position))
				.getUserfaceImg();
		// 时间
		String datetime = ((BulletinBean) mItems.get(position)).getTime();

		// 内容
		String text = ((BulletinBean) mItems.get(position)).getText();
		// 富文本显示
		String newText = MyRegexParseUtils.getRichTextString(text);
		CharSequence textCharSequence = Html.fromHtml(newText,
				new ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						Drawable drawable = context.getResources().getDrawable(
								getResourceId(source));
						// 这句话必写，不然图片是有了 不过显示的面积为0.
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
								drawable.getIntrinsicHeight());
						return drawable;
					}
				}, null);

		// 来源
		String source = ((BulletinBean) mItems.get(position)).getFrom();
		// 回复
		String reply = ((BulletinBean) mItems.get(position)).getReply();

		// 长按事件监听
		OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// 是否是匿名用户
				final boolean isAnonymous = MyApplication.getInstance()
						.getName().equals("4MyLove");
				final boolean notTopTen = board == null || board.equals("");
				// 如果是匿名用户，不能回复
				String[] choices;
				if (isAnonymous) {
					if (notTopTen) {
						choices = new String[1];
						choices[0] = "查看作者资料";
					} else {
						choices = new String[2];
						choices[0] = "查看作者资料";
						choices[1] = "跳转到版块";
					}
				} else {
					if (notTopTen) {
						choices = new String[3];
						choices[0] = "回复本楼";
						choices[1] = "回复楼主";
						choices[2] = "查看作者资料";
					} else {
						choices = new String[4];
						choices[0] = "回复本楼";
						choices[1] = "回复楼主";
						choices[2] = "查看作者资料";
						choices[3] = "跳转到版块";
					}
				}
				// 弹出回复对话框
				new AlertDialog.Builder(context)
						.setTitle("列表框")
						.setItems(choices,
								new DialogInterface.OnClickListener() {
									// 响应列表的点击事件
									public void onClick(DialogInterface dialog,
											int which) {
										// 处理
										processLongClick(which, position,
												isAnonymous, notTopTen);
									}
								}).show();
				return false;
			}
		};

		if (getItemViewType(position) == AUTHOR_ITEM) {
			// 获取“帖子作者”部分的控件
			BulletinAuthorViewHolder holder = new BulletinAuthorViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bulletin_author_item, null);
				holder.holderBulletinAuthorTitle = (TextView) convertView
						.findViewById(R.id.bulletin_author_title);
				holder.holderBulletinAuthorAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_author_author);
				holder.holderBulletinAuthorIcon = (ImageView) convertView
						.findViewById(R.id.bulletin_author_icon);
				holder.holderBulletinAuthorDateTime = (TextView) convertView
						.findViewById(R.id.bulletin_author_datetime);
				holder.holderBulletinAuthorText = (TextView) convertView
						.findViewById(R.id.bulletin_author_text);
				holder.holderBulletinAuthorText
						.setOnLongClickListener(mOnLongClickListener);
				holder.holderBulletinAuthorSource = (TextView) convertView
						.findViewById(R.id.bulletin_author_source);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinAuthorViewHolder) convertView.getTag();
			}
			// 填充控件
			holder.holderBulletinAuthorTitle.setText(title);
			holder.holderBulletinAuthorAuthor.setText(author);
			imageLoader.displayImage(MyConstants.HEAD_URL + userfaceImg,
					holder.holderBulletinAuthorIcon, options);
			holder.holderBulletinAuthorDateTime.setText(datetime);

			// holder.holderBulletinAuthorText.setText(text);
			holder.holderBulletinAuthorText.setText(textCharSequence);
			// 该语句在设置后必加，不然没有任何效果
			holder.holderBulletinAuthorText
					.setMovementMethod(LinkMovementMethod.getInstance());

			holder.holderBulletinAuthorSource.setText(source);

		} else if (getItemViewType(position) == COMMENT_ITEM) {
			// 获取“回复”部分的控件
			BulletinCommentViewHolder holder = new BulletinCommentViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bulletin_comment_item, null);
				holder.holderBulletinCommentAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_comment_author);
				holder.holderBulletinCommentAuthorIcon = (ImageView) convertView
						.findViewById(R.id.bulletin_comment_author_icon);
				holder.holderBulletinCommentContent = (TextView) convertView
						.findViewById(R.id.bulletin_comment_content);
				holder.holderBulletinCommentTime = (TextView) convertView
						.findViewById(R.id.bulletin_comment_time);
				holder.holderBulletinCommentContent
						.setOnLongClickListener(mOnLongClickListener);
				holder.holderBulletinCommentReplyLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.bulletin_comment_reply_linearLayout);
				holder.holderBulletinCommentReply = (TextView) convertView
						.findViewById(R.id.bulletin_comment_reply);
				holder.holderBulletinCommentSource = (TextView) convertView
						.findViewById(R.id.bulletin_comment_source);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinCommentViewHolder) convertView.getTag();
			}
			// 填充控件
			holder.holderBulletinCommentAuthor.setText(author);
			holder.holderBulletinCommentTime.setText(datetime);
			imageLoader.displayImage(MyConstants.HEAD_URL + userfaceImg,
					holder.holderBulletinCommentAuthorIcon, options);
			// if (reply.equals("null"))
			// holder.holderCommentReplyLinearLayout.setVisibility(View.GONE);
			// else
			holder.holderBulletinCommentReply.setText(reply);

			// holder.holderBulletinCommentContent.setText(text);
			holder.holderBulletinCommentContent.setText(textCharSequence);
			// 该语句在设置后必加，不然没有任何效果
			holder.holderBulletinCommentContent
					.setMovementMethod(LinkMovementMethod.getInstance());

			holder.holderBulletinCommentSource.setText(source);
		}
		// 设置长按动作
		convertView.setOnLongClickListener(mOnLongClickListener);
		return convertView;
	}

	/**
	 * 处理长按的响应事件
	 * 
	 * @param which
	 *            对话框中按下的选项序号
	 * @param position
	 *            长按的item在列表中的序号
	 * @param isAnonymous
	 *            是否匿名登陆
	 */

	private void processLongClick(int which, int position, boolean isAnonymous,
			boolean notTopTen) {
		switch (which) {
		case 0:
			if (isAnonymous)
				// 跳转到个人资料界面
				goToPersonActivity(position);
			else
				// 跳转到回复界面
				goToBulletinReply(position);
			break;
		case 1:
			if (isAnonymous && !notTopTen)
				// 跳转到版面
				goToTopic();
			else
				// 回复楼主即回复第一个item
				goToBulletinReply(0);
			break;
		case 2:
			// 跳转到个人资料界面
			goToPersonActivity(position);
			break;
		case 3:
			// 跳转到版面
			goToTopic();
			break;
		default:
			break;
		}
	}

	/**
	 * 跳转到评论界面
	 * 
	 * @param position
	 *            长按的数据在列表中的序号
	 */
	private void goToBulletinReply(int position) {
		String itemBoard = ((BulletinBean) mItems.get(position)).getBoard();
		String itemId = ((BulletinBean) mItems.get(position)).getId();
		String itemTitle = ((BulletinBean) mItems.get(position)).getTitle();
		String itemAuthor = ((BulletinBean) mItems.get(position)).getAuthor();
		String itemBody = ((BulletinBean) mItems.get(position)).getText();
		String itemSign = ((BulletinBean) mItems.get(position)).getSign();

		// 跳转到帖子回复界面
		Intent intent = new Intent(context, BulletinReplyActivity.class);

		// 添加参数
		intent.putExtra("head", MyConstants.BULLETIN_REPLY);
		intent.putExtra("board", itemBoard);
		intent.putExtra("id", itemId);
		intent.putExtra("title", itemTitle);
		intent.putExtra("author", itemAuthor);
		intent.putExtra("content", itemBody);
		intent.putExtra("signature", itemSign);

		// 启动Activity。并传递一个intend对象
		context.startActivity(intent);
	}

	/**
	 * 由于无法直接使用文件名来引用res/drawable中的图像资源， 我们利用反射技术 从R.drawable类中通过图像资源文件名，
	 * 去获得相应的图像资源ID，实现代码如下：
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

	/**
	 * 跳转到个人资料界面
	 * 
	 * @param position
	 *            长按的数据在列表中的序号
	 */
	private void goToPersonActivity(int position) {
		String itemAuthor = ((BulletinBean) mItems.get(position)).getAuthor();

		// 跳转到个人资料界面
		Intent intent = new Intent(context, PersonActivity.class);

		// 添加参数
		intent.putExtra("author", itemAuthor);

		// 启动Activity，并传递一个intend对象
		context.startActivity(intent);
	}

	/**
	 * 跳转到版面
	 */
	private void goToTopic() {
		// 跳转到帖子列表界面
		Intent mIntent = new Intent(context, TopicActivity.class);
		// 添加参数 app=topics&board=PieFriends&page=1
		mIntent.putExtra("board", board);
		mIntent.putExtra("name", boardName);
		context.startActivity(mIntent);
	}
}
