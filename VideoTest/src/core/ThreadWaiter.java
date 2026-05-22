package core;

import java.util.ArrayList;

public class ThreadWaiter extends Thread
{

	private ArrayList<ThreadWaiterListener> threadWaiterListeners = new ArrayList<ThreadWaiterListener>();

	public void addThreadWaiterListener(ThreadWaiterListener threadWaiterListener)
	{
		threadWaiterListeners.add(threadWaiterListener);
	}

	

	@Override
	public void run()
	{
		ArrayList<Thread> threads = MediaTaskManager.getInstance().getThreads();
		for (Thread t : threads)
		{
			try
			{
				t.join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (ThreadWaiterListener twl : threadWaiterListeners)
			twl.allThreadStop();
	}

}
