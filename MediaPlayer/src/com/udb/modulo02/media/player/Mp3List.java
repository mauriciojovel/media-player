package com.udb.modulo02.media.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Mp3List extends ActionBarActivity {
	private int ACTIVITY_LIST_RESULT = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_list);
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

	private void openActivityList() {
		Intent i = new Intent();
		i.setClass(this, ListDirectoryActivity.class);
		i.putExtra("rootDirectory", "/");
		startActivityForResult(i, ACTIVITY_LIST_RESULT);
	}
}
