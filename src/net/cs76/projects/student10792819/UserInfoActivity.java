/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * This is the activity that displays the user info of the user logged in.
 */
package net.cs76.projects.student10792819;

import net.cs76.projects.student10792819.model.RedditClient;
import android.os.Bundle;
import android.widget.ListView;

/**
 * The Class UserInfoActivity.
 */
public class UserInfoActivity extends RequireInternetActivity {
	
	/** The info handler. */
	private UserInfoResponseHandler infoHandler = null;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
	}
	
	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#onResume()
	 */
	public void onResume() {
		super.onResume();
		
		if (super.hasConnectivity) {
			if (infoHandler == null) {
				UserInfoAdapter adapter = new UserInfoAdapter(this);
				ListView lv = (ListView) this.findViewById(R.id.user_info);
				lv.setAdapter(adapter);
				lv.setClickable(false);
				infoHandler = new UserInfoResponseHandler(
						this, lv, adapter, new LoadingAdapter(this, "user info"), new FailureAdapter(this));
				
				RedditClient.getMe(this, infoHandler);
			} else if (super.justGotConnectivity) {
				RedditClient.getMe(this, infoHandler);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.cs76.projects.student10792819.RequireInternetActivity#noNetworkHandler()
	 */
	protected void noNetworkHandler() {
		FailureAdapter failAdapt = new FailureAdapter(this);
		failAdapt.updateError("there is no internet connectivity");
		ListView lv = (ListView) this.findViewById(R.id.user_info);
		lv.setAdapter(failAdapt);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	public void onStop() {
		super.onStop();
		RedditClient.cancelRequests(this);
	}
}
