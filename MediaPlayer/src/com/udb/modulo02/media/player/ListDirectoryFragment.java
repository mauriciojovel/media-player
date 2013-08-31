package com.udb.modulo02.media.player;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListDirectoryFragment extends ListFragment {
	private List<File> files;
	private String rootDirectory;
	
	private FileAdapter adapter;
	private Stack<String> historial;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		files = new ArrayList<File>();
		rootDirectory = getActivity().getIntent()
		                            .getExtras().getString("rootDirectory");
		readDirectory(rootDirectory);
		adapter = new FileAdapter(getActivity());
		setListAdapter(adapter);
		historial = new Stack<String>();
	}
	
	private void refreshList(){
	    adapter = new FileAdapter(getActivity());
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String swap = rootDirectory;
		rootDirectory = files.get(position).getAbsolutePath();
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
                    this.files.add(path);
                    
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
	
	class FileAdapter extends ArrayAdapter<File> {

		public FileAdapter(Context context) {
			super(context, R.layout.file_row_layout, files);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			FileHolder h = null;
			File f = files.get(position);
			if(convertView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				v = inflater.inflate(R.layout.file_row_layout, null);
				h = new FileHolder();
				h.icon = (ImageView) v.findViewById(R.id.iconFileImg);
				h.fileName = (TextView) v.findViewById(R.id.fileNameText);
				v.setTag(h);
			} else {
				v = convertView;
				h = (FileHolder) v.getTag();
			}
			
			if(f.isDirectory()) {
				h.icon.setImageDrawable(getResources()
						.getDrawable(R.drawable.ic_folder));
			} else {
				h.icon.setImageDrawable(getResources()
						.getDrawable(R.drawable.ic_launcher));
			}
			h.fileName.setText(f.getName());
			return v;
		}
		
	}
	
	static class FileHolder {
		ImageView icon;
		TextView fileName;
	}
}
