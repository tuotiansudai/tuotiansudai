package com.esoft.archer.user.service;

public interface ReferGradePtSysService {


	public boolean isExistInvestGrade(Integer grade) ;

	public boolean isExistMerchandiserGrade(Integer grade) ;

	public Integer getAddHighestInvestGrade();

	public Integer getAddHighestMerchandiserGrade();

	public Integer getMaxGradeByRole(String referrerId);

	public Integer getInvestMaxGrade();

	public Integer getMerchandiserMaxGrade();
}
