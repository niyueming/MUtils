package net.nym.library.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author nym
 *
 */
public class ADGallery extends Gallery {
	public static final int CHANGE_BANNER = 1;
	public static final int SPACE_TIME = 3000;
	private boolean isAuto;

	private Timer mTimer = new Timer(true);
	private TimerTask mTask = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(CHANGE_BANNER);

		}
	};
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CHANGE_BANNER:
				onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
				break;

			default:
				break;
			}
		}
	};
	public ADGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ADGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ADGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void auto()
	{
		if(!isAuto)
		{
			isAuto = true;
			mTimer.scheduleAtFixedRate(mTask, SPACE_TIME, SPACE_TIME);
		}
		
	}
	
	public void cancelAuto()
	{
		if(mTimer != null)
		{
			mTimer.cancel();
			mTimer = null;
			isAuto = false;
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

}
