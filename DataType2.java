package decode;

public class DataType2 {
	
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
	 * arr: 与payload_arr相同
	 * info: 与payload_info相同
	 * 
	 * 有分句的情况或者可变长度的情况会报错， 目前解决办法加if判断超出长度字段
	 * ***************************************************************************************/
	
	public static String[] type7(int[] payload_arr) {
		int len = payload_arr.length;
		String[] payload_info = new String[10];
		payload_info[0] = "Type 7: Binary Acknowledge";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "Source MMSI: " + mmsi;
		
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
		
		//mmsi1
		int mmsi1 = ASCtoDec(40,69,payload_arr);
		payload_info[1] = "MMSI #1: " + mmsi1;
		//sequence
		int mmsi1_seq = ASCtoDec(70,71,payload_arr);
		payload_info[2] = "Sequence: "+mmsi1_seq;
		
		if(payload_arr.length >= 103) {
			//mmsi2
			int mmsi2 = ASCtoDec(72,101,payload_arr);
			payload_info[3] = "MMSI #2: " + mmsi2;
			//sequence
			int mmsi2_seq = ASCtoDec(102,103,payload_arr);
			payload_info[4] = "Sequence: "+mmsi2_seq;		
			
			if(len >= 135) {
				//mmsi3
				int mmsi3 = ASCtoDec(104,133,payload_arr);
				payload_info[5] = "MMSI #3: " + mmsi3;
				//sequence
				int mmsi3_seq = ASCtoDec(134,135,payload_arr);
				payload_info[6] = "Sequence: "+mmsi3_seq;	
				
				if(len >= 167) {
					//mmsi4
					int mmsi4 = ASCtoDec(136,165,payload_arr);
					payload_info[7] = "MMSI #4: " + mmsi4;
					//sequence
					int mmsi4_seq = ASCtoDec(166,167,payload_arr);
					payload_info[8] = "Sequence: "+mmsi4_seq;
				}
			}
		}
		return payload_info;
	}
	
