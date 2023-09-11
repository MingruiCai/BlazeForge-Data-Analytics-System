package com.bcsd.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CommonUtils {


    /**
     * 流转string
     */
    public static String getStringFromStream(InputStream req) {
        InputStream is;
        try {
            is = req;
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[10240];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0) {
                    nTotalRead = nTotalRead + nRead;
                }
            }
            String str = new String(bytes, 0, nTotalRead, "utf-8");
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取request中的参数
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * java 对象转map
     *
     * @param obj
     * @return
     */
    public static Map dtoToMap(Object obj) {
        Map map = new HashMap();
        if (null != obj) {
            Class cla = obj.getClass();
            do {
                Field[] fields = cla.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    try {
                        Method method = cla.getMethod("get" + initCap(name), null);
                        map.put(name.toLowerCase(), method.invoke(obj, null).toString());
                    } catch (Exception e) {
                        fields = null;
                    }
                }
                //获取父类属性
                cla = cla.getSuperclass();
            } while (cla != Object.class);
        }
        return map;
    }


    public static List<String> copyListRange(List<String> src, int start, int end) {
        if (end > src.size()) {
            end = src.size();
        }
        ArrayList<String> dest = new ArrayList<>();
        for (int i = start; i < end; i++) {
            dest.add(src.get(i));
        }
        return dest;
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(ConstantsUtil.TOKEN_NAME);
        if (ConstantsUtil.TOKEN_NAME.equalsIgnoreCase("authentication") && StringUtils.isNotEmpty(token)) {
            token = getToken(token);
        }
        return token;
    }

    public static String getBackToken(HttpServletRequest request) {
        return request.getHeader(ConstantsUtil.BACK_TOKEN_NAME);
    }


    public static String getApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader(ConstantsUtil.API_KEY);
        if (ConstantsUtil.API_KEY.equalsIgnoreCase("apiKey") && StringUtils.isNotEmpty(apiKey)) {
            apiKey = getToken(apiKey);
        }
        return apiKey;
    }

    public static String getClientKey(HttpServletRequest request) {
        String clientKey = request.getHeader(ConstantsUtil.CLIENT_KEY);
        if (ConstantsUtil.CLIENT_KEY.equalsIgnoreCase("clientKey") && StringUtils.isNotEmpty(clientKey)) {
            clientKey = getToken(clientKey);
        }
        return clientKey;
    }

    public static String getApiSecret(HttpServletRequest request) {
        String apiSecret = request.getHeader(ConstantsUtil.API_SECRET);
        if (ConstantsUtil.API_SECRET.equalsIgnoreCase("apiSecret") && StringUtils.isNotEmpty(apiSecret)) {
            apiSecret = getToken(apiSecret);
        }
        return apiSecret;
    }

    public static String getToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            return token;
        }
    }

    public static String trimFirstAndLastChar(String str, String element) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do {
            int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
            int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
            str = str.substring(beginIndex, endIndex);
            beginIndexFlag = (str.indexOf(element) == 0);
            endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
        } while (beginIndexFlag || endIndexFlag);
        return str;
    }


    public static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inStream.close();
        } catch (Exception ex) {

        } finally {

        }

        return outStream.toByteArray();
    }

    /**
     * @param className
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getObjeckPropertyValue(String className, Object obj, String fieldName) {
       Object objectResult=null;
        try {
            Class clazz = Class.forName(className);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (fieldName.equals(field.getName())) {
                    field.setAccessible(true);
                    objectResult= field.get(obj);
                    break;
                }
            }
        } catch (Exception ex) {
        }
        return  objectResult;

    }

    public static boolean isValidLong(String str){
        try{
            long _v = Long.parseLong(str);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }


    private static String initCap(String attr) {
        return attr.substring(0, 1).toUpperCase() + attr.substring(1);
    }

    
	public static List<String> strToList(String contractStatusStr) {
		List<String> statusList=new ArrayList<>();
		if(StringUtils.isNotBlank(contractStatusStr)) {
			if(contractStatusStr.contains(",")) {
				String[] statusArr = contractStatusStr.split(",");
				for(String s:statusArr) {
					statusList.add(s);
				}
			}else {
				statusList.add(contractStatusStr);
			}
		}
		
		return statusList;
	}
	
	public static String getExceptionLimitMsg(Exception e,int limit) {
		if(e!=null && e.getMessage()!=null) {
			String message = e.getMessage();
			if(message.length()>limit) {
				return message.substring(0,limit);
			}
			return message;
		}
		
		return "";
	}
	
	public static String nullToEmpty(Object value) {
		if(value==null) {
			return "";
		}
		return value.toString();
	}

	public static String listToStr(List<String> list) {
      if(list==null || list.isEmpty()) {
    	  return "";
      }
      return  StringUtils.join(list,",");
	}

    public static String getDeliveryToken(HttpServletRequest request) {
        String delivery = request.getHeader(ConstantsUtil.DELIVERY);
        if (ConstantsUtil.DELIVERY.equalsIgnoreCase("deliver") && StringUtils.isNotEmpty(delivery)) {
            delivery = getToken(delivery);
        }
        return delivery;
    }

	public static String emptyToNull(String str) {
		if(StringUtils.isBlank(str)) {
			return null;
		}
		return str;
	}
}
