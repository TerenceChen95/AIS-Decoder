package decode;

public class decoder {
	
	public static String[] decode_GPRMC(String str) {                           //解析GPRMC类型
		String[] str_arr = str.split(",");
		String[] str_out = new String[6];
 
		int latitude_Degree = (int) (Double.valueOf(str_arr[3])/100);
		double lati_cent_temp = Double.valueOf(str_arr[3]) - latitude_Degree*100;
		int latitude_Cent = (int) lati_cent_temp;
		double lati_second_temp = (lati_cent_temp-latitude_Cent)*60;
		int latitude_Second = (int) lati_second_temp;
		
		int longtitude_Degree = (int) (Double.valueOf(str_arr[5])/100);
		double longti_cent_temp = Double.valueOf(str_arr[5]) - longtitude_Degree*100;
		int longtitude_Cent = (int) longti_cent_temp;
		double longti_second_temp = (longti_cent_temp-longtitude_Cent)*60;
		int longtitude_Second = (int) longti_second_temp;
		
		str_out[0] = "GPS info: ";
		str_out[1] = str_arr[4] + latitude_Degree + "°" + latitude_Cent + "'" +latitude_Second + "\""; 
		str_out[2] = str_arr[6] + longtitude_Degree + "°" + longtitude_Cent + "'" +longtitude_Second + "\"";
		
		float speed_tmp = Float.valueOf(str_arr[7]);
		double speed = speed_tmp * 1.85;
		
		str_out[3] = "Speed: " + String.valueOf(speed) + "nmi"; 
		str_out[4] = "Direction: " + str_arr[8];
		
		String date = str_arr[9];
		str_out[5] = "Date:" + String.valueOf(Integer.valueOf(date.substring(4,6))+2000) + "/" + date.substring(2,4) + "/" + date.substring(0, 2);
		
		return str_out;
	}
	

	public static String[] decode_GPGGA(String str) {                  //解析GPCCA类型
		String[] str_arr = str.split(",");
		String[] str_out = new String[6];
		
		int hour, minute;
		String temp = str_arr[1];
		String time;
		hour = Integer.valueOf(temp.substring(0, 2));
		minute = Integer.valueOf(temp.substring(2,4));
		time = "Time: " + String.valueOf(hour) + ":" + String.valueOf(minute);
		
		int latitude_Degree = (int) (Double.valueOf(str_arr[2])/100);
		double lati_cent_temp = Double.valueOf(str_arr[2]) - latitude_Degree*100;
		int latitude_Cent = (int) lati_cent_temp;
		double lati_second_temp = (lati_cent_temp-latitude_Cent)*60;
		int latitude_Second = (int) lati_second_temp;
		
		int longtitude_Degree = (int) (Double.valueOf(str_arr[4])/100);
		double longti_cent_temp = Double.valueOf(str_arr[4]) - longtitude_Degree*100;
		int longtitude_Cent = (int) longti_cent_temp;
		double longti_second_temp = (longti_cent_temp-longtitude_Cent)*60;
		int longtitude_Second = (int) longti_second_temp;
		
		str_out[0] = "GPS info:";
		str_out[1] = str_arr[3] + latitude_Degree + "°" + latitude_Cent + "'" +latitude_Second + "\""; 
		str_arr[2] = str_arr[5] + longtitude_Degree + "°" + longtitude_Cent + "'" +longtitude_Second + "\"";
		
		str_out[3] = "Sea-level Height:" + str_arr[8];
		str_out[4] = "Geoid Height" + str_arr[9];
		str_out[5] = time;
			
		return str_out;
	}
	
	public static String[] decode_GPGSV(String str) {             //解析GPGSV类型
		String[] str_arr = str.split(",");
		String[] str_new = new String[2];
		str_new[0]  = "SNR:" + str_arr[7];
		str_new[1]  = str_arr[0] + "Visible-satelite status";
		
		return str_new;
	}

	public static String decode_GPGSA(String str) {                //解析GPGSA类型
		str = "Satelite_info: " + str;
		return str;
	}
	
	public static String[] decode_AIVDM(String str){               //船舶动态消息
		String[] str_arr = str.split(",");                         //分开逗号
		String payload = str_arr[5];                               //消息负载在第6位
		int[] payload_arr = Payload.to6bitASII(payload);           //负载字符转为6位二进制

		int bin=1, type=0;
		String[] ship_info = new String[15];	                   
		for(int i=5; i>0; i--) {                                    //消息类型在负载的前6位
			type += payload_arr[i]*bin;
			bin *= 2;
		}
		switch (type) {                                             //根据不同类型解析
		case 1 : 
		case 2 : 
		case 3 : 
			ship_info = PayloadDataType.AIS_type123(payload_arr); break;
		case 4:
		case 11:
			ship_info = PayloadDataType.AIS_type11(payload_arr);break;
		case 5:
			ship_info = PayloadDataType.AIS_type5(payload_arr); break;
		case 6:
			ship_info = PayloadDataType.AIS_type6(payload_arr);break;
		case 7: case 13:
			ship_info = DataType2.type7(payload_arr);break;   //type 13 is a receipt acknowledgement of type 12 message
		case 8:
			ship_info = DataType2.type8(payload_arr);break;
		case 9 :
			ship_info = DataType2.type9(payload_arr);break;
		case 10:
			ship_info = DataType2.type10(payload_arr);break;
		case 12:
			ship_info = PayloadDataType.AIS_type12(payload_arr);break;
		case 14:
			ship_info = PayloadDataType.AIS_type14(payload_arr);break;
		case 15:
			ship_info = DataType2.type15(payload_arr);break;
		case 16:
			ship_info = DataType2.type16(payload_arr);break;
		case 17:
			ship_info = PayloadDataType.AIS_type17(payload_arr);break; 
		case 18:
			ship_info = PayloadDataType.AIS_type18(payload_arr);break;
		case 19:
			ship_info = DataType2.type19(payload_arr);break;
		case 20:
			ship_info = DataType2.type20(payload_arr);break;
		case 21:
			ship_info = DataType2.type21(payload_arr);break;
		case 22:
			ship_info = DataType2.type22(payload_arr);break;
		case 23:
			ship_info = DataType2.type23(payload_arr);break;
		case 24:
			ship_info = DataType2.type24(payload_arr);break;
		case 25:
			ship_info = DataType2.type25(payload_arr);break;
		case 27:
			ship_info = DataType2.type27(payload_arr);break;
		default:
			break;
		}
		return ship_info;
		
	}
}