	public static String[] type8(int[] payload_arr) {
		String[] payload_info = new String[40];
		payload_info[0] = "Type 8: Binary Broadcast Message";
		//MMSI
		int mmsi = ASCtoDec(8,37,payload_arr);
		payload_info[1] = "Source MMSI: " + mmsi;
		
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
		
		//dac
		int dac = ASCtoDec(40,49,payload_arr);
		int fid = ASCtoDec(50,55,payload_arr);
		payload_info[3] = "Designated Area Code(DAC) = "+dac+", "+"Functional ID = "+fid;
		if (dac == 1) {
			if(fid == 11) {
				payload_info[4] = "Description: Meteorological and Hydrological Data (IMO236)";
				String pos = toPosition(56,79,80,104,payload_arr);
				payload_info[5] = pos;
				
				//UTC
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
				payload_info[6] = "UTC: "+Day+", "+Hour+":"+Minute;
				
				//Wind Speed
				int wspeed = ASCtoDec(121,127,payload_arr);
				if(wspeed == 127) {
					payload_info[7]= "Wind Speed: N/A";
				}
				else {
					payload_info[7]= "Average Wind Speed: "+wspeed+"knots";
				}
				
				//Wind Gust
				int wgust = ASCtoDec(128,134,payload_arr);
				if( wgust == 127) {
					payload_info[8]= "Gust Speed: N/A";
				}
				else {
					payload_info[8]= "Gust Speed: "+wgust+"knots";
				}
				
				//wind direction
				int wdir = ASCtoDec(135,143,payload_arr);
				if(wdir<=359 & wdir>=0) {
					payload_info[9] = "Wind Direction: "+wdir+"degrees fom true north";
				}
				else if (wdir == 511) {
					payload_info[9] = "Wind Direction: N/A";
				}
				
				//wind Gust Direction
				int wgustdir = ASCtoDec(144,152,payload_arr);
				if(wgustdir <=359 & wgustdir >= 0) {
					payload_info[10] = "Wind Gust Direction: "+wgustdir+"degrees fom true north";
				}
				else if (wgustdir == 511) {
					payload_info[11] = "Wind Gust Direction: N/A";
				}
				
				//temperature
				float temp = ASCtoDec(154,163,payload_arr)/10;
				if (payload_arr[153] == 0 & temp <= 60) {
					if (temp == 2047) {
						payload_info[12] = "Temperature: N/A";
					}
					else {
						payload_info[12] = "Temperature: "+temp+"deg C";
					}
				}
				else if (payload_arr[153] == 1 & temp <= 60) {
					payload_info[12] = "Temperature: -"+temp+"deg C";
				}
				
				//humidity
				int humid = ASCtoDec(164,170,payload_arr);
				String Humid = "Relative Humidity: ";
				if(humid == 127) {
					Humid+="N/A";
				}
				else {
					Humid+=humid+"%";
				}
				payload_info[13] = Humid;
				
				//Dew point
				float dewpoint = ASCtoDec(170,180,payload_arr)/10;
				String Dew = "Dew Point: ";
				if(payload_arr[169] == 0) {
					if(dewpoint == 1023) {
						Dew += "N/A";
					}
					else if (dewpoint <= 50){
						Dew += dewpoint+"deg C";
					}
				}
				else if (payload_arr[169] == 1 & dewpoint <= 20) {
					Dew += "-"+dewpoint+"deg C";
				}
				payload_info[14] = Dew;
				
				//pressure
				int pre = ASCtoDec(181,189,payload_arr);
				String pressure = "Air Pressure: ";
				if(pre == 511) {
					pressure += "N/A";
				}
				else if (pre >= 800 & pre <= 1200) {
					pressure += pre+"hPa";
				}
				payload_info[15] = pressure;
				
				//Pressure Tendency
				int pressuretend = ASCtoDec(190,191,payload_arr);
				String pre_tend = "Pressure Tendency: ";
				switch(pressuretend) {
				case 0: pre_tend += "steady";break;
				case 1: pre_tend += "decreasing";break;
				case 2: pre_tend += "increasing";break;
				case 3: pre_tend += "N/A";break;
				default: break;
				}
				payload_info[16] = pre_tend;
				
				//visibility
				float vis = ASCtoDec(192,199,payload_arr)/10;
				String visi = "Horiz.Visibility: ";
				if(vis == 255) {
					visi += "N/A";
				}
				else if (vis <= 25) {
					visi += vis +"nm";
				}
				payload_info[17] = visi;
				
				//waterlevel
				float waterlevel = ASCtoDec(201,208,payload_arr)/10;
				String wl = "Water level: ";
				if(payload_arr[200] == 0) {
					if(waterlevel == 511) {
						wl += "N/A";
					}
					else if (waterlevel <= 30) {
						wl += waterlevel+"m";
					}
				}
				else if(payload_arr[200] == 1) {
					wl += "-" + waterlevel+ "m";
				}
				payload_info[18] = wl;
				
				//water level trend
				int waterleveltrend = ASCtoDec(209,210,payload_arr);
				String wlt = "Water Level Trend: ";
				switch(waterleveltrend) {
				case 0: wlt += "steady";break;
				case 1: wlt += "decreasing";break;
				case 2: wlt += "increasing";break;
				case 3: wlt += "N/A";break;
				default: break;
				}
				payload_info[19] = wlt;
				
				//Surface Current Speed
				float cspeed = ASCtoDec(211,218,payload_arr)/10;
				String scs = "Surface Current Speed: ";
				payload_info[20] = scs+cspeed+"knots";
				
				//Surface Current Direction
				int cdir = ASCtoDec(219,227,payload_arr);
				String scd = "Surface Current Direction: ";
				if(cdir == 511) {
					scd += "N/A";
				}
				else if (cdir >= 0 & cdir <= 359) {
					scd += cdir + "deg from true north";
				}
				payload_info[21] = scd;
				
				//Current Speed #2
				float cspeed2 = ASCtoDec(228,235,payload_arr)/10;
				String scs2 = "Current Speed #2: ";
				payload_info[22] = scs2+cspeed2+"knots";
				
				//Current Direction #2
				int cdir2 = ASCtoDec(236,244,payload_arr);
				String scd2 = "Current Direction #2: ";
				if(cdir2 == 511) {
					scd2 += "N/A";
				}
				else if (cdir2 >= 0 & cdir2 <= 359) {
					scd2 += cdir2 + "deg from true north";
				}
				payload_info[23] = scd2;
				
				//Measurement Depth #2
				float cdepth2 =  ASCtoDec(245,249,payload_arr)/10;
				String md2 = "Measurement Depth #2: ";
				if (cdepth2 == 31) {
					md2 += "N/A";
				}
				else if(cdepth2 >= 0 & cdepth2 <= 30) {
					md2 += "m down";
				}
				payload_info[24] = md2;
				
				//Current Speed #3
				float cspeed3 = ASCtoDec(250,257,payload_arr)/10;
				String scs3 = "Current Speed #3: ";
				payload_info[25] = scs3+cspeed3+"knots";
				
				//Current Direction #3
				int cdir3 = ASCtoDec(258,266,payload_arr);
				String scd3 = "Current Direction #3: ";
				if(cdir3 == 511) {
					scd3 += "N/A";
				}
				else if (cdir3 >= 0 & cdir3 <= 359) {
					scd3 += cdir3 + "deg from true north";
				}
				payload_info[26] = scd3;
				
				//Measurement Depth #3
				int cdepth3_int = ASCtoDec(267,271,payload_arr);
				float cdepth3 = cdepth3_int/10;
				String md3 = "Measurement Depth #3: ";
				if (cdepth3 == 310) {
					md3 += "N/A";
				}
				else if(cdepth3 >= 0 & cdepth3 <= 30) {
					md3 += "m down";
				}
				payload_info[27] = md3;
				
				//wave height 
				int wh_int = ASCtoDec(272,279,payload_arr);
				float wheight = wh_int/10;
				String wh = "Wave Height: ";
				if(wh_int == 255) {
					wh += "N/A";
				}
				else if(wheight >= 0 & wheight <= 25) {
					wh += wheight +"m";
				}
				payload_info[28] = wh;
				
				//wave period
				int wperiod = ASCtoDec(280,285,payload_arr);
				String wp = "Wave Period: ";
				if(wperiod == 63) {
					wp += "N/A";
				}
				else if(wperiod >= 0 & wperiod <= 60) {
					wp += "Seconds";
				}
				payload_info[29] = wp;
				
				//wave direction
				int wavedir = ASCtoDec(286,294,payload_arr);
				String wd = "Wave Direction: ";
				if (wavedir == 511) {
					wd += "N/A";
				}
				else if (wavedir >= 0 & wavedir <= 359) {
					wd += wavedir +"deg. fom true north";
				}
				payload_info[30] = wd;
				
				//swell height 
				int sheight_int = ASCtoDec(295,302,payload_arr);
				float sheight = sheight_int/10;
				String sh = "Swell Height: ";
				if(sheight_int == 255) {
					sh += "N/A";
				}
				else if(sheight_int >= 0 & sheight_int <= 250) {
					sh += sheight +"m";
				}
				payload_info[31] = sh;
				
				//swell period
				int speriod = ASCtoDec(303,308,payload_arr);
				String sp = "Swell Period: ";
				if(speriod == 63) {
					sp += "N/A";
				}
				else if(speriod >= 0 & speriod <= 60) {
					sp += "Seconds";
				}
				payload_info[32] = sp;
				
				//Swell direction
				int swelldir = ASCtoDec(309,317,payload_arr);
				String sd = "Swell Direction: ";
				if (swelldir == 511) {
					sd += "N/A";
				}
				else if (swelldir >= 0 & swelldir <= 359) {
					sd += swelldir +"deg. fom true north";
				}
				payload_info[33] = sd;
				
				//sea state
				int seastate = ASCtoDec(318,321,payload_arr);
				String ss = "Sea state: ";
				switch(seastate) {
				case 0: ss += "Calm, Flat.";break;
				case 1: ss += "Light air, Ripples without crests.";break;
				case 2: ss += "Light breeze, Small wavelets.";break;
				case 3: ss += "Crests of glassy appearance, not breaking. Gentle breeze.";
				case 4: ss += "Large wavelets. Crests begin to break; scattered whitecaps.";break;
				case 5: ss += "Moderate breeze, Small waves.";break;
				case 6: ss += "Fresh breeze, Moderate (1.2 m) longer waves. Some foam and spray.";break;
				case 7: ss += "Strong breeze, Large waves with foam crests and some spray.";break;
				case 8: ss += "High wind, Sea heaps up and foam begins to streak.";break;
				case 9: ss += "Strong gale, High waves (6-7 m) with dense foam.";break;
				case 10: ss += "Wave crests start to roll over. Considerable spray. Storm";break;
				case 11: ss += "Very high waves. The sea surface is white and there, is considerable tumbling. Visibility is reduced.";break;
				case 12: ss += "Violent storm, Exceptionally high waves.";break;
				case 13: ss += "greatly reduced."; break;
				case 14: case 15: ss += "N/A (default)";break;
				default: break;
				}
				payload_info[34] = ss;
				
				//water temperature
				int wt_int = ASCtoDec(323,331,payload_arr);
				float watertemp = wt_int/10;
				String st = "Water Temperature: ";
				if(payload_arr[322] == 0) {
					if(wt_int == 1023) {
						st += "N/A";
					}
					else if (wt_int <= 500) {
						st += watertemp +"degree C";
					}
				}
				else if (payload_arr[322] == 1) {
					st += "-"+watertemp + "degree C";
				}
				payload_info[35] = st;
				
				//Precipitation
				int preciptype =ASCtoDec(332,334,payload_arr);
				String pt = "Precipitation Tyoe: ";
				switch(preciptype) {
				case 0: pt += "Reserved";break;
				case 1: pt += "Rain"; break;
				case 2: pt += "Thunderstorm";break;
				case 3: pt += "Freezing rain";break;
				case 4: pt += "Mixed/ice"; break;
				case 5: pt += "Snow";break;
				case 6: pt += "Reserved";break;
				case 7: pt += "N/A (default)";break;
				default: break;
				}
				payload_info[36] = pt;
				
				//Salinity
				int sal_int = ASCtoDec(335,343,payload_arr);
				float salinity = sal_int/10;
				String sal = "Salinity: ";
				if (sal_int == 511) {
					sal += "N/A";
				}
				else if(sal_int >= 0 & sal_int <= 500) {
					sal += salinity+"%";
				}
				payload_info[37] = sal;
				
				//Ice
				int ice_int = ASCtoDec(344,345,payload_arr);
				String Ice = "Ice: ";
				switch(ice_int) {
				case 0: Ice += "No";break;
				case 1: Ice += "Yes";break;
				case 2: Ice += "(reserved for future use) ";break;
				case 3: Ice += "Not available = default";break;
				default: break;
				}
				payload_info[38] = Ice;
			}
			else if (fid == 13) {
				payload_info[4] = "Description: Fairway Closed";
				
				//close reason
				String reason = Bin2String(56,175,payload_arr);
				payload_info[5] = "Reason For Closing: "+reason;
				
				//close location
				String closefrom = Bin2String(176,295,payload_arr);
				String closeto = Bin2String(296,415,payload_arr);
				payload_info[6] = "Location Of Closing: "+"From "+closefrom+" to "+closeto;
				
				//radius
				int radius = ASCtoDec(416,425,payload_arr);
				String Ex_radius = "Radius Extension: ";
				String exunit = "";
				int exunit_int = ASCtoDec(426,427,payload_arr);
				switch(exunit_int) {
				case 0: exunit = "m";break;
				case 1: exunit = "km";break;
				case 2: exunit = "nm";break;
				case 3: exunit = "cables";break;
				default: break;
				}
				if(radius == 10001) {
					Ex_radius += "N/A";
				}
				else if(radius >= 0 & radius <=1000) {
					Ex_radius += radius + exunit;
				}
				payload_info[7] = Ex_radius; 
				
				//UTC
				String fmonth = toMonth(ASCtoDec(433,436,payload_arr));
				int fday = ASCtoDec(428,432,payload_arr);
				int fhour = ASCtoDec(437,441,payload_arr);
				int fminute = ASCtoDec(442,447,payload_arr);
				String tmonth = toMonth(ASCtoDec(453,456,payload_arr));
				int tday = ASCtoDec(448,452,payload_arr);
				int thour = ASCtoDec(457,461,payload_arr);
				int tminute = ASCtoDec(462,467,payload_arr);
				payload_info[8] = "From "+fmonth+" "+fday+", "+fhour+":"+fminute+" to "+tmonth+" "+tday+", "+thour+":"+tminute;
			}
			else if (fid == 15) {
				payload_info[4] = "Description: Extended Ship Static and Voyage Related Data";
				
				//air draught
				int airdraught = ASCtoDec(55,56,payload_arr);
				String ad = "Air Draught: ";
				if(airdraught == 0) {
					ad += "N/A";
				}
				else {
					ad += airdraught+"m";
				}
				payload_info[5] = ad;
			}
			else if (fid == 17) {
				payload_info[4] = "Description: VTS-Generated/Synthetic targets";
				
				int[] target = new int[122];
				for(int i=0; i<122; i++) {
					target[i] = payload_arr[57+i];
				}
				int idtype = ASCtoDec(0,1,target);
				String type = "";
				switch(idtype) {
				case 0: type = "MMSI";break;
				case 1: type = "IMO";break;
				case 2: type = "Call Sign";break;
				case 3: type = "Other"; break;
				default: break;
				}
				int id = ASCtoDec(2,43,target);
				payload_info[5] = type+": "+id;
				
				//position
				String pos = toPosition(48,71,72,96,target);
				payload_info[6] = pos;
				
				//course over ground
				int course = ASCtoDec(97,105,target);
				String cog = "Course Over Ground: ";
				if(course == 360) {
					cog += "N/A";
				}
				else {
					cog += course+"deg from true north";
				}
				payload_info[7] = cog;
				
				//Time stamp
				int second = ASCtoDec(106,111,target);
				payload_info[8] = "Time Stamp: "+second;
				
				//speed over ground
				int speed = ASCtoDec(112,121,target);
				String sog = "Speed Over Ground: ";
				if(speed == 255) {
					sog += "N/A";
				}
				else {
					sog += speed + "knots";
				}
				payload_info[9] = sog;
			}
			else if(fid == 19) {
				payload_info[4] = "Description: Marine Traffic Signal";
				
				//Message Linkage ID
				int mlid = ASCtoDec(56,65,payload_arr);
				payload_info[5] = "Message Linkage ID: "+mlid;
				
				//Name of Signal Station
				String station = Bin2String(66,185,payload_arr);
				payload_info[6] = "Name of Signal Station: "+station;
				
				//position
				String pos = toPosition(211,234,186,210,payload_arr);
				payload_info[7] = pos;
				
				//Status of Signal
				int status_int = ASCtoDec(235,236,payload_arr);
				String status = "Status of Signal: ";
				switch(status_int) {
				case 0: status += "N/A";break;
				case 1: status += "In regular service";break;
				case 2: status += "Irregular service";break;
				case 3: status += "Reserved for future use";break;
				default: break;
				}
				payload_info[8] = status;
				
				//UTC
				int hour = ASCtoDec(242,246,payload_arr);
				int minute = ASCtoDec(247,252,payload_arr);
				payload_info[7] = "UTC: "+hour+":"+minute;
				
				//Expected Next Signal
				int next = ASCtoDec(253,257,payload_arr);
				String ns = "Expected Next Signal: ";
				if (next >= 14 & next <= 31) {
					ns += "which receives the direction from the competent authority.";
				}
				else {
					switch(next) {
					case 0: ns += "N/A (default)";break;
					case 1: ns += "IALA port traffic signal 1: Serious emergency – all vessels to stop";break;
					case 2: ns += "or divert according to instructions.";break;
					case 3: ns += "IALA port traffic signal 2: Vessels shall not proceed.";break;
					case 4: ns += "IALA port traffic signal 3: Vessels may proceed. One way traffic.";break;
					case 5: ns += "IALA port traffic signal 4: Vessels may proceed. Two way traffic.";break;
					case 6: ns += "IALA port traffic signal 2a: Vessels shall not proceed, except that";break;
					case 7: ns += "IALA port traffic signal 5a: A vessel may proceed only when it has";break;
					case 8: ns += "Japan Traffic Signal - I = \"in-bound\" only acceptable.";break;
					case 9: ns += "Japan Traffic Signal - O = \"out-bound\" only acceptable.";break;
					case 10: ns += "Japan Traffic Signal - F = both \"in- and out-bound\" acceptable.";break;
					case 11: ns += "Japan Traffic Signal - XI = Code will shift to \"I\" in due time.";break;
					case 12: ns += "Japan Traffic Signal - XO = Code will shift to \"O\" in due time."; break;
					case 13: ns += "Japan Traffic Signal - X = Vessels shall not proceed, except a vessel";break;
					default:
						break;
					}
				}
				payload_info[8] = ns;
			}
			else if(fid == 21) {
				payload_info[4] = "Description: Weather observation report from ship: Non-WMO variant";
				if (payload_arr[56] == 0) {
					//location
					String location = Bin2String(57,176,payload_arr);
					payload_info[5] = "Location: "+location;
					
					//position
					String pos = toPosition(177,201,202,225,payload_arr);
					payload_info[6] = pos;
					
					//UTC
					int day = ASCtoDec(226,230,payload_arr);
					int hour = ASCtoDec(231,235,payload_arr);
					int minute = ASCtoDec(236,241,payload_arr);
					payload_info[7] = "UTC: "+day+" "+hour+":"+minute;
					
					//weather
					int w_int = ASCtoDec(242,245,payload_arr);
					String pw = "Present weather: ";
					switch(w_int) {
					case 0: pw += "Clear (no clouds at any level)";break;
					case 1: pw += "Cloudy";break;
					case 2: pw += "Rain";break;
					case 3: pw += "Fog";break;
					case 4: pw += "Snow";break;
					case 5: pw += "Typhoon/hurricane";break;
					case 6: pw += "Monsoon";break;
					case 7: pw += "Thunderstorm";break;
					case 8: pw += "N/A (default)";break;
					case 9: case 10: case 11: case 12: case 13: case 14: case 15:
						pw += "Reserved for future use";break;
						default:
							break;
					}
					payload_info[8] = pw;
					
					//visibility
					int vislimit = payload_arr[246];
					int vis = ASCtoDec(247,253,payload_arr);
					float visibility = vis/10;
					String visi = "Horiz. Visibility: ";
					if(vislimit == 1) {
						visi += "the maximum range of the visibility equipment was reached, visibility > 12.6nm";
					}
					else if(vislimit == 0) {
						visi += visibility+"nm";
					}
					payload_info[9] = visi;
					
					//humidity
					int humi = ASCtoDec(254,260,payload_arr);
					String humidity = "Relative Humidity: ";
					if(humi == 127) {
						humidity += "N/A";
					}
					else {
						humidity = humi + "%";
					}
					payload_info[10] = humidity;
					
					//Average Wind Speed
					int wspeed = ASCtoDec(261,267,payload_arr);
					String ws = "Average Wind Speed in 10 min: ";
					if(wspeed == 127) {
						ws += "N/A";
					}
					else {
						ws += wspeed + "knots";
					}
					
					//wind direction
					int wdir = ASCtoDec(268,276,payload_arr);
					String wd = "Wind Direction: ";
					if(wdir == 360) {
						wd += "N/A";
					}
					else {
						wd += wdir+"deg. fom true north";
					}
					payload_info[11] = wd;
					
					//pressure
					int pre = ASCtoDec(277,285,payload_arr);
					String pressure = "Air Pressure: ";
					if(pre == 403 | pre >= 1201) {
						pressure += "N/A";
					}
					else if (pre >= 800 & pre <= 1200) {
						pressure += (pre+400)+"hPa";
					}
					payload_info[12] = pressure;
					
					//Pressure Tendency
					int pressuretend = ASCtoDec(286,289,payload_arr);
					String pre_tend = "Pressure Tendency: ";
					switch(pressuretend) {
					case 0: pre_tend += "steady";break;
					case 1: pre_tend += "decreasing";break;
					case 2: pre_tend += "increasing";break;
					case 3: pre_tend += "N/A";break;
					default: break;
					}
					payload_info[13] = pre_tend;
					
					//Air temperature
					float airtemp = ASCtoDec(291,300,payload_arr)/10;
					if (payload_arr[290] == 0 & airtemp <= 60) {
							payload_info[14] = "Air Temperature: "+airtemp+"deg C";
					}
					else if (payload_arr[290] == 1) {
						if(airtemp <= 60) {
							payload_info[14] = "Air Temperature: -"+airtemp+"deg C";
						}
						else if(airtemp == 1024) {
							payload_info[14] = "Air Temperature: N/A";
						}
					}
					
					//water temperature
					int wt_int = ASCtoDec(302,310,payload_arr);
					float wtemp = wt_int/10;
					String wt = "Water Temperature: ";
					if(wt_int == 601) {
						wt += "N/A";
					}
					else {
						wt += (wtemp-10)+"deg C";
					}
					payload_info[15] = wt;
					
					//wave period
					int wperiod = ASCtoDec(311,316,payload_arr);
					String wp = "Wave Period: ";
					if(wperiod == 63) {
						wp += "N/A";
					}
					else if(wperiod >= 0 & wperiod <= 60) {
						wp += "Seconds";
					}
					payload_info[16] = wp;
					
					//wave height 
					int wh_int = ASCtoDec(317,324,payload_arr);
					float wheight = wh_int/10;
					String wh = "Wave Height: ";
					if(wh_int == 255) {
						wh += "N/A";
					}
					else if(wheight >= 0 & wheight <= 25) {
						wh += wheight +"m";
					}
					payload_info[17] = wh;
					
					//wave direction
					int wavedir = ASCtoDec(325,333,payload_arr);
					String wdi = "Wave Direction: ";
					if (wavedir == 360) {
						wdi += "N/A";
					}
					else if (wavedir >= 0 & wavedir <= 359) {
						wdi += wavedir +"deg. fom true north";
					}
					payload_info[18] = wdi;
					
					//swell height 
					int sheight_int = ASCtoDec(334,341,payload_arr);
					float sheight = sheight_int/10;
					String sh = "Swell Height: ";
					if(sheight_int == 255) {
						sh += "N/A";
					}
					else if(sheight_int >= 0 & sheight_int <= 250) {
						sh += sheight +"m";
					}
					payload_info[19] = sh;
					
					//Swell direction
					int swelldir = ASCtoDec(342,350,payload_arr);
					String sd = "Swell Direction: ";
					if (swelldir == 360) {
						sd += "N/A";
					}
					else if (swelldir >= 0 & swelldir <= 359) {
						sd += swelldir +"deg. fom true north";
					}
					payload_info[20] = sd;
					
					//swell period
					int speriod = ASCtoDec(351,356,payload_arr);
					String sp = "Swell Period: ";
					if(speriod == 63) {
						sp += "N/A";
					}
					else if(speriod >= 0 & speriod <= 60) {
						sp += "Seconds";
					}
					payload_info[21] = sp;
				}
			}
			
		}
		return payload_info;
	}
	
