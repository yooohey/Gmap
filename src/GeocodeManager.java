
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class GeocodeManager {

	// ���W����Z��������֕ϊ�
	public static String point2address(double latitude, double longitude, Context context)
		throws IOException
	{
		String address_string = new String();

		// �ϊ����s
		Geocoder coder = new Geocoder(context, Locale.JAPAN);
		List<Address> list_address = coder.getFromLocation(latitude, longitude, 1);

		if (!list_address.isEmpty()){

			// �ϊ��������́C�ŏ��̕ϊ������擾
			Address address = list_address.get(0);
			StringBuffer sb = new StringBuffer();

			// adress�̑�敪���珬�敪�܂ł����s�őS����
			String s;
			for (int i = 0; (s = address.getAddressLine(i)) != null; i++){
				sb.append( s + "\n" );
			}

			address_string = sb.toString();
		}

		return address_string;
	}

}