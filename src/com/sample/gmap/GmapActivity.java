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

	        // �Y�[������
	        map.getController().setZoom(15);

	        // �ꏊ��������郉�x��
	        final TextView tv = (TextView)findViewById(R.id.address_label);
	        final Activity context = this;

	        // ���x�������������邽�߂̃n���h��
	        final Handler ui_handler = new Handler() {
	            //@Override
	            public void handleMessage(Message msg) {
	                String str_address = msg.getData().get("str_address").toString();
	                tv.setText( str_address );
	            }
	        };

	        // �}�b�v��ɃI�[�o���C���`
	        overlay = new MyLocationOverlay(getApplicationContext(), map);
	        overlay.onProviderEnabled(LocationManager.GPS_PROVIDER);
	        overlay.enableMyLocation();

	        // GPS�̈ʒu���̕ύX���Ď�
	        overlay.runOnFirstFix(new Runnable() {
	            @Override
	            public void run() {
	                // �}�b�v��ŐV���Ȍ��݈ʒu�ֈړ�
	                GeoPoint g = overlay.getMyLocation();
	                map.getController().animateTo(g);
	                map.getController().setCenter(g);

	                // �ꏊ���𕶎���Ŏ擾����
	                String str_address = null;
	                try{
	                    // �Z�����擾
	                    str_address = GeocodeManager.point2address(
	                        (double)(g.getLatitudeE6() / 1000000),
	                        (double)(g.getLongitudeE6() / 1000000),
	                        context
	                    );
	                }
	                catch(IOException e)
	                {
	                    str_address = "���W��񂩂�Z���ւ̃f�R�[�h�Ɏ��s";
	                }
	                
	                // �Z�������b�Z�[�W�Ɏ�������
	                // �n���h����UI����������������
	                Message message = new Message();
	                Bundle bundle = new Bundle();
	                bundle.putString("str_address", str_address);
	                message.setData(bundle);
	                ui_handler.sendMessage(message);

	                Log.d("GPS����", "�ʒu�̍X�V�����m");
	            }
	        });

	        // ���̃I�[�o���C���}�b�v��ɒǉ�
	        map.getOverlays().add(overlay);
	        map.invalidate();
	    }
	    
	    @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {  
	        boolean ret = super.onCreateOptionsMenu(menu);  
	        menu.add(0,1,Menu.NONE,"���ݒn��\��").setIcon(android.R.drawable.ic_menu_mylocation);  
	        return ret;  
	    }
	    
	    // ���j���[�̓���  
		@Override  
		public boolean onOptionsItemSelected(MenuItem item) {  
			switch (item.getItemId()) {
			case 1:// ����  
				
				//���ݒn�{�^������i���L���j
				
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
	        // �j��
	        overlay.disableMyLocation();
	        map.getOverlays().remove(overlay);

	        super.onDestroy();
	    }

}

