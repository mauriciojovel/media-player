package com.udb.modulo02.media.player;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	private Integer currentSelected;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		paths = new ArrayList<File>();
		setListAdapter(new ListMusicAdapter(getActivity()));
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if(savedInstanceState != null) {
			currentSelected = savedInstanceState.getInt("current");
			if(currentSelected.intValue() <= 0) {
				currentSelected = null;
			}
		}
		//getListView().setSelector(R.drawable.mp3_list_item_selector);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    View root = inflater.inflate(R.layout.mp3_list_layaout
	            , container, false);
	    paths = new ArrayList<File>();
	    return root;
	}
	
	public void loadMp3(String path) {
	    File file = new File(path);
	    File[] files = file.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String filename) {
                return filename.toLowerCase(Locale.ENGLISH).endsWith("mp3");
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
    	    
    	    // verificamos que en la carga exista un valor ya cargado.
    	    if(currentSelected!= null && currentSelected >= 0) {
    	    	playSong(currentSelected);
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
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("current", currentSelected==null?-1:currentSelected);
		if(mp != null) {
			mp.release();
			mp = null;
		}
	}
	
	public MediaPlayer getMediaPlayer() {
        if(mp == null) {
            mp = new MediaPlayer();
        }else if(mp.isPlaying()) {
        	mp.release();
            mp = null;
            mp = new MediaPlayer();
        }
        return mp;
    }
	
	public void playSong(int position) {
		ListView listView = ListMusicFragment.this.getListView();
        MediaPlayer song = getMediaPlayer();
        File pathSong = (File)getListAdapter().getItem(position);
        listView.setItemChecked(position, true);
        //listView.setSelection(position);
        //listView.setSelected(true);
        try {
            song.setDataSource(pathSong.getAbsolutePath());
            song.prepare();
            song.start();
            ListMusicFragment.this.currentSelected = position;
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
	
	public void stopSong(int position) {
		if(mp != null && mp.isPlaying()) {
            ListView listView = ListMusicFragment.this.getListView();
            mp.release();
            mp = null;
            listView.setItemChecked(position, false);
        }
	}
	
	class StopMediaButtonListener extends MediaButtonListener {

        public StopMediaButtonListener(int position, File pathSong) {
            super(position, pathSong);
        }
        
        @Override
        public void onClick(View v) {
           stopSong(position);
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
            playSong(position);
        }
        
        
	    
	}
	
}
