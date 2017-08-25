package test.bqt.com.webviewtest;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;

public class HandlerTestActivity extends ListActivity {
	private TextView tv_info;
	private Handler uiHandler;
	private StaticThread thread;//一个子线程
	
	public static final int MSG_WHAT_1 = 1;
	public static final int MSG_WHAT_2 = 2;
	public static final int MSG_WHAT_3 = 3;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"开启子线程，并在子线程中创建一个Handler", //
				"在主线程中，通过子线程的Handler[向子线程]发消息", //
				"演示Handler的post方法"};
		tv_info = new TextView(this);
		tv_info.setText("Handler、Looper、Message、MQ、Thread关系");
		getListView().addFooterView(tv_info);
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));
		
		uiHandler = new StaticUiHandler(this); //系统启动时已经为主线程初始化了Looper、MQ等，我们可以直接创建Handler
		thread = new StaticThread(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (uiHandler != null) uiHandler.removeCallbacksAndMessages(null);
		if (thread != null && thread.getAnsyHandler() != null) thread.getAnsyHandler().removeCallbacksAndMessages(null);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
			case 0://开启子线程，并在子线程中创建一个Handler
				if (thread != null && !thread.isAlive()) thread.start();//A thread is alive if it has been started and has not yet died.
				break;
			case 1://在主线程中，通过子线程的Handler[向子线程]发消息
				Message msg = Message.obtain(null, MSG_WHAT_1, "消息内容"); //第一个参数Handler的作用是指定msg.target
				//这里设为null的原因是：后面调用sendMessage方法时重新指定了发送此消息的Handler为msg.target
				if (thread != null && thread.getAnsyHandler() != null) thread.getAnsyHandler().sendMessage(msg);
				tv_info.append("\n1、在UI线程中用子线程的Handler发消息，what=" + msg.what);
				break;
			case 2:
				//其实这个Runnable并没有创建什么线程，而是发送了一条消息，当Handler收到此消息后回调run()方法
				uiHandler.post(() -> tv_info.append("\n演示Handler的post方法"));
				break;
		}
	}
	//***********************************************静态内部类，防止内存泄漏*******************************************
	
	/**
	 * 主线程使用的Handler
	 */
	private static class StaticUiHandler extends Handler {
		private SoftReference<HandlerTestActivity> mSoftReference;
		
		public StaticUiHandler(HandlerTestActivity activity) {
			mSoftReference = new SoftReference<>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			HandlerTestActivity activity = mSoftReference.get();
			if (activity != null && activity.thread != null && activity.thread.getAnsyHandler() != null) {
				activity.tv_info.append("\n4、UI线程的Handler收到消息，what=" + msg.what);
				Message msg3 = Message.obtain(null, MSG_WHAT_3, msg.obj);
				activity.thread.getAnsyHandler().sendMessageAtTime(msg3, SystemClock.uptimeMillis() + 2000);
				activity.tv_info.append("\n5、在UI线程中用子线程的Handler发消息，what=" + msg3.what);
			}
		}
	}
	
	/**
	 * 异步线程(子线程)使用的Handler
	 */
	private static class StaticAnsyHandler extends Handler {
		private SoftReference<HandlerTestActivity> mSoftReference;
		
		public StaticAnsyHandler(HandlerTestActivity activity) {
			mSoftReference = new SoftReference<>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			HandlerTestActivity activity = mSoftReference.get();
			
			if (activity != null) {
				final Message tempMsg = Message.obtain(msg);//把收到的消息保存起来
				//注意，一定要注意！根据消息池机制，当此消息不再在【此子线程】中使用时，此msg会立即被重置(引用虽在，内容为空)
				//所以，如果想把此消息转发到其他线程，或者想在其他线程中引用此消息，一定要手动把消息保存起来！
				
				activity.runOnUiThread(() -> {//在子线程中创建Handler的目的是为了和其他线程通讯，绝对不是(也不能)更新UI
					activity.tv_info.append("\n2、子线程的Handler收到消息，what=" + tempMsg.what);
					
					if (activity.uiHandler != null && tempMsg.what == MSG_WHAT_1) {
						Message msg2 = Message.obtain(null, MSG_WHAT_2, tempMsg.obj);
						activity.uiHandler.sendMessageDelayed(msg2, 2000);
						//注意，不能直接把一条还在使用的消息转发出去，否则IllegalStateException: This message is already in use
						activity.tv_info.append("\n3、在子线程中用UI线程的Handler发消息，what=" + msg2.what);
					}
				});
			}
		}
	}
	
	/**
	 * 一个线程，用于执行耗时的操作
	 */
	private static class StaticThread extends Thread {
		private SoftReference<HandlerTestActivity> mSoftReference;
		
		public StaticThread(HandlerTestActivity activity) {
			mSoftReference = new SoftReference<>(activity);
		}
		
		private Handler ansyHandler;
		
		public Handler getAnsyHandler() {
			return ansyHandler;
		}
		
		public void run() {
			HandlerTestActivity activity = mSoftReference.get();
			if (activity != null) {
				Looper.prepare(); //在创建Handler【前】必须调用此方法初始化Looper，否则直接报否则报RuntimeException崩溃
				//里面做的事情：①为当前线程创建唯一的Looper对象  ②在它的构造方法中会创建一个的MessageQueue对象
				//此方法只能被调用一次，这保证了在一个线程中只有一个Looper实例以及只有一个与其关联的MessageQueue实例
				ansyHandler = new StaticAnsyHandler(activity);  //任何线程都可通过此Handler发送信息！
				Looper.loop(); //若要能够接收到消息，创建Handler后，必须调用loop方法。当然此方法必须是在prepar之后执行
				//里面做的事情：启动一个死循环，不断从MQ中取消息，没有则阻塞等待，有则将消息传给指定的Handler去处理
				activity.runOnUiThread(() -> Toast.makeText(activity, "会一直阻塞在这里", Toast.LENGTH_SHORT).show());
			}
		}
	}
}