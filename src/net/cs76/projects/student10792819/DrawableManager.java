/*
 * CSCI E-76 - Android Student Choice
 * by Peter Lin (10792819) peterlin@iinet.net.au
 * 
 * The DrawableManager is used in ListingAdapter to lazy load thumbnail images from reddit.
 * Based on code found on http://stackoverflow.com/questions/541966/android-how-do-i-do-a-lazy-load-of-images-in-listview
 * 
 * This code has been modified to use SoftReference in order to prevent out of memory exceptions. The original code also
 * touched UI elements directly, which resulted in unexpected thumbnail behaviors. It has been modified to not touch UI
 * elements directly, and call the notifyDataSetChanged method on the adapter instead.
 */
package net.cs76.projects.student10792819;

/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * The Class DrawableManager.
 */
public class DrawableManager {
   
   /** The drawable map. This is used as the cache. */
   private static final Map<String, SoftReference<Drawable>> drawableMap = new HashMap<String, SoftReference<Drawable>>();

   /**
    * Fetch drawable. This fetches a drawable from the url and returns it. Returns null on errors.
    *
    * @param urlString the url string
    * @return the drawable
    */
   public static Drawable fetchDrawable(String urlString) {
       if (drawableMap.containsKey(urlString)) {
    	   Drawable o = drawableMap.get(urlString).get();
    	   
    	   if (o != null)
    		   return o;
       }

       try {
           InputStream is = fetch(urlString);
           Drawable drawable = Drawable.createFromStream(is, "src");

           if (drawable != null) {
               drawableMap.put(urlString, new SoftReference<Drawable>(drawable));
           } else {
             Log.w("DrawableManager", "could not get thumbnail");
           }

           return drawable;
       } catch (MalformedURLException e) {
           Log.e("DrawableManager", "fetchDrawable failed", e);
           return null;
       } catch (IOException e) {
           Log.e("DrawableManager", "fetchDrawable failed", e);
           return null;
       }
   }

   /**
    * Fetch drawable on thread. Fetches drawable on a separate thread.
    *
    * @param urlString the url string
    * @param listingAdapter the listing adapter
    * @param thumbnailCache the image view
    * @param position the position
    */
   public static void fetchDrawableOnThread(final String urlString, 
		   final ListingAdapter listingAdapter, final Drawable[] thumbnailCache, final int position) {
       if (drawableMap.containsKey(urlString)) {
    	   Drawable o = drawableMap.get(urlString).get();
    	   
    	   if (o != null) {
    		   thumbnailCache[position] = o;
    		   listingAdapter.notifyDataSetChanged();
    		   return;
    	   }
       }

       final Handler handler = new Handler() {
           @Override
           public void handleMessage(Message message) {
        	   thumbnailCache[position] = (Drawable) message.obj;
        	   listingAdapter.notifyDataSetChanged();
           }
       };

       Thread thread = new Thread() {
           @Override
           public void run() {
               Drawable drawable = fetchDrawable(urlString);
               Message message = handler.obtainMessage(1, drawable);
               handler.sendMessage(message);
           }
       };
       thread.start();
   }

   /**
    * Fetch.
    *
    * @param urlString the url string
    * @return the input stream
    * @throws MalformedURLException the malformed url exception
    * @throws IOException Signals that an I/O exception has occurred.
    */
   private static InputStream fetch(String urlString) throws MalformedURLException, IOException {
       DefaultHttpClient httpClient = new DefaultHttpClient();
       HttpGet request = new HttpGet(urlString);
       HttpResponse response = httpClient.execute(request);
       return response.getEntity().getContent();
   }

   /**
    * Gets the cache. Returns null if the cache does not contain the url, or if the soft reference has
    * already cleared the cached drawable.
    *
    * @param url the url
    * @return the cache
    */
   public static Drawable getCache(String url) {
	   if (drawableMap.containsKey(url))
		   return drawableMap.get(url).get();
	   else
		   return null;
   }
}