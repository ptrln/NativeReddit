/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the activity responsible for loading the link of a reddit submission. It opens up a web view 
 * and loads the links. The loading adapter and failure adapter are also used to show loading and failures.
 * 
 * Based on code from http://developer.android.com/reference/android/webkit/WebView.html
 * Scaling code from http://stackoverflow.com/questions/3916330/android-webview-webpage-should-fit-the-device-screen
 */
package net.cs76.projects.student10792819;

import java.util.concurrent.atomic.AtomicBoolean;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

/**
 * The Class StoryViewActivity.
 */
public class StoryViewActivity extends RequireInternetActivity {

	/** The list view. */
	private ListView listView;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#onResume()
	 */
	public void onResume() {
		super.onResume();
		
		if (super.hasConnectivity) {
			String url = getIntent().getStringExtra("url");
			
			listView = new ListView(this);
			this.setContentView(listView);
			listView.setAdapter(new LoadingAdapter(this, url));			// set loading first
			listView.setClickable(false);
			
			final Activity activity = this;

			final Handler handlerTimer = new Handler();
			final AtomicBoolean timedOut = new AtomicBoolean(false);
			final Runnable timeoutRun = new Runnable() {				// this is run when timed out
		    	public void run() {
		    		timedOut.set(true);
		    		onFailure("Connection timed out");
		    	}
		    };
		    
		    // the timeout limit set on web view is double that for reddit requests. this is because the web
		    // page load could be much larger than a reddit load
			handlerTimer.postDelayed(timeoutRun, TimeoutResponseHandler.TIMEOUT_LIMIT * 2 * 1000);
			
			final WebView webview = new WebView(this);
			webview.setBackgroundColor(Color.BLACK);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					activity.setTitle("loading... (" + progress +  "% done!)");
					if (progress == 100) {
						activity.setTitle(R.string.app_name);
						if (!timedOut.get()) {							// load complete without time out
							handlerTimer.removeCallbacks(timeoutRun);
							activity.setContentView(webview);
						}
					}
				}
			});
			webview.setWebViewClient(new WebViewClient() {				// set error handler
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					onFailure(description);
				}
			});

			webview.getSettings().setLoadWithOverviewMode(true);		// set appropriate scaling
			webview.getSettings().setUseWideViewPort(true);
			webview.loadUrl(url);	
		}
	}
	
	/**
	 * On failure. This is called when the web view experiences a problem loading the link.
	 *
	 * @param error the error
	 */
	private void onFailure(String error) {
		FailureAdapter fail = new FailureAdapter(this);
		fail.updateError(error);
		listView.setAdapter(fail);
		this.setContentView(listView);
	}

	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#noNetworkHandler()
	 */
	@Override
	protected void noNetworkHandler() {
		onFailure("there is no internet connectivity");
	}

}
