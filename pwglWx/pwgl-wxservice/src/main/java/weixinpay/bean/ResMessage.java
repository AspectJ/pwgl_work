package weixinpay.bean;

/**
 * 返回参数对应表
 * @author stone
 * 2015-6-18下午5:13:10
 */
public enum ResMessage {

	Success(1000, "Success"),

	// 用户(1001 - 1150)
	Not_Reqkey(1001, "操作前请先登录"),
	Login_Timeout(1002, "登录超时"),
	Logged_In_Elsewhere(1003, "登录超时或在其他地方登录"),
	User_Login_pass_Fail(1004, "密码错误"),
	User_Login_mobile_Fail(1005, "手机号错误"),
	User_MobileHasRegist(1006, "手机号码已经注册"),
	User_OldPassFail(1007, "原始密码错误"),
	User_mobile_fomart_Fail(1008, "手机号格式错误"),

	// 会员卡（1051 - 1100）
	Card_exist(1051, "此卡已绑定其他帐号"),
	Card_Paied_Fail(1052, "会员卡支付失败"),
	Card_not_exist(1054, "您输入的卡号不存在"),
	Card_Binding(1053, "绑定已提交"),
	Not_Online(1055, "此卡不支持在线支付,请到影院前台购票"),
	Card_Bind_Fail(1056, "会员卡绑定失败"),
	Card_Pass_Fail(1057, "您输入的密码错误"),
	Card_Overdue(1058, "您的卡已过使用期"),
	Card_Sum_Not_Enough(1059, "次数/余额不够"),
	Card_Pay_Fail(1060, "支付失败"),

	ECard_Ciname_Fail(1101, "此卡劵不能在此影院消费"),

	// 影片、影院(1101 - 1200)
	Ciname_Nonsupport_Card(1101, "影院不支持会员卡"),

	// 排期、 影厅、座位 (1201 - 1300)
	Plan_NotExist(1201,"排期不存在"),
	Plan_No_Policy(1202, "可优惠数小于购票数,将按正常价购票"),

	//　订单(1301 - 1400)
	Lock_Unfind(1301, "锁座失败，座位信息或排期错误"),
	SYSSell_Fail(1302, "系统商出票失败,已成功退款"),
	SYSSellBack_Fail(1303, "系统商出票失败,退款失败,请联系客服"),
	OrderStateFail(1304, "订单信息不存在"),
	AlreadyPrint(1305, "订单已打印成功，不能重复打印"),
	BackTicketFail(1306, "系统商退票失败"),
	CardRefund_Fail(1307, "会员卡退款失败"),
	Order_Status_Fail(1308, "订单状态错误,请重新下单!"),
	Order_Payed(1309, "订单已支付!"),
	NonPay(1310,"未支付订单数过多"),
	Pay_Wx_Pre_Fail(1311, "统一下单，获取预备支付ID错误"),
	Paied_Fail(1312, "支付失败"),
	Order_Cancel_Fail(1313, "取消订单失败"),
	Lock_Must_Concurrent(1314, "情侣座需要同时选择"),

	Order_Not_Exsit(4107, "订单不存在,请重新下单"), // 订单号在系统找不到

	// 影票(1401 - 1500)

	// 卖品(1501 - 1600)
	Merchan_Sell_Fail(1501, "购买卖品错误！"),
	Merchan_Rule1_Ultralimit(1502, "您的卖品超出限额！"),

	// 公共信息(1901 - 2000)
	TimeStamp_Over(1901, "请求时间戳超时"),
	SysBusi_Fail(1902, "系统商调用异常"),
	Sign_Fail(1903, "Sign验证错误"),
	Server_Abnormal(1904, "服务器处理异常"),
	Verify_Fail(1905, "短信验证码错误"),
	Lack_Param(1906, "缺少参数"),
	SmsVerity_SendSucess(1908, "Success"), // 短信验证码发送成功
	SmsVerity_Fail(1909, "短信验证码不正确"),
	NoDataQuery(1908, "参数有误"),
	SimplePass(1910, "对不起，您的密码过于简单,请持卡至影院售票处修改后再绑定。"),
	SysBusi_Net_Fail(1911, "系统商网络异常"),
	Request_Fail(1912, "请求失败"),
	;

	public int code;
	public String message;

	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static String getMessage(int code) {
		for (ResMessage rm : values()) {
			if (rm.code == code) {
				return rm.message;
			}
		}
		return "";
	}
}
