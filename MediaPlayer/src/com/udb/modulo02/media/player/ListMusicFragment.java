package com.udb.modulo02.media.player;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListMusicFragment extends ListFragment {
	private List<File> paths;
	private MediaPlayer mp;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		paths = new ArrayList<File>();
		setListAdapter(new ListMusicAdapter(getActivity()));
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    View root = inflater.inflate(R.layout.mp3_list_layaout
	            , container, false);
	    return root;
	}
	
	public void loadMp3(String path) {
	    File file = new File(path);
	    File[] files = file.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith("mp3");
            }
        });
	    if(files != null) {
	        paths.clear();
    	    for (File mp3File : files) {
    	        paths.add(mp3File);
            }
    	    setListAdapter(new ListMusicAdapter(getActivity()));
    	    if(files.length <= 0) {
    	        Toast.makeText(getActivity(), "No hay musica :("
    	                , Toast.LENGTH_SHORT).show();
    	    }
	    } else {
	        Toast.makeText(getActivity(), "No hay musica :("
	                , Toast.LENGTH_SHORT).show();
	    }
	}
	
	class ListMusicAdapter extends ArrayAdapter<File> {

		public ListMusicAdapter(Context context) {
			super(context, R.layout.mp3_row_layout, paths);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			Holder h = null;
			File path = null;
			String song = null;
			
			if(convertView == null) {
				v = getActivity().getLayoutInflater().inflate(
				        R.layout.mp3_row_layout, null);
				h = new Holder();
				h.nameSong = (TextView) v.findViewById(R.id.nameSongText);
				h.playBtn = (ImageButton) v.findViewById(R.id.playBtn);
				h.stopBtn = (ImageButton) v.findViewById(R.id.stopBtn);
				v.setTag(h);
			} else {
				v = convertView;
				h = (Holder) v.getTag();
			}
			
			path = paths.get(position);
			song = path.getName().substring(0,path.getName().lastIndexOf("."));
			h.nameSong.setText(song);
			h.playBtn.setOnClickListener(
			                        new MediaButtonListener(position, path));
			h.stopBtn.setOnClickListener(
			        new StopMediaButtonListener(position, path));
			return v;
		}
		
	}
	
	static class Holder{
		TextView nameSong;
		ImageButton playBtn;
		ImageButton stopBtn;
	}
	
	class StopMediaButtonListener extends MediaButtonListener {

        public StopMediaButtonListener(int position, File pathSong) {
            super(position, pathSong);
        }
        
        @Override
        public void onClick(View v) {
            if(mp != null && mp.isPlaying()) {
                ListView listView = ListMusicFragment.this.getListView();
                mp.stop();
                mp.release();
                listView.setItemChecked(position, false);
            }
        }
	    
	}
	
	class MediaButtonListener implements OnClickListener {
	    protected int position;
	    protected File pathSong;
	    
        public MediaButtonListener(int position, File pathSong) {
            super();
            this.position = position;
            this.pathSong = pathSong;
        }


        @Override
        public void onClick(View v) {
            ListView listView = ListMusicFragment.this.getListView();
            MediaPlayer song = getMediaPlayer();
            listView.setItemChecked(position, true);
            try {
                song.setDataSource(pathSong.getAbsolutePath());
                song.prepare();
                song.start();
            } catch (IllegalArgumentException e) {
                Log.e("Mp3Player", "Error al ejecutar la cancion: " 
                                        + pathSong.getAbsolutePath(), e);
                Toast.makeText(getActivity()
                        , "No se pudo ejecutar la cancion :("
                        , Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Log.e("Mp3Player", "Error al ejecutar la cancion: " 
                        + pathSong.getAbsolutePath(), e);
                Toast.makeText(getActivity()
                        , "No se pudo ejecutar la cancion :("
                        , Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Log.e("Mp3Player", "Error al ejecutar la cancion: " 
                        + pathSong.getAbsolutePath(), e);
                Toast.makeText(getActivity()
                        , "No se pudo ejecutar la cancion :("
                        , Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("Mp3Player", "Error al ejecutar la cancion: " 
                        + pathSong.getAbsolutePath(), e);
                Toast.makeText(getActivity()
                        , "No se pudo ejecutar la cancion :("
                        , Toast.LENGTH_LONG).show();
            }
        }
        
        public MediaPlayer getMediaPlayer() {
            if(mp == null) {
                mp = new MediaPlayer();
            }
            if(mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
            return mp;
        }
	    
	}
	
}
