package decode;

public class TalkerIDS {
	public String talker(String str){
		String talkerID = null;
		switch(str){
			case "!AB" : talkerID = "MMEA 4.0 Dependent AIS Base Station";
			case "!AD" : talkerID = "Mobile AIS Station";
			case "!AN" : talkerID = "NMEA 4.0 Aid to Navigation AIS Station";
			case "!AR" : talkerID = "NMEA 4.0 AIS Receiving Station";
			case "!AS" : talkerID = "NMEA 4.0 Limited Base Station";
			case "!AT" : talkerID = "NMEA 4.0 AIS Transmitting Station";
			case "!AX" : talkerID = "NMEA 4.0 Repeater AIS Station";
			case "!BS" : talkerID = "Base AIS Staion";
			case "!SA" : talkerID = "NMEA 4.0 Physical Shore AIS Station";
		}
		return talkerID;			
	}
}
