package decode;

public class Payload {
	public static int[] Character_Stringfield(String str) {
		char[] array = str.toCharArray();
		String bit_string = null;
		String bit_all = null;
		for(int i=0; i < array.length; i++) { 
			switch(array[i]) {
			case '@' : bit_string = "000000";
			case 'A' : bit_string = "000001";
			case 'B' : bit_string = "000010";
			case 'C' : bit_string = "000011";
			case 'D' : bit_string = "000100";
			case 'E' : bit_string = "000101";
			case 'F' : bit_string = "000110";
			case 'G' : bit_string = "000111";
			case 'H' : bit_string = "001000";
			case 'I' : bit_string = "001001";
			case 'J' : bit_string = "001010";
			case 'K' : bit_string = "001011";
			case 'L' : bit_string = "001100";
			case 'M' : bit_string = "001101";
			case 'N' : bit_string = "001110";
			case 'O' : bit_string = "001111";
			case 'P' : bit_string = "010000";
			case 'Q' : bit_string = "010001";
			case 'R' : bit_string = "010010";
			case 'S' : bit_string = "010011";
			case 'T' : bit_string = "010100";
			case 'U' : bit_string = "010101";
			case 'V' : bit_string = "010110";
			case 'W' : bit_string = "010111";
			case 'X' : bit_string = "011000";
			case 'Y' : bit_string = "011001";
			case 'Z' : bit_string = "011010";
			case '[' : bit_string = "011011";
			case '\\': bit_string = "011100";
			case ']' : bit_string = "011101";
			case '^' : bit_string = "011110";
			case '_' : bit_string = "011111";
			case ' ' : bit_string = "100000";
			case '!' : bit_string = "100001";
			case '"' : bit_string = "100010";
			case '#' : bit_string = "100011";
			case '$' : bit_string = "100100";
			case '%' : bit_string = "100101";
			case '&' : bit_string = "100110";
			case '\'': bit_string = "100111";
			case '(' : bit_string = "101000";
			case ')' : bit_string = "101001";
			case '*' : bit_string = "101010";
			case '+' : bit_string = "101011";
			case ',' : bit_string = "101100";
			case '-' : bit_string = "101101";
			case '.' : bit_string = "101110";
			case '/' : bit_string = "101111";
			case '0' : bit_string = "110000"; 
			case '1' : bit_string = "110001";
			case '2' : bit_string = "110010";
			case '3' : bit_string = "110011";
			case '4' : bit_string = "110100";
			case '5' : bit_string = "110101";
			case '6' : bit_string = "110110";
			case '7' : bit_string = "110111";
			case '8' : bit_string = "111000";
			case '9' : bit_string = "111001";
			case ':' : bit_string = "111010";
			case ';' : bit_string = "111011";
			case '<' : bit_string = "111100";
			case '=' : bit_string = "111101";
			case '>' : bit_string = "111110";
			case '?' : bit_string = "111111";
			}
			bit_all += bit_string;
		}
		char[] bit_char =  bit_all.toCharArray();
		int[] bit_arr = charToInt(bit_char);
		return bit_arr;
	}
	
	
	public static int[] charToInt(char[] str) {
		int[] a = new int[str.length];
		for(int i=0; i<str.length; i++) {
			a[i] = Integer.valueOf(str[i]);
		}
		return a;
	}
	
