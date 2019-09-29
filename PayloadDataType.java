package decode;

public class PayloadDataType {
	
	/*****************************************************************************************
	 * 主要功能函数：
	 * ASCtoDec函数功能：将二进制转为十进制数。 格式: ASCtoDec(开始位， 结束位， 二进制数组 )
	 * Bin2String函数： 将二进制转为字符串类型。 格式：Bin2String(开始位, 结束位, 二进制数组)
	 * toPosition函数： 得到经纬度数据。 格式：toPosition(纬度开始位, 纬度结束位, 经度开始位, 经度结束位, 二进制数组)
	 * toMonth函数： 得到日期的月份
	 * **************************************************************************************
	 * 主要变量说明：
	 * payload_arr: 负载二进制数组
	 * payload_info: 解析后字符串数组
	 * ***************************************************************************************/
	
	public static String[] AIS_type123(int[] payload_arr) {                           //类型1,2,3
		String[] payload_info = new String[10] ;
		payload_info[0] = "Type1/2/3: Position Report Class A";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);    //ASCtoDec函数功能：将二进制转为十进制数
		payload_info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Navigation_status航行状态
		String Navigation_status = null;
		int NS = ASCtoDec(38,41,payload_arr);
		switch(NS) {
		case 0: Navigation_status = "Under way using engine";
		case 1: Navigation_status = "At anchor";
		case 2: Navigation_status = "Not under command";
		case 3: Navigation_status = "Restricted manoeuverability";
		case 4: Navigation_status = "Constrained by her draught";
		case 5: Navigation_status = "Moored";
		case 6: Navigation_status = "Aground";
		case 7: Navigation_status = "Enaged in Fishing";
		case 8: Navigation_status = "Under way sailing";
		}
		payload_info[2] = "Navigation_status: " + Navigation_status;
		
		//Repeat Indicator重复指标
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[3] = "Repeate_info: " + Repeat_info;
		
		//Rate of Turn转向角度
		int ROT = ASCtoDec(42,49,payload_arr);
		payload_info[4] = "RateOfTurn: " + String.valueOf(ROT);
		
		//Speed Over Ground对地速度
		double SOG = ASCtoDec(50,59,payload_arr)/10.0;
		payload_info[5] = "SpeedOverGround: " + String.valueOf(SOG);
		
		//Position Accuracy位置精确度
		String Position_Accuracy = null;
		int PA = ASCtoDec(60,60, payload_arr);
		if(PA==1) {
			Position_Accuracy = "High";
		}
		else if (PA == 0) {
			Position_Accuracy = "Low";
		}
		payload_info[6] = "PositionAccuracy: " + Position_Accuracy;
		
		//Longitude经度
		String longitude = null;
		int longit = ASCtoDec(61,88,payload_arr);
		double longi = longit/600000.0;
		if(longi<180 && longi>-180) {
			if(longi>=0) {
				longitude = "E " + String.valueOf(longi);
			}
			else {
				longitude = "W " + String.valueOf(longi);
			}
		}
		else {
			longitude = "longitude: default";
		}
		payload_info[7] = longitude;
		
