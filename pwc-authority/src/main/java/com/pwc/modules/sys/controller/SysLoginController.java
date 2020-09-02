

package com.pwc.modules.sys.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.pwc.common.utils.R;
import com.pwc.common.utils.RedisUtils;
import com.pwc.common.utils.StatusDefine;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;


/**
 * 登录相关
 *
 * @author
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private SysDeptService sysDeptService;

	@GetMapping("/captcha.jpg")
	public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
	}

	/**
	 * 登录
	 */
	@ResponseBody
	@PostMapping("/sys/login")
	public R login(String username, String password, String captcha) {
//		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
//		if(!captcha.equalsIgnoreCase(kaptcha)){
//			return R.error("验证码不正确");
//		}
		String authorization = "";
		Set<String> perms ;
		Map<String, Object> map = MapUtil.newHashMap();
		try{
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
			authorization = ShiroUtils.getSession().getId().toString();
			map.put("token", authorization);
			redisUtils.set(authorization, ShiroUtils.getUserEntity());
			perms = (Set<String>)ShiroUtils.getSession().getAttribute("perms");
			map.put("perms", perms);
			SysUserEntity userEntity = ShiroUtils.getUserEntity();
			if (null != userEntity.getExpireDate()) {
				if (DateUtil.compare(userEntity.getExpireDate(), DateUtil.date()) < 0) {
					return R.error("用户已过期");
				}
			}
			SysDeptEntity sysDeptEntity = sysDeptService.getById(userEntity.getDeptId());
			if (null != sysDeptEntity) {
				if (sysDeptEntity.getStatus() == StatusDefine.DeptStatus.DISABLE.getValue()) {
					return R.error("当前组织已被禁用");
				}
			}
		}catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			return R.error("账号或密码不正确");
		}catch (LockedAccountException e) {
			return R.error("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}

		return R.ok().put("data", map);
	}

	/**
	 * 退出
	 */
	@ResponseBody
	@GetMapping(value = "/logout")
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}

	/**
	 * 退出
	 */
	@ResponseBody
	@GetMapping(value = "/unauth")
	public R unAuth() {
		return R.error(401,"Unauthorized");
	}
}
