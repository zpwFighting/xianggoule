package com.xianggole.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@ResponseBody
@Controller
@RequestMapping("/login")
public class LoginController {
	/**
	 * 显示登录名
	 */
	@RequestMapping("/name")
	public Map login(){
		System.out.println("sssss");
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		 System.out.println(name);
		Map map = new HashMap();
		map.put("loginName", name);
		return map;		
	}
}
