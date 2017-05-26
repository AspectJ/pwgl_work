package weixinpay.util;

/**
 * 定义常量
 * @author Administrator
 * @create 2015-12-21 上午11:25:42
 */
public class Constant
{	
	/*
	 * --------------------------------------------------
	 * ------------公众号类型(易得票、单个影院)------------
	 * --------------------------------------------------
	 */
	// 易得票微信公众号
	public static String WeChatType_YDP = "ydp";
	
	// 单个影院微信公众号
	public static String WeChatType_Ciname = "cinema";
	
	
	/*
	 * --------------------------------------------------
	 * -------------------微信定义渠道号------------------
	 * --------------------------------------------------
	 */
	// 微信渠道-62
	public static int WXCHANNEL = 62;

	/*
	 * --------------------------------------------------
	 * ---------------------座位状态---------------------
	 * --------------------------------------------------
	 */
	// 座位可用
	public static final int Seat_Usable = 0;
	
	// 座位不可用
	public static final int Seat_Disabled = 1;

	/*
	 * --------------------------------------------------
	 * ---------------------支付方式---------------------
	 * --------------------------------------------------
	 */
	// 会员卡支付
	public static final String Pay_Member = "MEMBER";
	
	// 支付宝
	public static final String Pay_Alipay = "ALIPAY";
	
	// 微信支付
	public static final String Pay_WeiX = "WXPAY";
	
	// 建行
	public static final String Pay_CCB = "HNCCB";
	
	/*
	 * --------------------------------------------------
	 * ---------------------支付预留字段区分---------------------
	 * --------------------------------------------------
	 */
	// 微站
	public static final String WZ = "wz"; // 微站 
	
	// 卡劵支付
	public static final String EC = "ec"; // ecard
}
