package com.udb.modulo02.media.player;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListDirectoryFragment extends ListFragment {
	private List<String> files;
	private String rootDirectory;
	
	private ArrayAdapter<String> adapter;
	private Stack<String> historial;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		files = new ArrayList<String>();
		rootDirectory = getActivity().getIntent()
		                            .getExtras().getString("rootDirectory");
		readDirectory(rootDirectory);
		adapter = new ArrayAdapter<String>(getActivity()
		                        , android.R.layout.simple_list_item_1, files);
		setListAdapter(adapter);
		historial = new Stack<String>();
	}
	
	private void refreshList(){
	    adapter = new ArrayAdapter<String>(getActivity()
	                            , android.R.layout.simple_list_item_1, files);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String swap = rootDirectory;
		rootDirectory += "/"+files.get(position);
		if(readDirectory(rootDirectory)) {
			historial.push(swap);
		}else {
			rootDirectory = swap;
		}
		refreshList();
	}

	private boolean readDirectory(String string) {
		File f = new File(string);
		boolean r = false;
		if(f.isDirectory()) {
    		File[] files = f.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return !pathname.isHidden()&&(pathname.isDirectory()
							||
							(pathname.getName().toLowerCase(Locale.ENGLISH)
									.endsWith("mp3")));
				}
			});
    		this.files.clear();
    		
    		if (files != null) {
                for (File path : files) {
                    this.files.add(path.getName());
                    
                }
            }
    		r = true;
    		getActivity().setTitle(string);
		}
		return r;
	}
	
	public boolean hashUp() {
	    return !historial.empty();
	}
	
	public String getCurrentDirectory() {
	    return rootDirectory;
	}
	
	public void upDirectory() {
	    if(hashUp()) {
	        rootDirectory = historial.pop();
	        readDirectory(rootDirectory);
	        refreshList();
	    } else {
	        Toast.makeText(getActivity(), "No hay mas directorios"
	                , Toast.LENGTH_SHORT).show();
	    }
	}
}