	public static String[] type9(int[] arr) {
		String[] info = new String[13];
		
		info[0] = "Type 9: Standard SAR Aircraft Position Report";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[1] = "MMSI: " + mmsi;
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,6,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No repeat";
		}
		else if(Repeat_Indicator == 0) {
			Repeat_info = "default";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[2] = "Repeate_info: " + Repeat_info;
		
		//Altitude
		int alt = ASCtoDec(38,49,arr);
		String altt = "Altitude: ";
		if(alt == 4095) {
			altt += "No available";
		}
		else if(alt == 4094) {
			altt += "4094m or higher";
		}
		else {
			altt += alt+"m";
		}
		info[3] = altt;
		
		//sog
		int speed = ASCtoDec(50,59,arr);
		String sog = "Speed Over Ground: ";
		if(speed == 1023) {
			sog += "speed not available";
		}
		else if (speed == 1022) {
			sog += "1022 knots or faster";
		}
		else {
			sog += speed+" knots";
		}
		info[4] = sog;
		
		//Position Accuracy
		String Position_Accuracy = null;
		int PA = ASCtoDec(60,60, arr);
		if(PA==1) {
			Position_Accuracy = "High";
		}
		else if (PA == 0) {
			Position_Accuracy = "Low";
		}
		info[5] = "PositionAccuracy: " + Position_Accuracy;
		
		//Longitude
		String longitude = null;
		int longit = ASCtoDec(61,88,arr);
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
		info[6] = longitude;
		
		//Latitude
		String latitude;
		int latit = ASCtoDec(89,115,arr);
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
		info[7] = latitude;
		
		//Course Over Ground
		float COG = (float) (ASCtoDec(116,127,arr)/10.0);
		info[8] = "CourseOverGround: " + String.valueOf(COG);
		
		//Time Stamp
		int second = ASCtoDec(128,133,arr);
		info[9] = "Time Stamp: "+second+"second";
		
		//DTE
		int dte = arr[142];
		String DTE = "DTE: ";
		if(dte == 0) {
			DTE += "Data terminal ready";
		}
		else if(dte == 1) {
			DTE += "Data terminal not ready (default)";
		}
		info[10] = DTE;
		
		//Assigned mode
		int assigned = arr[146];
		String aflag = "Assigned mode: ";
		if(assigned == 1) {
			aflag += "assigned";
		}
		else if(assigned == 0) {
			aflag += "auto";
		}
		info[11] = aflag;
		
		//RAIM
		int raim = arr[147];
		String rflag = "Receiver Autonomous Integrity Monitoring: ";
		if(raim == 1) {
			rflag += "In use";
		}
		else if (raim == 0) {
			rflag += "Not in use";
		}
		info[12] = rflag;
		
		//radio status
		int radio = ASCtoDec(148,167,arr);
		info[13] = "Radio Status: "+radio+", check IALA for details";
		
		return info;
	}
	