	public static int[] to6bitASII(String str) {
		char[] array = str.toCharArray();
		String bit_string = "";
		String bit_all = "";
		for(int i=0; i < array.length; i++) { 
			switch(array[i]) {
			case '0' : bit_string = "000000";break;
			case '1' : bit_string = "000001";break;
			case '2' : bit_string = "000010";break; 
			case '3' : bit_string = "000011";break;
			case '4' : bit_string = "000100";break;
			case '5' : bit_string = "000101";break;
			case '6' : bit_string = "000110";break;
			case '7' : bit_string = "000111";break;
			case '8' : bit_string = "001000";break;
			case '9' : bit_string = "001001";break;
			case ':' : bit_string = "001010";break;
			case ';' : bit_string = "001011";break;
			case '<' : bit_string = "001100";break;
			case '=' : bit_string = "001101";break;
			case '>' : bit_string = "001110";break;
			case '?' : bit_string = "001111";break;
			case '@' : bit_string = "010000";break;
			case 'A' : bit_string = "010001";break;
			case 'B' : bit_string = "010010";break;
			case 'C' : bit_string = "010011";break;
			case 'D' : bit_string = "010100";break;
			case 'E' : bit_string = "010101";break;
			case 'F' : bit_string = "010110";break;
			case 'G' : bit_string = "010111";break;
			case 'H' : bit_string = "011000";break;
			case 'I' : bit_string = "011001";break;
			case 'J' : bit_string = "011010";break;
			case 'K' : bit_string = "011011";break;
			case 'L' : bit_string = "011100";break;
			case 'M' : bit_string = "011101";break;
			case 'N' : bit_string = "011110";break;
			case 'O' : bit_string = "011111";break;
			case 'P' : bit_string = "100000";break;
			case 'Q' : bit_string = "100001";break;
			case 'R' : bit_string = "100010";break;
			case 'S' : bit_string = "100011";break;
			case 'T' : bit_string = "100100";break;
			case 'U' : bit_string = "100101";break;
			case 'V' : bit_string = "100110";break;
			case 'W' : bit_string = "100111";break;
			case '`' : bit_string = "101000";break;
			case 'a' : bit_string = "101001";break;
			case 'b' : bit_string = "101010";break;
			case 'c' : bit_string = "101011";break;
			case 'd' : bit_string = "101100";break;
			case 'e' : bit_string = "101101";break;
			case 'f' : bit_string = "101110";break;
			case 'g' : bit_string = "101111";break;
			case 'h' : bit_string = "110000";break;
			case 'i' : bit_string = "110001";break;
			case 'j' : bit_string = "110010";break;
			case 'k' : bit_string = "110011";break;
			case 'l' : bit_string = "110100";break;
			case 'm' : bit_string = "110101";break;
			case 'n' : bit_string = "110110";break;
			case 'o' : bit_string = "110111";break;
			case 'p' : bit_string = "111000";break;
			case 'q' : bit_string = "111001";break;
			case 'r' : bit_string = "111010";break;
			case 's' : bit_string = "111011";break;
			case 't' : bit_string = "111100";break;
			case 'u' : bit_string = "111101";break;
			case 'v' : bit_string = "111110";break;
			case 'w' : bit_string = "111111";break;
			default:
				break;
			}
			bit_all += bit_string;
		}
		char[] bit_char =  bit_all.toCharArray();
		int[] bit_arr = charToInt(bit_char);
		for(int i=0; i<bit_arr.length; i++) {
			if(bit_arr[i] == 48) {
				bit_arr[i] = 0;
			}
			else if (bit_arr[i] == 49) {
				bit_arr[i] = 1;
			}
		}
		return bit_arr;
	}
	
	public static String Bin_decode(String str) {
		String message="";
		switch(str) {
		case "000000" : message = "@";break;
		case "000001" : message = "A";break;
		case "000010" : message = "B";break;
		case "000011" : message = "C";break;
		case "000100" : message = "D";break;
		case "000101" : message = "E";break;
		case "000110" : message = "F";break;
		case "000111" : message = "G";break;
		case "001000" : message = "H";break;
		case "001001" : message = "I";break;
		case "001010" : message = "J";break;
		case "001011" : message = "K";break;
		case "001100" : message = "L";break;
		case "001101" : message = "M";break;
		case "001110" : message = "N";break;
		case "001111" : message = "O";break;
		case "010000" : message = "P";break;
		case "010001" : message = "Q";break;
		case "010010" : message = "R";break;
		case "010011" : message = "S";break;
		case "010100" : message = "T";break;
		case "010101" : message = "U";break;
		case "010110" : message = "V";break;
		case "010111" : message = "W";break;
		case "011000" : message = "X";break;
		case "011001" : message = "Y";break;
		case "011010" : message = "Z";break;
		case "011011" : message = "[";break;
		case "011100" : message = "\\";break;
		case "011101" : message = "]";break;
		case "011110" : message = "^";break;
		case "011111" : message = "_";break;
		case "100000" : message = " ";break;
		case "100001" : message = "!";break;
		case "100010" : message = "\"";break;
		case "100011" : message = "#";break;
		case "100100" : message = "$";break;
		case "100101" : message = "%";break;
		case "100110" : message = "&";break;
		case "100111" : message = "'";break;
		case "101000" : message = "(";break;
		case "101001" : message = ")";break;
		case "101010" : message = "*";break;
		case "101011" : message = "+";break;
		case "101100" : message = ",";break;
		case "101101" : message = "-";break;
		case "101110" : message = ".";break;
		case "101111" : message = "/";break;
		case "110000" : message = "0"; break;
		case "110001" : message = "1";break;
		case "110010" : message = "2";break;
		case "110011" : message = "3";break;
		case "110100" : message = "4";break;
		case "110101" : message = "5";break;
		case "110110" : message = "6";break;
		case "110111" : message = "7";break;
		case "111000" : message = "8";break;
		case "111001" : message = "9";break;
		case "111010" : message = ":";break;
		case "111011" : message = ";";break;
		case "111100" : message = "<";break;
		case "111101" : message = "=";break;
		case "111110" : message = ">";break;
		case "111111" : message = "?";break;
		default:
			break;
		}
		return message;
	}
}
