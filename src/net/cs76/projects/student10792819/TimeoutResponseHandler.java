/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the base class for all response handler classes that need a 
 * timeout mechanism. Also provides the ability to check if a request is
 * currently being processed.
 */
package net.cs76.projects.student10792819;

import java.util.concurrent.atomic.AtomicBoolean;
import net.cs76.projects.student10792819.model.RedditClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * The Class TimeoutResponseHandler.
 */
abstract class TimeoutResponseHandler extends JsonHttpResponseHandler {

	/** The Constant TIMEOUT_LIMIT. */
	public static final int TIMEOUT_LIMIT = 30;
	
	/** The my context. */
	protected Context myContext;
	
	/** The timed out atomic boolean. */
	private AtomicBoolean timedOut = new AtomicBoolean(false);
	
	/** The currently loading atomic boolean. */
	private AtomicBoolean currentlyLoading = new AtomicBoolean(false);
	
	/** The handler timer. */
	private Handler handlerTimer = new Handler();
	
	/** The timeout handler. */
	private Runnable timeoutHandler = new Runnable() {
    	public void run() {
    		timedOut.set(true);
    		onFailure(new ConnectTimeoutException("Connection timed out"));		
    		RedditClient.cancelRequests(myContext);		// cancel request for this context
    	}
    };
    
	/**
	 * Instantiates a new timeout response handler.
	 *
	 * @param c the context
	 */
	public TimeoutResponseHandler(Context c) {
		this.myContext = c;
	}

	/* (non-Javadoc)
	 * @see com.loopj.android.http.AsyncHttpResponseHandler#onStart()
	 */
	@Override
	public void onStart() {
		timedOut.set(false);
		currentlyLoading.set(true);
		onStartHandler();
		handlerTimer.postDelayed(timeoutHandler, TIMEOUT_LIMIT * 1000);
	}

	/* (non-Javadoc)
	 * @see com.loopj.android.http.JsonHttpResponseHandler#onSuccess(org.json.JSONObject)
	 */
	public void onSuccess(JSONObject response) {
		if (!timedOut.get())
			onSuccessHandler(response);
	}
	
	/* (non-Javadoc)
	 * @see com.loopj.android.http.JsonHttpResponseHandler#onSuccess(org.json.JSONArray)
	 */
	public void onSuccess(JSONArray response) {
		if (!timedOut.get())
			onSuccessHandler(response);
	}
	
	/* (non-Javadoc)
	 * @see com.loopj.android.http.AsyncHttpResponseHandler#onFailure(java.lang.Throwable)
	 */
	@Override
	public void onFailure(Throwable e) {
		// Response failed :(
		onFailureHandler(e);
	}
	
	/* (non-Javadoc)
	 * @see com.loopj.android.http.AsyncHttpResponseHandler#onFinish()
	 */
	public void onFinish() {
		handlerTimer.removeCallbacks(timeoutHandler);
		timedOut.set(false);
		currentlyLoading.set(false);
		onFinishHandler();
	}
	
	/**
	 * On start handler. Does nothing, subclass should overwrite this if they want to do something.
	 */
	protected void onStartHandler() {}

	/**
	 * On success handler. Does nothing, subclass should overwrite this if they want to do something.
	 *
	 * @param object the object
	 */
	protected void onSuccessHandler(Object object) {}
	
	/**
	 * On failure handler. Does nothing, subclass should overwrite this if they want to do something.
	 *
	 * @param e the exception
	 */
	protected void onFailureHandler(Throwable e) {}
	
	/**
	 * On finish handler. Does nothing, subclass should overwrite this if they want to do something.
	 */
	protected void onFinishHandler() {}
	
	/**
	 * Currently loading.
	 *
	 * @return true, if currently waiting on a request to finish
	 */
	public boolean currentlyLoading() {
		return currentlyLoading.get();
	}
}
