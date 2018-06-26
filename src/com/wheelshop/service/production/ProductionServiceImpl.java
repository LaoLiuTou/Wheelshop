package com.wheelshop.service.production;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.wheelshop.dao.production.IProductionMapper;
import com.wheelshop.model.production.Production;
public class ProductionServiceImpl  implements IProductionService {

	@Autowired
	private IProductionMapper iProductionMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Production selectProductionById(String id){
		return iProductionMapper.selectproductionById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Production> selectProductionByParam(Map paramMap){ 
		return iProductionMapper.selectproductionByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountProductionByParam(Map paramMap){ 
		return iProductionMapper.selectCountproductionByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateProduction(Production production){
		return iProductionMapper.updateproduction(production);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addProduction(Production production){
		return iProductionMapper.addproduction(production);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdProduction(List<Production> list){
		return iProductionMapper.muladdproduction(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteProduction(String id){
		return iProductionMapper.deleteproduction(id);
	}

}

