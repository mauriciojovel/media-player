package com.udb.modulo02.media.player;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Mp3List extends ActionBarActivity {
    private String path = Environment
                    .getExternalStorageDirectory().getAbsolutePath();
    private String currentPath;
	private int ACTIVITY_LIST_RESULT = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_list);
		if(savedInstanceState != null) {
			currentPath = savedInstanceState.getString("current");
			loadMusic();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mp3_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean r = true;
		switch (item.getItemId()) {
		case R.id.action_open_dir:
			openActivityList();
			break;

		default:
			r = super.onOptionsItemSelected(item);
			break;
		}
		return r;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode
	                                    , Intent data) {
	    if(requestCode == ACTIVITY_LIST_RESULT) {
	        if(resultCode == RESULT_OK) {
	        	currentPath = data.getExtras().getString("rootDirectory");
	            loadMusic();
	        }
	    }
	}
	
	private void loadMusic() {
		if(currentPath != null 
				&& !currentPath.trim().equals("")) {
		    ListMusicFragment f = (ListMusicFragment) 
	                getSupportFragmentManager()
	                                    .findFragmentById(R.id.fragment1);
	        f.loadMp3(currentPath);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("current", currentPath);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentPath = savedInstanceState.getString("current");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadMusic();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

    private void openActivityList() {
		Intent i = new Intent();
		i.setClass(this, ListDirectoryActivity.class);
		i.putExtra("rootDirectory", path);
		startActivityForResult(i, ACTIVITY_LIST_RESULT);
	}
}