	public static String[] type10(int[] arr) {
		String[] info = new String[4];
		info[0] = "Type 10: UTC/Date Inquiry";
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,6,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No repeat";
		}
		else if(Repeat_Indicator == 0) {
			Repeat_info = "default";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[1] = "Repeate_info: " + Repeat_info;
		
		//SourceMMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[2] = "Source MMSI: " + mmsi;
		
		//DesMMSI
		int desmmsi = ASCtoDec(40,69,arr);
		info[3] = "Destnation MMSI: " + desmmsi;
		
		return info;
	}
	
	public static String[] type15(int[] arr) {
		String[] info = new String[11];
		int len = arr.length;
		info[0] = "Type 15: Interrogation";
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,6,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No repeat";
		}
		else if(Repeat_Indicator == 0) {
			Repeat_info = "default";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[1] = "Repeate_info: " + Repeat_info;
		
		//SourceMMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[2] = "Source MMSI: " + mmsi;
		
		//Interrogated MMSI
		int mmsi1 = ASCtoDec(40,69,arr);
		info[3] = "Interrogated MMSI1: " + mmsi1;
		
		//first message type
		int type1_1 = ASCtoDec(70,75,arr);
		String type1 = "First Message Type: ";
		info[4] = type1+type1_1;
		
		//first slot offset
		int offset1 = ASCtoDec(76,87,arr);
		String fso = "First slot offset: ";
		info[5] = fso + offset1; 
		
		if(len >= 107) {
			//second message type
			int type1_2 = ASCtoDec(90,95,arr);
			String t1_2 = "Second Message Type: ";
			info[6] = t1_2+type1_1;
			
			//second slot offset
			int offset1_2 = ASCtoDec(96,107,arr);
			String sso = "Second slot offset: ";
			info[7] = sso + offset1_2;
			
			if(len >= 157) {
				//Interrogated MMSI2
				int mmsi2 = ASCtoDec(110,139,arr);
				info[8] = "Interrogated MMSI2: " + mmsi2;
				
				//first message type
				int type2_1 = ASCtoDec(140,145,arr);
				String t2_1 = "First Message Type: ";
				info[9] = t2_1+type2_1;
				
				//first slot offset
				int offset2_1 = ASCtoDec(146,157,arr);
				String fso_2 = "First slot offset: ";
				info[10] = fso_2 + offset2_1;
			}
		}
		return info;
	}
	
