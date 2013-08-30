package com.udb.modulo02.media.player;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListMusicFragment extends ListFragment {
	private List<String> paths;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		paths = new ArrayList<String>();
		setListAdapter(new ListMusicAdapter(getActivity()));
	}
	
	class ListMusicAdapter extends ArrayAdapter<String> {

		public ListMusicAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1, paths);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			Holder h = null;
			String path = null;
			String song = null;
			
			if(convertView == null) {
				v = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
				h = new Holder();
				h.nameSong = (TextView) v.findViewById(android.R.id.text1);
				v.setTag(h);
			} else {
				v = convertView;
				h = (Holder) v.getTag();
			}
			
			path = paths.get(position);
			song = path.substring(path.lastIndexOf("/")+1);
			h.nameSong.setText(song);
			return super.getView(position, convertView, parent);
		}
		
	}
	
	static class Holder{
		TextView nameSong;
	}
	
}
