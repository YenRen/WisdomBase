

package com.hnxx.wisdombase.ui.image;

import android.os.Handler;
import android.os.Message;

import com.hnxx.wisdombase.framework.executors.ThreadManager;
import com.hnxx.wisdombase.framework.utils.LogUtil;

import java.util.HashMap;
import java.util.LinkedList;


public class NetworkImageHelp {
	private static NetworkImageHelp ourInstance;

	public static NetworkImageHelp Instance() {
		
		if (ourInstance == null) {
			ourInstance = new NetworkImageHelp();
		}
		return ourInstance;
		
	}

	private NetworkImageHelp() {
		
	}

	private volatile boolean myInitialized;

	public boolean isInitialized() {
		return myInitialized;
	}


	private static final int COVER_LOADING_THREADS_NUMBER = 3; // TODO: how many threads ???
	//创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程，在需要时使用提供的 ThreadFactory 创建新线程
//	private final ExecutorService myPool = Executors.newFixedThreadPool(COVER_LOADING_THREADS_NUMBER, new MinPriorityThreadFactory());

	private final HashMap<String, LinkedList<Runnable>> myOnCoverSyncRunnables = new HashMap<String, LinkedList<Runnable>>();

	private class CoverSynchronizedHandler extends Handler {
		@Override
 public void handleMessage(Message message)
        {
            try
            {
                final String imageUrl = (String) message.obj;
                final LinkedList<Runnable> runables = myOnCoverSyncRunnables.remove(imageUrl);
                for (Runnable runnable : runables)
                {
                    runnable.run();
                }
            }
            catch (Exception e)
            {
                // TODO: handle exception
                LogUtil.e("NetworkImageHelp", e+"");
            }
        }

		public void fireMessage(String imageUrl) {
			sendMessage(obtainMessage(0, imageUrl));
		}
	}

    private final CoverSynchronizedHandler myCoverSynchronizedHandler = new CoverSynchronizedHandler();

	public void performCoverSynchronization(final NetworkImage image, Runnable finishRunnable) {
		
		if (myOnCoverSyncRunnables.containsKey(image.Url)) {
			return;
		}
		final LinkedList<Runnable> runnables = new LinkedList<Runnable>();
		if (finishRunnable != null) {
			runnables.add(finishRunnable);
		}
		myOnCoverSyncRunnables.put(image.Url, runnables);
		ThreadManager.getShortPool().execute((Runnable) () -> {
			//起一个线程到到服务器去读取图片，读到之后将队列中的图片移除
			image.synchronize();
			myCoverSynchronizedHandler.fireMessage(image.Url);
		});
	}

	public final boolean isCoverLoading(String coverUrl) {
		return myOnCoverSyncRunnables.containsKey(coverUrl);
	}

	public void addCoverSynchronizationRunnable(String coverUrl, Runnable finishRunnable) {
		final LinkedList<Runnable> runnables = myOnCoverSyncRunnables.get(coverUrl);
		if (runnables != null && finishRunnable != null) {
			runnables.add(finishRunnable);
		}
	}


	

}
