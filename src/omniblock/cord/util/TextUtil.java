/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */

package omniblock.cord.util;

import org.apache.commons.lang3.StringUtils;

import net.md_5.bungee.api.ChatColor;
import omniblock.cord.util.lib.minecraft.MinecraftFont;

public class TextUtil {

	public static final String BAR = getCenteredMessage("&8&l&m=============================================");
	
	private final static int CENTER_PX = 154;
	 
	public static String format(String s){
	    if(s == null) return null;
    	return s.replaceAll("&", "§");
    }

	public static String filter(String message) {
		
		if(message.contains("[center]")) {
			
			/*
			 * 
			 * - Aplicador de prefix para el formato
			 * del filtro.
			 * 
			 */
			
			String tocenter = StringUtils.substringBetween(message, "[center]", "[/center]");
			message = message.replace("[center]" + tocenter + "[/center]", "");
			
			tocenter = centerText(tocenter);
			message = tocenter + message;
			
		}
		
		if(message.contains("[br]")) {
			
			message = message.replace("[br]", "\n");
			
		}
		
		return format(message);
		
	}
	
	public static String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static String removeChatColor(String text){
		return ChatColor.stripColor(text);
	}

    public static String centerText(String text) {
    	return centerText(text, 65);
    }
    
    public static String centerText(String text, int size) {
    	int i = text.length();
		int j = size - i;
		if (j <= 0) {
			return text;
		}
    	return StringUtils.leftPad(text, i + j / 2, " ");
    }
    
    public static String getCenteredMessage(String message){
            if(message == null || message.equals(""))
                    message = format(message);
                   
                    int messagePxSize = 0;
                    boolean previousCode = false;
                    boolean isBold = false;
                   
                    for(char c : message.toCharArray()){
                            if(c == '§'){
                                    previousCode = true;
                                    continue;
                            }else if(previousCode == true){
                                    previousCode = false;
                                    if(c == 'l' || c == 'L'){
                                            isBold = true;
                                            continue;
                                    }else isBold = false;
                            }else{
                                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                                    messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                                    messagePxSize++;
                            }
                    }
                   
                    int halvedMessageSize = messagePxSize / 2;
                    int toCompensate = CENTER_PX - halvedMessageSize;
                    int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
                    int compensated = 0;
                    StringBuilder sb = new StringBuilder();
                    while(compensated < toCompensate){
                            sb.append(" ");
                            compensated += spaceLength;
                    }
                    return TextUtil.format(sb.toString() + message);
    }
    
    public static String replaceAllTextWith(String text, char replace) {
		char chArray[] = text.toCharArray();
		for(int i = 0; i < chArray.length; i++){
			chArray[i] = replace;
		}
		
		return String.valueOf(chArray);
	}
    
	public static String secToMin(int i)
	{
	 int ms = i / 60;
	 int ss = i % 60;
	 String m = (ms < 10 ? "0" : "") + ms;
	 String s = (ss < 10 ? "0" : "") + ss;
	 String f = m + ":" + s;
	 return f;
	}

	public static String centerAndFormat(String a) {
		
		String formattedString = TextUtil.format(a);
		
		int spaceWidth = MinecraftFont.Font.getWidth(" ");
		int width = MinecraftFont.Font.getWidth(TextUtil.removeChatColor(formattedString));
		int chatWidth = 180;
		
		if(width < chatWidth){
			int diff = chatWidth - width;
			int leftSpace = diff / 2;
			int spaces = (leftSpace / spaceWidth);
			
			String space = "";
			
			for(int i = 0; i < spaces; i++){
				space += " ";
			}
			
			formattedString = space + formattedString;
		}
		
		return formattedString;
	}
    
}
