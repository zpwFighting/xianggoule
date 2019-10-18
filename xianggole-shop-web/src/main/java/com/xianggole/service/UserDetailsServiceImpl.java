package com.xianggole.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.xianggole.pojo.TbSeller;
import com.xianggole.sellergoods.service.SellerService;

public class UserDetailsServiceImpl implements UserDetailsService {

	private SellerService sellerService;
	


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		
		//构建一个角色列表
		Collection<GrantedAuthority> grantAuths = new ArrayList();
		grantAuths.add(new  SimpleGrantedAuthority("ROLE_SELLER"));
		
		//得到商家对象
		TbSeller seller = sellerService.findOne(username);
		if(seller!=null) {
			if(seller.getStatus().equals("1")) {
				return new User(username,seller.getPassword(),grantAuths );
			}else {
				return null;
			}
		}else {
			return null;
		}
		
		
		
	}

	
	public SellerService getSellerService() {
		return sellerService;
	}


	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

}