	public static String[] type16(int[] arr) {
		String[] info = new String[9];
		info[0] = "Type 16: Assignment Mode Command";
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,6,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No repeat";
		}
		else if(Repeat_Indicator == 0) {
			Repeat_info = "default";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[1] = "Repeate_info: " + Repeat_info;
		
		//SourceMMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[2] = "Source MMSI: " + mmsi;
		
		//DestinationA MMSI
		int mmsi1 = ASCtoDec(40,69,arr);
		info[3] = "Destination A MMSI: " + mmsi1;
		
		//offset A
		int o1 = ASCtoDec(70,81,arr);
		info[4] = "Offset A: "+o1;
		
		//Increment A
		int i1 = ASCtoDec(82,91,arr);
		info[5] = "Increment A: "+ i1;
		
		if(arr.length > 96) {
			//DestinationB MMSI
			int mmsi2 = ASCtoDec(92,121,arr);
			info[6] = "Destination B MMSI: " + mmsi2;
			
			//offset B
			int o2 = ASCtoDec(122,133,arr);
			info[7] = "Offset B: "+o2;
			
			//Increment B
			int i2 = ASCtoDec(134,143,arr);
			info[8] = "Increment B: "+ i2;
		}
		return info;
	}
	
	public static String[] type19(int[] payload_arr) {
		String[] payload_info = new String[18];
		payload_info[0] = "Type 19: Extended Class B CS Position Report";
		
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
			payload_info[6] = latitude;
			
			//Course Over Ground
			float COG = (float) (ASCtoDec(116,127,payload_arr)/10.0);
			payload_info[7] = "CourseOverGround: " + String.valueOf(COG);
			
			//True Heading
			int True_Heading = ASCtoDec(124,132,payload_arr);
			payload_info[8] = "True Heading: "+ String.valueOf(True_Heading);
			
			//Time Stamp
			int second = ASCtoDec(133,138,payload_arr);
			payload_info[9] = "Time Stamp: "+second;
			
			//name
			String shipname = Bin2String(143,262,payload_arr);
			payload_info[10] = "Shipname(No relaible: )"+shipname;
			
			//shiptype
			int type = ASCtoDec(263,270,payload_arr);
			String stype = PayloadDataType.getShiptype(type);
			payload_info[11] = "Ship Type: "+stype;
			
			//ShipDimension
			int to_bow = ASCtoDec(271,279,payload_arr);
			int to_stern = ASCtoDec(280,288,payload_arr);
			int to_port = ASCtoDec(289,294,payload_arr);
			int to_starboard = ASCtoDec(295,300,payload_arr);
			payload_info[12] = "Ship_Dimensions: A="+to_bow+", B="+to_stern+", C="+to_port+", D="+to_starboard;
			
			//Position Fix Type
			int epfd = ASCtoDec(301,304,payload_arr);
			String Ep = getEPFD(epfd);
			payload_info[13] = "EPFD Fix Type: "+Ep;
			
			//RAIM
			int raim = payload_arr[305];
			String rflag = "Receiver Autonomous Integrity Monitoring: ";
			if(raim == 1) {
				rflag += "In use";
			}
			else if (raim == 0) {
				rflag += "Not in use";
			}
			payload_info[14] = rflag;		
			
			//DTE
			int dte = payload_arr[306];
			String DTE = "DTE: ";
			if(dte == 0) {
				DTE += "Data terminal ready";
			}
			else if(dte == 1) {
				DTE += "Data terminal not ready (default)";
			}
			payload_info[15] = DTE;
			
			//Assigned mode
			int assigned = payload_arr[307];
			String aflag = "Assigned mode: ";
			if(assigned == 1) {
				aflag += "assigned";
			}
			else if(assigned == 0) {
				aflag += "auto";
			}
			payload_info[16] = aflag;
		}

