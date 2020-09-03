package com.dustproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.fragment.app.Fragment;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingPreferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SettingPreferenceFragment extends PreferenceFragment {
/*    SharedPreferences prefs;

    ListPreference soundPreference,keywordSoundPreference;
    PreferenceScreen keywordScreen;
    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("sound_list")){
                soundPreference.setSummary(prefs.getString("sound_list","카톡"));
            }
            if(key.equals("keyword_sound_list")){
                keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list","카톡"));
            }
            if(key.equals("keyword")){
                if(prefs.getBoolean("keyword",false)){
                    keywordScreen.setSummary("사용");
                } else{
                    keywordScreen.setSummary("사용안함");
                }
                ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
            }
        }
    };*/
    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
/*        soundPreference = (ListPreference)findPreference("sound_List");
        keywordSoundPreference = (ListPreference)findPreference("keyword_sound_list");
        keywordScreen = (PreferenceScreen)findPreference("keyword_screen");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(!prefs.getString("sound_list","").equals("")){
            soundPreference.setSummary(prefs.getString("sound_list","카톡"));
        }
        if(!prefs.getString("keyword_sound_list","").equals("")){
            keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list","카톡"));
        }
        if(prefs.getBoolean("keyword",false));{
            keywordScreen.setSummary("사용");
        }
        prefs.registerOnSharedPreferenceChangeListener(prefListener);*/

    }
}
