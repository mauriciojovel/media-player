package com.udb.modulo02.media.player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListDirectoryFragment extends ListFragment {
	private List<String> files;
	private String rootDirectory;
	private ArrayAdapter<String> adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		files = new ArrayList<String>();
		rootDirectory = getActivity().getIntent().getExtras().getString("rootDirectory");;
		readDirectory(rootDirectory);
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, files);
		setListAdapter(adapter);
	}
	
	private void refreshList(){
		adapter.clear();
		for (String s : files) {
			adapter.add(s);
		}
		adapter.notifyDataSetChanged();
		getListView().invalidateViews();
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		rootDirectory = files.get(position);
		readDirectory(rootDirectory);
		refreshList();
	}

	private void readDirectory(String string) {
		File f = new File(string);
		String[] files = f.list();
		this.files.clear();
		for (String path : files) {
			File dir = new File(path);
			if(dir.isDirectory()) {
				this.files.add(dir.getAbsolutePath());
			}
		}
	}
}
