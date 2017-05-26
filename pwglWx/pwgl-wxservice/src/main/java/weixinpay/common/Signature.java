package weixinpay.common;


import com.ydp.util.Md5;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * User: rizenguo Date: 2014/10/29 Time: 15:23
 */
public class Signature
{

	/**
	 * 签名算法
	 * 
	 * @param o
	 *            要参与签名的数据对象
	 * @return 签名
	 * @throws IllegalAccessException
	 */
	public static String getSign(Object o) throws IllegalAccessException
	{
		ArrayList<String> list = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Class cls = o.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields)
		{
			f.setAccessible(true);
			if (f.get(o) != null && f.get(o) != "")
			{
				list.add(f.getName() + "=" + f.get(o) + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + Configure.getKey();
		// Util.log("Sign Before MD5:" + result);
		result = MD5.MD5Encode(result).toUpperCase();
		// Util.log("Sign Result:" + result);
		return result;
	}

	public static String getSign(Map<String, Object> map)
	{
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			if (entry.getValue() != "")
			{
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		// 将参数进行字典序排序
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + Configure.getKey();
		// Util.log("Sign Before MD5:" + result);
		try
		{
			// result = MD5.MD5Encode(result).toUpperCase();
			result = Md5.md5(result).toUpperCase();
		} catch (Exception e)
		{
		}
		return result;
	}

	/**
	 * 从API返回的XML数据里面重新计算一次签名
	 * 
	 * @param responseString
	 *            API返回的XML数据
	 * @return 新鲜出炉的签名
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static String getSignFromResponseString(String responseString) throws IOException, SAXException, ParserConfigurationException
	{
		Map<String, Object> map = XMLParser.getMapFromXML(responseString);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		map.put("sign", "");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		return Signature.getSign(map);
	}

	/**
	 * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
	 * 
	 * @param responseString
	 *            API返回的XML数据字符串
	 * @return API签名是否合法
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static boolean checkIsSignValidFromResponseString(String responseString) throws ParserConfigurationException, IOException, SAXException
	{

		Map<String, Object> map = XMLParser.getMapFromXML(responseString);
		Util.log(map.toString());

		String signFromAPIResponse = map.get("sign").toString();
		if (signFromAPIResponse == "" || signFromAPIResponse == null)
		{
			Util.log("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
			return false;
		}
		// Util.log("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		map.put("sign", "");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		String signForAPIResponse = Signature.getSign(map);

		if (!signForAPIResponse.equals(signFromAPIResponse))
		{
			// 签名验不过，表示这个API返回的数据有可能已经被篡改了
			Util.log("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			return false;
		}
		// Util.log("恭喜，API返回的数据签名验证通过!!!");
		return true;
	}

	/**
	 * @Title: getJsSdkSign
	 * @Description: 获取jssdk签名算法
	 * @param @param noncestr
	 * @param @param tsapiTicket
	 * @param @param timestamp
	 * @param @param url
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getJsSdkSign(String noncestr, String tsapiTicket, String timestamp, String url)
	{
		String content = "jsapi_ticket=" + tsapiTicket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
		String ciphertext = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			ciphertext = byteToStr(digest);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return ciphertext;
	}

	public static String byteToStr(byte[] byteArray)
	{
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++)
		{
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	public static String byteToHexStr(byte mByte)
	{
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}

}
