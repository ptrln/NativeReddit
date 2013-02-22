NativeReddit
============

CSCI E-76 Android Student’s Choice Project

Summary
-----

NativeReddit is a native Android client for the website reddit.com. It uses the reddit API to grab data from reddit, and all of the data is then displayed natively.


Features
--------
*   Displays all of the listings on the front page of reddit.
*   The listing shows thumbnail images if they are available. These images are lazy loaded when required, and are stored in a cache using soft reference, so they are cached without ever overflowing memory.
*   Allows users to go to the next page of a subreddit and view the subsequent listings.
*   Users can click on the story title to open the link and view the contents.
*   Self links (links to a subreddit or comment) are recognized and are loaded natively in app, rather than a web view.
*   Users can click on the story’s subreddit to visit that subreddit.
*   Users can click on comments to view the comments.
*   Users can go to any subreddit using the change subreddit dialog.
*   Users can also view multiple pages of subreddits as well.
*   The story’s “karma” score is displayed. If an user is logged in, they can click on the arrows to up vote or down vote stories.
*   If the user has already voted on a story, the arrows will be in color to show that they have already voted.
*   Users can cancel their votes, or change existing votes as they wish.
*   Loading screen is shown whenever something is loading.
*   Failure screen or Toast error message is shown to user when something goes wrong.
*   App checks for network connectivity and will not work if there is not connectivity. All requests also timeout after 30 seconds, with the appropriate error message shown to the user.
*   Listings automatically scroll to the top on page change or subreddit changes.
*   Activities that require a lot of data implement the onRetainCongifurationInstance method, so on orientation changes the data is not reloaded.
*   Users are able to log in and log out using the login dialog.
*   After user is logged in, they can view their user info.
*   The log in credentials are saved in the shared preferences, so the user remains logged in even after closing the app.

Some Limitations
-----

*   Comments page only shows the top level comments. Nested comments are correctly loaded, but I did not have enough time to get to displaying them. Only the top 100 comments are loaded in order to reduce load times, but because nested comments are not displayed, the actual displayed comments will be less than 100. Loading the comments after the top 100 is not supported at the moment.
*   Menu in ICS does not grey out the items that are disabled. They all look enabled, but in fact do nothing when they are clicked. Works perfectly fine with Android 2.1.

On connectivity issues
-----

The use of the Async HTTP Library made handling connectivity issues a little tricky. The library spawns its own threads to execute the HTTP requests, and exposes very little control over this.

I tried using setting the socket timeout and connection timeout on the HTTP client, and while it correctly times out, there is no way for me to catch the TimeoutExceptions that are thrown. They are thrown in one of the library’s threads, and the library does not pass the ￼timeout exceptions through the exposed onFailure method. The request will still fail though, but the uncaught exceptions are printed on LogCat, and it’s just not a good way of doing things.

I decided to implement my own timeout mechanism. This logic is found in the TimeoutResponseHandler class. On every request, there is a timer set for some number of seconds. If the timer alarm goes off before the request finishes, we throw an exception and cancel the request. All of the request handlers used in NativeReddit are subclasses of TimeoutResponseHandler, so all requests are passed with a timeout (even voting and logging in).

On thumbnail images...
-----

I have decided to lazy load the thumbnail images in the listing. The images are loaded on demand and are loaded in a separate thread spawned in the DrawableManager. There are two levels of caching. The list view adapter has its cache of the drawables so when user scrolls up/down, the thumbnails don’t need to be reloaded. The DrawableManager also maintains a cache of all thumbs loaded, stored using soft reference. I’ve taken this approach to make use of as much avail. memory as possible to cache thumbnails. This way we can reduce the reloading of the same thumbnails as much as possible. Using the SoftReference instead of using hard references ensures that the DrawableManager cache will never use too much memory.

Screenshots
-----

![alt text](http://peterl.in/projects/nativereddit/4.png)
![alt text](http://peterl.in/projects/nativereddit/2.png)
![alt text](http://peterl.in/projects/nativereddit/5.png)
![alt text](http://peterl.in/projects/nativereddit/6.png)
![alt text](http://peterl.in/projects/nativereddit/8.png)
![alt text](http://peterl.in/projects/nativereddit/11.png)
