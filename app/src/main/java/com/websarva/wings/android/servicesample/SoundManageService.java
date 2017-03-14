package com.websarva.wings.android.servicesample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

/**
 * CodeZine
 * Android Studio 2で始めるアプリ開発入門
 * サービスサンプル
 *
 * サービスクラス。
 *
 * @author Shinzo SAITO
 */
public class SoundManageService extends Service {

	/**
	 * メディアプレーヤーフィールド。
	 */
	private MediaPlayer _player;

	@Override
	public void onCreate() {
		super.onCreate();
		_player = new MediaPlayer();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.mountain_stream;
		Uri mediaFileUri = Uri.parse(mediaFileUriStr);
		try {
			_player.setDataSource(SoundManageService.this, mediaFileUri);
			_player.setOnPreparedListener(new PlayerPreparedListener());
			_player.setOnCompletionListener(new PlayerCompletionListener());
			_player.prepareAsync();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Service", "サービス破棄");
		if(_player.isPlaying()) {
			_player.stop();
		}
		_player.release();
		_player = null;
	}

	/**
	 * メディア再生準備が完了時のリスナクラス。
	 */
	private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mp) {
			Log.i("Service", "再生開始");

			mp.start();

			NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this);
			builder.setSmallIcon(android.R.drawable.ic_dialog_info);
			builder.setContentTitle("再生開始");
			builder.setContentText("音声ファイルの再生を開始しました");

			Intent intent = new Intent(SoundManageService.this, SoundStartActivity.class);
			intent.putExtra("fromNotification", true);
			PendingIntent stopServiceIntent = PendingIntent.getActivity(SoundManageService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			builder.setContentIntent(stopServiceIntent);
			builder.setAutoCancel(true);

			Notification notification = builder.build();
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(1, notification);
		}
	}

	/**
	 * メディア再生が終了したときのリスナクラス。
	 */
	private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.i("Service", "再生終了");

			NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this);
			builder.setSmallIcon(android.R.drawable.ic_dialog_info);
			builder.setContentTitle("再生終了");
			builder.setContentText("音声ファイルの再生が終了しました");
			Notification notification = builder.build();
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(0, notification);

			stopSelf();
		}
	}
}