		return payload_info;
	}
	
	public static String[] type20(int[] arr) {
		String[] info = new String[20];
		info[0] = "Type 20 Data Link Management Message";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[2] = "Repeate_info: " + Repeat_info;
		
		//offset number 1
		int offset1 = ASCtoDec(40,51,arr);
		info[3] = "Offset number1: "+offset1;
		
		//Reserved slots
		int number1 = ASCtoDec(52,55,arr);
		info[4] = "Reserved slots 1: " + number1;
		
		//Time out 1
		int timeout1 = ASCtoDec(56,58,arr);
		info[5] = "Time Out 1: " + timeout1+" minutes";
		
		//repeat increment1
		int incre1 = ASCtoDec(59,69,arr);
		info[6] = "Repeat Increment 1: "+incre1;
		
		if(arr.length >= 99) {
			//offset number 2
			int offset2 = ASCtoDec(70,81,arr);
			info[7] = "Offset number2: "+offset2;
			
			//Reserved slots2
			int number2 = ASCtoDec(82,85,arr);
			info[8] = "Reserved slots 2: " + number2;
			
			//Time out 2
			int timeout2 = ASCtoDec(86,88,arr);
			info[9] = "Time Out 2: " + timeout2+" minutes";
			
			//repeat increment2
			int incre2 = ASCtoDec(89,99,arr);
			info[10] = "Repeat Increment 2: "+incre2;
			
			if(arr.length >= 129) {
				//offset number 3
				int offset3 = ASCtoDec(100,111,arr);
				info[11] = "Offset number3: "+offset3;
				
				//Reserved slots3
				int number3 = ASCtoDec(112,115,arr);
				info[12] = "Reserved slots 3: " + number3;
				
				//Time out 3
				int timeout3 = ASCtoDec(116,118,arr);
				info[13] = "Time Out 3: " + timeout3+" minutes";
				
				//repeat increment3
				int incre3 = ASCtoDec(119,129,arr);
				info[14] = "Repeat Increment 3: "+incre3;
				
				if(arr.length >= 159) {
					//offset number 4
					int offset4 = ASCtoDec(130,141,arr);
					info[15] = "Offset number4: "+offset4;
					
					//Reserved slots4
					int number4 = ASCtoDec(142,145,arr);
					info[16] = "Reserved slots 4: " + number4;
					
					//Time out 4
					int timeout4 = ASCtoDec(146,148,arr);
					info[17] = "Time Out 4: " + timeout4+" minutes";
					
					//repeat increment4
					int incre4 = ASCtoDec(149,159,arr);
					info[18] = "Repeat Increment 4: "+incre4;
				}
			}
		}
		return info;
	}
	
	public static String[] type21(int[] arr) {
		String[] info = new String[15];
		info[0] = "Type 21: Aid-to-Navigation Report";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[2] = "Repeate_info: " + Repeat_info;
		
		//Aid type
		int atype = ASCtoDec(38,42,arr);
		String aid_type = getAidType(atype);
		info[3] = aid_type;
		
		if(arr.length > 162) {
			//name
			String name = Bin2String(43,162,arr);
			info[4] = "Name: "+name;
			
			//Position Accuracy
			String Position_Accuracy = null;
			int PA = ASCtoDec(163,163, arr);
			if(PA==1) {
				Position_Accuracy = "High";
			}
			else if (PA == 0) {
				Position_Accuracy = "Low";
			}
			info[5] = "PositionAccuracy: " + Position_Accuracy;
			
			//Longitude
			String longitude = null;
			int longit = ASCtoDec(164,191,arr);
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
			info[6] = longitude;
			
			//Latitude
			String latitude;
			int latit = ASCtoDec(192,218,arr);
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
			info[7] = latitude;
			
			//ShipDimension
			int to_bow = ASCtoDec(219,227,arr);
			int to_stern = ASCtoDec(228,236,arr);
			int to_port = ASCtoDec(237,242,arr);
			int to_starboard = ASCtoDec(243,248,arr);
			info[8] = "Ship_Dimensions: A="+to_bow+", B="+to_stern+", C="+to_port+", D="+to_starboard;
			
			//epfd
			int epfd = ASCtoDec(249,251,arr);
			String Ep = getEPFD(epfd);
			info[9] = "EPFD Fix Type: "+Ep;
			
			//Time Stamp
			int second = ASCtoDec(253,258,arr);
			info[9] = "Time Stamp: "+second;
			
			if(second <= 59) {
				//Off-Position Indicator
				int off_position = arr[259];
				String op = "Off Positioin Indicator: ";
				if(off_position == 0) {
					op += "on position";
				}
				else if(off_position == 1) {
					op += "off position";
				}
				info[10] = op;
			}
			
			//RAIM
			int raim = arr[268];
			String rflag = "Receiver Autonomous Integrity Monitoring: ";
			if(raim == 1) {
				rflag += "In use";
			}
			else if (raim == 0) {
				rflag += "Not in use";
			}
			info[11] = rflag;
			
			//Virtual-aid flag
			int vflag = arr[269];
			String v = "Virtual_aid flag: ";
			if(vflag == 0) {
				v += "real Aid to Navigation at indicated position";
			}
			else if(vflag == 1) {
				v += "virtual Aid to Navigation simulated by nearby AIS station";
			}
			info[12] = v;
			
			//Assigned mode
			int assigned = arr[270];
			String aflag = "Assigned mode: ";
			if(assigned == 1) {
				aflag += "assigned";
			}
			else if(assigned == 0) {
				aflag += "auto";
			}
			info[13] = aflag;
			
			if(arr.length > 272) {
				//name extension
				String Name_ex = Bin2String(272,arr.length,arr);
				info[14] = "Name Extension: "+ Name_ex;
			}
		}
		return info;
	}
	
	public static String[] type22(int[] arr) {
		String[] info = new String[15];
		info[0] = "Type 22: Channel Management";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[2] = "Repeate_info: " + Repeat_info;
		
		//Channel
		int chan_a = ASCtoDec(40,51,arr);
		int chan_b = ASCtoDec(52,63,arr);
		String channel = "Channel A: "+chan_a +", Channel B: "+chan_b;
		info[3] = channel;
		
		//txrx mode
		int txrx = ASCtoDec(64,67,arr);
		String tr = "Tranmit/Recieve Modes: ";
		switch(txrx) {
		case 0: tr += "TxA/TxB, RxA/RxB (default)";break;
		case 1: tr += "TxA, RxA/RxB";break;
		case 2: tr += "TxB, RxA/RxB";break;
		case 3: tr += "Reserved for Future Use";break;
		default: break;
		}
		info[4] = tr;
		
		//power
		int power = arr[68];
		String p = "Power to Use: ";
		if(power == 0) {
			p += "Low";
		}
		else if(power == 1) {
			p += "High";
		} 
		info[5] = p;
		
		//NE position
		String pos = "NE position: "+toPosition(69,86,87,103,arr);
		info[6] = pos;
		String pos2 = "SW position: "+toPosition(104,121,122,138,arr);
		info[7] = pos2;
		
		//MMSI1
		int mmsi1 = ASCtoDec(69,98,arr);
		info[8] = "MMSI1: "+mmsi1;
		
		//MMSI2
		int mmsi2 = ASCtoDec(104,133,arr);
		info[9] = "MMSI2: "+mmsi2;
		
		//addressed
		int add = arr[139];
		String ad = "Addressed: ";
		if(add == 0) {
			ad += "Broadcast";
		}
		else if(add == 1) {
			ad += "Addressed";
		}
		info[10] = ad;
		
		//channel band
		int b1 = arr[140];
		int b2 = arr[141];
		String CB = "Channel A Band: ";
		if(b1 == 0) {
			CB += "Default";
		}
		else if(b1 == 1) {
			CB += "12.5kHz";
		}
		CB += ", Channel B Band: ";
		if(b2 == 0) {
			CB += "Default";
		}
		else if(b2 == 1) {
			CB += "12.5kHz";
		}
		info[11] = CB;
		
		//zonesize
		int zonesize = ASCtoDec(142,144,arr);
		info[12] = "Size of transitional zone: "+zonesize;
		
		return info;
	}
	
	public static String[] type23(int[] arr) {
		String[] info = new String[13];
		info[0] = "Type 22: Channel Management";
		
		//MMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,7,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No more repeat";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[2] = "Repeate_info: " + Repeat_info;
		
		//NE position
		String pos = "NE position: "+toPosition(69,86,87,103,arr);
		info[6] = pos;
		//SW position
		String pos2 = "SW position: "+toPosition(104,121,122,138,arr);
		info[7] = pos2;
		
		//station type
		int station_type = ASCtoDec(110,113,arr);
		String stt = "Station type: ";
		switch(station_type) {
		case 0: stt += "All types of mobiles (default)";break;
		case 1: stt += "Reserved for future use";break;
		case 2: stt += "All types of Class B mobile stations";break;
		case 3: stt += "SAR airborne mobile station";break;
		case 4: stt += "Aid to Navigation station";break;
		case 5: stt += "Class B shipborne mobile station (IEC62287 only)";break;
		case 6: 
		case 7: case 8: case 9: stt += "Regional use and inland waterways";break;
		case 10: case 11: case 12: case 13: case 14: case 15:
			stt += "Reserved for future use";break;
		default: break;
		}
		info[8] = stt;
		
		//ship type
		int st = ASCtoDec(114,121,arr);
		String stype = "Ship type: "+PayloadDataType.getShiptype(st);
		info[9] = stype;
		
		//txrx mode
		int txrx = ASCtoDec(144,145,arr);
		String tr = "Tranmit/Recieve Modes: ";
		switch(txrx) {
		case 0: tr += "TxA/TxB, RxA/RxB (default)";break;
		case 1: tr += "TxA, RxA/RxB";break;
		case 2: tr += "TxB, RxA/RxB";break;
		case 3: tr += "Reserved for Future Use";break;
		default: break;
		}
		info[10] = tr;
		
		//report interval
		int interval = ASCtoDec(146,149,arr);
		String rinterval = "Report Interval: ";
		if(txrx == 1 | txrx == 2) {
			switch(interval) {
			case 0: rinterval += "As given by the autonomous mode";break;
			case 1: rinterval += "20 Minutes";break;
			case 2: rinterval += "12 Minutes";break;
			case 3: rinterval += "6 Minutes";break;
			case 4: rinterval += "2 Minute";break;
			case 5: rinterval += "60 Seconds";break;
			case 6: rinterval += "30 Seconds";break;
			case 7: rinterval += "20 Seconds";break;
			case 8: rinterval += "10 Seconds";break;
			case 9: rinterval += "Next Shorter Reporting Interval";break;
			case 10: rinterval += "Next Longer Reporting Interval";break;
			case 11: case 12: case 13: case 14: case 15: 
				rinterval += "Reserved for future use";break;
			default: break;
			}
		}
		else {
			switch(interval) {
			case 0: rinterval += "As given by the autonomous mode";break;
			case 1: rinterval += "10 Minutes";break;
			case 2: rinterval += "6 Minutes";break;
			case 3: rinterval += "3 Minutes";break;
			case 4: rinterval += "1 Minute";break;
			case 5: rinterval += "30 Seconds";break;
			case 6: rinterval += "15 Seconds";break;
			case 7: rinterval += "10 Seconds";break;
			case 8: rinterval += "5 Seconds";break;
			case 9: rinterval += "Next Shorter Reporting Interval";break;
			case 10: rinterval += "Next Longer Reporting Interval";break;
			case 11: case 12: case 13: case 14: case 15: 
				rinterval += "Reserved for future use";break;
			default: break;
			}
		}
		info[11] = rinterval;
		
		//quiet time
		int quiet = ASCtoDec(150,153,arr);
		info[12] = "Quiet time: "+quiet+" minute(s)";
				
		return info;
	}
	
	public static String[] type24(int[] payload_arr) {
		String[] payload_info = new String[13];
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

		//part
		int part = ASCtoDec(38,39,payload_arr);
		if(part == 0) {
			payload_info[3] = "Part A";
			
			//vessel name
			String Vessel_Name = Bin2String(40, 159, payload_arr);
			payload_info[4] = "Vessel Name: " + Vessel_Name;
		}
		else if(part == 1) {
			payload_info[3] = "Part B";
						
			//ship type
			int st = ASCtoDec(40,47, payload_arr);
			String shiptype = PayloadDataType.getShiptype(st);
			payload_info[4] = "Ship Type: "+shiptype;
			
			//vendor ID
			int vender = ASCtoDec(48,65,payload_arr);
			payload_info[5] = "Vendor ID: "+vender;
			
			//Unit model code
			int umc = ASCtoDec(66,69,payload_arr);
			payload_info[6] = "Unit Model Code: "+umc;
			
			//Serial number
			int serial = ASCtoDec(70,89, payload_arr);
			payload_info[7] = "Serial Number: "+serial;
			
			//call sign
			String callsign = Bin2String(90,131, payload_arr);
			payload_info[8] = "Call Sign: "+callsign;
			
			if(isAuxiliary(mmsi)) {
				//ShipDimension
				int to_bow = ASCtoDec(132,140,payload_arr);
				int to_stern = ASCtoDec(141,149,payload_arr);
				int to_port = ASCtoDec(150,155,payload_arr);
				int to_starboard = ASCtoDec(156,161,payload_arr);
				payload_info[9] = "Ship_Dimensions: A="+to_bow+", B="+to_stern+", C="+to_port+", D="+to_starboard;
			}
			else {
				//mother-ship mmsi
				int mmmsi = ASCtoDec(8,37,payload_arr);
				payload_info[9] = "MotherShip MMSI: " + String.valueOf(mmmsi);
			}
			
		}
		return payload_info;
	}
	
	public static boolean isAuxiliary(int mmsi) {
		
		String str_mmsi = String.valueOf(mmsi);
		if(str_mmsi.substring(0,2) == "98") {
			return true;
		}
		else {
			return false;
		}
	}

	public static String[] type25(int[] arr) {
		String[] info = new String[8];
		info[0] = "Type 25: Single Slot Binary Message";
		//MMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,6,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No repeat";
		}
		else if(Repeat_Indicator == 0) {
			Repeat_info = "default";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[2] = "Repeate_info: " + Repeat_info;
		
		//Destination indicator
		int add = arr[38];
		String ad = "Destination indicator: ";
		if(add == 0) {
			ad += "broadcast";
		}
		else if(add == 1) {
			ad += "addressed";
		}
		info[3] = ad;
		
		int structed = arr[39];
		if(add == 1) {
			//Destination MMSI
			int dmmsi = ASCtoDec(40,69,arr);
			info[4] = "Destination MMSI: " + dmmsi;
		}
		else {
			if(structed == 1) {
				int dac = ASCtoDec(40,49,arr);
				int fid = ASCtoDec(50,55,arr);
			}
		}
		return info;
	}
	//type 26 and 25 are extremely rare
	
	public static String[] type27(int[] arr) {
		String[] info = new String[10];
		info[0] = "Type 27: Long Range AIS Broadcast message";
		//MMSI
		int mmsi = ASCtoDec(8,37,arr);
		info[1] = "MMSI: " + String.valueOf(mmsi);
		
		//Repeat Indicator
		String Repeat_info;
		int Repeat_Indicator = ASCtoDec(6,6,arr);
		if(Repeat_Indicator == 3) {
			Repeat_info = "No repeat";
		}
		else if(Repeat_Indicator == 0) {
			Repeat_info = "default";
		}
		else {
			Repeat_info = "Repeating";
		}
		info[2] = "Repeate_info: " + Repeat_info;
		
		//Position Accuracy
		String Position_Accuracy = null;
		int PA = ASCtoDec(38,38, arr);
		if(PA==1) {
			Position_Accuracy = "High";
		}
		else if (PA == 0) {
			Position_Accuracy = "Low";
		}
		info[3] = "PositionAccuracy: " + Position_Accuracy;
		
		//RAIM
		int raim = arr[39];
		String rflag = "Receiver Autonomous Integrity Monitoring: ";
		if(raim == 1) {
			rflag += "In use";
		}
		else if (raim == 0) {
			rflag += "Not in use";
		}
		info[4] = rflag;
		
		//Navigation_status
		String Navigation_status = null;
		int NS = ASCtoDec(40,43,arr);
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
		info[5] = "Navigation_status: " + Navigation_status;
		
		String pos = toPosition(44,61,62,78,arr);
		info[6] = pos;
		
		//Speed Over Ground
		int SOG = ASCtoDec(79,84,arr);
		info[7] = "SpeedOverGround: " + String.valueOf(SOG);
		
		//Course Over Ground
		int COG = ASCtoDec(85,93,arr);
		info[8] = "CourseOverGround: " + String.valueOf(COG);
		
		//GNSS Position status
		int gps = arr[94];
		String gnss = "GNSS Position status: ";
		if(gps == 1) {
			gnss += "not GNSS position (default)";
		}
		else if(gps == 0) {
			gnss += "current GNSS position";
		}
		info[9] = gnss;
		
		return info;
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
	
	public static String getEPFD(int epfd) {
		String str = "";
		switch(epfd) {
		case 0: str = "Undefined (default)";break;
		case 1: str = "GPS";break;
		case 2: str = "GLONASS";break;
		case 3: str = "Combined GPS/GLONASS";break;
		case 4: str = "Loran-C";break;
		case 5: str = "Chayka";break;
		case 6: str = "Integrated navigation system";break;
		case 7: str = "Surveyed";break;
		case 8: str = "Galileo";break;
		default: break;
		}
		return str;
	}
	
	public static String getAidType(int a) {
		String aa = "Aid Type: ";
		switch(a) {
		case 0: aa += "Default, Type of Aid to Navigation not specified";break;
		case 1: aa += "Reference point";break;
		case 2: aa += "RACON (radar transponder marking a navigation hazard)";break;
		case 3: aa += "Fixed structure off shore, such as oil platforms, wind farms,rigs.";break;
		case 4: aa += "Spare, Reserved for future use.";break;
		case 5: aa += "Light, without sectors";break;
		case 6: aa += "Light, with sectors";break;
		case 7: aa += "Leading Light Front";break;
		case 8: aa += "Leading Light Rear";break;
		case 9: aa += "Beacon, Cardinal N";break;
		case 10: aa += "Beacon, Cardinal E";break;
		case 11: aa += "Beacon, Cardinal S";break;
		case 12: aa += "Beacon, Cardinal W";break;
		case 13: aa += "Beacon, Port hand";break;
		case 14: aa += "Beacon, Starboard hand";break;
		case 15: aa += "Beacon, Preferred Channel port hand";break;
		case 16: aa += "Beacon, Preferred Channel starboard hand";break;
		case 17: aa += "Beacon, Isolated danger";break;
		case 18: aa += "Beacon, Safe water";break;
		case 19: aa += "Beacon, Special mark";break;
		case 20: aa += "Cardinal Mark N";break;
		case 21: aa += "Cardinal Mark E";break;
		case 22: aa += "Cardinal Mark S";break;
		case 23: aa += "Cardinal Mark W";break;
		case 24: aa += "Port hand Mark";break;
		case 25: aa += "Starboard hand Mark";break;
		case 26: aa += "Preferred Channel Port hand";break;
		case 27: aa += "Preferred Channel Starboard hand";break;
		case 28: aa += "Isolated danger";break;
		case 29: aa += "Safe Water";break;
		case 30: aa += "Special Mark";break;
		case 31: aa += "Light Vessel / LANBY / Rigs";break;
		default:
			break;
		}
		return aa;
	}
}
