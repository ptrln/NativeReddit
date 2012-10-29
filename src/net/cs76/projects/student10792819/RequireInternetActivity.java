/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This activity is used as the base class for all activities that required a network connection. The
 * network connectivity is checked on resume, and if there is no network connectivity, the noNetworkHandler
 * method is called.
 */
package net.cs76.projects.student10792819;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * The Class RequireInternetActivity.
 */
abstract class RequireInternetActivity extends Activity {
	
	/** The has connectivity. */
	protected boolean hasConnectivity = true;
	
	/** The just got connectivity can be used to check if the activity just got network connectivity. */
	protected boolean justGotConnectivity = false;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	public void onResume() {
		super.onResume();
		
		boolean hasNetwork = hasNetworkConnection();
				
		if (!hasConnectivity && hasNetwork)
			justGotConnectivity = true;
		else
			justGotConnectivity = false;
		
		hasConnectivity = hasNetwork;

		if (!hasConnectivity)
			noNetworkHandler();
	}
	
	/**
	 * Checks for network connection.
	 *
	 * @return true, if successful
	 */
	public boolean hasNetworkConnection() {
		ConnectivityManager conMgr =  (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null || !i.isConnected() || !i.isAvailable())
			return false;
		return true;
	}
	
	/**
	 * No network handler. Subclasses should implement this method to handle no network connection error.
	 */
	protected abstract void noNetworkHandler();
}
