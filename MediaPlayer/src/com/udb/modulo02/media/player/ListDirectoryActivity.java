package com.udb.modulo02.media.player;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ListDirectoryActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_directory);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_directory, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			ListDirectoryFragment d = (ListDirectoryFragment) 
	           getSupportFragmentManager().findFragmentById(R.id.fragment1);
		    if(d != null) {
		    	if(d.hashUp()) {
		    		d.upDirectory();
		    	} else {
		    		setResult(RESULT_CANCELED);
				    finish();
		    	}
		    } else {
			    setResult(RESULT_CANCELED);
			    finish();
		    }
			return true;
		/*case R.id.action_up:
		    d = (ListDirectoryFragment) 
		           getSupportFragmentManager().findFragmentById(R.id.fragment1);
		    if(d != null) {
		        d.upDirectory();
		    }
		    return true;
		*/
		case R.id.action_play_music:
		    d = (ListDirectoryFragment) 
            getSupportFragmentManager().findFragmentById(R.id.fragment1);
		    if(d != null) {
    		    Intent data = new Intent();
    		    data.putExtra("rootDirectory", d.getCurrentDirectory());
    		    setResult(RESULT_OK, data);
		    }
		    finish();
		    return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
