package com.websarva.wings.android.servicesample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * CodeZine
 * Android Studio 2で始めるアプリ開発入門
 * サービスサンプル
 *
 * アクティビティクラス。
 *
 * @author Shinzo SAITO
 */
public class SoundStartActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_start);

		Intent intent = getIntent();
		boolean fromNotification = intent.getBooleanExtra("fromNotification", false);
		if(fromNotification) {
			Button btPlay = (Button) findViewById(R.id.btPlay);
			Button btStop = (Button) findViewById(R.id.btStop);
			btPlay.setEnabled(false);
			btStop.setEnabled(true);
		}
	}

	/**
	 * 再生ボタンタップ時の処理メソッド。
	 *
	 * @param view 画面部品
	 */
	public void onPlayButtonClick(View view) {
		Intent intent = new Intent(SoundStartActivity.this, SoundManageService.class);
		startService(intent);
		Button btPlay = (Button) findViewById(R.id.btPlay);
		Button btStop = (Button) findViewById(R.id.btStop);
		btPlay.setEnabled(false);
		btStop.setEnabled(true);
	}

	/**
	 * 停止ボタンタップ時の処理メソッド。
	 *
	 * @param view 画面部品
	 */
	public void onStopButtonClick(View view) {
		Intent intent = new Intent(SoundStartActivity.this, SoundManageService.class);
		stopService(intent);
		Button btPlay = (Button) findViewById(R.id.btPlay);
		Button btStop = (Button) findViewById(R.id.btStop);
		btPlay.setEnabled(true);
		btStop.setEnabled(false);
	}
}
