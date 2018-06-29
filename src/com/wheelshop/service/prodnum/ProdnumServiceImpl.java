package com.wheelshop.service.prodnum;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.wheelshop.dao.prodnum.IProdnumMapper;
import com.wheelshop.model.prodnum.Prodnum;
public class ProdnumServiceImpl  implements IProdnumService {

	@Autowired
	private IProdnumMapper iProdnumMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Prodnum selectProdnumById(String id){
		return iProdnumMapper.selectprodnumById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Prodnum> selectProdnumByParam(Map paramMap){ 
		return iProdnumMapper.selectprodnumByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountProdnumByParam(Map paramMap){ 
		return iProdnumMapper.selectCountprodnumByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateProdnum(Prodnum prodnum){
		return iProdnumMapper.updateprodnum(prodnum);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addProdnum(Prodnum prodnum){
		return iProdnumMapper.addprodnum(prodnum);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdProdnum(List<Prodnum> list){
		return iProdnumMapper.muladdprodnum(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteProdnum(String id){
		return iProdnumMapper.deleteprodnum(id);
	}

}

