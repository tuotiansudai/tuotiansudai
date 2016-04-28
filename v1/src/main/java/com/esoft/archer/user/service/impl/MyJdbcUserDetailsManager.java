package com.esoft.archer.user.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Service;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-7 下午8:33:27
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-7 wangzhi 1.0
 */
public class MyJdbcUserDetailsManager extends JdbcDaoImpl {

	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return new ArrayList<UserDetails>();
		}
		return getJdbcTemplate()
				.query("select username, password, status from user where username=? or email=? or mobile_number=?",
						new String[] { username, username, username },
						new RowMapper<UserDetails>() {
							public UserDetails mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								String username = rs.getString(1);
								String password = rs.getString(2);
								boolean enabled = rs.getBoolean(3);
								return new User(username, password, enabled,
										true, true, true,
										AuthorityUtils.NO_AUTHORITIES);
							}

						});
	}

	@Override
	protected List<GrantedAuthority> loadUserAuthorities(String username) {
		return getJdbcTemplate()
				.query("select user.username as username,role_permission.permission_id as authority from user left join user_role on user.id=user_role.user_id left join role_permission on user_role.role_id=role_permission.role_id where user.username=? or user.email=? or user.mobile_number=?",
						new String[] { username, username, username },
						new RowMapper<GrantedAuthority>() {
							public GrantedAuthority mapRow(ResultSet rs,
									int rowNum) throws SQLException {
								String roleName = getRolePrefix()
										+ rs.getString(2);
								GrantedAuthorityImpl authority = new GrantedAuthorityImpl(
										roleName);

								return authority;
							}
						});
	}

	public List<GrantedAuthority> getUserAuthorities(String username) {
		return loadUserAuthorities(username);
	}
}
