package org.bc.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class Escape {  
    private final static String[] hex = {  
        "00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",  
        "10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",  
        "20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",  
        "30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",  
        "40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",  
        "50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",  
        "60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",  
        "70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",  
        "80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",  
        "90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",  
        "A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",  
        "B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",  
        "C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",  
        "D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",  
        "E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",  
        "F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF"  
    };  
    private final static byte[] val = {  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,  
        0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F,0x3F  
    };  
    public static String escape(String s) {  
        StringBuffer sbuf = new StringBuffer();  
        int len = s.length();  
        for (int i = 0; i < len; i++) {  
            int ch = s.charAt(i);  
            if (ch == ' ') {                        // space : map to '+'   
                sbuf.append('+');  
            } else if ('A' <= ch && ch <= 'Z') {    // 'A'..'Z' : as it was  
                sbuf.append((char)ch);  
            } else if ('a' <= ch && ch <= 'z') {    // 'a'..'z' : as it was  
                sbuf.append((char)ch);  
            } else if ('0' <= ch && ch <= '9') {    // '0'..'9' : as it was  
                sbuf.append((char)ch);  
            }else if (ch == '-' || ch == '_'       // unreserved : as it was  
                || ch == '.' || ch == '!'  
                || ch == '~' || ch == '*'  
                || ch == '\'' || ch == '('  
                || ch == ')') {  
                sbuf.append((char)ch);  
            } else if (ch <= 0x007F) {              // other ASCII : map to %XX  
                sbuf.append('%');  
                sbuf.append(hex[ch]);  
            } else {                                // unicode : map to %uXXXX  
                sbuf.append('%');  
                sbuf.append('u');  
                sbuf.append(hex[(ch >>> 8)]);  
                sbuf.append(hex[(0x00FF & ch)]);  
            }  
        }  
        return sbuf.toString();  
    }  
    public static String unescape(String s) {  
        StringBuffer sbuf = new StringBuffer();  
        int i = 0;  
        int len = s.length();  
        while (i < len) {  
            int ch = s.charAt(i);  
            if (ch == '+') {                        // + : map to ' '   
                sbuf.append(' ');  
            } else if ('A' <= ch && ch <= 'Z') {    // 'A'..'Z' : as it was  
                sbuf.append((char)ch);  
            } else if ('a' <= ch && ch <= 'z') {    // 'a'..'z' : as it was  
                sbuf.append((char)ch);  
            } else if ('0' <= ch && ch <= '9') {    // '0'..'9' : as it was  
                sbuf.append((char)ch);  
            } else if(ch=='/'){
            	sbuf.append((char)ch); 
            } else if (ch == '-' || ch == '_'       // unreserved : as it was  
                || ch == '.' || ch == '!'  
                || ch == '~' || ch == '*'  
                || ch == '\'' || ch == '('  
                || ch == ')') {  
                sbuf.append((char)ch);  
            } else if (ch == '%') {  
                int cint = 0;  
                if ('u' != s.charAt(i+1)) {         // %XX : map to ascii(XX)  
                    cint = (cint << 4) | val[s.charAt(i+1)];  
                    cint = (cint << 4) | val[s.charAt(i+2)];  
                    i+=2;  
                } else {                            // %uXXXX : map to unicode(XXXX)  
                    cint = (cint << 4) | val[s.charAt(i+2)];  
                    cint = (cint << 4) | val[s.charAt(i+3)];  
                    cint = (cint << 4) | val[s.charAt(i+4)];  
                    cint = (cint << 4) | val[s.charAt(i+5)];  
                    i+=5;  
                }  
                sbuf.append((char)cint);  
            }  
            i++;  
        }  
        return sbuf.toString();  
    }  
    public static void main(String[] args) throws UnsupportedEncodingException {
    	
        String stest = "%5B%7B%22zjia%22%3A86%2C%22fhao%22%3A%22903%22%2C%22id%22%3A290464%2C%22area%22%3A%22%E9%9D%99%E5%AE%89%E6%96%B0%E5%A4%A9%E5%9C%B0%22%2C%22zceng%22%3A11%2C%22djia%22%3A7611%2C%22lceng%22%3A9%2C%22ztai%22%3A%224%22%2C%22mji%22%3A113%2C%22quyu%22%3A%22%E7%91%B6%E6%B5%B7%E5%8C%BA%22%2C%22dhao%22%3A%228%22%7D%2C%7B%22zjia%22%3A45%2C%22fhao%22%3A%221609%22%2C%22id%22%3A290463%2C%22area%22%3A%22%E7%91%9E%E5%8D%87%E8%87%AA%E7%94%B1%E8%88%B1%22%2C%22zceng%22%3A28%2C%22djia%22%3A7500%2C%22lceng%22%3A16%2C%22ztai%22%3A%224%22%2C%22mji%22%3A60%2C%22quyu%22%3A%22%E8%9C%80%E5%B1%B1%E5%8C%BA%22%2C%22dhao%22%3A%221%22%7D%2C%7B%22zjia%22%3A42%2C%22fhao%22%3A%22202%22%2C%22id%22%3A290462%2C%22area%22%3A%22%E8%BD%B4%E6%89%BF%E5%8E%82%E5%AE%BF%E8%88%8D%22%2C%22zceng%22%3A6%2C%22djia%22%3A7000%2C%22lceng%22%3A2%2C%22ztai%22%3A%224%22%2C%22mji%22%3A60%2C%22quyu%22%3A%22%E7%BB%8F%E5%BC%80%E5%8C%BA%22%2C%22dhao%22%3A%2213%22%7D%2C%7B%22zjia%22%3A98%2C%22fhao%22%3A%22601%22%2C%22id%22%3A290460%2C%22area%22%3A%22%E5%90%8D%E9%82%A6%E9%94%A6%E7%BB%A3%E5%B9%B4%E5%8D%8E%E6%99%AF%E7%A7%80%E8%8B%91%22%2C%22zceng%22%3A6%2C%22djia%22%3A6533%2C%22lceng%22%3A6%2C%22ztai%22%3A%224%22%2C%22mji%22%3A150%2C%22quyu%22%3A%22%E7%BB%8F%E5%BC%80%E5%8C%BA%22%2C%22dhao%22%3A%221%22%7D%2C%7B%22zjia%22%3A75%2C%22fhao%22%3A%221205%22%2C%22id%22%3A290459%2C%22area%22%3A%22%E7%8E%AB%E7%91%B0%E7%BB%85%E5%9F%8E%22%2C%22zceng%22%3A24%2C%22djia%22%3A8523%2C%22lceng%22%3A12%2C%22ztai%22%3A%224%22%2C%22mji%22%3A88%2C%22quyu%22%3A%22%E5%8C%85%E6%B2%B3%E5%8C%BA%22%2C%22dhao%22%3A%22C1%22%7D%2C%7B%22zjia%22%3A72%2C%22fhao%22%3A%222001%22%2C%22id%22%3A290458%2C%22area%22%3A%22%E6%BB%A8%E6%B9%96%E5%81%87%E6%97%A5%E8%8A%B1%E5%9B%AD%E7%BF%B0%E6%9E%97%E8%8B%91%22%2C%22zceng%22%3A33%2C%22djia%22%3A8304%2C%22lceng%22%3A20%2C%22ztai%22%3A%224%22%2C%22mji%22%3A86.7%2C%22quyu%22%3A%22%E6%BB%A8%E6%B9%96%E6%96%B0%E5%8C%BA%22%2C%22dhao%22%3A%22B1%22%7D%2C%7B%22zjia%22%3A105%2C%22fhao%22%3A%22301%22%2C%22id%22%3A290457%2C%22area%22%3A%22%E5%90%9F%E6%98%A5%E5%9B%AD%22%2C%22zceng%22%3A9%2C%22djia%22%3A7325%2C%22lceng%22%3A3%2C%22ztai%22%3A%224%22%2C%22mji%22%3A143.34%2C%22quyu%22%3A%22%E7%91%B6%E6%B5%B7%E5%8C%BA%22%2C%22dhao%22%3A%22G12%22%7D%2C%7B%22zjia%22%3A70%2C%22fhao%22%3A%226%22%2C%22id%22%3A290456%2C%22area%22%3A%22%E4%B8%AD%E7%BA%A7%E6%B3%95%E9%99%A2%E5%AE%BF%E8%88%8D%22%2C%22zceng%22%3A6%2C%22djia%22%3A8750%2C%22lceng%22%3A6%2C%22ztai%22%3A%224%22%2C%22mji%22%3A80%2C%22quyu%22%3A%22%E5%BA%90%E9%98%B3%E5%8C%BA%22%2C%22dhao%22%3A%221%22%7D%2C%7B%22zjia%22%3A37%2C%22fhao%22%3A%221418%22%2C%22id%22%3A290455%2C%22area%22%3A%22%E6%B8%A9%E8%8E%8E%E6%9D%B0%E5%BA%A7%22%2C%22zceng%22%3A28%2C%22djia%22%3A7872%2C%22lceng%22%3A14%2C%22ztai%22%3A%224%22%2C%22mji%22%3A47%2C%22quyu%22%3A%22%E6%96%B0%E7%AB%99%E5%8C%BA%22%2C%22dhao%22%3A%225%22%7D%2C%7B%22zjia%22%3A78%2C%22fhao%22%3A%22201%22%2C%22id%22%3A290454%2C%22area%22%3A%22%E5%92%8C%E9%A1%BA%E4%B8%9C%E6%96%B9%E8%8A%B1%E5%9B%AD%E4%B8%9C%E6%96%B9%E7%BB%BF%E6%B4%B2%22%2C%22zceng%22%3A6%2C%22djia%22%3A6446%2C%22lceng%22%3A2%2C%22ztai%22%3A%224%22%2C%22mji%22%3A121%2C%22quyu%22%3A%22%E7%91%B6%E6%B5%B7%E5%8C%BA%22%2C%22dhao%22%3A%2217%22%7D%2C%7B%22zjia%22%3A120%2C%22fhao%22%3A%221101%22%2C%22id%22%3A290453%2C%22area%22%3A%22%E9%87%91%E9%83%BD%E5%8D%8E%E5%BA%AD%E4%B8%80%E6%9C%9F%22%2C%22zceng%22%3A12%2C%22djia%22%3A8163%2C%22lceng%22%3A11%2C%22ztai%22%3A%224%22%2C%22mji%22%3A147%2C%22quyu%22%3A%22%E5%BA%90%E9%98%B3%E5%8C%BA%22%2C%22dhao%22%3A%2212%22%7D%2C%7B%22zjia%22%3A85%2C%22fhao%22%3A%222703%22%2C%22id%22%3A290452%2C%22area%22%3A%22%E6%BB%A8%E6%B9%96%E4%B9%A6%E9%A6%99%E9%97%A8%E7%AC%AC%22%2C%22zceng%22%3A33%2C%22djia%22%3A10493%2C%22lceng%22%3A27%2C%22ztai%22%3A%224%22%2C%22mji%22%3A81.01%2C%22quyu%22%3A%22%E6%BB%A8%E6%B9%96%E6%96%B0%E5%8C%BA%22%2C%22dhao%22%3A%225%22%7D%2C%7B%22zjia%22%3A65%2C%22fhao%22%3A%221101%22%2C%22id%22%3A290450%2C%22area%22%3A%22%E9%91%AB%E8%8B%91%E6%9C%9B%E6%B1%9F%E8%8A%B1%E5%9B%AD%22%2C%22zceng%22%3A18%2C%22djia%22%3A8585%2C%22lceng%22%3A11%2C%22ztai%22%3A%224%22%2C%22mji%22%3A75.71%2C%22quyu%22%3A%22%E5%8C%85%E6%B2%B3%E5%8C%BA%22%2C%22dhao%22%3A%227%22%7D%2C%7B%22zjia%22%3A90%2C%22fhao%22%3A%2200%22%2C%22id%22%3A290449%2C%22area%22%3A%22%E4%BF%A1%E6%97%BA%E5%8D%8E%E5%BA%9C%E9%AA%8F%E8%8B%91%22%2C%22zceng%22%3Anull%2C%22djia%22%3A5294%2C%22lceng%22%3A10%2C%22ztai%22%3A%224%22%2C%22mji%22%3A170%2C%22quyu%22%3A%22%E6%94%BF%E5%8A%A1%E5%8C%BA%22%2C%22dhao%22%3A%2200%22%7D%2C%7B%22zjia%22%3A100%2C%22fhao%22%3A%22605%22%2C%22id%22%3A290448%2C%22area%22%3A%22%E5%92%8C%E7%85%A6%E5%9B%AD%E4%B8%89%E6%9C%9F%22%2C%22zceng%22%3A6%2C%22djia%22%3A7246%2C%22lceng%22%3A6%2C%22ztai%22%3A%224%22%2C%22mji%22%3A138%2C%22quyu%22%3A%22%E5%BA%90%E9%98%B3%E5%8C%BA%22%2C%22dhao%22%3A%2227%22%7D%5D";  
        System.out.println(URLDecoder.decode(stest, "UTF-8"));  
       // System.out.println(escape(stest));  
        //System.out.println(unescape(escape(stest)));  
        
        System.out.println(unescape(stest));
    }  
}  
