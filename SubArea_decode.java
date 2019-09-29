package decode;

public class SubArea_decode {
	public static String[] CircleOrPoint(int[] subArea_arr) {
		String[] str_area = new String[6];
		str_area[0] = "SubArea Shape: Circle or Point";
		
		int scale = PayloadDataType.ASCtoDec(3, 4, subArea_arr);
		
		int lat = PayloadDataType.ASCtoDec(30,53,subArea_arr);
		double latit = lat/600000.0;
		if(lat != 91000) {
			if(latit>=0) {
				str_area[1] = "N "+latit;
			}
			else {
				str_area[1] = "S "+latit;
			}
		}
		
		int lon = PayloadDataType.ASCtoDec(5,29,subArea_arr);
		double longi = lon/600000.0;
		if(lon != 181000) {
			if(lon>=0) {
				str_area[2] = "E "+longi;
			}
			else {
				str_area[2] = "W "+longi;
			}
		}
		
		int pre = PayloadDataType.ASCtoDec(54, 56, subArea_arr);
		str_area[3] = "Precision: "+pre;
		
		int radius = PayloadDataType.ASCtoDec(57, 68, subArea_arr);
		if(radius > 0) {
			str_area[4] = "Radius: "+radius+ScaleFactor(scale);
		}
		else if(radius == 0) {
			str_area[4] = "Radius: default";
		}
		return str_area;
	}
	public static String[] Rectangle(int[] subArea_arr) {
		String[] str_area = new String[7];
		str_area[0] = "SubArea Shape: Rectangle";
		
		int scale = PayloadDataType.ASCtoDec(3, 4, subArea_arr);
		
		int lat = PayloadDataType.ASCtoDec(30,53,subArea_arr);
		double latit = lat/600000.0;
		if(lat != 91000) {
			if(latit>=0) {
				str_area[1] = "N "+latit;
			}
			else {
				str_area[1] = "S "+latit;
			}
		}
		
		int lon = PayloadDataType.ASCtoDec(5,29,subArea_arr);
		double longi = lon/600000.0;
		if(lon != 181000) {
			if(lon>=0) {
				str_area[2] = "E "+longi;
			}
			else {
				str_area[2] = "W "+longi;
			}
		}
		
		int pre = PayloadDataType.ASCtoDec(54, 56, subArea_arr);
		str_area[3] = "Precision: "+pre;
		
		int east = PayloadDataType.ASCtoDec(57, 64, subArea_arr);
		int north = PayloadDataType.ASCtoDec(65, 72, subArea_arr);
		if(east == 0) {
			str_area[4] = "N/S line(default)";
		}
		else {
			str_area[4] = "E-dimension: "+east+ScaleFactor(scale);
		}
		if(north == 0) {
			str_area[5] = "E/W line(default)";
		}
		else {
			str_area[5] = "N-dimension: "+north+ScaleFactor(scale);
		}
		int orient = PayloadDataType.ASCtoDec(73, 81, subArea_arr);
		if (orient == 0) {
			str_area[6] = "No rotation";
		}
		else {
			str_area[6] = "Orientation: "+orient+" Degrees clockwise from true N";
		}
		return str_area;
	}
	
	public static String[] Sector(int[] subArea_arr) {
		String[] str_area = new String[7];
		str_area[0] = "SubArea Shape: Sector";
		
		int scale = PayloadDataType.ASCtoDec(3, 4, subArea_arr);
		
		int lat = PayloadDataType.ASCtoDec(30,53,subArea_arr);
		double latit = lat/600000.0;
		if(lat != 91000) {
			if(latit>=0) {
				str_area[1] = "N "+latit;
			}
			else {
				str_area[1] = "S "+latit;
			}
		}
		
		int lon = PayloadDataType.ASCtoDec(5,29,subArea_arr);
		double longi = lon/600000.0;
		if(lon != 181000) {
			if(lon>=0) {
				str_area[2] = "E "+longi;
			}
			else {
				str_area[2] = "W "+longi;
			}
		}
		
		int pre = PayloadDataType.ASCtoDec(54, 56, subArea_arr);
		str_area[3] = "Precision: "+pre;
		
		int radius = PayloadDataType.ASCtoDec(57, 68, subArea_arr);
		if(radius > 0) {
			str_area[4] = "Radius: "+radius+ScaleFactor(scale);
		}
		else if(radius == 0) {
			str_area[4] = "Radius: default";
		}
		
		int left = PayloadDataType.ASCtoDec(69,77,subArea_arr);
		str_area[5] = "Left Boundary: "+left;
		
		int right = PayloadDataType.ASCtoDec(78,86,subArea_arr);
		str_area[6] = "Right Boundary: " +right;
		
		return str_area;
	}
	
	public static String[] Polygon(int[] subArea_arr) {
		String[] str_area = new String[10];
		str_area[0] = "SubArea Shape: Polygon";
		int scale = PayloadDataType.ASCtoDec(3, 4, subArea_arr);
		
		for (int i=0; i<4; i++) {
			int bearing = PayloadDataType.ASCtoDec(6+i*20, 15+i*20, subArea_arr);
			str_area[1+2*i] = "Bearing :"+bearing+ScaleFactor(scale);
			
			int distance = PayloadDataType.ASCtoDec(16+i*20, 25+i*20, subArea_arr);
			str_area[2+2*i] = "Distance: "+distance+ScaleFactor(scale);
		}
		return str_area;
	}
	
	public static String[] Polyline(int[] subArea_arr) {
		String[] str_area = new String[10];
		str_area[0] = "SubArea Shape: Polyline";
		int scale = PayloadDataType.ASCtoDec(3, 4, subArea_arr);
		
		for (int i=0; i<4; i++) {
			int bearing = PayloadDataType.ASCtoDec(6+i*20, 15+i*20, subArea_arr);
			str_area[1+2*i] = "Bearing :"+bearing+ScaleFactor(scale);
			
			int distance = PayloadDataType.ASCtoDec(16+i*20, 25+i*20, subArea_arr);
			str_area[2+2*i] = "Distance: "+distance+ScaleFactor(scale);
		}
		return str_area;
	}
	public static String[] Associated_text(int[] subArea_arr) {
		String[] str_area = new String[2];
		str_area[0] = "SubArea Shape: Associated Text";
		str_area[1] = "Text: "+PayloadDataType.Bin2String(3, 86, subArea_arr);
		return str_area;
	}

	public static String ScaleFactor(int scale) {
		String sf="";
		switch(scale) {
		case 1: sf = "m"; break;
		case 2: sf = "m^2"; break;
		case 3: sf = "m^3"; break;
		default:
			break;
		}
		return sf;
	}
}
