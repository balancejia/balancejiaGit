package com.bb.dd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bb.dd.modle.UploadApk;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Util;

/**
 * @author Hzy
 * @version 创建时间：2013-1-29 上午9:42:20 类说明
 */
public class UpdateVersionActivity extends Activity {
	private static final String TAG = UpdateVersionActivity.class.getName();
	private TextView version_content;
	private ProgressBar jindutiao;
	private DownSoft downSoft;
	AlertDialog builder;
	TextView do_cancle;
	private UploadApk newSuj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		Intent intent = getIntent();
		newSuj = (UploadApk) intent.getSerializableExtra("newSuj");
		showLoadDialog();
		doUpdate();
	}

	public void doUpdate() {
		downSoft = new DownSoft();
		downSoft.execute();
	}

	private void showLoadDialog() {
		builder = new AlertDialog.Builder(this).create();
		builder.show();
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				downSoft.cancel(true);
				finish();
			}
		});
		Window win = builder.getWindow();
		win.setContentView(R.layout.update);
		version_content = (TextView) win.findViewById(R.id.version_content);
		jindutiao = (ProgressBar) win.findViewById(R.id.progressBar1);
		version_content.setVisibility(View.GONE);
		jindutiao.setVisibility(View.VISIBLE);
		TextView update_title = (TextView) win.findViewById(R.id.update_title);
		update_title.setText("正在下载...");
		TextView do_update = (TextView) win.findViewById(R.id.do_update);
		do_cancle = (TextView) win.findViewById(R.id.b_cancel_text);
		do_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				downSoft.cancel(true);
				finish();
			}
		});
		do_update.setTextColor(Util.getColor(R.color.darkgrey));
		ImageView update_logo = (ImageView) win.findViewById(R.id.update_logo);
		update_logo.setImageResource(R.drawable.update_logo);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                  && event.getRepeatCount() == 0) {
        	downSoft.cancel(true);
			finish();
              return true;
          }
          return super.onKeyDown(keyCode, event);
      }

	public class DownSoft extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			if ("".equals(newSuj.apkUrl))
				return null;
			HttpGet get = new HttpGet(newSuj.apkUrl);
			HttpResponse response;
			try {
				response = client.execute(get);
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				FileOutputStream fileOutputStream = null;
				if (is != null) {
					File file = new File(
							Environment.getExternalStorageDirectory(),
							newSuj.apkname);
					if (file.exists()) {
						file.deleteOnExit();
					}
					fileOutputStream = new FileOutputStream(file);

					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;

						if (length > 0) {
							int d = (int) (count * 100 / length);
							onProgressUpdate(d);
						}

					}

				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), newSuj.apkname)),
					"application/vnd.android.package-archive");
			startActivity(intent);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			jindutiao.setProgress(values[0]);
		}

	}
}
