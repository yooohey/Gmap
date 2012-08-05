package com.sample.gmap;

import java.io.IOException;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GmapActivity extends MapActivity {
	   private MyLocationOverlay overlay = null;
	    private MapView map = null;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_gmap);

	        map = (MapView)findViewById(R.id.map_view1);
	        map.setBuiltInZoomControls(true);

	        // ズームする
	        map.getController().setZoom(15);

	        // 場所を説明するラベル
	        final TextView tv = (TextView)findViewById(R.id.address_label);
	        final Activity context = this;

	        // ラベルを書き換えるためのハンドラ
	        final Handler ui_handler = new Handler() {
	            //@Override
	            public void handleMessage(Message msg) {
	                String str_address = msg.getData().get("str_address").toString();
	                tv.setText( str_address );
	            }
	        };

	        // マップ上にオーバレイを定義
	        overlay = new MyLocationOverlay(getApplicationContext(), map);
	        overlay.onProviderEnabled(LocationManager.GPS_PROVIDER);
	        overlay.enableMyLocation();

	        // GPSの位置情報の変更を監視
	        overlay.runOnFirstFix(new Runnable() {
	            @Override
	            public void run() {
	                // マップ上で新たな現在位置へ移動
	                GeoPoint g = overlay.getMyLocation();
	                map.getController().animateTo(g);
	                map.getController().setCenter(g);

	                // 場所名を文字列で取得する
	                String str_address = null;
	                try{
	                    // 住所を取得
	                    str_address = GeocodeManager.point2address(
	                        (double)(g.getLatitudeE6() / 1000000),
	                        (double)(g.getLongitudeE6() / 1000000),
	                        context
	                    );
	                }
	                catch(IOException e)
	                {
	                    str_address = "座標情報から住所へのデコードに失敗";
	                }
	                
	                // 住所をメッセージに持たせて
	                // ハンドラにUIを書き換えさせる
	                Message message = new Message();
	                Bundle bundle = new Bundle();
	                bundle.putString("str_address", str_address);
	                message.setData(bundle);
	                ui_handler.sendMessage(message);

	                Log.d("GPS操作", "位置の更新を検知");
	            }
	        });

	        // このオーバレイをマップ上に追加
	        map.getOverlays().add(overlay);
	        map.invalidate();
	    }
	    
	    @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {  
	        boolean ret = super.onCreateOptionsMenu(menu);  
	        menu.add(0,1,Menu.NONE,"現在地を表示").setIcon(android.R.drawable.ic_menu_mylocation);  
	        return ret;  
	    }
	    
	    // メニューの動作  
		@Override  
		public boolean onOptionsItemSelected(MenuItem item) {  
			switch (item.getItemId()) {
			case 1:// 検索  
				
				//現在地ボタン動作（未記入）
				
				return true;  
			} 		      
		    return false;  
		}  


	    @Override
	    protected boolean isRouteDisplayed() {
	        return false;
	    }

	    @Override
	    protected void onDestroy() {
	        // 破棄
	        overlay.disableMyLocation();
	        map.getOverlays().remove(overlay);

	        super.onDestroy();
	    }

}