		if(payload_arr.length > 115) {
			//Latitude纬度
			String latitude;
			int latit = ASCtoDec(89,115,payload_arr);
			double lati = latit/600000.0;
			if(lati <90 && lati >-90) {
				if(latit>=0) {
					latitude = "N " + String.valueOf(lati);
				}
				else {
					latitude = "S "+ String.valueOf(lati);
				}
			}
			else {
				latitude = "latitude: default";
			}
			payload_info[8] = latitude;
			
			//Course Over Ground
			float COG = (float) (ASCtoDec(116,127,payload_arr)/10.0);
			payload_info[9] = "CourseOverGround: " + String.valueOf(COG);
		}
		return payload_info;
	}
	
	public static String[] AIS_type5(int[] payload_arr) {
		String[] payload_info = new String[11];
		payload_info[0] = "Type5: Static and Voyage Related Data";
		//MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,6,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No repeat";
		}
		else if(Repeat_Indicator == 0) {
			Repeat_info = "default";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[2] = "Repeate_info: " + Repeat_info;
		
		//IMO number
		int IMO = ASCtoDec(40,69,payload_arr);
		payload_info[3] = "IMO number: " + String.valueOf(IMO);
		
		if(payload_arr.length > 111) {
			//Call sign
			String Callsign = Bin2String(70,111,payload_arr);
			payload_info[4] = "Call Sign: " + Callsign; 
			
			//Ship Name
			String VesselName = Bin2String(112,231,payload_arr);
			payload_info[5] = "Ship_Name: " + VesselName;
			
			//Type of Ship
			int Ship_Type = ASCtoDec(232,239,payload_arr);
			String stype = getShiptype(Ship_Type);
			payload_info[6] = "Ship_Type: " + stype;
			
			//ShipDimension
			int to_bow = ASCtoDec(240,248,payload_arr);
			int to_stern = ASCtoDec(249,257,payload_arr);
			int to_port = ASCtoDec(258,263,payload_arr);
			int to_starboard = ASCtoDec(264,269,payload_arr);
			payload_info[7] = "Ship_Dimensions: A="+to_bow+", B="+to_stern+", C="+to_port+", D="+to_starboard;
			
			//UTC
			int month = ASCtoDec(274,277,payload_arr);
			int day = ASCtoDec(278,282,payload_arr);
			int hour = ASCtoDec(283,287,payload_arr);
			int minute = ASCtoDec(288,293,payload_arr);
			payload_info[8] = "ETA: "+toMonth(month)+String.valueOf(day)+", "+String.valueOf(hour)+":"+String.valueOf(minute);
			
			//Draught
			int dra = ASCtoDec(294,301,payload_arr);
			float Draught = dra/10;
			payload_info[9] = "Draught: "+Draught;
			
			//Destination
			String Des = "";
			if(payload_arr.length<=421) {
				Des = Bin2String(302,336,payload_arr);
			}
			else {
				Des = Bin2String(302,421,payload_arr);
			}
					
			payload_info[10] = "Destination: "+Des;
		}
		return payload_info;
	}
	
	public static String[] AIS_type11(int[] payload_arr) {
		String[] payload_info = new String[6];
		payload_info[0] = "Type11: UTC/Date Response";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[2] = "Repeate_info: " + Repeat_info;
		
		//UTC
		int year = ASCtoDec(38,51,payload_arr);
		String Year = String.valueOf(year);
		if (year == 0) {
			Year = "N/A";
		}
		int month = ASCtoDec(52,55,payload_arr);
		String Mon = toMonth(month);
		if(month == 3) {
			Mon = "N/A";
		}
		int day = ASCtoDec(56,60,payload_arr);
		String Day = String.valueOf(day);
		if (day == 0) {
			Day = "N/A";
		}
		int hour = ASCtoDec(61,65,payload_arr);
		String Hour = String.valueOf(hour);
		if(hour >= 24) {
			Hour = "N/A";
		}
		int minute = ASCtoDec(66,71,payload_arr);
		String Minute = String.valueOf(minute);
		if(minute>=60) {
			Minute = "N/A";
		}
		int second = ASCtoDec(72,77,payload_arr);
		String Second = String.valueOf(second);
		if(second >=60) {
			Second = "N/A";
		}
		payload_info[3] = "UTC: "+Year+", "+Mon+" "+Day+", "+Hour+":"+Minute+"."+Second+"'";
		
		//Longitude
		String longitude = null;
		int longit = ASCtoDec(61,88,payload_arr);
		double longi = longit/600000.0;
		if(longi<180 && longi>-180) {
			if(longi>=0) {
				longitude = "E " + String.valueOf(longi);
			}
			else {
				longitude = "W " + String.valueOf(longi);
			}
		}
		else {
			longitude = "longitude: default";
		}
		payload_info[4] = longitude;
		
		if(payload_arr.length > 115) {
			//Latitude
			String latitude;
			int latit = ASCtoDec(89,115,payload_arr);
			double lati = latit/600000.0;
			if(lati <90 && lati >-90) {
				if(latit>=0) {
					latitude = "N " + String.valueOf(lati);
				}
				else {
					latitude = "S "+ String.valueOf(lati);
				}
			}
			else {
				latitude = "latitude: default";
			}
			payload_info[5] = latitude;
		}
		
		return payload_info;
	}
	
	public static String[] AIS_type12(int[] payload_arr) {
		String[] payload_info = new String[7];
		payload_info[0] = "Type12: Addressed Safety-Related Message";
		
		//Source MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "Source MMSI: " + String.valueOf(mmsi);
		
		//Destination MMSI
		payload_info[2] = "Destination MMSI: "+ String.valueOf(ASCtoDec(40,69,payload_arr));
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[3] = "Repeate_info: " + Repeat_info;
		
		//Sequence Number
		String SquenceNumber = String.valueOf(ASCtoDec(38,39,payload_arr));
		payload_info[4] = SquenceNumber;
		
		//Retransmit flag
		int Retran = payload_arr[70];
		String Retransmit = null;
		if(Retran == 1) {
			Retransmit = "retransmitted";
		}
		else if(Retran == 0) {
			Retransmit = "No retransmit";
		}
		payload_info[5] = Retransmit;
		
		//Text
		String text = Bin2String(72,payload_arr.length,payload_arr);
		payload_info[6] = "Text: " + text;
		
		return payload_info;
	}
	
	public static String[] AIS_type14(int[] payload_arr) {
		String[] payload_info = new String[4];
		payload_info[0] = "Type14: Safety-Related Broadcast Message";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "Source MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[2] = "Repeate_info: " + Repeat_info;	
		
		//Text
		String Text = Bin2String(40,payload_arr.length,payload_arr);
		payload_info[3] = "Text: "+Text;
		
		return payload_info;
	}
	
	public static String[] AIS_type18(int[] payload_arr) {
		String[] payload_info = new String[10];
		payload_info[0] = "Type18: Standard Class B CS Position Report";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[2] = "Repeate_info: " + Repeat_info;
		
		//Speed Over Ground
		double SOG = ASCtoDec(50,59,payload_arr)/10.0;
		payload_info[3] = "SpeedOverGround: " + String.valueOf(SOG);
		
		//Position Accuracy
		String Position_Accuracy = null;
		int PA = ASCtoDec(60,60, payload_arr);
		if(PA==1) {
			Position_Accuracy = "High";
		}
		else if (PA == 0) {
			Position_Accuracy = "Low";
		}
		payload_info[4] = "PositionAccuracy: " + Position_Accuracy;
		
		//Longitude
		String longitude = null;
		int longit = ASCtoDec(61,88,payload_arr);
		double longi = longit/600000.0;
		if(longi<180 && longi>-180) {
			if(longi>=0) {
				longitude = "E " + String.valueOf(longi);
			}
			else {
				longitude = "W " + String.valueOf(longi);
			}
		}
		else {
			longitude = "longitude: default";
		}
		payload_info[5] = longitude;
		
		//Latitude
		String latitude;
		int latit = ASCtoDec(89,115,payload_arr);
		double lati = latit/600000.0;
		if(lati <90 && lati >-90) {
			if(latit>=0) {
				latitude = "N " + String.valueOf(lati);
			}
			else {
				latitude = "S "+ String.valueOf(lati);
			}
		}
		else {
			latitude = "latitude: default";
		}
		payload_info[6] = latitude;
		
		//Course Over Ground
		float COG = (float) (ASCtoDec(116,127,payload_arr)/10.0);
		payload_info[7] = "CourseOverGround: " + String.valueOf(COG);
		
		//True Heading
		int True_Heading = ASCtoDec(124,132,payload_arr);
		payload_info[8] = "True Heading: "+ String.valueOf(True_Heading);
		
		//CS Unit
		int CS_Unit = payload_arr[141];
		String unit = null;
		if(CS_Unit == 0) {
			unit = "Class B SOTDMA unit";
		}
		else if (CS_Unit == 1) {
			unit = "Class B CS(Carrier Sense) unit ";
		}
		payload_info[9] = "CS Unit: "+ unit;
		
		return payload_info;
		
	}
	
	public static String[] AIS_type17(int[] payload_arr) {
		String[] payload_info = new String[5];
		payload_info[0] = "Type17: DGNSS Broadcast Binary Message";
		
		//Source MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "Source MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[2] = "Repeate_info: " + Repeat_info;
		
		//Longitude
		String longitude = null;
		int longit = ASCtoDec(61,88,payload_arr);
		double longi = longit/600.0;
		if(longi<180 && longi>-180) {
			if(longi>=0) {
				longitude = "E " + String.valueOf(longi);
			}
			else {
				longitude = "W " + String.valueOf(longi);
			}
		}
		else {
			longitude = "longitude: default";
		}
		payload_info[3] = longitude;
		
		//Latitude
		String latitude;
		int latit = ASCtoDec(89,115,payload_arr);
		double lati = latit/600.0;
		if(lati <90 && lati >-90) {
			if(latit>=0) {
				latitude = "N " + String.valueOf(lati);
			}
			else {
				latitude = "S "+ String.valueOf(lati);
			}
		}
		else {
			latitude = "latitude: default";
		}
		payload_info[4] = latitude;
		
		//DGNSS correction Data
		//decoded by RTCM2 protocol
		String data ="DGNSS correction Data";
		payload_info[5] = data + Bin2String(80,payload_arr.length,payload_arr);
		
		return payload_info;
	}
	
	public static String[] AIS_type6(int[] payload_arr) {
		String[] payload_info = new String[25];
		payload_info[0] = "Type6: Binary Addressed Message";
		
		//Source MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "Source MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,payload_arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		payload_info[2] = "Repeate_info: " + Repeat_info;
		
		//Sequence Number
		int SeqNum = ASCtoDec(38,39,payload_arr);
		payload_info[3] = "Sequence Number: " + String.valueOf(SeqNum);
		
		//Destination MMSI
		int Des_mmsi = ASCtoDec(40,69,payload_arr);
		payload_info[4] = "Destination MMSI: " + String.valueOf(Des_mmsi);
		
		//Retransmit flag
		String retransmit = "";
		if (payload_arr[70] == 1) {
			retransmit = "No retransmit";
		}
		else if (payload_arr[70] == 0) {
			retransmit = "Retransmitted";
		}
		payload_info[5] = "Retransmit_flag: "+ retransmit;
		
		//Designated Area Code
		int dac = ASCtoDec(72,81,payload_arr);
		payload_info[6] = "Designated Area Code: " + dac;
		
		//Functional ID
		int fid = ASCtoDec(82,87,payload_arr);
		payload_info[7] = "Functional ID: "+fid;
		
		//Data
		String data = "";
		if(dac == 1) {
			if(fid == 12) {
				payload_info[8] = "Description: Dangerous cargo indication";
				String LastPortOfCall = Bin2String(88,117,payload_arr);
				String lmonth = toMonth(ASCtoDec(118,121,payload_arr));
				int lday = ASCtoDec(122,126,payload_arr);
				int lhour = ASCtoDec(127,131,payload_arr);
				int lminute = ASCtoDec(132,137,payload_arr);
				payload_info[9] = "Last Port Of Call: "+LastPortOfCall + "Time: "+lmonth+" "+lday+", "+lhour+" : "+lminute;
				
				String NextPortOfCall = Bin2String(138,167,payload_arr);
				String nmonth = toMonth(ASCtoDec(168,171,payload_arr));
				int nday = ASCtoDec(172,176,payload_arr);
				int nhour = ASCtoDec(177,181,payload_arr);
				int nminute = ASCtoDec(182,187,payload_arr);
				payload_info[10] = "Next Port Of Call: "+NextPortOfCall + "Time: "+nmonth+" "+nday+", "+nhour+" : "+nminute;

				String DangerousGood = Bin2String(188,307,payload_arr);
				payload_info[11] = "Main Dangerous Good: "+DangerousGood;
				
				String imdcat = Bin2String(308,331,payload_arr);
				payload_info[12] = "IMD Category: " +imdcat;
				
				int unid = ASCtoDec(332,344,payload_arr);
				payload_info[13] = "UN Number: "+ unid;
				
				int amount = ASCtoDec(345,354,payload_arr);
				int unit_int = ASCtoDec(355,356,payload_arr);
				String unit = "";
				switch(unit_int) {
					case 0 : unit = "default";break;
					case 1 : unit = "kg";break;
					case 2 : unit = "tons";break;
					case 3 : unit = "kilotons";break;
					default:
						break;
				}
				payload_info[14] = "Amount Of Cargo: "+amount+unit;
			}
			else if(fid == 14) {
				payload_info[8] = "Description: Tidal Window";
				
				String month = toMonth(ASCtoDec(88,91,payload_arr));
				int day = ASCtoDec(92,96,payload_arr);
				payload_info[9] = "Date: "+month+" "+day;
				
				//tidal info Array
				int[] tidal_info = new int[94];
				for(int i=0; i<93; i++) {
					tidal_info[i] = payload_arr[98+i];
				}
				int lat = ASCtoDec(0,26,tidal_info);
				double latit = lat/600000.0;
				if(lat != 91000) {
					if(latit>=0) {
						payload_info[10] = "N "+latit;
					}
					else {
						payload_info[10] = "S "+latit;
					}
				}
				
				int lon = ASCtoDec(27,54,tidal_info);
				double longi = lon/600000.0;
				if(lon != 181000) {
					if(lon>=0) {
						payload_info[11] = "E "+longi;
					}
					else {
						payload_info[11] = "W "+longi;
					}
				}
				
				int from_hour = ASCtoDec(55,59,tidal_info);
				int from_min = ASCtoDec(60,65,tidal_info);
				int to_hour = ASCtoDec(66,70,tidal_info);
				int to_min = ASCtoDec(71,76,tidal_info);
				payload_info[12] = "From "+from_hour+" : "+from_min+", to "+to_hour+" : "+to_min;
				
				//current info
				int cdir = ASCtoDec(77,85,tidal_info);
				int cspeed = ASCtoDec(86,92,tidal_info);
				payload_info[13] = "Current Direction(predict): "+cdir+"Degree,  "+"Current Speed(predict): "+cspeed*0.1+"knots";
			}
			else if(fid == 16) {  
				payload_info[8] = "Description: Number of persons on board";
			    int persons = ASCtoDec(55,68,payload_arr);
			    if (persons != 0) {
			    	payload_info[9] = "Persons Number: "+persons;
			    }
			    else {
			    	payload_info[9] = "Persons Number: default";
			    }
			}
			else if(fid == 18) {
				payload_info[8] = "Description: Clearance Time to Enter Port";
				int linkage = ASCtoDec(88,97,payload_arr);
				payload_info[9] = "Message Linkage ID: "+linkage;
				
				String month = toMonth(ASCtoDec(98,101,payload_arr));
				int day = ASCtoDec(102,106,payload_arr);
				int hour = ASCtoDec(107,111,payload_arr);
				int minute = ASCtoDec(112,117,payload_arr);
				payload_info[10] = "UTC: "+month+" "+day+", "+hour+" : "+minute;
				
				String portname = Bin2String(118,237,payload_arr);
				payload_info[11] = "Name of Port&Berth: "+portname;
				
				String des = Bin2String(238,267,payload_arr);
				payload_info[12] = "Destination" + des;
				
				int lat = ASCtoDec(293,316,payload_arr);
				double latit = lat/600000.0;
				if(lat != 91000) {
					if(latit>=0) {
						payload_info[13] = "N "+latit;
					}
					else {
						payload_info[13] = "S "+latit;
					}
				}
				
				int lon = ASCtoDec(268,292,payload_arr);
				double longi = lon/600000.0;
				if(lon != 181000) {
					if(lon>=0) {
						payload_info[14] = "E "+longi;
					}
					else {
						payload_info[14] = "W "+longi;
					}
				}
			}
			else if(fid == 20) {
				payload_info[8] = "Description: Berthing Data";
				
				int linkage = ASCtoDec(88,97,payload_arr);
				payload_info[9] = "Message Linkage ID: "+linkage;
				
				int berth_length = ASCtoDec(98,106,payload_arr);
				if(berth_length>0 & berth_length<512) {
					payload_info[10] = "Berth length: "+berth_length+"m";
				}
				
				double berth_depth = ASCtoDec(107,114,payload_arr)/10.0;
				if(berth_depth>0 & berth_depth<25.6) {
					payload_info[11] = "Berth Depth: "+berth_depth+"m";
				}
				
				int mooring = ASCtoDec(115,117,payload_arr);
				String Mooring_Pos = "Mooring Position: ";
				switch(mooring) {
					case 0: Mooring_Pos += "default";break;
					case 1: Mooring_Pos += "Port-side to";break;
					case 2: Mooring_Pos += "Starboard-side to";break;
					case 3: Mooring_Pos += "Mediterranean (end-on) mooring";break;
					case 4: Mooring_Pos += "Mooring buoy";break;
					case 5: Mooring_Pos += "Anchorage";break;
					case 6: 
					case 7: Mooring_Pos += "Reserved for future use";break;
				}
				payload_info[12] = Mooring_Pos;
				
				String month = toMonth(ASCtoDec(118,121,payload_arr));
				int day = ASCtoDec(122,126,payload_arr);
				int hour = ASCtoDec(127,131,payload_arr);
				int minute = ASCtoDec(132,137,payload_arr);
				payload_info[13] = "UTC: "+month+" "+day+", "+hour+" : "+minute;
				
				String Services_Availability = "Services Availability: ";
				if(payload_arr[138] == 1) {
					Services_Availability += "services know";
				}
				else if (payload_arr[138] == 0) {
					Services_Availability += "services unknown";
				}
				payload_info[14] = Services_Availability;
				
				String services ="";
				String agent = Service_status(ASCtoDec(139,40,payload_arr));
				services += "Agent: "+agent;				
				String fuel = Service_status(ASCtoDec(141,142,payload_arr));
				services += ", bunker/fuel: "+fuel;				
				String chandler = Service_status(ASCtoDec(143,144,payload_arr));
				services += ", Chandler: "+chandler;				
				String stevedore = Service_status(ASCtoDec(145,146,payload_arr));
				services += ", Stevedore: "+stevedore;				
				String electrical = Service_status(ASCtoDec(147,148,payload_arr));
				services += ", Electrical: "+electrical;				
				String water = Service_status(ASCtoDec(149,150,payload_arr));
				services +=  ", Potable Water; "+water;				
				String customs = Service_status(ASCtoDec(151,152,payload_arr));
				services += ", Customs house: "+customs;
				String cartage = Service_status(ASCtoDec(153,154,payload_arr));
				services += ", Cartage: "+cartage;
				String crane = Service_status(ASCtoDec(155,156,payload_arr));
				services +=", Crane(s): "+ crane;
				String lift = Service_status(ASCtoDec(157,158,payload_arr));
				services +="Lift(s): "+lift;
				String medical = Service_status(ASCtoDec(159,160,payload_arr));
				services +="Medical facilities: "+medical;
				String navrepair = Service_status(ASCtoDec(161,162,payload_arr));
				services +="Navigation repair: "+navrepair;
				String provisions = Service_status(ASCtoDec(163,164,payload_arr));
				services +="Provisions: "+provisions;
				String shiprepair = Service_status(ASCtoDec(165,166,payload_arr));
				services +="Ship Repair: "+shiprepair;
				String surveyor = Service_status(ASCtoDec(167,168,payload_arr));
				services +="Surveyor: "+surveyor;
				String steam = Service_status(ASCtoDec(169,170,payload_arr));
				services +="Steam: "+steam;
				String tugs = Service_status(ASCtoDec(171,172,payload_arr));
				services +="Tugs: "+tugs;
				String solidwaste = Service_status(ASCtoDec(173,174,payload_arr));
				services +="Waste disposal (solid): "+solidwaste;
				String liquidwaste = Service_status(ASCtoDec(175,176,payload_arr));
				services +="Waste disposal (liquid): "+liquidwaste;
				String hazardouswaste = Service_status(ASCtoDec(177,178,payload_arr));
				services +="Waste disposal (hazardous): "+hazardouswaste;
				String ballast = Service_status(ASCtoDec(179,180,payload_arr));
				services +="Reserved ballast exchange: "+ballast;
				String additional = Service_status(ASCtoDec(181,182,payload_arr));
				services +="Additional services: "+additional;
				payload_info[15] = services;
				
				String berth_name = Bin2String(191,payload_arr.length,payload_arr);
				payload_info[16] = "Berth Name: "+ berth_name;
				
				/*int lat = ASCtoDec(336,359,payload_arr);
				double latit = lat/600000.0;
				if(lat != 91000) {
					if(latit>=0) {
						payload_info[17] = "N "+latit;
					}
					else {
						payload_info[17] = "S "+latit;
					}
				}
				
				int lon = ASCtoDec(311,335,payload_arr);
				double longi = lon/600000.0;
				if(lon != 181000) {
					if(lon>=0) {
						payload_info[18] = "E "+longi;
					}
					else {
						payload_info[18] = "W "+longi;
					}
				}*/
			}
			else if (fid == 23) {
				payload_info[8] = "Description: Area Notice (addressed) message header";
				int linkage = ASCtoDec(88,97,payload_arr);
				payload_info[9] = "Message Linkage ID: "+linkage;
				
				int notice = ASCtoDec(98,104,payload_arr);
				String notice_des = "Notice Description: "+AreaNoticeDescription(notice);
				payload_info[10] = notice_des;
				
				String month = toMonth(ASCtoDec(105,108,payload_arr));
				int day = ASCtoDec(109,113,payload_arr);
				int hour = ASCtoDec(114,118,payload_arr);
				int minute = ASCtoDec(119,124,payload_arr);
				payload_info[11] = "UTC: "+month+" "+day+", "+hour+" : "+minute;
				
				int duration = ASCtoDec(125,142,payload_arr);
				if(duration != 262143 && duration != 0) {
					payload_info[12] = "Duration: "+duration+"minutes";
				}
				else {
					payload_info[12] = "Duration: N/A";
				}
				
			    int[] subArea_arr = new int[87];
			    for(int i=0; i<87; i++) {
			    	subArea_arr[i] = payload_arr[143+i]; 
			    }
			    int subarea_type = ASCtoDec(0,2,subArea_arr);
			    String[] str_area = new String[10];
			    switch(subarea_type) {
			    case 0: str_area = SubArea_decode.CircleOrPoint(subArea_arr);break;
			    case 1: str_area = SubArea_decode.Rectangle(subArea_arr);break;
			    case 2: str_area = SubArea_decode.Sector(subArea_arr);break;
			    case 3: str_area = SubArea_decode.Polyline(subArea_arr);break;
			    case 4: str_area = SubArea_decode.Polygon(subArea_arr);break;
			    case 5: str_area = SubArea_decode.Associated_text(subArea_arr);break;
			    }
			    for(int i=0; i<str_area.length;i++) {
			    	payload_info[13+i] = str_area[i];
			    }
			}
			else if(fid == 25) {
				payload_info[8] = "Description: Dangerous Cargo Indication";
				int amount = ASCtoDec(90,99,payload_arr);
				int unit_int = ASCtoDec(88,89,payload_arr);
				String unit = "";
				switch(unit_int) {
					case 0 : unit = "default";break;
					case 1 : unit = "kg";break;
					case 2 : unit = "tons";break;
					case 3 : unit = "kilotons";break;
					default:
						break;
				}
				payload_info[9] = "Amount Of Cargo: "+amount+unit;
				
				for(int i=0; i<(payload_arr.length-100)/17;i++) {
					int code = ASCtoDec(101+i*17,104+i*17,payload_arr);
					String Cargo_code="";
					switch(code) {
						case 0: Cargo_code="Not available (default)";break;
						case 1: Cargo_code="IMDG Code (in packed form)";break;
						case 2: Cargo_code="IGC code";break;
						case 3: Cargo_code="BC Code (from 1.1.2011 IMSBC)";break;
						case 4: Cargo_code="MARPOL Annex I List of oils (Appendix 1)";break;
						case 5: Cargo_code="MARPOL Annex II IBC Code";break;
						case 6: Cargo_code="Regional use";break;
						case 7: Cargo_code="Reserved for future use";break;
						default: break;
					}
					payload_info[10+2*i] = "Cargo Code: "+Cargo_code;
					int subtype = ASCtoDec(4+i*17,16+i*17,payload_arr);
					String type="";
					if(code == (1 | 2 | 3 | 4 | 5)) {
						switch(subtype) {
							case 0:  type = "N/A (default)";break;
							case 1:  type = "asphalt solutions";break;
							case 2:  type = "oils";break;
							case 3:  type = "distillates";break;
							case 4:  type = "gas oil";break;
							case 5:  type = "gasoline blending stocks";break;
							case 6:  type = "gasoline";break;
							case 7:  type = "jet fuels";break;
							case 8:  type =  "naphtha";break;
							default: type = "reserved for future use";break;
						}
					}
					payload_info[11+2*i] = "Cargo Subtype: "+type;
				}
			}
			else if(fid == 28) {
				payload_info[8] = "Description: Route Information (addressed)";
				int linkage = ASCtoDec(88,97,payload_arr);
				payload_info[9] = "Message Linkage ID: "+linkage;
				//Sender Class
				int sender = ASCtoDec(98,100,payload_arr);
				String SenderClass="Sender Class: ";
				switch(sender) {
				case 0: SenderClass += "ship(default)";break;
				case 1: SenderClass += "authority";break;
				case 27 : SenderClass += "Reserved for future use";break;
				}
				payload_info[10] = SenderClass;
				//Route type
				int rtype = ASCtoDec(101,105,payload_arr); String type = "Route type: ";
				switch(rtype) {
				case 0: type += "Undefined (default)";break;
				case 1: type += "Mandatory";break;
				case 2: type += "Recommended";break;
				case 3: type += "Alternative";break;
				case 4: type += "Recommended route through ice";break;
				case 5: type += "Ship route plan";break;
				case 31: type += "Cancel route identified by message linkage";break;
				}
				if(rtype>=6 & rtype<= 30) {
					type += "Reserved for future usage";
				}
				payload_info[11] = type;
				//UTC
				String month = toMonth(ASCtoDec(106,109,payload_arr));
				int day = ASCtoDec(110,114,payload_arr);
				int hour = ASCtoDec(115,119,payload_arr);
				int minute = ASCtoDec(120,125,payload_arr);
				payload_info[12] = "UTC: "+month+" "+day+", "+hour+" : "+minute;
				//Duration
				int duration = ASCtoDec(126,143,payload_arr);
				if(duration==0) {
					payload_info[13] = "Duration: Canceled route";
				}
				else {
					payload_info[14] = "Duration: "+duration+" minutes";
				}
				payload_info[15] = "WayPoints :";
				for(int i=0; i<(payload_arr.length-150)/55; i++) {
					payload_info[16+i] = toPosition(150+i*55, 177+i*55, 178+i*55, 204+i*55,payload_arr); 
				}
			}
			else if(fid == 30) {
				payload_info[8] = "Description: Text description (addressed)";
				int linkage = ASCtoDec(88,97,payload_arr);
				payload_info[9] = "Message Linkage ID: "+linkage;
				
				payload_info[10] = "description: "+Bin2String(98, payload_arr.length, payload_arr);
			}
			else if(fid == 32) {
				payload_info[8] = "Description: Tidal Window";
				
				String month = toMonth(ASCtoDec(88,91,payload_arr));
				int day = ASCtoDec(92,96,payload_arr);
				payload_info[9] = "Date: "+month+" "+day;
				
				//tidal info Array
				int[] tidal_info = new int[88];
				for(int i=0; i<88; i++) {
					tidal_info[i] = payload_arr[98+i];
				}
				int lat = ASCtoDec(25,48,tidal_info);
				double latit = lat/600000.0;
				if(lat != 91000) {
					if(latit>=0) {
						payload_info[10] = "N "+latit;
					}
					else {
						payload_info[10] = "S "+latit;
					}
				}
				
				int lon = ASCtoDec(0,24,tidal_info);
				double longi = lon/600000.0;
				if(lon != 181000) {
					if(lon>=0) {
						payload_info[11] = "E "+longi;
					}
					else {
						payload_info[11] = "W "+longi;
					}
				}
				
				int from_hour = ASCtoDec(49,53,tidal_info);
				int from_min = ASCtoDec(54,59,tidal_info);
				int to_hour = ASCtoDec(60,64,tidal_info);
				int to_min = ASCtoDec(65,70,tidal_info);
				payload_info[12] = "From "+from_hour+" : "+from_min+", to "+to_hour+" : "+to_min;
				
				//current info
				int cdir = ASCtoDec(71,79,tidal_info);
				int cspeed = ASCtoDec(80,87,tidal_info);
				payload_info[13] = "Current Direction(predict): "+cdir+"Degree,  "+"Current Speed(predict): "+cspeed*0.1+"knots";
			}
			
		}
		else if (dac == 200) {
			if(fid == 21) {
				payload_info[8] = "Description: ETA at lock/bridge/terminal";
				String Country_code = "UN Country Code: "+Bin2String(88,99,payload_arr);
				payload_info[9] = Country_code;
				
				String locode = "Local Code: "+Bin2String(100,117,payload_arr);
				payload_info[10] = locode;
				
				String section = "Fairway Section: "+Bin2String(118,147,payload_arr);
				payload_info[11] = section;
				
				String terminal = "Terminal Code: "+ Bin2String(148,177,payload_arr);
				payload_info[12] = terminal;
				
				String hectometre = "Fairway hectometre: "+Bin2String(178,207,payload_arr);
				payload_info[13] = hectometre;
				//UTC
				String month = toMonth(ASCtoDec(208,211,payload_arr));
				int day = ASCtoDec(212,216,payload_arr);
				int hour = ASCtoDec(217,221,payload_arr);
				int minute = ASCtoDec(222,227,payload_arr);
				payload_info[14] = "ETA: "+month+" "+day+", "+hour+" : "+minute;
				
				int tugs = ASCtoDec(228,230,payload_arr);
				if(tugs == 7) {
					payload_info[15] = "Assisting Tugs: Unknown";
				}
				else {
					payload_info[15] = "Assisting Tugs: "+tugs;
				}
				
				float airdraught = ASCtoDec(231,242,payload_arr)/100;
				payload_info[16] = "Air Draught: "+airdraught+"m";
			}
			else if (fid == 22) {
				payload_info[8] = "Description: RTA at lock/bridge/terminal (Inland AIS)";
				String Country_code = "UN Country Code: "+Bin2String(88,99,payload_arr);
				payload_info[9] = Country_code;
				
				String locode = "Local Code: "+Bin2String(100,117,payload_arr);
				payload_info[10] = locode;
				
				String section = "Fairway Section: "+Bin2String(118,147,payload_arr);
				payload_info[11] = section;
				
				String terminal = "Terminal Code: "+ Bin2String(148,177,payload_arr);
				payload_info[12] = terminal;
				
				String hectometre = "Fairway hectometre: "+Bin2String(178,207,payload_arr);
				payload_info[13] = hectometre;
				
				//UTC
				String month = toMonth(ASCtoDec(208,211,payload_arr));
				int day = ASCtoDec(212,216,payload_arr);
				int hour = ASCtoDec(217,221,payload_arr);
				int minute = ASCtoDec(222,227,payload_arr);
				payload_info[14] = "RTA: "+month+" "+day+", "+hour+" : "+minute;
				
				int status_int = ASCtoDec(228,229,payload_arr);
				String status = "Lock/Bridge/Terminal status: ";
				switch(status_int) {
				case 0: status+="Operational"; break;
				case 1: status+="Limited operation"; break;
				case 2: status+="Out of order"; break;
				case 3: status+="N/A"; break;
				}
				payload_info[15] = status;
			}
			else if(fid == 55) {
				payload_info[8] = "Description: Number of persons on board (Inland AIS)";
				//crew on board
				int crew = ASCtoDec(88,95,payload_arr);
				payload_info[9] = "#Crew on Board: "+crew;
				//Passenger on board
				int passangers = ASCtoDec(96,108,payload_arr);
				payload_info[10] = "#Passangers on Board: "+passangers;
				//Personnel on board
				int personnel = ASCtoDec(109,116,payload_arr);
				payload_info[11] = "Personnel on Board: "+personnel;
			}
		}
		else if((dac == 235 | dac ==250)& fid == 10) {
			//mainly used in UK and Ireland
			payload_info[8] = "Description: AtoN monitoring data (GLA)";
			float ana_int = ASCtoDec(88,97,payload_arr)/100;
			float ana_ext1 = ASCtoDec(98,107,payload_arr)/100;
			float ana_ext2 = ASCtoDec(108,117,payload_arr)/100;
			payload_info[9] = "Analogue: "+ana_int+"V, "+"Analogue (ext. #1): "+ana_ext1+"V, "+"Analogue (ext. #2): "+ana_ext2+"V";
			
			int racon = ASCtoDec(118,119,payload_arr);
			String Racon_status = "RACON status: ";
			switch(racon) {
			case 0: Racon_status += "No RACON installed.";break;
			case 1: Racon_status += "RACON not monitored.";break;
			case 2: Racon_status += "RACON operational.";break;
			case 3: Racon_status += "RACON ERROR.";break;
			default: break;
			}
			payload_info[10] = Racon_status;
			
			int light = ASCtoDec(120,121,payload_arr);
			String light_status = "Light status: ";
			switch(light) {
			case 0: light_status += "no light or no monitoring ";break;
			case 1: light_status += "Light ON";break;
			case 2: light_status += "Light OFF";break;
			case 3: light_status += "Light ERROR";break;
			default: break;
			}
			payload_info[11] = light_status;
			
			String health = "Health: ";
			if(payload_arr[122] == 1) {
				payload_info[12] = health+"Alarm";
			}
			else if(payload_arr[122] == 0) {
				payload_info[12] = health+"Good Health";
			}
			String stat_ext = "Digital Input: ";
			String stat = "";
			for (int i = 0; i<8; i++) {
				if(payload_arr[123+i] == 1) {
					stat = "On";
				}
				else if (payload_arr[123+i] == 0){
					stat = "Off";
				}
				if (i!=7) {
					stat_ext = i+stat_ext+stat+", ";
				}
				else {
					stat_ext = i+stat_ext+stat;
				}
			}
			payload_info[13] = stat_ext;
			
			String off_pos = "Position status: ";
			if(payload_arr[131] == 1) {
				payload_info[14] = off_pos + "Off position"; 
			}
			else if (payload_arr[132] == 0) {
				payload_info[14] = off_pos + "On position";
			}
		}
		return payload_info;
	}
	
	public static String AreaNoticeDescription(int n) {
		String des="";
		switch(n) {
		case 0: des = "Caution Area: Marine mammals habitat";break;
		case 1: des = "Caution Area: Marine mammals in area - reduce speed";break;
		case 2: des = "Caution Area: Marine mammals in area - stay clear";break;
		case 3: des = "Caution Area: Marine mammals in area - report sightings";break;
		case 4: des = "Caution Area: Protected habitat - reduce speed";break;
		case 5: des = "Caution Area: Protected habitat - stay clear";break;
		case 6: des = "Caution Area: Protected habitat - no fishing or anchoring";break;
		case 7: des = "Caution Area: Derelicts (drifting objects)";break;
		case 8 :des = "Caution Area: Traffic congestion";break;
		case 9 :des = "Caution Area: Marine event";break;
		case 10: des = "Caution Area: Divers down";break;
		case 11: des = "Caution Area: Swim area";break;
		case 12: des = "Caution Area: Dredge operations";break;
		case 13: des = "Caution Area: Survey operations";break;
		case 14: des = "Caution Area: Underwater operation";break;
		case 15: des = "Caution Area: Seaplane operations";break;
		case 16: des = "Caution Area: Fishery – nets in water";break;
		case 17: des = "Caution Area: Cluster of fishing vessels";break;
		case 18: des = "Caution Area: Fairway closed";break;
		case 19: des = "Caution Area: Harbor closed";break;
		case 20: des = "Caution Area: Risk (define in associated text field)";break;
		case 21: des = "Caution Area: Underwater vehicle operation";break;
		case 22:
		case 31: case 86: case 87: case 109: case 110: case 111: case 113: case 115: case 116: case 117: case 118: case 119: case 123: case 124:
		case 39: case 46: case 47: case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 59: case 60: case 61: case 62: case 63: case 77: case 78: case 79:
			des = "(reserved for future use)";break;
		case 23: des = "Environmental Caution Area: Storm front (line squall)";break;
		case 24: des = "Environmental Caution Area: Hazardous sea ice";break;
		case 25: des = "Environmental Caution Area: Storm warning (storm cell or line of storms)";break;
		case 26: des = "Environmental Caution Area: High wind";break;
		case 27: des = "Environmental Caution Area: High waves";break;
		case 28: des = "Environmental Caution Area: Restricted visibility (fog, rain, etc.)";break;
		case 29: des = "Environmental Caution Area: Strong currents";break;
		case 30: des = "Environmental Caution Area: Heavy icing";break;
		case 32: des = "Restricted Area: Fishing prohibited";break;
		case 33: des = "Restricted Area: No anchoring";break;
		case 34: des = "Restricted Area: Entry approval required prior to transit";break;
		case 35: des = "Restricted Area: Entry prohibited";break;
		case 36: des = "Restricted Area: Active military OPAREA";break;
		case 37: des = "Restricted Area: Firing – danger area";break;
		case 38: des = "Restricted Area: Drifting Mines";break;
		case 40: des = "Anchorage Area: Anchorage open";break;
		case 41: des = "Anchorage Area: Anchorage closed";break;
		case 42: des = "Anchorage Area: Anchorage prohibited";break;
		case 43: des = "Anchorage Area: Deep draft anchorage";break;
		case 44: des = "Anchorage Area: Shallow draft anchorage";break;
		case 45: des = "Anchorage Area: Vessel transfer operations";break;
		case 56: des = "Security Alert - Level 1";break;
		case 57: des = "Security Alert - Level 2";break;
		case 58: des = "Security Alert - Level 3";break;
		case 64: des = "Distress Area: Vessel disabled and adrift";break;
		case 65: des = "Distress Area: Vessel sinking";break;
		case 66: des = "Distress Area: Vessel abandoning ship";break;
		case 67: des = "Distress Area: Vessel requests medical assistance";break;
		case 68: des = "Distress Area: Vessel flooding";break;
		case 69: des = "Distress Area: Vessel fire/explosion";break;
		case 70: des = "Distress Area: Vessel grounding";break;
		case 71: des = "Distress Area: Vessel collision";break;
		case 72: des = "Distress Area: Vessel listing/capsizing";break;
		case 73: des = "Distress Area: Vessel under assault"; break;
		case 74: des = "Distress Area: Person overboard";break;
		case 75: des = "Distress Area: SAR area";break;
		case 76: des = "Distress Area: Pollution response area";break;
		case 80: des = "Instruction: Contact VTS at this point/juncture";break;
		case 81: des = "Instruction: Contact Port Administration at this point/juncture";break;
		case 82: des = "Instruction: Do not proceed beyond this point/juncture";break;
		case 83: des = "Instruction: Await instructions prior to proceeding beyond this point/juncture";break;
		case 84: des = "Proceed to this location – await instructions";break;
		case 85: des = "Clearance granted – proceed to berth";break;
		case 88: des = "Information: Pilot boarding position";break;
		case 89: des = "Information: Icebreaker waiting area";break;
		case 90: des = "Information: Places of refuge";break;
		case 91: des = "Information: Position of icebreakers";break;
		case 92: des = "Information: Location of response units";break;
		case 93: des = "VTS active target";break;
		case 94: des = "Rogue or suspicious vessel";break;
		case 95: des = "Vessel requesting non-distress assistance";break;
		case 96: des = "Chart Feature: Sunken vessel";break;
		case 97: des = "Chart Feature: Submerged object";break;
		case 98: des = "Chart Feature: Semi-submerged object";break;
		case 99: des = "Chart Feature: Shoal area";break;
		case 100: des = "Chart Feature: Shoal area due north";break;
		case 101: des = "Chart Feature: Shoal area due east";break;
		case 102: des = "Chart Feature: Shoal area due south";break;
		case 103: des = "Chart Feature: Shoal area due west";break;
		case 104: des = "Chart Feature: Channel obstruction";break;
		case 105: des = "Chart Feature: Reduced vertical clearance";break;
		case 106: des = "Chart Feature: Bridge closed";break;
		case 107: des = "Chart Feature: Bridge partially open";break;
		case 108: des = "Chart Feature: Bridge fully open";break;
		case 112: des = "Report from ship: Icing info";break;
		case 114: des = "Report from ship: Miscellaneous information – define in associated text field";break;
		case 120: des = "Route: Recommended route";break;
		case 121: des = "Route: Alternative route";break;
		case 122: des = "Route: Recommended route through ice";break;
		case 125: des = "Other – Define in associated text field";break;
		case 126: des = "Cancellation – cancel area as identified by Message Linkage ID";break;
		case 127: des = "Undefined (default)";
		default:
			break;
		}
		return des;
	}
	public static String Service_status(int flag) {
		String Service_status="";
		switch(flag) {
		case 0 : Service_status = "No available or requested(default)";break;
		case 1 : Service_status = "Service available";break;
		case 2 : Service_status = "No data or unknown";break;
		case 3 : Service_status = "No to be used";break;
		default:
			break;
		}
		return Service_status;
	}
	
	public static int ASCtoDec(int begin, int end, int[] payload_arr) {
		int sum=0, bin=1;
		for(int i=end; i>begin; i--) {
			sum += bin*payload_arr[i];
			bin *= 2;
		}
		return sum;
	}
	
	public static String Bin2String(int begin, int end, int[]payload_arr) {
		String bit_string="";
		String str_message="";
		for(int i=begin; i<end-6; i=i+6) {
			bit_string = "";
			for(int j=0; j<6; j++) {
				bit_string += String.valueOf(payload_arr[i+j]);
			}
			str_message += Payload.Bin_decode(bit_string);
		}
		return str_message;
	}
	
	public static String toMonth(int num) {
		String month="";
		switch(num) {
		case 0: month = "default";break;
		case 1: month = "Jan";break;
		case 2: month = "Feb";break;
		case 3: month = "Mar";break;
		case 4: month = "Apr";break;
		case 5: month = "May";break;
		case 6: month = "Jun"; break;
		case 7: month = "Jul";break;
		case 8: month = "Sep";break;
		case 9: month = "Oct";break;
		case 10: month = "Nov";break;
		case 11: month = "Dec"; break;
		}
		return month;
	}
	
	public static String toPosition(int begin1, int end1, int begin2, int end2, int[] payload_arr) {
		String position = "";
		int lat = ASCtoDec(begin1, end1,payload_arr);
		double latit = lat/600000.0;
		if(lat != 91000) {
			if(latit>=0) {
				position += "N "+latit;
			}
			else {
				position += "S "+latit;
			}
		}
		
		int lon = ASCtoDec(begin2,end2,payload_arr);
		double longi = lon/600000.0;
		if(lon != 181000) {
			if(lon>=0) {
				position += ", E "+longi;
			}
			else {
				position += ", W "+longi;
			}
		}
		return position;
	}
	public static String getShiptype(int Ship_Type) {
		String stype = "";
		switch(Ship_Type) {
		case 0 : stype = "Not available(default)"; break;
		case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19:
			stype = "Reserved for future use";break;
		case 20: stype = "Wing in ground(WIG), all ship of this type";break;
		case 21: stype = "Wing in ground(WIG), Hazardous Category A";break;
		case 22: stype = "Wing in ground(WIG), Hazardous Category B";break;
		case 23: stype = "Wing in ground(WIG), Hazardous Category C";break;
		case 24: stype = "Wing in ground(WIG), Hazardous Category D";break;
		case 25: case 26: case 27: case 28: 
		case 29: stype = "Wing in ground(WIG)";break;
		case 30: stype = "Fishing";break;
		case 31: stype = "Towing";break;
		case 32: stype = "Towing: length exceeds 200m or breadth exceeds 25m";break;
		case 33: stype = "Dredging or underwater ops";break;
		case 34: stype = "Diving ops";break;
		case 35: stype = "Military ops";break;
		case 36: stype = "Sailing";break;
		case 37: stype = "Pleasure Craft";break;
		case 40: stype = "High Speed Craft(HSC), all ship of this type";break;
		case 41: stype = "High Speed Craft(HSC), Hazardous Category A";break;
		case 42: stype = "High Speed Craft(HSC), Hazardous Category B";break;
		case 43: stype = "High Speed Craft(HSC), Hazardous Category C";break;
		case 44: stype = "High Speed Craft(HSC), Hazardous Category D";break;
		case 45: case 46: case 47: case 48: stype = "High Speed Craft(HSC)";break;
		case 49: stype = "High Speed Craft(HSC), No additional information";break;
		case 50: stype = "Pilot Vessel";break;
		case 51: stype = "Search for Rescue Vessel";break;
		case 52: stype = "Tug";break;
		case 53: stype = "Port Tender";break;
		case 54: stype = "Anti-Pollution equipment";break;
		case 55: stype = "Law Enforcement";break;
		case 56: case 57: stype = "Spare-local Vessel";break;
		case 58: stype = "Medical Transport";break;
		case 59: stype = "Noncombatant ship according to RR Resolution No. 18";break;
		case 60: stype = "Passenger, all ships of this type";break;
		case 61: stype = "Passenger, Hazardous category A";break;
		case 62: stype = "Passenger, Hazardous category B";break;
		case 63: stype = "Passenger, Hazardous category C";break;
		case 64: stype = "Passenger, Hazardous category D";break;
		case 65: case 66: case 67: case 68: stype = "Passenger"; break;
		case 69: stype = "Passenger, No additional information";break;
		case 70: stype = "Cargo, all ships of this type";break;
		case 71: stype = "Cargo, Hazardous category A";break;
		case 72: stype = "Cargo, Hazardous category B";break;
		case 73: stype = "Cargo, Hazardous category C";break;
		case 74: stype = "Cargo, Hazardous category D";break;
		case 75: case 76: case 77: case 78: stype = "Cargo";break;
		case 79: stype = "Tanker, No addditional information";break;
		case 80: stype = "Tanker, all ships of this type";break;
		case 81: stype = "Tanker, Hazardous category A";break;
		case 82: stype = "Tanker, Hazardous category B";break;
		case 83: stype = "Tanker, Hazardous category C";break;
		case 84: stype = "Tanker, Hazardous category D";break;
		case 85: case 86: case 87: case 88: stype = "Tanker";break;
		case 89: stype = "Tanker, No addditional information";break;
		case 90: stype = "Other Type, all ships of this type";break;
		case 91: stype = "Other Type, Hazardous category A";break;
		case 92: stype = "Other Type, Hazardous category B";break;
		case 93: stype = "Other Type, Hazardous category C";break;
		case 94: stype = "Other Type, Hazardous category D";break;
		case 95: case 96: case 97: case 98: stype = "Other Type";break;
		case 99: stype = "Other Type, No addditional information";break;
		default:
			break;
		}
		return stype;
	}
}

