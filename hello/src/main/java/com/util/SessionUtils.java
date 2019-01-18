package com.util;
//package com.test.util;
//
//import java.io.IOException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.montnets.commons.auth.AuthenticationResponse;
//import com.montnets.commons.constants.Constans;
//import com.montnets.commons.enums.AuthErrorCode;
//import com.montnets.commons.exceptions.AuthException;
//
///**
// * @author: chaiz
// * @Description: 获取Session中用户信息权限等工具类
// * @date: 2018年11月1日 下午19:51:32
// * @version: v1.0.0
// */
//public class SessionUtils {
//
//	
//	/**
//	 * @Title: getUserid
//	 * @Description: 获取用户账号userid
//	 * @return
//	 * @throws IOException
//	 */
//	public static String getUserid() {
//		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        HttpSession sessions = request.getSession(false);
//		AuthenticationResponse so = (AuthenticationResponse) sessions.getAttribute(Constans.SESSION_NAME);
//		if(so!=null) {
//			return so.getUserid();
//		}
//		//您无操作权限或已超时
//		return null;
//	}
//	
//	/**
//	 * @Title: getUsername
//	 * @Description: 获取用户名username
//	 * @return
//	 * @throws IOException
//	 */
//	public static String getUsername() {
//		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        HttpSession sessions = request.getSession(false);
//		AuthenticationResponse so = (AuthenticationResponse) sessions.getAttribute(Constans.SESSION_NAME);
//		if(so!=null) {
//			return so.getUsername();
//		}
//		//您无操作权限或已超时
//		return null;
//	}
//	
//	/**
//	 * @Title: getOpid
//	 * @Description: 获取用户opid
//	 * @return
//	 * @throws IOException
//	 */
//	public static Integer getOpid() {
//		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        HttpSession sessions = request.getSession(false);
//		AuthenticationResponse so = (AuthenticationResponse) sessions.getAttribute(Constans.SESSION_NAME);
//		if(so!=null) {
//			return so.getOpid();
//		}
//		//您无操作权限或已超时
//		return null;
//	}
//	
//	/**
//	 * @Title: getOrgid
//	 * @Description: 获取用户组织机构orgid
//	 * @return
//	 * @throws IOException
//	 */
//	public static Integer getOrgid() {
//		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        HttpSession sessions = request.getSession(false);
//		AuthenticationResponse so = (AuthenticationResponse) sessions.getAttribute(Constans.SESSION_NAME);
//		if(so!=null) {
//			return so.getOrgid();
//		}
//		//您无操作权限或已超时
//		return null;
//	}
//	
//	/**
//	 * @Title: getSalesFlag
//	 * @Description: 是否是销售人员:0非销售人员，1销售人员
//	 * @return
//	 * @throws IOException
//	 */
//	public static Integer getSalesFlag() {
//		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        HttpSession sessions = request.getSession(false);
//		AuthenticationResponse so = (AuthenticationResponse) sessions.getAttribute(Constans.SESSION_NAME);
//		if(so!=null) {
//			return so.getSalesflag();
//		}
//		//您无操作权限或已超时
//		throw new AuthException(AuthErrorCode.USER_INVALID_AUTH);
//	}
//	
//	
//	
//}
